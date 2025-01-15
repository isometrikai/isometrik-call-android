package io.isometrik.meeting.rtcengine

import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.twilio.audioswitch.AudioDevice
import io.isometrik.meeting.BuildConfig
import io.isometrik.meeting.utils.UserIdGenerator
import io.livekit.android.AudioOptions
import io.livekit.android.AudioType
import io.livekit.android.ConnectOptions
import io.livekit.android.LiveKit
import io.livekit.android.LiveKitOverrides
import io.livekit.android.RoomOptions
import io.livekit.android.audio.AudioSwitchHandler
import io.livekit.android.events.RoomEvent
import io.livekit.android.events.collect
import io.livekit.android.renderer.TextureViewRenderer
import io.livekit.android.room.Room
import io.livekit.android.room.participant.VideoTrackPublishDefaults
import io.livekit.android.room.track.CameraPosition
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.LocalVideoTrackOptions
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.Track.Source.CAMERA
import io.livekit.android.room.track.Track.Source.MICROPHONE
import io.livekit.android.room.track.VideoPreset169
import io.livekit.android.room.track.VideoTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture


open class MeetingOperations {
    private val preferredDeviceListForVideo = listOf(
        AudioDevice.BluetoothHeadset::class.java,
        AudioDevice.WiredHeadset::class.java,
        AudioDevice.Speakerphone::class.java,
        AudioDevice.Earpiece::class.java
        )
    private val preferredDeviceListForAudio = listOf(
        AudioDevice.BluetoothHeadset::class.java,
        AudioDevice.WiredHeadset::class.java,
        AudioDevice.Earpiece::class.java,
        AudioDevice.Speakerphone::class.java
    )

    private var meetingRoom: Room? = null
    private var userId: String = ""

    private lateinit var audioHandler: AudioSwitchHandler

    private val mHandler = ArrayList<MeetingSessionEventsHandler>()
    fun addMeetingSessionEventsHandler(handler: MeetingSessionEventsHandler) {
        mHandler.add(handler)
    }

    fun removeMeetingSessionEventsHandler(handler: MeetingSessionEventsHandler?) {
        mHandler.remove(handler)
    }

    fun joinMeetingAsync(
        context: Context,
        token: String?,
        hdMeeting: Boolean,
        videoMeeting: Boolean,
        userId: String
    ): CompletableFuture<Unit> = GlobalScope.future {

        joinMeeting(
            context,
            token, hdMeeting, videoMeeting, userId
        )
    }


    private suspend fun joinMeeting(
        context: Context,
        token: String?,
        hdMeeting: Boolean,
        videoMeeting: Boolean,
        userId: String
    ) {
        this.userId = userId


        val roomOptions = RoomOptions(
            adaptiveStream = false,
            dynacast = true,
            videoTrackCaptureDefaults = LocalVideoTrackOptions(
                position = CameraPosition.FRONT,
                captureParams = if (hdMeeting) VideoPreset169.H720.capture else VideoPreset169.H360.capture,
            ),
            videoTrackPublishDefaults = VideoTrackPublishDefaults(
                videoEncoding = if (hdMeeting) VideoPreset169.H720.encoding else VideoPreset169.H360.encoding,
            )


        )
        audioHandler = AudioSwitchHandler(context)
        if(videoMeeting){
            audioHandler.preferredDeviceList = preferredDeviceListForVideo
        }else{
            audioHandler.preferredDeviceList = preferredDeviceListForAudio
        }
        audioHandler.audioMode = AudioManager.MODE_IN_CALL


        val room = LiveKit.create(
            context, roomOptions, overrides = LiveKitOverrides(
                audioOptions = AudioOptions(
                    audioOutputType = AudioType.CallAudioType(),
                    audioHandler = audioHandler
                )
            )
        )


        this.meetingRoom = room

        val connectOptions: ConnectOptions =
            if (videoMeeting) {
                ConnectOptions(video = true, audio = true)
            } else {
                ConnectOptions(video = false, audio = true)
            }


        setupRoomListeners(room)
        room.connect(BuildConfig.WEBSOCKET_URL, token!!, connectOptions)

        val localParticipant = room.localParticipant

        localParticipant.setCameraEnabled(videoMeeting)
        localParticipant.setMicrophoneEnabled(true)
        if (videoMeeting) {
            val localTrackPublication = localParticipant.getTrackPublication(CAMERA)
            if (localTrackPublication != null) {
                for (handler in mHandler) {
                    handler.onLocalTrackSubscribed(
                        room.name,
                        localParticipant.identity?.value,
                        UserIdGenerator.getUid(localParticipant.identity?.value),
                        localParticipant.name,
                        localTrackPublication.track,
                        true
                    )
                }

            }
        } else {
            val localTrackPublication = localParticipant.getTrackPublication(MICROPHONE)

            if (localTrackPublication != null) {

                for (handler in mHandler) {
                    handler.onLocalTrackSubscribed(
                        room.name,
                        localParticipant.identity?.value,
                        UserIdGenerator.getUid(localParticipant.identity?.value),
                        localParticipant.name,
                        localTrackPublication.track,
                        false
                    )
                }

            }
        }

        for (handler in mHandler) {
            handler.onConnected(room.name)
        }
    }

    fun leaveMeeting() {
        try {

            meetingRoom?.disconnect()
            meetingRoom?.release()
            meetingRoom = null

        } catch (_: Exception) {

        }
    }

    suspend fun setupRoomListeners(_room: Room) {
        // Handle room events.

        CoroutineScope(Dispatchers.IO).launch {
            _room!!.events.collect { roomEvent ->
                when (roomEvent) {
                    is RoomEvent.Reconnecting -> {
                        for (handler in mHandler) {
                            Log.e("RoomListener", "onReconnecting")
                            handler.onReconnecting(roomEvent.room.name)
                        }
                    }

                    is RoomEvent.Reconnected -> {
                        for (handler in mHandler) {
                            handler.onReconnected(roomEvent.room.name)
                        }
                    }

                    is RoomEvent.Disconnected -> {
                        for (handler in mHandler) {
                            Log.e("RoomListener", "onDisconnect ${roomEvent.error}")

                            handler.onDisconnect(roomEvent.room.name, roomEvent.error)
                        }
                    }

                    is RoomEvent.ParticipantConnected -> {
                        for (handler in mHandler) {
                            handler.onParticipantConnected(
                                roomEvent.room.name,
                                roomEvent.participant.identity?.value,
                                UserIdGenerator.getUid(roomEvent.participant.identity?.value)
                            )
                        }
                    }

                    is RoomEvent.ParticipantDisconnected -> {
                        for (handler in mHandler) {
                            handler.onParticipantDisconnected(
                                roomEvent.room.name,
                                roomEvent.participant.identity?.value,
                                UserIdGenerator.getUid(roomEvent.participant.identity?.value)
                            )
                        }
                    }

                    is RoomEvent.ActiveSpeakersChanged -> {
                        val remoteUsersAudioInfo = ArrayList<RemoteUsersAudioLevelInfo>()
                        for (speaker in roomEvent.speakers) {
                            remoteUsersAudioInfo.add(
                                RemoteUsersAudioLevelInfo(
                                    speaker.identity?.value,
                                    UserIdGenerator.getUid(speaker.identity?.value),
                                    speaker.audioLevel, speaker.isSpeaking
                                )
                            )

                        }
                        for (handler in mHandler) {
                            handler.onActiveSpeakersChanged(
                                roomEvent.room.name, remoteUsersAudioInfo
                            )
                        }
                    }

                    is RoomEvent.FailedToConnect -> {
                        for (handler in mHandler) {
                            handler.onFailedToConnect(roomEvent.room.name, roomEvent.error)
                        }
                    }

                    is RoomEvent.TrackMuted -> {
                        for (handler in mHandler) {
                            if (roomEvent.publication.track is VideoTrack) {
                                handler.onVideoTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    true
                                )
                            } else {
                                handler.onAudioTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    true
                                )
                            }

                        }
                    }

                    is RoomEvent.TrackUnmuted -> {
                        for (handler in mHandler) {
                            if (roomEvent.publication.track is VideoTrack) {
                                handler.onVideoTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    false
                                )
                            } else {
                                handler.onAudioTrackMuteStateChange(
                                    roomEvent.room.name,
                                    roomEvent.participant.identity?.value,
                                    UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                    false
                                )
                            }

                        }
                    }

                    is RoomEvent.TrackSubscribed -> {
                        for (handler in mHandler) {

                            if (roomEvent.participant.identity?.equals(userId) == true) {

                                GlobalScope.future {
                                    handler.onLocalTrackSubscribed(
                                        roomEvent.room.name,
                                        roomEvent.participant.identity?.value,
                                        UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                        roomEvent.participant.name,
                                        roomEvent.track,
                                        roomEvent.track is VideoTrack
                                    )
                                }

                            } else {

                                GlobalScope.future {
                                    handler.onRemoteTrackSubscribed(
                                        roomEvent.room.name,
                                        roomEvent.participant.identity?.value,
                                        UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                        roomEvent.participant.name,
                                        roomEvent.track
                                    )
                                }
                            }
                        }
                    }

                    is RoomEvent.ConnectionQualityChanged -> {
                        for (handler in mHandler) {

                            handler.onConnectionQualityChanged(
                                roomEvent.participant.identity?.value,
                                UserIdGenerator.getUid(roomEvent.participant.identity?.value),
                                io.isometrik.meeting.rtcengine.ConnectionQuality.fromInt(roomEvent.quality.ordinal)
                            )

                        }
                    }

                    else -> {

                    }
                }
            }
        }

    }

    /**
     * Mute local video.
     *
     * @param mute whether to mute or un-mute the local user's video
     */
    fun muteLocalVideo(mute: Boolean) {
        GlobalScope.future {
            val localParticipant = meetingRoom?.localParticipant
            localParticipant?.setCameraEnabled(!mute)

        }
    }

    fun initVideoRenderer(textureViewRenderer: TextureViewRenderer) {
        meetingRoom?.initVideoRenderer(
            textureViewRenderer
        )
    }

    /**
     * Mute local audio.
     *
     * @param mute whether to mute or un-mute the local user's audio
     */
    fun muteLocalAudio(mute: Boolean) {
        val localParticipant = meetingRoom?.localParticipant

        GlobalScope.future {
            localParticipant?.setMicrophoneEnabled(!mute)
        }
    }

    fun switchCamera() {
        val videoTrack =
            meetingRoom?.localParticipant?.getTrackPublication(Track.Source.CAMERA)?.track as? LocalVideoTrack
                ?: return

        val newPosition = when (videoTrack.options.position) {
            CameraPosition.FRONT -> CameraPosition.BACK
            CameraPosition.BACK -> CameraPosition.FRONT
            else -> null
        }

        videoTrack.switchCamera(position = newPosition)
    }

    fun switchAudioDevice(speakerDisabled: Boolean) {
        Log.e("ivSpeaker","set   ${speakerDisabled}")
        if (!speakerDisabled) {
            val audioDevice =
                audioHandler.availableAudioDevices.find { it.name.contains("Speakerphone") }
            Log.e("ivSpeaker","found   ${audioDevice?.name}")

            audioHandler.selectDevice(audioDevice)
        } else {
            val audioDevice =
                audioHandler.availableAudioDevices.find { it.name.contains("Earpiece") }
            Log.e("ivSpeaker","found   ${audioDevice?.name}")

            audioHandler.selectDevice(audioDevice)
        }

    }
}