package io.isometrik.ui.meetings.meeting.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.ArrayList;

import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmMeetingMemberItemBinding;
import io.isometrik.ui.meetings.meeting.details.MeetingDetailsFragment;
import io.isometrik.ui.meetings.meeting.details.MembersModel;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.ui.utils.PlaceholderUtils;

/**
 * The recycler view adapter for the list of the members in a meeting.
 */
public class MembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final ArrayList<MembersModel> membersModels;
    private final MeetingDetailsFragment meetingDetailsFragment;

    /**
     * Instantiates a new Members watchers adapter.
     *
     * @param mContext      the m context
     * @param membersModels the members models
     */
    public MembersAdapter(Context mContext, ArrayList<MembersModel> membersModels, MeetingDetailsFragment meetingDetailsFragment) {
        this.mContext = mContext;
        this.membersModels = membersModels;
        this.meetingDetailsFragment = meetingDetailsFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        IsmMeetingMemberItemBinding ismMeetingMemberItemBinding = IsmMeetingMemberItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new MemberViewHolder(ismMeetingMemberItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MemberViewHolder holder = (MemberViewHolder) viewHolder;

        try {
            MembersModel membersModel = membersModels.get(position);
            if (membersModel != null) {

                holder.ismMeetingMemberItemBinding.tvUserName.setText(membersModel.getMemberName());

                if (membersModel.isOnline()) {
                    holder.ismMeetingMemberItemBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
                } else {
                    holder.ismMeetingMemberItemBinding.ivOnlineStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
                }

                if (membersModel.isUserAnAdmin() && !membersModel.isSelf()) {
                    holder.ismMeetingMemberItemBinding.ivKickOut.setVisibility(View.VISIBLE);
                    holder.ismMeetingMemberItemBinding.ivKickOut.setOnClickListener(v -> {

                        if (meetingDetailsFragment != null) {
                            meetingDetailsFragment.kickOutMember(membersModel.getMemberId(), membersModel.getMemberName());
                        }
                    });
                } else {
                    holder.ismMeetingMemberItemBinding.ivKickOut.setVisibility(View.GONE);
                }
                if (membersModel.isAdmin()) {
                    holder.ismMeetingMemberItemBinding.tvAdmin.setText(mContext.getString(R.string.ism_admin));
                    holder.ismMeetingMemberItemBinding.tvAdmin.setVisibility(View.VISIBLE);

                    if (membersModel.isUserAnAdmin() && !membersModel.isSelf()) {
                        holder.ismMeetingMemberItemBinding.ivRemoveAdmin.setVisibility(View.VISIBLE);

                        holder.ismMeetingMemberItemBinding.ivRemoveAdmin.setOnClickListener(v -> {

                            if (meetingDetailsFragment != null) {
                                meetingDetailsFragment.removeAdmin(membersModel.getMemberId(), membersModel.getMemberName());
                            }
                        });
                    } else {
                        holder.ismMeetingMemberItemBinding.ivRemoveAdmin.setVisibility(View.GONE);
                    }
                } else {
                    holder.ismMeetingMemberItemBinding.ivRemoveAdmin.setVisibility(View.GONE);
                    if (membersModel.isUserAnAdmin()) {
                        holder.ismMeetingMemberItemBinding.tvAdmin.setText(mContext.getString(R.string.ism_make_admin));
                        holder.ismMeetingMemberItemBinding.tvAdmin.setVisibility(View.VISIBLE);

                        holder.ismMeetingMemberItemBinding.tvAdmin.setOnClickListener(v -> {

                            if (meetingDetailsFragment != null) {
                                meetingDetailsFragment.makeAdmin(membersModel.getMemberId(), membersModel.getMemberName());
                            }
                        });
                    } else {
                        holder.ismMeetingMemberItemBinding.tvAdmin.setVisibility(View.GONE);
                    }
                }
                if (PlaceholderUtils.isValidImageUrl(membersModel.getMemberProfileImageUrl())) {

                    try {
                        GlideApp.with(mContext).load(membersModel.getMemberProfileImageUrl()).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(holder.ismMeetingMemberItemBinding.ivProfilePic);
                    } catch (IllegalArgumentException | NullPointerException ignore) {
                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(mContext, membersModel.getMemberName(), holder.ismMeetingMemberItemBinding.ivProfilePic, position, 10);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return membersModels.size();
    }

    /**
     * The type Member view holder.
     */
    static class MemberViewHolder extends RecyclerView.ViewHolder {

        private final IsmMeetingMemberItemBinding ismMeetingMemberItemBinding;

        /**
         * Instantiates a new Member view holder.
         *
         * @param ismMeetingMemberItemBinding the ism meeting member item binding
         */
        public MemberViewHolder(final IsmMeetingMemberItemBinding ismMeetingMemberItemBinding) {
            super(ismMeetingMemberItemBinding.getRoot());
            this.ismMeetingMemberItemBinding = ismMeetingMemberItemBinding;
        }
    }
}
