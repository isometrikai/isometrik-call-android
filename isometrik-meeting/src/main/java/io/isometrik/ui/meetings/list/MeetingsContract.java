package io.isometrik.ui.meetings.list;

import android.content.Intent;

import java.util.ArrayList;

/**
 * The interface meetings contract containing interfaces Presenter and View to be implemented
 * by the
 * MeetingsPresenter{@link MeetingsPresenter} and
 * MeetingsActivity{@link MeetingsActivity} respectively.
 *
 * @see MeetingsPresenter
 * @see MeetingsActivity
 */
public interface MeetingsContract {

    /**
     * The interface MeetingsContract.Presenter to be implemented by MeetingsPresenter{@link
     * MeetingsPresenter}*
     *
     * @see MeetingsPresenter
     */
    interface Presenter {

        /**
         * Request meetings data.
         *
         * @param offset          the skip
         * @param refreshRequest  whether to fetch the first page of meetings or the specific page of
         *                        meetings,with paging
         * @param isSearchRequest the is search request
         * @param searchTag       the search tag
         */
        void requestMeetingsData(int offset, boolean refreshRequest, boolean isSearchRequest,
                                 String searchTag);

        /**
         * Request meetings data on scroll.
         *
         * @param firstVisibleItemPosition the first visible item position in the recyclerview
         *                                 containing list of meetings
         * @param visibleItemCount         the visible item count in the recyclerview containing list of meetings
         * @param totalItemCount           the total number of meetings in the recyclerview containing list of meetings
         */
        void requestMeetingsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
                                         int totalItemCount);

        void joinMeeting(String meetingId, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean audioOnly, boolean hdMeeting);


        void registerMeetingEventListener();

        void unregisterMeetingEventListener();

    }

    /**
     * The interface MeetingsContract.View to be implemented by MeetingsActivity{@link
     * MeetingsActivity}*
     *
     * @see MeetingsActivity
     */
    interface View {

        /**
         * On meetings data received.
         *
         * @param meetings       the list of meetings MeetingsModel{@link MeetingsModel} fetched
         * @param latestMeetings whether the list of meetings fetched is for the first page or with paging
         * @see MeetingsModel
         */
        void onMeetingsDataReceived(ArrayList<MeetingsModel> meetings, boolean latestMeetings);

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);

        void onMeetingJoined(String meetingId, String rtcToken, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean audioOnly, boolean hdMeeting, long joinMeetingTime);

        void onMeetingEnded(String meetingId);

        void onMeetingCreated(MeetingsModel meetingsModel);

        void onNewIncomingCall(Intent intent);

    }
}
