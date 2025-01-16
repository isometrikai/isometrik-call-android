package io.isometrik.ui.meetings.incoming.accept;

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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;

import io.isometrik.meeting.builder.action.AcceptJoinMeetingRequestQuery;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmActivityAcceptCallFromNotificationBinding;
import io.isometrik.ui.meetings.meeting.core.MeetingActivity;
import com.bumptech.glide.Glide;
import io.isometrik.ui.utils.PlaceholderUtils;

public class AcceptCallFromNotificationActivity extends AppCompatActivity {

    private IsmActivityAcceptCallFromNotificationBinding ismActivityAcceptCallFromNotificationBinding;
    private String meetingId, meetingTitle, meetingImageUrl;
    private long meetingCreationTime;
    private boolean hdMeeting, audioOnly;

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
        ismActivityAcceptCallFromNotificationBinding = IsmActivityAcceptCallFromNotificationBinding.inflate(getLayoutInflater());
        View view = ismActivityAcceptCallFromNotificationBinding.getRoot();
        setContentView(view);

        meetingId = getIntent().getStringExtra("meetingId");
        meetingTitle = getIntent().getStringExtra("meetingTitle");
        meetingImageUrl = getIntent().getStringExtra("meetingImageUrl");
        meetingCreationTime = getIntent().getLongExtra("meetingCreationTime", 0);
        hdMeeting = getIntent().getBooleanExtra("hdMeeting", false);
        audioOnly = getIntent().getBooleanExtra("audioOnly", false);

        ismActivityAcceptCallFromNotificationBinding.tvMeetingTitle.setText(meetingTitle);
        if (PlaceholderUtils.isValidImageUrl(meetingImageUrl)) {

            try {
                Glide.with(AcceptCallFromNotificationActivity.this).load(meetingImageUrl).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(ismActivityAcceptCallFromNotificationBinding.ivMeetingImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
        } else {
            PlaceholderUtils.setTextRoundDrawable(AcceptCallFromNotificationActivity.this, IsometrikUiSdk.getInstance().getUserSession().getUserName(), ismActivityAcceptCallFromNotificationBinding.ivMeetingImage, 60);
        }
        checkJoinMeetingPermissions();
    }

    @Override
    public void onBackPressed() {
        //To disable back-press
    }


    private void requestPermissions() {

        ActivityCompat.requestPermissions(AcceptCallFromNotificationActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
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

        if ((ContextCompat.checkSelfPermission(AcceptCallFromNotificationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(AcceptCallFromNotificationActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(AcceptCallFromNotificationActivity.this, Manifest.permission.CAMERA)) || (ActivityCompat.shouldShowRequestPermissionRationale(AcceptCallFromNotificationActivity.this, Manifest.permission.RECORD_AUDIO))) {
                Snackbar snackbar = Snackbar.make(ismActivityAcceptCallFromNotificationBinding.rlParent, R.string.ism_permission_call, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view1 -> requestPermissions());

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

        IsometrikUiSdk.getInstance().getIsometrik().getRemoteUseCases().getActionUseCases().acceptJoinMeetingRequest(new AcceptJoinMeetingRequestQuery.Builder().setDeviceId(IsometrikUiSdk.getInstance().getUserSession().getDeviceId()).setMeetingId(meetingId).setUserToken(IsometrikUiSdk.getInstance().getUserSession().getUserToken()).build(), (var1, var2) -> {
            if (var1 != null) {
                Intent intent = new Intent(AcceptCallFromNotificationActivity.this, MeetingActivity.class);
                intent.putExtra("audioOnly", audioOnly);
                intent.putExtra("hdMeeting", hdMeeting);
                intent.putExtra("meetingTitle", meetingTitle);
                intent.putExtra("meetingImageUrl", meetingImageUrl);
                intent.putExtra("meetingId", meetingId);
                intent.putExtra("rtcToken", var1.getRtcToken());
                intent.putExtra("meetingCreationTime", meetingCreationTime);
                intent.putExtra("joinMeetingTime", var1.getJoinTime());

                startActivity(intent);
                supportFinishAfterTransition();
            } else {
                supportFinishAfterTransition();
            }
        });
    }

}
