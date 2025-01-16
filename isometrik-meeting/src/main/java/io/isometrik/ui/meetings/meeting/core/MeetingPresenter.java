package io.isometrik.ui.meetings.meeting.core;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.meetings.LeaveMeetingQuery;
import io.isometrik.meeting.callbacks.JoinActionsEventCallback;
import io.isometrik.meeting.callbacks.MeetingEventCallback;
import io.isometrik.meeting.callbacks.MessageEventCallback;
import io.isometrik.meeting.events.action.JoinMeetingAcceptEvent;
import io.isometrik.meeting.events.action.JoinMeetingRejectEvent;
import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToNoUserPublishingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToRejectionByAllEvent;
import io.isometrik.meeting.events.meeting.EndMeetingEvent;
import io.isometrik.meeting.events.message.PublishMessageEvent;
import io.isometrik.ui.IsometrikCallSdk;
import io.isometrik.meeting.R;


public class MeetingPresenter implements MeetingContract.Presenter {

    /**
     * Instantiates a new users presenter.
     *
     * @param meetingView the meeting view
     */
    MeetingPresenter(MeetingContract.View meetingView) {
        this.meetingView = meetingView;
    }

    private final MeetingContract.View meetingView;
    private final Isometrik isometrik = IsometrikCallSdk.getInstance().getIsometrik();
    private final String userToken = IsometrikCallSdk.getInstance().getUserSession().getUserToken();


    @Override
    public void endMeeting(String meetingId) {
        isometrik.getRemoteUseCases().getMeetingUseCases().leaveMeeting(new LeaveMeetingQuery.Builder().setMeetingId(meetingId).setUserToken(userToken).build(), (var1, var2) -> {
            if (var1 != null) {
                meetingView.onMeetingEnded(meetingId);
            } else {
                meetingView.onError(meetingId, var2.getErrorMessage());
            }
        });
    }

    @Override
    public void registerMeetingEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMeetingListenerManager().addListener(meetingEventCallback);
    }

    @Override
    public void unregisterMeetingEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMeetingListenerManager().removeListener(meetingEventCallback);
    }

    private final MeetingEventCallback meetingEventCallback = new MeetingEventCallback() {
        @Override
        public void meetingCreated(@NotNull Isometrik isometrik, @NotNull CreateMeetingEvent createMeetingEvent) {
            //TODO Nothing
        }

        @Override
        public void meetingEndedDueToNoUserPublishing(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToNoUserPublishingEvent endMeetingDueToNoUserPublishingEvent) {
            meetingView.onMeetingEnded(endMeetingDueToNoUserPublishingEvent.getMeetingId());
        }

        @Override
        public void meetingEndedDueToRejectionByAll(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToRejectionByAllEvent endMeetingDueToRejectionByAllEvent) {
            meetingView.onMeetingEnded(endMeetingDueToRejectionByAllEvent.getMeetingId());
        }

        @Override
        public void meetingEnded(@NotNull Isometrik isometrik, @NotNull EndMeetingEvent endMeetingEvent) {
            meetingView.onMeetingEnded(endMeetingEvent.getMeetingId());
        }
    };

    @Override
    public void registerJoinActionsEventListener() {
        isometrik.getRealtimeEventsListenerManager().getJoinActionsListenerManager().addListener(joinActionsEventCallback);

    }

    @Override
    public void unregisterJoinActionsEventListener() {
        isometrik.getRealtimeEventsListenerManager().getJoinActionsListenerManager().removeListener(joinActionsEventCallback);

    }

    private final JoinActionsEventCallback joinActionsEventCallback = new JoinActionsEventCallback() {
        @Override
        public void joinMeetingAccepted(@NotNull Isometrik isometrik, @NotNull JoinMeetingAcceptEvent joinMeetingAcceptEvent) {
            meetingView.onJoinMeetingAccepted(joinMeetingAcceptEvent.getMeetingId(), joinMeetingAcceptEvent.getSentAt());
        }

        @Override
        public void joinMeetingRejected(@NotNull Isometrik isometrik, @NotNull JoinMeetingRejectEvent joinMeetingRejectEvent) {
            meetingView.onMeetingEnded(joinMeetingRejectEvent.getMeetingId());
        }
    };

    @Override
    public void registerMessageEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMessageListenerManager().addListener(messageEventCallback);

    }

    @Override
    public void unregisterMessageEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMessageListenerManager().removeListener(messageEventCallback);

    }

    private final MessageEventCallback messageEventCallback = new MessageEventCallback() {
        @Override
        public void messagePublished(@NotNull Isometrik isometrik, @NotNull PublishMessageEvent publishMessageEvent) {
            {
                if (!publishMessageEvent.getSenderId().equals(IsometrikCallSdk.getInstance().getUserSession().getUserId())) {

                    if (publishMessageEvent.getMessage().equals(IsometrikCallSdk.getInstance().getContext().getString(R.string.ism_call_ringing_event))) {
                        meetingView.onRingingForOpponent(publishMessageEvent.getMeetingId());
                    }
                }

            }
        }
    };
}
