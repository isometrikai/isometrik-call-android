package io.isometrik.ui.meetings.meeting.core;


import io.isometrik.ui.meetings.meeting.core.MeetingPresenter;

/**
 * The interface meeting contract containing interfaces Presenter and View to be implemented
 * by the
 * MeetingPresenter{@link MeetingPresenter} and
 * MeetingActivity{@link MeetingActivity} respectively.
 *
 * @see MeetingPresenter
 * @see MeetingActivity
 */
public interface MeetingContract {

    /**
     * The interface MeetingContract.Presenter to be implemented by MeetingPresenter{@link
     * MeetingPresenter}*
     *
     * @see MeetingPresenter
     */
    interface Presenter {

        void endMeeting(String meetingId);

        void registerMeetingEventListener();

        void unregisterMeetingEventListener();

        void registerJoinActionsEventListener();

        void unregisterJoinActionsEventListener();

        void registerMessageEventListener();

        void unregisterMessageEventListener();
    }

    /**
     * The interface MeetingContract.View to be implemented by MeetingActivity{@link
     * MeetingActivity}*
     *
     * @see MeetingActivity
     */
    interface View {

        void onError(String meetingId, String errorMessage);


        void onMeetingEnded(String meetingId);

        void onJoinMeetingAccepted(String meetingId, long joinMeetingTime);

        void onRingingForOpponent(String meetingId);
    }
}
