package io.isometrik.ui.meetings.meeting.core;

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.Rational
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.meeting.rtcengine.ConnectionQuality
import io.isometrik.meeting.rtcengine.MeetingOperations
import io.isometrik.meeting.rtcengine.MeetingSessionEventsHandler
import io.isometrik.meeting.rtcengine.RemoteUsersAudioLevelInfo
import io.isometrik.ui.IsometrikUiSdk
import io.isometrik.meeting.R
import io.isometrik.meeting.databinding.IsmActivityMeetingBinding
import io.isometrik.ui.utils.CallingToneUtils
import io.isometrik.ui.utils.Constants
import io.isometrik.ui.utils.NotificationUtil
import io.isometrik.ui.utils.PlaceholderUtils
import io.isometrik.ui.utils.TimeUtil
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoTrack
import java.util.Timer
import java.util.TimerTask


class MeetingActivity : AppCompatActivity(), MeetingSessionEventsHandler, MeetingContract.View {
    //
    private lateinit var ismActivityMeetingBinding: IsmActivityMeetingBinding

    private lateinit var meetingPresenter: MeetingContract.Presenter
    private var durationTimer: Timer? = null

    //    private var timerPaused = false
    private var duration: Long = 0
    private var meetingCreationTime: Long = 0
    private val timerHandler: TimerHandler = TimerHandler()
    private var audioOnly = false
    private var hdMeeting = false
    private lateinit var meetingOperations: MeetingOperations
    private var localAudioMuted = false
    private var localVideoMuted = false
    private var speakerDisabled = true
    private var unregisteredListeners = false
    private var leaveMeetingRequested = false
    private var isInPictureInPictureMode = false
    private var meetingId: String? = null
    private var initiatingCall = false
    private var joinMeetingTime: Long = 0
    private var joinRequestAccepted = false

    private var dX = 0f
    private var dY = 0f
    private var screenWidth = 0
    private var screenHeight = 0
    private val CLICK_THRESHOLD = 10 // Tweak this value as needed

    private var localTrackInFullScreen = true
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var remoteVideoTrack: VideoTrack
    private var timer: Timer? = null

    companion object {
        const val INCREASE_TIMER = 1
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (meetingId != null && intent != null && meetingId != (intent.getStringExtra("meetingId"))) {
            meetingPresenter.endMeeting(meetingId)
            meetingId = intent.getStringExtra("meetingId")
            stopTimer()
            meetingOperations.leaveMeeting()

            localTrackInFullScreen = true
            duration = 0
            meetingCreationTime = 0
            audioOnly = false
            hdMeeting = false
            localAudioMuted = false
            localVideoMuted = false
            speakerDisabled = true
            unregisteredListeners = false
            leaveMeetingRequested = false
            isInPictureInPictureMode = false
            initiatingCall = false
            joinMeetingTime = 0
            joinRequestAccepted = false

            ismActivityMeetingBinding.ivMuteAudio.isSelected = false
            ismActivityMeetingBinding.ivMuteVideo.isSelected = false
            ismActivityMeetingBinding.ivSpeaker.isSelected = false

            ismActivityMeetingBinding.ivRemoteAudioMutedCardView.visibility = View.INVISIBLE
            ismActivityMeetingBinding.ivRemoteVideoMutedCardView.visibility = View.INVISIBLE
            ismActivityMeetingBinding.ivRemoteAudioMutedFullScreen.visibility = View.INVISIBLE
            ismActivityMeetingBinding.ivRemoteVideoMutedFullScreen.visibility = View.INVISIBLE

            setIntent(intent)

            ismActivityMeetingBinding.tvCardView.release()
            ismActivityMeetingBinding.tvFullScreenView.release()

            setupMeetingUI()
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true)
            setShowWhenLocked(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        }

        super.onCreate(savedInstanceState)
        ismActivityMeetingBinding = IsmActivityMeetingBinding.inflate(layoutInflater)
        val view: View = ismActivityMeetingBinding.root
        setContentView(view)

        meetingPresenter = MeetingPresenter(this)

        meetingOperations = MeetingOperations()
        meetingOperations.addMeetingSessionEventsHandler(this)

        timer = Timer()

        setupMeetingUI()

        ismActivityMeetingBinding.rlParent.setOnClickListener {
            if (!isInPictureInPictureMode) {
                if (ismActivityMeetingBinding.rlOverlay.visibility == View.VISIBLE) {
                    ismActivityMeetingBinding.rlOverlay.visibility = View.GONE
                } else {
                    ismActivityMeetingBinding.rlOverlay.visibility = View.VISIBLE
                }
            }
        }
        ismActivityMeetingBinding.ivMuteVideo.setOnClickListener {
            localVideoMuted = !localVideoMuted
            meetingOperations.muteLocalVideo(localVideoMuted)
            ismActivityMeetingBinding.ivMuteVideo.isSelected = localVideoMuted
        }

        ismActivityMeetingBinding.ivMuteAudio.setOnClickListener {
            localAudioMuted = !localAudioMuted
            meetingOperations.muteLocalAudio(localAudioMuted)
            ismActivityMeetingBinding.ivMuteAudio.isSelected = localAudioMuted
        }

        ismActivityMeetingBinding.ivSpeaker.setOnClickListener {

            speakerDisabled = !speakerDisabled
            meetingOperations.switchAudioDevice(speakerDisabled)
            ismActivityMeetingBinding.ivSpeaker.isSelected = speakerDisabled

            Log.e("ivSpeaker","Active is  ${ ismActivityMeetingBinding.ivSpeaker.isSelected}")

        }
        ismActivityMeetingBinding.ivSwitchCamera.setOnClickListener {
            meetingOperations.switchCamera()
        }
        ismActivityMeetingBinding.ivEndMeeting.setOnClickListener {
            endMeeting()
            unregisterListeners()
        }

//        ismActivityMeetingBinding.ivEndMeetingPip.setOnClickListener {
//            endMeeting()
//            unregisterListeners()
//        }

        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
        var isBeingDragged = false
        var startX = 0f
        var startY = 0f

        ismActivityMeetingBinding.rlCardView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    startX = event.rawX
                    startY = event.rawY
                    isBeingDragged = false
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = (event.rawX + dX).coerceIn(0f, (screenWidth - v.width).toFloat())
                    val newY = (event.rawY + dY).coerceIn(0f, (screenHeight - v.height).toFloat())

                    v.animate().x(newX).y(newY).setDuration(0).start()

                    if (isBeingDragged || Math.abs(startX - event.rawX) > CLICK_THRESHOLD || Math.abs(startY - event.rawY) > CLICK_THRESHOLD) {
                        isBeingDragged = true
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (!isBeingDragged) {
                        // Click event
                        v.performClick()
                    }
                }
            }
            true
        }
        ismActivityMeetingBinding.rlCardView.setOnClickListener {
            localTrackInFullScreen = !localTrackInFullScreen

            if (localTrackInFullScreen) {
                localVideoTrack.removeRenderer(ismActivityMeetingBinding.tvCardView)
                remoteVideoTrack.removeRenderer(ismActivityMeetingBinding.tvFullScreenView)
                localVideoTrack.addRenderer(ismActivityMeetingBinding.tvFullScreenView)
                remoteVideoTrack.addRenderer(ismActivityMeetingBinding.tvCardView)

                if (ismActivityMeetingBinding.ivRemoteAudioMutedFullScreen.visibility == View.VISIBLE) {
                    ismActivityMeetingBinding.ivRemoteAudioMutedFullScreen.visibility = View.INVISIBLE
                    ismActivityMeetingBinding.ivRemoteAudioMutedCardView.visibility = View.VISIBLE
                }
                if (ismActivityMeetingBinding.ivRemoteVideoMutedFullScreen.visibility == View.VISIBLE) {
                    ismActivityMeetingBinding.ivRemoteVideoMutedFullScreen.visibility = View.INVISIBLE
                    ismActivityMeetingBinding.ivRemoteVideoMutedCardView.visibility = View.VISIBLE
                }
            } else {
                localVideoTrack.removeRenderer(ismActivityMeetingBinding.tvFullScreenView)
                remoteVideoTrack.removeRenderer(ismActivityMeetingBinding.tvCardView)
                localVideoTrack.addRenderer(ismActivityMeetingBinding.tvCardView)
                remoteVideoTrack.addRenderer(ismActivityMeetingBinding.tvFullScreenView)

                if (ismActivityMeetingBinding.ivRemoteAudioMutedCardView.visibility == View.VISIBLE) {
                    ismActivityMeetingBinding.ivRemoteAudioMutedCardView.visibility = View.INVISIBLE
                    ismActivityMeetingBinding.ivRemoteAudioMutedFullScreen.visibility = View.VISIBLE
                }
                if (ismActivityMeetingBinding.ivRemoteVideoMutedCardView.visibility == View.VISIBLE) {
                    ismActivityMeetingBinding.ivRemoteVideoMutedCardView.visibility = View.INVISIBLE
                    ismActivityMeetingBinding.ivRemoteVideoMutedFullScreen.visibility = View.VISIBLE
                }
            }

        }
        ismActivityMeetingBinding.ivChat.setOnClickListener {
            enterPipMode()
        }
        meetingPresenter.registerMeetingEventListener()
        meetingPresenter.registerJoinActionsEventListener()
        meetingPresenter.registerMessageEventListener()
    }

    private fun setupMeetingUI() {

        meetingId = intent.getStringExtra("meetingId")

        initiatingCall = intent.getBooleanExtra("initiatingCall", false)
        audioOnly = intent.getBooleanExtra("audioOnly", false)
        hdMeeting = intent.getBooleanExtra("hdMeeting", false)
        meetingCreationTime = intent.getLongExtra("meetingCreationTime", 0)
        joinMeetingTime = intent.getLongExtra("joinMeetingTime", 0)

        speakerDisabled=audioOnly
        ismActivityMeetingBinding.ivSpeaker.isSelected = speakerDisabled
        Log.e("ivSpeaker","Active is  ${ ismActivityMeetingBinding.ivSpeaker.isSelected}")

        localTrackInFullScreen = !audioOnly

        ismActivityMeetingBinding.tvMeetingTitle.text = intent.getStringExtra("meetingTitle")

        if (initiatingCall) {
            ismActivityMeetingBinding.tvMeetingDurationOrConnectionStatus.text = getString(R.string.ism_dialing)
            CallingToneUtils.playCallingTone(this)
        } else {
            ismActivityMeetingBinding.tvMeetingDurationOrConnectionStatus.text = getString(R.string.ism_connecting)
        }

        meetingOperations.joinMeetingAsync(applicationContext, intent.getStringExtra("rtcToken"), hdMeeting, !audioOnly, IsometrikUiSdk.getInstance().userSession.userId)

        val meetingImageUrl = intent.getStringExtra("meetingImageUrl")
        try {
            if (PlaceholderUtils.isValidImageUrl(meetingImageUrl)) {
                try {
                    Glide.with(this).load(meetingImageUrl).placeholder(R.drawable.ism_ic_profile).transform(CircleCrop()).into(ismActivityMeetingBinding.ivMeetingImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(this, intent.getStringExtra("meetingTitle"), ismActivityMeetingBinding.ivMeetingImage, 20)
            }

        } catch (ignore: java.lang.Exception) {
        }

        if (audioOnly) {
            ismActivityMeetingBinding.ivSwitchCamera.visibility = View.GONE
            ismActivityMeetingBinding.ivMuteVideo.visibility = View.GONE
            ismActivityMeetingBinding.tvFullScreenView.visibility = View.GONE
            ismActivityMeetingBinding.rlCardView.visibility = View.GONE

        } else {
            ismActivityMeetingBinding.ivSwitchCamera.visibility = View.VISIBLE
            ismActivityMeetingBinding.ivMuteVideo.visibility = View.VISIBLE
            ismActivityMeetingBinding.tvFullScreenView.visibility = View.VISIBLE
            ismActivityMeetingBinding.rlCardView.visibility = View.VISIBLE
        }

        if (initiatingCall) {
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    if (!joinRequestAccepted) {
                        meetingPresenter.endMeeting(meetingId)
                    }
                }
            }, Constants.RINGING_DURATION_MILLISECONDS.toLong())
        }
        IsometrikUiSdk.getInstance().userSession.setUserIsBusy(true)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.cancel(meetingId, NotificationUtil.getNotificationId(meetingId))
    }

    override fun onUserLeaveHint() {
        enterPipMode()
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        this.isInPictureInPictureMode = isInPictureInPictureMode
        if (isInPictureInPictureMode) {
            ismActivityMeetingBinding.rlOverlay.visibility = View.GONE
            ismActivityMeetingBinding.tvCardView.visibility = View.GONE
//            ismActivityMeetingBinding.ivEndMeetingPip.visibility = View.VISIBLE
        } else {
//            ismActivityMeetingBinding.ivEndMeetingPip.visibility = View.GONE
            ismActivityMeetingBinding.tvCardView.visibility = View.VISIBLE
            ismActivityMeetingBinding.rlOverlay.visibility = View.VISIBLE
        }

        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            //user clicked on close button of PiP window
            endMeeting()
            unregisterListeners()
        }
    }

    override fun onBackPressed() {
        //To disable back press
        enterPipMode()
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    enterPictureInPictureMode(updatePictureInPictureParams(audioOnly))

                } else {
                    enterPictureInPictureMode()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updatePictureInPictureParams(audioOnly: Boolean): PictureInPictureParams {
        val aspectRatio = Rational(ismActivityMeetingBinding.tvFullScreenView.width, ismActivityMeetingBinding.tvFullScreenView.height)
        val visibleRect = Rect()
        ismActivityMeetingBinding.tvFullScreenView.getGlobalVisibleRect(visibleRect)
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (audioOnly) {
                PictureInPictureParams.Builder().setAutoEnterEnabled(true).build()
            } else {
                PictureInPictureParams.Builder().setAspectRatio(aspectRatio).setSourceRectHint(visibleRect).setAutoEnterEnabled(true).build()
            }

        } else {
            if (audioOnly) {
                PictureInPictureParams.Builder().build()
            } else {
                PictureInPictureParams.Builder().setAspectRatio(aspectRatio).setSourceRectHint(visibleRect).build()
            }
        }
        setPictureInPictureParams(params)
        return params
    }

    override fun onDestroy() {
        unregisterListeners()
        super.onDestroy()
    }

    inner class TimerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == INCREASE_TIMER) {
//                if (!timerPaused) {
                ismActivityMeetingBinding.tvMeetingDurationOrConnectionStatus.text = TimeUtil.getDurationString(duration)
//                }
            }
        }

    }

    private fun startTimer() {
        if (joinMeetingTime > 0) {
            duration = TimeUtil.getDuration(joinMeetingTime)
            durationTimer = Timer()
            durationTimer!!.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    duration += 1 //increase every sec
                    timerHandler.obtainMessage(INCREASE_TIMER).sendToTarget()
                }
            }, 0, 1000)
        }
    }

    private fun unregisterListeners() {

        if (!unregisteredListeners) {

            unregisteredListeners = true
            CallingToneUtils.cleanup()
            stopTimer()
            try {
                timer?.cancel()
            } catch (ignore: Exception) {

            }

            meetingOperations.removeMeetingSessionEventsHandler(this)
            meetingPresenter.unregisterMeetingEventListener()
            meetingPresenter.unregisterJoinActionsEventListener()
            meetingPresenter.unregisterMessageEventListener()
            try {
                runOnUiThread { meetingOperations.leaveMeeting() }
            } catch (ignore: Exception) {
            }
            endMeeting()
            ismActivityMeetingBinding.tvCardView.release()
            ismActivityMeetingBinding.tvFullScreenView.release()
            try {
                finish()
            } catch (ignore: Exception) {
            }
            IsometrikUiSdk.getInstance().userSession.setUserIsBusy(false)
        }
    }

    private fun endMeeting() {
        if (!leaveMeetingRequested) {
            leaveMeetingRequested = true
            try {
                runOnUiThread { meetingPresenter.endMeeting(meetingId) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopTimer() {
        if (durationTimer != null) {
            durationTimer!!.cancel()
        }
    }

    override fun onError(meetingId: String, errorMessage: String?) {
        if (this.meetingId == meetingId) {
            Log.e("onError","==> ${errorMessage}")
            unregisterListeners()
        }
    }

    override fun onMeetingEnded(meetingId: String) {
        if (this.meetingId == meetingId) {
            Log.e("onError","onMeetingEnded==> ")

            unregisterListeners()
        }
    }

    override fun onConnected(meetingId: String?) {

        if (this.meetingId == meetingId) {
            if (!initiatingCall) {
                startTimer()
            }
        }
    }

    override fun onReconnecting(meetingId: String?) {

        if (this.meetingId == meetingId) {
            stopTimer()
            runOnUiThread {
                ismActivityMeetingBinding.tvMeetingDurationOrConnectionStatus.text = getString(R.string.ism_reconnecting)
            }
        }
    }

    override fun onReconnected(meetingId: String?) {

        if (this.meetingId == meetingId) {
            startTimer()
        }
    }

    override fun onDisconnect(meetingId: String?, exception: Exception?) {

        if (this.meetingId == meetingId) {
            stopTimer()
            runOnUiThread {
                ismActivityMeetingBinding.tvMeetingDurationOrConnectionStatus.text = getString(R.string.ism_disconnected)
            }
        }
    }

    override fun onParticipantConnected(meetingId: String?, memberId: String?, memberUid: Int) {
        //TODO Nothing

    }

    override fun onParticipantDisconnected(meetingId: String?, memberId: String?, memberUid: Int) {
        if (this.meetingId == meetingId) {
            if (memberId != (IsometrikUiSdk.getInstance().userSession.userId)) {
                Log.e("onError","onParticipantDisconnected==> ")

                unregisterListeners()
            }
        }
    }

    override fun onFailedToConnect(meetingId: String?, error: Throwable?) {
        if (this.meetingId == meetingId) {
            Log.e("onError","onFailedToConnect==> ")

            unregisterListeners()
        }

    }

    override fun onVideoTrackMuteStateChange(meetingId: String?, memberId: String?, memberUid: Int, muted: Boolean) {
        if (this.meetingId == meetingId) {
            if (memberId != (IsometrikUiSdk.getInstance().userSession.userId)) {
                runOnUiThread {
                    if (muted) {
                        if (localTrackInFullScreen) {
                            ismActivityMeetingBinding.ivRemoteVideoMutedCardView.visibility = View.VISIBLE
                        } else {
                            ismActivityMeetingBinding.ivRemoteVideoMutedFullScreen.visibility = View.VISIBLE
                        }

                    } else {
                        if (localTrackInFullScreen) {
                            ismActivityMeetingBinding.ivRemoteVideoMutedCardView.visibility = View.INVISIBLE
                        } else {
                            ismActivityMeetingBinding.ivRemoteVideoMutedFullScreen.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }

    }

    override fun onAudioTrackMuteStateChange(meetingId: String?, memberId: String?, memberUid: Int, muted: Boolean) {
        if (this.meetingId == meetingId) {
            if (memberId != (IsometrikUiSdk.getInstance().userSession.userId)) {
                runOnUiThread {

                    if (muted) {

                        if (localTrackInFullScreen) {

                            ismActivityMeetingBinding.ivRemoteAudioMutedCardView.visibility = View.VISIBLE
                        } else {

                            ismActivityMeetingBinding.ivRemoteAudioMutedFullScreen.visibility = View.VISIBLE
                        }

                    } else {

                        if (localTrackInFullScreen) {

                            ismActivityMeetingBinding.ivRemoteAudioMutedCardView.visibility = View.INVISIBLE
                        } else {

                            ismActivityMeetingBinding.ivRemoteAudioMutedFullScreen.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onConnectionQualityChanged(memberId: String?, memberUid: Int, quality: ConnectionQuality?) {
        //TODO Nothing

    }

    override suspend fun onRemoteTrackSubscribed(meetingId: String?, memberId: String?, memberUid: Int, memberName: String?, track: Track?) {
        if (this.meetingId == meetingId) {
            if (!audioOnly && track is VideoTrack) {
                remoteVideoTrack = track
                runOnUiThread {
                    if (localTrackInFullScreen) {
                        meetingOperations.initVideoRenderer(ismActivityMeetingBinding.tvCardView)
                        track.addRenderer(ismActivityMeetingBinding.tvCardView)
                    } else {
                        meetingOperations.initVideoRenderer(ismActivityMeetingBinding.tvFullScreenView)
                        track.addRenderer(ismActivityMeetingBinding.tvFullScreenView)
                    }
                }
            }
        }
    }

    override suspend fun onLocalTrackSubscribed(meetingId: String?, memberId: String?, memberUid: Int, memberName: String?, track: Track?, videoTrack: Boolean) {
        if (this.meetingId == meetingId) {
            if (!audioOnly && videoTrack && track is VideoTrack) {
                localVideoTrack = track
                runOnUiThread {
                    if (localTrackInFullScreen) {
                        meetingOperations.initVideoRenderer(ismActivityMeetingBinding.tvFullScreenView)
                        track.addRenderer(ismActivityMeetingBinding.tvFullScreenView)
                    } else {
                        meetingOperations.initVideoRenderer(ismActivityMeetingBinding.tvCardView)
                        track.addRenderer(ismActivityMeetingBinding.tvCardView)
                    }
                }
            }
        }
    }

    override fun onActiveSpeakersChanged(meetingId: String?, remoteUsersAudioInfo: ArrayList<RemoteUsersAudioLevelInfo>?) {
        //TODO Nothing

    }

    override fun onJoinMeetingAccepted(meetingId: String?, joinMeetingTime: Long) {
        if (this.meetingId == meetingId) {
            if (initiatingCall) {
                joinRequestAccepted = true
                this.joinMeetingTime = joinMeetingTime
                runOnUiThread {
                    startTimer()
                    CallingToneUtils.cleanup()
                }
            }
        }
    }

    override fun onRingingForOpponent(meetingId: String?) {
        if (this.meetingId == meetingId) {
            if (initiatingCall) {
                runOnUiThread {
                    ismActivityMeetingBinding.tvMeetingDurationOrConnectionStatus.text = getString(R.string.ism_ringing)
                }
            }
        }
    }
}
