package io.isometrik.ui.meetings.incoming;


import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmActivityIncomingCallBinding;
import io.isometrik.ui.meetings.meeting.core.MeetingActivity;
import io.isometrik.ui.utils.Constants;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.ui.utils.NotificationUtil;
import io.isometrik.ui.utils.PlaceholderUtils;
import io.isometrik.ui.utils.RingtoneUtils;

public class IncomingCallActivity extends AppCompatActivity implements IncomingCallContract.View {

    private IncomingCallContract.Presenter incomingCallPresenter;
    private IsmActivityIncomingCallBinding ismActivityIncomingCallBinding;
    private String meetingId, meetingTitle, meetingImageUrl;
    private long meetingCreationTime;
    private boolean hdMeeting, audioOnly, unregisteredListeners, joinMeetingRequestAccepted;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
            setShowWhenLocked(true);

        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
        super.onCreate(savedInstanceState);

        ismActivityIncomingCallBinding = IsmActivityIncomingCallBinding.inflate(getLayoutInflater());
        View view = ismActivityIncomingCallBinding.getRoot();
        setContentView(view);
        incomingCallPresenter = new IncomingCallPresenter(this);

        meetingId = getIntent().getStringExtra("meetingId");
        meetingTitle = getIntent().getStringExtra("meetingTitle");
        meetingImageUrl = getIntent().getStringExtra("meetingImageUrl");
        meetingCreationTime = getIntent().getLongExtra("meetingCreationTime", 0);
        hdMeeting = getIntent().getBooleanExtra("hdMeeting", false);
        audioOnly = getIntent().getBooleanExtra("audioOnly", false);

        ismActivityIncomingCallBinding.tvMeetingTitle.setText(meetingTitle);
        if (PlaceholderUtils.isValidImageUrl(meetingImageUrl)) {

            try {
                GlideApp.with(IncomingCallActivity.this).load(meetingImageUrl).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(ismActivityIncomingCallBinding.ivMeetingImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
        } else {
            PlaceholderUtils.setTextRoundDrawable(IncomingCallActivity.this, IsometrikUiSdk.getInstance().getUserSession().getUserName(), ismActivityIncomingCallBinding.ivMeetingImage, 60);
        }
        ismActivityIncomingCallBinding.ivAccept.setOnClickListener(view1 -> {
            ismActivityIncomingCallBinding.ivAccept.setEnabled(false);
            checkJoinMeetingPermissions();
        });

        ismActivityIncomingCallBinding.ivReject.setOnClickListener(view1 -> {
            ismActivityIncomingCallBinding.ivReject.setEnabled(false);
            incomingCallPresenter.rejectJoinMeetingRequest(meetingId);
        });
        incomingCallPresenter.registerMeetingEventListener();
        RingtoneUtils.playRingtoneAndVibrate(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                incomingCallPresenter.rejectJoinMeetingRequest(meetingId);
            }
        }, Constants.RINGING_DURATION_MILLISECONDS);

        IsometrikUiSdk.getInstance().getUserSession().setUserIsBusy(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(meetingId, NotificationUtil.getNotificationId(meetingId));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra("fromNotification", false)) {
            incomingCallPresenter.checkIfMeetingStillActive(meetingId);
        }
    }

    @Override
    public void onError(String errorMessage) {

        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(IncomingCallActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IncomingCallActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDestroy() {

        unregisterListeners();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //To disable back-press
    }

    private void unregisterListeners() {
        if (!unregisteredListeners) {
            RingtoneUtils.cleanup();
            unregisteredListeners = true;
            incomingCallPresenter.unregisterMeetingEventListener();
            try {
                timer.cancel();
            } catch (Exception ignore) {
            }
            if (!joinMeetingRequestAccepted) {
                IsometrikUiSdk.getInstance().getUserSession().setUserIsBusy(false);
            }
        }
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(IncomingCallActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionDenied = false;
        if (requestCode == 0) {

            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionDenied = true;
                    break;
                }
            }
            if (permissionDenied) {
                Toast.makeText(this, getString(R.string.ism_permission_call_denied), Toast.LENGTH_LONG).show();
            } else {
                handlePermissionsGranted();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void checkJoinMeetingPermissions() {

        if ((ContextCompat.checkSelfPermission(IncomingCallActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(IncomingCallActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(IncomingCallActivity.this, Manifest.permission.CAMERA)) || (ActivityCompat.shouldShowRequestPermissionRationale(IncomingCallActivity.this, Manifest.permission.RECORD_AUDIO))) {
                Snackbar snackbar = Snackbar.make(ismActivityIncomingCallBinding.rlParent, R.string.ism_permission_call, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view1 -> requestPermissions());

                snackbar.show();

                ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
            } else {

                requestPermissions();
            }
        } else {

            handlePermissionsGranted();
        }

    }

    private void handlePermissionsGranted() {
        incomingCallPresenter.acceptJoinMeetingRequest(meetingId);
    }

    @Override
    public void onJoinMeetingRequestAccepted(String meetingId, String rtcToken, long joinMeetingTime) {
        joinMeetingRequestAccepted = true;
        Intent intent = new Intent(IncomingCallActivity.this, MeetingActivity.class);
        intent.putExtra("audioOnly", audioOnly);
        intent.putExtra("hdMeeting", hdMeeting);
        intent.putExtra("meetingTitle", meetingTitle);
        intent.putExtra("meetingImageUrl", meetingImageUrl);
        intent.putExtra("meetingId", meetingId);
        intent.putExtra("rtcToken", rtcToken);
        intent.putExtra("meetingCreationTime", meetingCreationTime);
        intent.putExtra("joinMeetingTime", joinMeetingTime);

        startActivity(intent);
        supportFinishAfterTransition();
    }

    @Override
    public void onJoinMeetingRequestRejected(String meetingId) {
        supportFinishAfterTransition();
    }

    @Override
    public void onMeetingEnded(String meetingId) {

        if (this.meetingId.equals(meetingId)) {

            supportFinishAfterTransition();
        }
    }
}
