package io.isometrik.ui.meetings.create;

import java.util.ArrayList;

import io.isometrik.ui.meetings.create.CreateMeetingPresenter;
import io.isometrik.ui.meetings.create.UsersModel;
import io.isometrik.ui.meetings.create.group.CreateMeetingActivity;

/**
 * The interface create meeting contract containing interfaces Presenter and View to be implemented
 * by the
 * CreateMeetingPresenter{@link CreateMeetingPresenter} and
 * CreateMeetingActivity{@link CreateMeetingActivity} respectively.
 *
 * @see CreateMeetingPresenter
 * @see CreateMeetingActivity
 */
public interface CreateMeetingContract {

    /**
     * The interface CreateMeetingContract.Presenter to be implemented by CreateMeetingPresenter{@link
     * CreateMeetingPresenter}*
     *
     * @see CreateMeetingPresenter
     */
    interface Presenter {

        /**
         * Fetch non blocked users.
         *
         * @param skip            the skip
         * @param refreshRequest  the refresh request
         * @param isSearchRequest the is search request
         * @param searchTag       the search tag
         */
        void fetchNonBlockedUsers(int skip, boolean refreshRequest, boolean isSearchRequest, String searchTag);

        /**
         * Request non blocked users on scroll.
         *
         * @param firstVisibleItemPosition the first visible item position
         * @param visibleItemCount         the visible item count
         * @param totalItemCount           the total item count
         */
        void fetchNonBlockedUsersOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);


        void createMeeting(String memberId, String meetingDescription, String opponentName, String opponentImageUrl, String customType);
    }

    /**
     * The interface CreateMeetingContract.View to be implemented by CreateMeetingActivity{@link
     * CreateMeetingActivity}*
     *
     * @see CreateMeetingActivity
     */
    interface View {

        /**
         * On non blocked users fetched successfully.
         *
         * @param nonBlockedUsers the non blocked users
         * @param resultsOnScroll the results on scroll
         */
        void onNonBlockedUsersFetchedSuccessfully(ArrayList<UsersModel> nonBlockedUsers, boolean resultsOnScroll);


        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);


        void onMeetingCreated(String meetingId, String rtcToken, String opponentName, String opponentImageUrl, long meetingCreationTime, String customType);


    }
}
