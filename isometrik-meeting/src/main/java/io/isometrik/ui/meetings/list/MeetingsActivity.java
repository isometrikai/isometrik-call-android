package io.isometrik.ui.meetings.list;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmActivityMeetingsBinding;
import io.isometrik.ui.meetings.create.onetoone.InitiateCallActivity;
import io.isometrik.ui.meetings.list.MeetingsContract;
import io.isometrik.ui.meetings.list.MeetingsModel;
import io.isometrik.ui.meetings.list.MeetingsPresenter;
import io.isometrik.ui.meetings.meeting.core.MeetingActivity;
import io.isometrik.ui.users.details.UserDetailsActivity;
import io.isometrik.ui.utils.AlertProgress;
import com.bumptech.glide.Glide;
import io.isometrik.ui.utils.PlaceholderUtils;

public class MeetingsActivity extends AppCompatActivity implements MeetingsContract.View {

    private MeetingsContract.Presenter meetingsPresenter;

    private AlertProgress alertProgress;

    private AlertDialog alertDialog;

    private final ArrayList<MeetingsModel> meetings = new ArrayList<>();
    private MeetingsAdapter meetingsAdapter;

    private LinearLayoutManager layoutManager;
    private IsmActivityMeetingsBinding ismActivityMeetingsBinding;
    private boolean createMeeting, unregisteredListeners;
    private boolean audioOnly, hdMeeting;
    private String meetingId, opponentName, opponentImageUrl;
    private long meetingCreationTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityMeetingsBinding = IsmActivityMeetingsBinding.inflate(getLayoutInflater());
        View view = ismActivityMeetingsBinding.getRoot();
        setContentView(view);

        meetingsPresenter = new MeetingsPresenter(this);
        alertProgress = new AlertProgress();
        updateShimmerVisibility(true);
        layoutManager = new LinearLayoutManager(this);
        ismActivityMeetingsBinding.rvMeetings.setLayoutManager(layoutManager);
        meetingsAdapter = new MeetingsAdapter(this, meetings);
        ismActivityMeetingsBinding.rvMeetings.addOnScrollListener(recyclerViewOnScrollListener);
        ismActivityMeetingsBinding.rvMeetings.setAdapter(meetingsAdapter);

        fetchLatestMeetings(false, null, false);

        ismActivityMeetingsBinding.refresh.setOnRefreshListener(() -> fetchLatestMeetings(false, null, true));

        ismActivityMeetingsBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fetchLatestMeetings(true, s.toString(), false);
                } else {

                    fetchLatestMeetings(false, null, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            IsometrikUiSdk.getInstance().getIsometrik().getExecutor().execute(() -> IsometrikUiSdk.getInstance().getIsometrik().createConnection(IsometrikUiSdk.getInstance().getUserSession().getUserId() + IsometrikUiSdk.getInstance().getUserSession().getDeviceId(), IsometrikUiSdk.getInstance().getUserSession().getUserToken()));
        } catch (Exception ignore) {

        }
        loadUserImage(IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic());


        ismActivityMeetingsBinding.ivUserImage.setOnClickListener(v -> startActivity(new Intent(this, UserDetailsActivity.class)));

        ismActivityMeetingsBinding.ivCreateMeeting.setOnClickListener(view1 -> checkJoinOrCreateMeetingPermissions(true, null, null, null, 0, false, false));

        meetingsPresenter.registerMeetingEventListener();
        askNotificationPermission();
        NotificationManagerCompat.from(this).cancelAll();

        IsometrikUiSdk.getInstance().getUserSession().setUserIsBusy(false);
    }


    private void fetchLatestMeetings(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
        if (showProgressDialog) {

            showProgressDialog(getString(R.string.ism_fetching_meetings));
        }
        try {
            meetingsPresenter.requestMeetingsData(0, true, isSearchRequest, searchTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The Recycler view on scroll listener.
     */
    private final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            meetingsPresenter.requestMeetingsDataOnScroll(layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(), layoutManager.getItemCount());
        }
    };


    @Override
    public void onMeetingsDataReceived(ArrayList<MeetingsModel> meetings, boolean latestMeetings) {

        runOnUiThread(() -> {

            if (latestMeetings) {
                this.meetings.clear();
            }
            this.meetings.addAll(meetings);

            if (MeetingsActivity.this.meetings.size() > 0) {
                ismActivityMeetingsBinding.tvNoMeetings.setVisibility(View.GONE);
                ismActivityMeetingsBinding.rvMeetings.setVisibility(View.VISIBLE);
                meetingsAdapter.notifyDataSetChanged();
            } else {
                ismActivityMeetingsBinding.tvNoMeetings.setVisibility(View.VISIBLE);
                ismActivityMeetingsBinding.rvMeetings.setVisibility(View.GONE);
            }
        });
        hideProgressDialog();
        if (ismActivityMeetingsBinding.refresh.isRefreshing()) {
            ismActivityMeetingsBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }


    @Override
    public void onError(String errorMessage) {
        if (ismActivityMeetingsBinding.refresh.isRefreshing()) {
            ismActivityMeetingsBinding.refresh.setRefreshing(false);
        }
        hideProgressDialog();
        updateShimmerVisibility(false);
        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(MeetingsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MeetingsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            ismActivityMeetingsBinding.shimmerFrameLayout.startShimmer();
            ismActivityMeetingsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            if (ismActivityMeetingsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
                ismActivityMeetingsBinding.shimmerFrameLayout.setVisibility(View.GONE);
                ismActivityMeetingsBinding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    public void joinMeeting(String meetingId, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean audioOnly, boolean hdMeeting) {
        checkJoinOrCreateMeetingPermissions(false, meetingId, opponentName, opponentImageUrl, meetingCreationTime, audioOnly, hdMeeting);
    }

    /**
     * Load user image.
     *
     * @param userProfileImageUrl the user profile image url
     */
    public void loadUserImage(String userProfileImageUrl) {

        runOnUiThread(() -> {
            if (PlaceholderUtils.isValidImageUrl(userProfileImageUrl)) {

                try {
                    Glide.with(MeetingsActivity.this).load(userProfileImageUrl).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(ismActivityMeetingsBinding.ivUserImage);
                } catch (IllegalArgumentException | NullPointerException ignore) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(MeetingsActivity.this, IsometrikUiSdk.getInstance().getUserSession().getUserName(), ismActivityMeetingsBinding.ivUserImage, 13);
            }
        });
    }

    @Override
    public void onMeetingJoined(String meetingId, String rtcToken, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean audioOnly, boolean hdMeeting, long joinMeetingTime) {
        hideProgressDialog();
        Intent intent = new Intent(MeetingsActivity.this, MeetingActivity.class);
        intent.putExtra("audioOnly", audioOnly);
        intent.putExtra("hdMeeting", hdMeeting);
        intent.putExtra("meetingTitle", opponentName);
        intent.putExtra("meetingImageUrl", opponentImageUrl);
        intent.putExtra("meetingId", meetingId);
        intent.putExtra("rtcToken", rtcToken);
        intent.putExtra("meetingCreationTime", meetingCreationTime);
        intent.putExtra("joinMeetingTime", joinMeetingTime);

        startActivity(intent);

    }


    private void requestPermissions() {

        ActivityCompat.requestPermissions(MeetingsActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
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
                Toast.makeText(this, getString(R.string.ism_permission_meeting_denied), Toast.LENGTH_LONG).show();
            } else {
                handlePermissionsGranted();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void checkJoinOrCreateMeetingPermissions(boolean createMeeting, String meetingId, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean audioOnly, boolean hdMeeting) {
        this.createMeeting = createMeeting;
        this.meetingId = meetingId;
        this.opponentName = opponentName;
        this.opponentImageUrl = opponentImageUrl;
        this.meetingCreationTime = meetingCreationTime;
        this.audioOnly = audioOnly;
        this.hdMeeting = hdMeeting;

        if ((ContextCompat.checkSelfPermission(MeetingsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MeetingsActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MeetingsActivity.this, Manifest.permission.CAMERA)) || (ActivityCompat.shouldShowRequestPermissionRationale(MeetingsActivity.this, Manifest.permission.RECORD_AUDIO))) {
                Snackbar snackbar = Snackbar.make(ismActivityMeetingsBinding.rlParent, R.string.ism_permission_meeting, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ism_ok), view1 -> requestPermissions());

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
        if (createMeeting) {
//            startActivity(new Intent(MeetingsActivity.this, CreateMeetingActivity.class));
            startActivity(new Intent(MeetingsActivity.this, InitiateCallActivity.class));
        } else {
            showProgressDialog(getString(R.string.ism_joining_meeting));
            meetingsPresenter.joinMeeting(meetingId, opponentName, opponentImageUrl, meetingCreationTime, audioOnly, hdMeeting);
        }
    }

    @Override
    public void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        unregisterListeners();
        super.onBackPressed();
    }

    private void unregisterListeners() {

        if (!unregisteredListeners) {

            unregisteredListeners = true;
            meetingsPresenter.unregisterMeetingEventListener();
        }
    }

    @Override
    public void onMeetingEnded(String meetingId) {
        runOnUiThread(() -> {

            int size = meetings.size();
            for (int i = 0; i < size; i++) {
                if (meetings.get(i).getMeetingId().equals(meetingId)) {
                    meetings.remove(i);
                    meetingsAdapter.notifyItemRemoved(i);

                    if (size == 1) {
                        ismActivityMeetingsBinding.tvNoMeetings.setVisibility(View.VISIBLE);
                        ismActivityMeetingsBinding.rvMeetings.setVisibility(View.GONE);
                    }

                    break;
                }
            }
        });
    }

    @Override
    public void onMeetingCreated(MeetingsModel meetingsModel) {
        runOnUiThread(() -> {

            int size = meetings.size();
            boolean meetingAlreadyAddedToList = false;
            for (int i = 0; i < size; i++) {
                if (meetings.get(i).getMeetingId().equals(meetingId)) {
                    meetingAlreadyAddedToList = true;
                    break;
                }
            }

            if (!meetingAlreadyAddedToList) {
                meetings.add(0, meetingsModel);
                meetingsAdapter.notifyItemInserted(0);

                if (size == 0) {
                    ismActivityMeetingsBinding.tvNoMeetings.setVisibility(View.GONE);
                    ismActivityMeetingsBinding.rvMeetings.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onNewIncomingCall(Intent intent) {

        startActivity(intent);
    }


    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (!isGranted) {
            Toast.makeText(this, getString(R.string.ism_notification_permission_denied), Toast.LENGTH_SHORT).show();
        }
    });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    new AlertDialog.Builder(this).setTitle(getString(R.string.ism_notifications_permission)).setMessage(getString(R.string.ism_notification_permission_ask)).setCancelable(true).setPositiveButton(getString(R.string.ism_ok), (dialog, id) -> {

                        dialog.cancel();
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                    }).setNegativeButton(getString(R.string.ism_no_thanks), (dialog, id) -> dialog.cancel()).create().show();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
    }
}
