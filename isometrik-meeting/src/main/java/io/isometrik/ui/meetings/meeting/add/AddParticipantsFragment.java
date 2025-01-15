package io.isometrik.ui.meetings.meeting.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.isometrik.meeting.databinding.IsmBottomsheetAddParticipantsBinding;
import io.isometrik.meeting.R;
import io.isometrik.ui.meetings.meeting.add.AddParticipantsContract;
import io.isometrik.ui.meetings.meeting.add.AddParticipantsPresenter;
import io.isometrik.ui.meetings.meeting.add.ParticipantsAdapter;
import io.isometrik.ui.meetings.meeting.add.ParticipantsModel;
import io.isometrik.ui.meetings.meeting.add.SelectedParticipantsAdapter;
import io.isometrik.ui.utils.AlertProgress;
import io.isometrik.ui.utils.Constants;
import io.isometrik.ui.utils.RecyclerItemClickListener;

/**
 * The bottomsheet fragment to fetch list of users that can be added as participants to a meeting with
 * paging, search and pull to refresh option.Code to add participant to a meeting.
 */
public class AddParticipantsFragment extends BottomSheetDialogFragment implements AddParticipantsContract.View {

    public static final String TAG = "AddParticipantsFragment";
    private final ArrayList<ParticipantsModel> participantsModels = new ArrayList<>();
    private final ArrayList<ParticipantsModel> selectedParticipantsModels = new ArrayList<>();
    private final int MAXIMUM_MEMBERS = Constants.MEETING_MEMBERS_SIZE_AT_A_TIME;
    private Activity activity;
    private AddParticipantsContract.Presenter addParticipantsPresenter;
    private IsmBottomsheetAddParticipantsBinding ismBottomsheetAddParticipantsBinding;
    private ParticipantsAdapter participantsAdapter;
    private SelectedParticipantsAdapter selectedParticipantsAdapter;
    private LinearLayoutManager participantsLayoutManager;
    private final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            addParticipantsPresenter.requestMembersToAddToMeetingOnScroll(participantsLayoutManager.findFirstVisibleItemPosition(), participantsLayoutManager.getChildCount(), participantsLayoutManager.getItemCount());
        }
    };
    private LinearLayoutManager selectedParticipantsLayoutManager;
    private AlertProgress alertProgress;
    private AlertDialog alertDialog;
    private int count;
    private boolean selectedUsersStateNeedToBeSaved = false;
    private String meetingId, meetingTitle;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetAddParticipantsBinding == null) {
            ismBottomsheetAddParticipantsBinding = IsmBottomsheetAddParticipantsBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetAddParticipantsBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetAddParticipantsBinding.getRoot().getParent()).removeView(ismBottomsheetAddParticipantsBinding.getRoot());
            }
        }

        alertProgress = new AlertProgress();

        updateShimmerVisibility(true);
        ismBottomsheetAddParticipantsBinding.tvMeetingTitle.setText(meetingTitle);

        participantsLayoutManager = new LinearLayoutManager(activity);
        ismBottomsheetAddParticipantsBinding.rvUsers.setLayoutManager(participantsLayoutManager);
        participantsAdapter = new ParticipantsAdapter(activity, participantsModels);
        ismBottomsheetAddParticipantsBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
        ismBottomsheetAddParticipantsBinding.rvUsers.setAdapter(participantsAdapter);

        selectedParticipantsLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        ismBottomsheetAddParticipantsBinding.rvUsersSelected.setLayoutManager(selectedParticipantsLayoutManager);
        selectedParticipantsAdapter = new SelectedParticipantsAdapter(activity, selectedParticipantsModels, this);
        ismBottomsheetAddParticipantsBinding.rvUsersSelected.setAdapter(selectedParticipantsAdapter);
        addParticipantsPresenter.initialize(meetingId);
        fetchLatestParticipantsToAddToMeeting(false, null, false);

        ismBottomsheetAddParticipantsBinding.rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(activity, ismBottomsheetAddParticipantsBinding.rvUsers, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {

                    ParticipantsModel participantsModel = participantsModels.get(position);

                    if (participantsModel.isSelected()) {
                        participantsModel.setSelected(false);
                        count--;
                        removeSelectedParticipant(participantsModel.getUserId());
                    } else {

                        if (count < MAXIMUM_MEMBERS) {
                            //Maximum 99 members can be added
                            participantsModel.setSelected(true);
                            count++;
                            addSelectedParticipant(participantsModel);
                        } else {
                            Toast.makeText(activity, getString(R.string.ism_max_participants_limit_reached), Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateSelectedMembersText();
                    participantsModels.set(position, participantsModel);
                    participantsAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        ismBottomsheetAddParticipantsBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fetchLatestParticipantsToAddToMeeting(true, s.toString(), false);
                } else {
                    if (selectedParticipantsModels.size() > 0) {
                        selectedUsersStateNeedToBeSaved = true;
                    }
                    fetchLatestParticipantsToAddToMeeting(false, null, false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ismBottomsheetAddParticipantsBinding.tvAdd.setOnClickListener(v -> {

            if (selectedParticipantsModels.isEmpty()) {
                Toast.makeText(activity, R.string.ism_atleast_one_participant, Toast.LENGTH_SHORT).show();
            } else {

                int size = selectedParticipantsModels.size();
                String message = (size == 1) ? getString(R.string.ism_add_participant_confirmation) : getString(R.string.ism_add_participants_confirmation, size);

                new AlertDialog.Builder(activity).setTitle(getString(R.string.ism_add_participants)).setMessage(message).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                    dialog.cancel();
                    showProgressDialog(getString(R.string.ism_adding_participants));
                    addParticipantsPresenter.addMembers(selectedParticipantsModels);
                }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
            }
        });

        ismBottomsheetAddParticipantsBinding.refresh.setOnRefreshListener(() -> fetchLatestParticipantsToAddToMeeting(false, null, true));

        return ismBottomsheetAddParticipantsBinding.getRoot();
    }

    @Override
    public void onMembersAddedSuccessfully() {
        hideProgressDialog();
        dismissDialog();
    }

    private void dismissDialog() {
        try {
            dismiss();
        } catch (Exception ignore) {
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        activity = getActivity();
        addParticipantsPresenter = new AddParticipantsPresenter();
        addParticipantsPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        addParticipantsPresenter.detachView();
        activity = null;
    }

    @Override
    public void onMembersToAddToMeetingFetched(ArrayList<ParticipantsModel> participantsModels, boolean latestUsers, boolean isSearchRequest) {
        if (latestUsers) {
            this.participantsModels.clear();

            if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
                int size = participantsModels.size();
                ParticipantsModel participantsModel;

                for (int i = 0; i < size; i++) {

                    for (int j = 0; j < selectedParticipantsModels.size(); j++) {
                        if (selectedParticipantsModels.get(j).getUserId().equals(participantsModels.get(i).getUserId())) {
                            participantsModel = participantsModels.get(i);
                            participantsModel.setSelected(true);
                            participantsModels.set(i, participantsModel);
                            break;
                        }
                    }
                }
                if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
            } else {
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        selectedParticipantsModels.clear();
                        count = 0;
                        selectedParticipantsAdapter.notifyDataSetChanged();
                        updateSelectedMembersText();
                    });
                }
            }
        } else {
            int size = participantsModels.size();
            ParticipantsModel participantsModel;

            for (int i = 0; i < size; i++) {

                for (int j = 0; j < selectedParticipantsModels.size(); j++) {
                    if (selectedParticipantsModels.get(j).getUserId().equals(participantsModels.get(i).getUserId())) {
                        participantsModel = participantsModels.get(i);
                        participantsModel.setSelected(true);
                        participantsModels.set(i, participantsModel);
                        break;
                    }
                }
            }
        }

        this.participantsModels.addAll(participantsModels);
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (AddParticipantsFragment.this.participantsModels.size() > 0) {
                    ismBottomsheetAddParticipantsBinding.tvNoUsers.setVisibility(View.GONE);
                    ismBottomsheetAddParticipantsBinding.rvUsers.setVisibility(View.VISIBLE);
                    participantsAdapter.notifyDataSetChanged();
                } else {
                    ismBottomsheetAddParticipantsBinding.tvNoUsers.setVisibility(View.VISIBLE);
                    ismBottomsheetAddParticipantsBinding.rvUsers.setVisibility(View.GONE);
                }
                selectedParticipantsAdapter.notifyDataSetChanged();

                hideProgressDialog();
                if (ismBottomsheetAddParticipantsBinding.refresh.isRefreshing()) {
                    ismBottomsheetAddParticipantsBinding.refresh.setRefreshing(false);
                }

                updateShimmerVisibility(false);
            });
        }
    }

    @Override
    public void onError(String errorMessage) {
        if (ismBottomsheetAddParticipantsBinding.refresh.isRefreshing()) {
            ismBottomsheetAddParticipantsBinding.refresh.setRefreshing(false);
        }

        hideProgressDialog();
        updateShimmerVisibility(false);
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (errorMessage != null) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Remove participant.
     *
     * @param userId the user id
     */
    public void removeParticipant(String userId) {
        int size = participantsModels.size();
        for (int i = 0; i < size; i++) {

            if (participantsModels.get(i).getUserId().equals(userId)) {

                ParticipantsModel selectParticipantsModel = participantsModels.get(i);
                selectParticipantsModel.setSelected(false);
                participantsModels.set(i, selectParticipantsModel);
                if (i == 0) {
                    participantsAdapter.notifyDataSetChanged();
                } else {
                    participantsAdapter.notifyItemChanged(i);
                }
                count--;
                updateSelectedMembersText();
                break;
            }
        }

        for (int i = 0; i < selectedParticipantsModels.size(); i++) {

            if (selectedParticipantsModels.get(i).getUserId().equals(userId)) {
                selectedParticipantsModels.remove(i);
                if (i == 0) {
                    selectedParticipantsAdapter.notifyDataSetChanged();
                } else {
                    selectedParticipantsAdapter.notifyItemRemoved(i);
                }

                break;
            }
        }
    }

    private void fetchLatestParticipantsToAddToMeeting(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
        if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_participants));

        try {
            addParticipantsPresenter.fetchMembersToAddToMeeting(0, true, isSearchRequest, searchTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSelectedParticipant(String userId) {

        for (int i = 0; i < selectedParticipantsModels.size(); i++) {
            if (selectedParticipantsModels.get(i).getUserId().equals(userId)) {
                selectedParticipantsModels.remove(i);
                if (i == 0) {
                    selectedParticipantsAdapter.notifyDataSetChanged();
                } else {
                    selectedParticipantsAdapter.notifyItemRemoved(i);
                }
                break;
            }
        }
    }

    private void addSelectedParticipant(ParticipantsModel selectMembersModel) {
        selectedParticipantsModels.add(selectMembersModel);
        try {
            selectedParticipantsAdapter.notifyDataSetChanged();
            selectedParticipantsLayoutManager.scrollToPosition(selectedParticipantsModels.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSelectedMembersText() {

        if (count > 0) {
            ismBottomsheetAddParticipantsBinding.tvMembersCount.setText(getString(R.string.ism_number_of_participants_selected, String.valueOf(count), String.valueOf(MAXIMUM_MEMBERS)));
        } else {
            ismBottomsheetAddParticipantsBinding.tvMembersCount.setText(getString(R.string.ism_add_participants));
        }
    }

    private void showProgressDialog(String message) {
        if (activity != null) {
            alertDialog = alertProgress.getProgressDialog(activity, message);

            if (!activity.isFinishing() && !activity.isDestroyed()) alertDialog.show();
        }
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            ismBottomsheetAddParticipantsBinding.shimmerFrameLayout.startShimmer();
            ismBottomsheetAddParticipantsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            if (ismBottomsheetAddParticipantsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
                ismBottomsheetAddParticipantsBinding.shimmerFrameLayout.setVisibility(View.GONE);
                ismBottomsheetAddParticipantsBinding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    public void updateParameters(String meetingId, String meetingTitle) {
        this.meetingId = meetingId;
        this.meetingTitle = meetingTitle;
    }

}
