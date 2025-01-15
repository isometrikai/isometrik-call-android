package io.isometrik.ui.meetings.meeting.add;

import java.util.ArrayList;

import io.isometrik.ui.utils.BasePresenter;

/**
 * The interface add participants contract containing presenter and view interfaces implemented
 * by AddParticipantsPresenter and AddParticipantsActivity respectively.
 */
public interface AddParticipantsContract {
    /**
     * The interface Presenter.
     */
    interface Presenter extends BasePresenter<View> {

        /**
         * Add members.
         *
         * @param participantsModels the participants models
         */
        void addMembers(ArrayList<ParticipantsModel> participantsModels);

        /**
         * Request members to add to meeting data.
         *
         * @param offset          the offset
         * @param refreshRequest  the refresh request
         * @param isSearchRequest whether fetch users request is from the search or not
         * @param searchTag       the user's name to be searched
         */
        void fetchMembersToAddToMeeting(int offset, boolean refreshRequest,
                                        boolean isSearchRequest, String searchTag);

        /**
         * Request members to add to meeting data on scroll.
         *
         * @param firstVisibleItemPosition the first visible item position
         * @param visibleItemCount         the visible item count
         * @param totalItemCount           the total item count
         */
        void requestMembersToAddToMeetingOnScroll(int firstVisibleItemPosition,
                                                  int visibleItemCount, int totalItemCount);

        void initialize(String meetingId);
    }

    /**
     * The interface View.
     */
    interface View {

        /**
         * On members added successfully.
         */
        void onMembersAddedSuccessfully();

        /**
         * On members to add to meeting fetched.
         *
         * @param participantsModels the participants models
         * @param latestUsers        the latest users
         * @param isSearchRequest    the is search request
         */
        void onMembersToAddToMeetingFetched(ArrayList<ParticipantsModel> participantsModels,
                                            boolean latestUsers, boolean isSearchRequest);

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);
    }
}
