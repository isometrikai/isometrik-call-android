package io.isometrik.ui.meetings.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmMeetingItemBinding;
import io.isometrik.ui.meetings.list.MeetingsActivity;
import io.isometrik.ui.meetings.list.MeetingsModel;

/**
 * The recycler view adapter for the list of the meetings.
 */
public class MeetingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final ArrayList<MeetingsModel> meetings;

    /**
     * Instantiates a new meetings adapter.
     *
     * @param mContext the m context
     * @param meetings the meetings
     */
    MeetingsAdapter(Context mContext, ArrayList<MeetingsModel> meetings) {
        this.mContext = mContext;
        this.meetings = meetings;
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        IsmMeetingItemBinding ismMeetingItemBinding = IsmMeetingItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new MeetingsViewHolder(ismMeetingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        MeetingsViewHolder holder = (MeetingsViewHolder) viewHolder;

        try {
            MeetingsModel meeting = meetings.get(position);
            if (meeting != null) {
                holder.ismMeetingItemBinding.tvMeetingTitle.setText(meeting.getMeetingDescription());
                holder.ismMeetingItemBinding.tvMeetingDetails.setText(mContext.getString(R.string.ism_meeting_details, meeting.getInitiatorName(), meeting.getDuration(), meeting.getMembersCount()));

                if (meeting.isSelfHosted()) {
                    holder.ismMeetingItemBinding.tvJoinMeeting.setVisibility(View.VISIBLE);
                    holder.ismMeetingItemBinding.tvJoinMeeting.setOnClickListener(v -> ((MeetingsActivity) mContext).joinMeeting(meeting.getMeetingId(), meeting.getOpponentName(), meeting.getOpponentImageUrl(), meeting.getMeetingCreationTime(), meeting.isAudioOnly(), meeting.isHdMeeting()));
                } else {
                    holder.ismMeetingItemBinding.tvJoinMeeting.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The type Meetings view holder.
     */
    static class MeetingsViewHolder extends RecyclerView.ViewHolder {
        private final IsmMeetingItemBinding ismMeetingItemBinding;

        /**
         * Instantiates a new Meetings view holder.
         *
         * @param ismMeetingItemBinding the ism meetings item binding
         */
        public MeetingsViewHolder(final IsmMeetingItemBinding ismMeetingItemBinding) {
            super(ismMeetingItemBinding.getRoot());
            this.ismMeetingItemBinding = ismMeetingItemBinding;
        }
    }
}