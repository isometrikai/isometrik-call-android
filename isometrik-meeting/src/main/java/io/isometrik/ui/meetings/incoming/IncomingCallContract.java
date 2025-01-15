package io.isometrik.ui.meetings.incoming;

public interface IncomingCallContract {


    interface Presenter {

        void acceptJoinMeetingRequest(String meetingId);

        void rejectJoinMeetingRequest(String meetingId);

        void checkIfMeetingStillActive(String meetingId);

        void registerMeetingEventListener();

        void unregisterMeetingEventListener();
    }


    interface View {


        void onJoinMeetingRequestAccepted(String meetingId, String rtcToken, long joinMeetingTime);

        void onJoinMeetingRequestRejected(String meetingId);

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);

        void onMeetingEnded(String meetingId);
    }
}
