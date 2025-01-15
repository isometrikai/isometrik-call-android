package io.isometrik.meeting.rtcengine

import io.livekit.android.room.track.Track

interface MeetingSessionEventsHandler {
    fun onConnected(meetingId: String?)
    fun onReconnecting(meetingId: String?)
    fun onReconnected(meetingId: String?)
    fun onDisconnect(meetingId: String?, exception: Exception?)
    fun onParticipantConnected(meetingId: String?, memberId: String?, memberUid: Int)
    fun onParticipantDisconnected(meetingId: String?, memberId: String?, memberUid: Int)
    fun onFailedToConnect(meetingId: String?, error: Throwable?)
    fun onVideoTrackMuteStateChange(
        meetingId: String?, memberId: String?, memberUid: Int,
        muted: Boolean
    )

    fun onAudioTrackMuteStateChange(
        meetingId: String?, memberId: String?, memberUid: Int,
        muted: Boolean
    )

    fun onConnectionQualityChanged(
        memberId: String?, memberUid: Int,
        quality: ConnectionQuality?
    )

   suspend fun onRemoteTrackSubscribed(
        meetingId: String?, memberId: String?, memberUid: Int, memberName: String?,
        track: Track?
    )

   suspend fun onLocalTrackSubscribed(
        meetingId: String?, memberId: String?, memberUid: Int, memberName: String?,
        track: Track?, videoTrack: Boolean
    )

    fun onActiveSpeakersChanged(
        meetingId: String?,
        remoteUsersAudioInfo: ArrayList<RemoteUsersAudioLevelInfo>?
    )
}