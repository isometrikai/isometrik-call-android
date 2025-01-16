package io.isometrik.ui.meetings.create.onetoone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.util.ArrayList;

import io.isometrik.meeting.R;
import io.isometrik.meeting.databinding.IsmSelectOpponentItemBinding;
import io.isometrik.ui.meetings.create.UsersModel;
import io.isometrik.ui.meetings.create.onetoone.enums.CallType;
import com.bumptech.glide.Glide;
import io.isometrik.ui.utils.PlaceholderUtils;

/**
 * The recycler view adapter for the list of the users.
 */
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final ArrayList<UsersModel> users;

    /**
     * Instantiates a new Users adapter.
     *
     * @param mContext the m context
     * @param users    the users
     */
    public UsersAdapter(Context mContext, ArrayList<UsersModel> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        IsmSelectOpponentItemBinding ismSelectOpponentItemBinding = IsmSelectOpponentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new UsersViewHolder(ismSelectOpponentItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        UsersViewHolder holder = (UsersViewHolder) viewHolder;

        try {
            UsersModel user = users.get(position);
            if (user != null) {
                holder.ismSelectOpponentItemBinding.tvUserName.setText(user.getUserName());
                holder.ismSelectOpponentItemBinding.tvUserIdentifier.setText(user.getUserIdentifier());
                if (PlaceholderUtils.isValidImageUrl(user.getUserProfileImageUrl())) {

                    try {
                        Glide.with(mContext).load(user.getUserProfileImageUrl()).placeholder(R.drawable.ism_ic_profile).transform(new CircleCrop()).into(holder.ismSelectOpponentItemBinding.ivUserImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {
                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(), holder.ismSelectOpponentItemBinding.ivUserImage, position, 20);
                }

                holder.ismSelectOpponentItemBinding.rlVideo.setOnClickListener(view -> ((InitiateCallActivity) mContext).initiateCall(CallType.VideoCall, user));

                holder.ismSelectOpponentItemBinding.rlAudio.setOnClickListener(view -> ((InitiateCallActivity) mContext).initiateCall(CallType.AudioCall, user));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The type Users view holder.
     */
    static class UsersViewHolder extends RecyclerView.ViewHolder {
        private final IsmSelectOpponentItemBinding ismSelectOpponentItemBinding;

        /**
         * Instantiates a new Users view holder.
         *
         * @param ismSelectOpponentItemBinding the ism users item binding
         */
        public UsersViewHolder(final IsmSelectOpponentItemBinding ismSelectOpponentItemBinding) {
            super(ismSelectOpponentItemBinding.getRoot());
            this.ismSelectOpponentItemBinding = ismSelectOpponentItemBinding;
        }
    }
}