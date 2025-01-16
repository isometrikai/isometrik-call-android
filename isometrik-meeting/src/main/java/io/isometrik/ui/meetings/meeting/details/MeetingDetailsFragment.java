package io.isometrik.ui.meetings.meeting.details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmBottomsheetMeetingDetailsBinding;
import io.isometrik.ui.meetings.meeting.details.MeetingDetailsContract;
import io.isometrik.ui.meetings.meeting.details.MeetingDetailsPresenter;
import io.isometrik.ui.utils.AlertProgress;


public class MeetingDetailsFragment extends BottomSheetDialogFragment implements MeetingDetailsContract.View {

    public static final String TAG = "MeetingDetailsFragment";

    private Activity activity;
    private MeetingDetailsContract.Presenter meetingDetailsPresenter;
    private AlertProgress alertProgress;
    private AlertDialog alertDialog;
    private ArrayList<MembersModel> members;
    private MembersAdapter membersAdapter;
    private LinearLayoutManager layoutManager;
    private IsmBottomsheetMeetingDetailsBinding ismBottomsheetMeetingDetailsBinding;

    private String meetingId;
    private boolean isUserAnAdmin;

    public MeetingDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (ismBottomsheetMeetingDetailsBinding == null) {
            ismBottomsheetMeetingDetailsBinding = IsmBottomsheetMeetingDetailsBinding.inflate(inflater, container, false);
        } else {

            if (ismBottomsheetMeetingDetailsBinding.getRoot().getParent() != null) {
                ((ViewGroup) ismBottomsheetMeetingDetailsBinding.getRoot().getParent()).removeView(ismBottomsheetMeetingDetailsBinding.getRoot());
            }
        }
        ismBottomsheetMeetingDetailsBinding.tvNoMembers.setVisibility(View.GONE);
        ismBottomsheetMeetingDetailsBinding.rvMembers.setVisibility(View.GONE);
        alertProgress = new AlertProgress();
        updateShimmerVisibility(true);

        ismBottomsheetMeetingDetailsBinding.rlAddParticipants.setVisibility(isUserAnAdmin ? View.VISIBLE : View.GONE);

        layoutManager = new LinearLayoutManager(activity);
        ismBottomsheetMeetingDetailsBinding.rvMembers.setLayoutManager(layoutManager);
        ismBottomsheetMeetingDetailsBinding.rvMembers.addOnScrollListener(recyclerViewOnScrollListener);

        members = new ArrayList<>();
        membersAdapter = new MembersAdapter(activity, members, this);
        ismBottomsheetMeetingDetailsBinding.rvMembers.setAdapter(membersAdapter);

        meetingDetailsPresenter.initialize(meetingId, isUserAnAdmin);
        fetchMembers(false, null);

        ismBottomsheetMeetingDetailsBinding.refresh.setOnRefreshListener(() -> fetchMembers(false, null));
        ismBottomsheetMeetingDetailsBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fetchMembers(true, s.toString());
                } else {

                    fetchMembers(false, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //To allow scroll on member's recyclerview
        ismBottomsheetMeetingDetailsBinding.rvMembers.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            v.onTouchEvent(event);
            return true;
        });

        return ismBottomsheetMeetingDetailsBinding.getRoot();
    }

    /**
     * The Recycler view on scroll listener.
     */
    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            meetingDetailsPresenter.fetchMembersOnScroll(layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(), layoutManager.getItemCount());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        activity = getActivity();
        meetingDetailsPresenter = new MeetingDetailsPresenter();
        meetingDetailsPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        meetingDetailsPresenter.detachView();
        activity = null;
    }

    private void fetchMembers(boolean isSearchRequest, String searchTag) {

        try {
            meetingDetailsPresenter.fetchMembers(0, true, isSearchRequest, searchTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@link MeetingDetailsContract.View#onError(String)}
     */
    @Override
    public void onError(String errorMessage) {
        if (ismBottomsheetMeetingDetailsBinding.refresh.isRefreshing()) {
            ismBottomsheetMeetingDetailsBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
        hideProgressDialog();
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


    @Override
    public void onMembersReceived(ArrayList<MembersModel> memberModels, boolean refreshRequest, int membersCount) {
        if (activity != null) {
            activity.runOnUiThread(() -> {
                if (refreshRequest) {
                    members.clear();
                }
                if (membersCount != -1) {
                    ismBottomsheetMeetingDetailsBinding.tvParticipantsCount.setText(String.valueOf(membersCount));
                }
                members.addAll(memberModels);

                if (members.size() > 0) {
                    ismBottomsheetMeetingDetailsBinding.tvNoMembers.setVisibility(View.GONE);
                    ismBottomsheetMeetingDetailsBinding.rvMembers.setVisibility(View.VISIBLE);
                    membersAdapter.notifyDataSetChanged();
                } else {
                    ismBottomsheetMeetingDetailsBinding.tvNoMembers.setVisibility(View.VISIBLE);
                    ismBottomsheetMeetingDetailsBinding.rvMembers.setVisibility(View.GONE);
                }
            });
        }

        updateShimmerVisibility(false);

        if (ismBottomsheetMeetingDetailsBinding.refresh.isRefreshing()) {
            ismBottomsheetMeetingDetailsBinding.refresh.setRefreshing(false);
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

    public void updateParameters(String meetingId, boolean isUserAnAdmin) {
        this.meetingId = meetingId;
        this.isUserAnAdmin = isUserAnAdmin;
    }

    private void updateShimmerVisibility(boolean visible) {
        if (visible) {
            ismBottomsheetMeetingDetailsBinding.shimmerFrameLayout.startShimmer();
            ismBottomsheetMeetingDetailsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            if (ismBottomsheetMeetingDetailsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
                ismBottomsheetMeetingDetailsBinding.shimmerFrameLayout.setVisibility(View.GONE);
                ismBottomsheetMeetingDetailsBinding.shimmerFrameLayout.stopShimmer();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        try {
            dialog.setOnShowListener(dialog1 -> new Handler().postDelayed(() -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog1;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                @SuppressWarnings("rawtypes") BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }, 0));
        } catch (Exception ignore) {
        }
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {

        if (activity != null) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        super.onDismiss(dialog);
    }


    /**
     * Kick out member.
     *
     * @param memberId   the member id
     * @param memberName the member name
     */
    public void kickOutMember(String memberId, String memberName) {
        if (activity != null) {
            new AlertDialog.Builder(activity).setTitle(getString(R.string.ism_kick_out_member_heading, memberName)).setMessage(getString(R.string.ism_kick_out_member_alert_message)).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                dialog.cancel();
                showProgressDialog(getString(R.string.ism_kicking_out_member, memberName));
                meetingDetailsPresenter.kickOutMembers(meetingId, Collections.singletonList(memberId));
            }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
        }
    }

    /**
     * Make admin.
     *
     * @param memberId   the member id
     * @param memberName the member name
     */
    public void makeAdmin(String memberId, String memberName) {
        if (activity != null) {
            new AlertDialog.Builder(activity).setTitle(getString(R.string.ism_make_admin_alert_heading, memberName)).setMessage(getString(R.string.ism_make_admin_alert_message)).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                dialog.cancel();
                showProgressDialog(getString(R.string.ism_making_admin, memberName));
                meetingDetailsPresenter.makeAdmin(meetingId, memberId);
            }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
        }
    }

    /**
     * Remove admin.
     *
     * @param memberId   the member id
     * @param memberName the member name
     */
    public void removeAdmin(String memberId, String memberName) {
        if (activity != null) {
            new AlertDialog.Builder(activity).setTitle(getString(R.string.ism_remove_as_admin_alert_heading, memberName)).setMessage(getString(R.string.ism_remove_as_admin_alert_message)).setCancelable(true).setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                dialog.cancel();
                showProgressDialog(getString(R.string.ism_removing_admin, memberName));
                meetingDetailsPresenter.revokeAdminPermissions(meetingId, memberId);
            }).setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel()).create().show();
        }
    }

    @Override
    public void onMemberRemovedSuccessfully(String memberId, int membersCount) {
        hideProgressDialog();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                for (int i = 0; i < members.size(); i++) {

                    if (members.get(i).getMemberId().equals(memberId)) {

                        members.remove(i);
                        membersAdapter.notifyItemRemoved(i);
                        break;
                    }
                }
                ismBottomsheetMeetingDetailsBinding.tvParticipantsCount.setText(getString(R.string.ism_participants_count, membersCount));
            });
        }
    }

    @Override
    public void onMemberAdminPermissionsUpdated(String memberId, boolean admin) {
        hideProgressDialog();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                for (int i = 0; i < members.size(); i++) {

                    if (members.get(i).getMemberId().equals(memberId)) {

                        MembersModel membersModel = members.get(i);
                        membersModel.setAdmin(admin);
                        members.set(i, membersModel);
                        membersAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            });
        }
    }
}
