package io.isometrik.ui.meetings.create.group;

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

import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmActivityCreateMeetingBinding;
import io.isometrik.ui.meetings.create.CreateMeetingContract;
import io.isometrik.ui.meetings.create.CreateMeetingPresenter;
import io.isometrik.ui.meetings.create.UsersModel;
import io.isometrik.ui.meetings.create.group.UsersAdapter;
import io.isometrik.ui.meetings.meeting.core.MeetingActivity;
import io.isometrik.ui.utils.AlertProgress;
import io.isometrik.ui.utils.RecyclerItemClickListener;


public class CreateMeetingActivity extends AppCompatActivity implements CreateMeetingContract.View {

    private CreateMeetingContract.Presenter createMeetingPresenter;

    private AlertProgress alertProgress;

    private AlertDialog alertDialog;

    private final ArrayList<UsersModel> users = new ArrayList<>();
    private UsersAdapter usersAdapter;

    private LinearLayoutManager layoutManager;
    private IsmActivityCreateMeetingBinding ismActivityCreateMeetingBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityCreateMeetingBinding = IsmActivityCreateMeetingBinding.inflate(getLayoutInflater());
        View view = ismActivityCreateMeetingBinding.getRoot();
        setContentView(view);

        createMeetingPresenter = new CreateMeetingPresenter(this);
        alertProgress = new AlertProgress();
        updateShimmerVisibility(true);
        layoutManager = new LinearLayoutManager(this);
        ismActivityCreateMeetingBinding.rvUsers.setLayoutManager(layoutManager);
        usersAdapter = new UsersAdapter(this, users);
        ismActivityCreateMeetingBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
        ismActivityCreateMeetingBinding.rvUsers.setAdapter(usersAdapter);

        fetchNonBlockedUsers(false, null, false);

        ismActivityCreateMeetingBinding.refresh.setOnRefreshListener(() -> fetchNonBlockedUsers(false, null, true));

        ismActivityCreateMeetingBinding.etSearch.addTextChangedListener(new TextWatcher() {
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
        ismActivityCreateMeetingBinding.ibBack.setOnClickListener(v -> onBackPressed());

        ismActivityCreateMeetingBinding.rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(this, ismActivityCreateMeetingBinding.rvUsers, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (ismActivityCreateMeetingBinding.etMeetingTitle.getText() != null) {

                        if (ismActivityCreateMeetingBinding.etMeetingTitle.getText().toString().isEmpty()) {
                            Toast.makeText(CreateMeetingActivity.this, getString(R.string.ism_add_meeting_title), Toast.LENGTH_SHORT).show();
                        } else {
                            UsersModel user = users.get(position);
                            new AlertDialog.Builder(CreateMeetingActivity.this).setTitle(getString(R.string.ism_create_meeting)).setMessage(getString(R.string.ism_create_meeting_with, user.getUserName())).setCancelable(true).setPositiveButton(getString(R.string.ism_yes), (dialog, id) -> {

                                dialog.cancel();
                                showProgressDialog(getString(R.string.ism_creating_meeting));
                                createMeetingPresenter.createMeeting(user.getUserId(), ismActivityCreateMeetingBinding.etMeetingTitle.getText().toString(), user.getUserName(), user.getUserProfileImageUrl(), getString(R.string.ism_meeting));

                            }).setNegativeButton(getString(R.string.ism_no), (dialog, id) -> dialog.cancel()).create().show();
                        }
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

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
                    ismActivityCreateMeetingBinding.tvNoUsers.setVisibility(View.GONE);
                    ismActivityCreateMeetingBinding.rvUsers.setVisibility(View.VISIBLE);
                } else {
                    ismActivityCreateMeetingBinding.tvNoUsers.setVisibility(View.VISIBLE);
                    ismActivityCreateMeetingBinding.rvUsers.setVisibility(View.GONE);
                }
            }
            usersAdapter.notifyDataSetChanged();
            if (ismActivityCreateMeetingBinding.refresh.isRefreshing()) {
                ismActivityCreateMeetingBinding.refresh.setRefreshing(false);
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
        if (ismActivityCreateMeetingBinding.refresh.isRefreshing()) {
            ismActivityCreateMeetingBinding.refresh.setRefreshing(false);
        }
        hideProgressDialog();
        updateShimmerVisibility(false);
        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(CreateMeetingActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateMeetingActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            ismActivityCreateMeetingBinding.shimmerFrameLayout.startShimmer();
            ismActivityCreateMeetingBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            if (ismActivityCreateMeetingBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
                ismActivityCreateMeetingBinding.shimmerFrameLayout.setVisibility(View.GONE);
                ismActivityCreateMeetingBinding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    @Override
    public void onMeetingCreated(String meetingId, String rtcToken, String opponentName, String opponentImageUrl, long meetingCreationTime, String customType) {
        hideProgressDialog();

        Intent intent = new Intent(CreateMeetingActivity.this, MeetingActivity.class);
        intent.putExtra("audioOnly", false);
        intent.putExtra("hdMeeting", true);
        intent.putExtra("meetingTitle", opponentName);
        intent.putExtra("meetingImageUrl", opponentImageUrl);
        intent.putExtra("meetingId", meetingId);
        intent.putExtra("rtcToken", rtcToken);
        intent.putExtra("meetingCreationTime", meetingCreationTime);
        startActivity(intent);
        supportFinishAfterTransition();
    }
}
