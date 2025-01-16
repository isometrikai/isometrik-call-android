package io.isometrik.ui.meetings.create.onetoone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.isometrik.ui.IsometrikCallSdk;
import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmActivityInitiateCallBinding;
import io.isometrik.ui.meetings.create.CreateMeetingContract;
import io.isometrik.ui.meetings.create.CreateMeetingPresenter;
import io.isometrik.ui.meetings.create.UsersModel;
import io.isometrik.ui.meetings.create.onetoone.enums.CallType;
import io.isometrik.ui.meetings.meeting.core.MeetingActivity;
import io.isometrik.ui.utils.AlertProgress;


public class InitiateCallActivity extends AppCompatActivity implements CreateMeetingContract.View {

    private CreateMeetingContract.Presenter createMeetingPresenter;

    private AlertProgress alertProgress;

    private AlertDialog alertDialog;

    private final ArrayList<UsersModel> users = new ArrayList<>();
    private UsersAdapter usersAdapter;

    private LinearLayoutManager layoutManager;
    private IsmActivityInitiateCallBinding ismActivityInitiateCallBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityInitiateCallBinding = IsmActivityInitiateCallBinding.inflate(getLayoutInflater());
        View view = ismActivityInitiateCallBinding.getRoot();
        setContentView(view);

        createMeetingPresenter = new CreateMeetingPresenter(this);
        alertProgress = new AlertProgress();
        updateShimmerVisibility(true);
        layoutManager = new LinearLayoutManager(this);
        ismActivityInitiateCallBinding.rvUsers.setLayoutManager(layoutManager);
        usersAdapter = new UsersAdapter(this, users);
        ismActivityInitiateCallBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
        ismActivityInitiateCallBinding.rvUsers.setAdapter(usersAdapter);

        fetchNonBlockedUsers(false, null, false);

        ismActivityInitiateCallBinding.refresh.setOnRefreshListener(() -> fetchNonBlockedUsers(false, null, true));

        ismActivityInitiateCallBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fetchNonBlockedUsers(true, s.toString(), false);
                } else {

                    fetchNonBlockedUsers(false, null, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ismActivityInitiateCallBinding.ibBack.setOnClickListener(v -> onBackPressed());


    }


    /**
     * Fetch non blocked users.
     *
     * @param isSearchRequest    the is search request
     * @param searchTag          the search tag
     * @param showProgressDialog the show progress dialog
     */
    public void fetchNonBlockedUsers(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
        if (showProgressDialog) {
            showProgressDialog(getString(R.string.ism_fetching_users));
        }
        try {
            createMeetingPresenter.fetchNonBlockedUsers(0, true, isSearchRequest, searchTag);
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

            createMeetingPresenter.fetchNonBlockedUsersOnScroll(layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(), layoutManager.getItemCount());
        }
    };

    /**
     * {@link CreateMeetingContract.View#onNonBlockedUsersFetchedSuccessfully(ArrayList, boolean)}
     */
    @Override
    public void onNonBlockedUsersFetchedSuccessfully(ArrayList<UsersModel> usersModels, boolean refreshRequest) {
        if (refreshRequest) {
            users.clear();
        }

        users.addAll(usersModels);

        runOnUiThread(() -> {
            if (refreshRequest) {
                if (users.size() > 0) {
                    ismActivityInitiateCallBinding.tvNoUsers.setVisibility(View.GONE);
                    ismActivityInitiateCallBinding.rvUsers.setVisibility(View.VISIBLE);
                } else {
                    ismActivityInitiateCallBinding.tvNoUsers.setVisibility(View.VISIBLE);
                    ismActivityInitiateCallBinding.rvUsers.setVisibility(View.GONE);
                }
            }
            usersAdapter.notifyDataSetChanged();
            if (ismActivityInitiateCallBinding.refresh.isRefreshing()) {
                ismActivityInitiateCallBinding.refresh.setRefreshing(false);
            }
            hideProgressDialog();
            updateShimmerVisibility(false);
        });

    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    /**
     * {@link CreateMeetingContract.View#onError(String)}
     */
    @Override
    public void onError(String errorMessage) {
        if (ismActivityInitiateCallBinding.refresh.isRefreshing()) {
            ismActivityInitiateCallBinding.refresh.setRefreshing(false);
        }
        hideProgressDialog();
        updateShimmerVisibility(false);
        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(InitiateCallActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(InitiateCallActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            ismActivityInitiateCallBinding.shimmerFrameLayout.startShimmer();
            ismActivityInitiateCallBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            if (ismActivityInitiateCallBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
                ismActivityInitiateCallBinding.shimmerFrameLayout.setVisibility(View.GONE);
                ismActivityInitiateCallBinding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    @Override
    public void onMeetingCreated(String meetingId, String rtcToken, String opponentName, String opponentImageUrl, long meetingCreationTime, String customType) {
        hideProgressDialog();

        Intent intent = new Intent(InitiateCallActivity.this, MeetingActivity.class);
        intent.putExtra("audioOnly", customType.equals(CallType.AudioCall.getValue()));
        intent.putExtra("hdMeeting", true);
        intent.putExtra("meetingTitle", opponentName);
        intent.putExtra("meetingImageUrl", opponentImageUrl);
        intent.putExtra("meetingId", meetingId);
        intent.putExtra("rtcToken", rtcToken);
        intent.putExtra("meetingCreationTime", meetingCreationTime);
        intent.putExtra("initiatingCall", true);
        startActivity(intent);
        supportFinishAfterTransition();
    }


    public void initiateCall(CallType callType, UsersModel user) {

        new AlertDialog.Builder(InitiateCallActivity.this).setTitle(getString(R.string.ism_initiate_call)).setMessage(getString(R.string.ism_initiate_call_with, ((callType == CallType.VideoCall) ? getString(R.string.ism_video) : getString(R.string.ism_audio)), user.getUserName())).setCancelable(true).setPositiveButton(getString(R.string.ism_yes), (dialog, id) -> {

            dialog.cancel();
            showProgressDialog(getString(R.string.ism_initiating_call));
            createMeetingPresenter.createMeeting(user.getUserId(), user.getUserName() + "_" + IsometrikCallSdk.getInstance().getUserSession().getUserName(), user.getUserName(), user.getUserProfileImageUrl(), callType.getValue());

        }).setNegativeButton(getString(R.string.ism_no), (dialog, id) -> dialog.cancel()).create().show();
    }
}
