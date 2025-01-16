package io.isometrik.ui.meetings.incoming;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.action.AcceptJoinMeetingRequestQuery;
import io.isometrik.meeting.builder.action.RejectJoinMeetingRequestQuery;
import io.isometrik.meeting.builder.meetings.FetchMeetingsQuery;
import io.isometrik.meeting.callbacks.MeetingEventCallback;
import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToNoUserPublishingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToRejectionByAllEvent;
import io.isometrik.meeting.events.meeting.EndMeetingEvent;
import io.isometrik.ui.IsometrikCallSdk;


public class IncomingCallPresenter implements IncomingCallContract.Presenter {


    IncomingCallPresenter(IncomingCallContract.View incomingCallsView) {
        this.incomingCallsView = incomingCallsView;
    }

    private final IncomingCallContract.View incomingCallsView;

    private final Isometrik isometrik = IsometrikCallSdk.getInstance().getIsometrik();
    private final String userToken = IsometrikCallSdk.getInstance().getUserSession().getUserToken();

    @Override
    public void acceptJoinMeetingRequest(String meetingId) {
        isometrik.getRemoteUseCases().getActionUseCases().acceptJoinMeetingRequest(new AcceptJoinMeetingRequestQuery.Builder().setDeviceId(IsometrikCallSdk.getInstance().getUserSession().getDeviceId()).setMeetingId(meetingId).setUserToken(userToken).build(), (var1, var2) -> {
            if (var1 != null) {
                incomingCallsView.onJoinMeetingRequestAccepted(meetingId, var1.getRtcToken(), var1.getJoinTime());
            } else {
                if (var2.getHttpResponseCode() == 400) {
                    //Assuming meeting already ended, fails to accept join request
                    incomingCallsView.onMeetingEnded(meetingId);
                } else {
                    incomingCallsView.onError(var2.getErrorMessage());
                }
            }
        });
    }

    @Override
    public void rejectJoinMeetingRequest(String meetingId) {
        isometrik.getRemoteUseCases().getActionUseCases().rejectJoinMeetingRequest(new RejectJoinMeetingRequestQuery.Builder().setMeetingId(meetingId).setUserToken(userToken).build(), (var1, var2) -> {
            if (var1 != null) {
                incomingCallsView.onJoinMeetingRequestRejected(meetingId);
            } else {
                if (var2.getHttpResponseCode() == 400) {
                    //Assuming meeting already ended, fails to reject join request
                    incomingCallsView.onMeetingEnded(meetingId);
                } else {
                    incomingCallsView.onError(var2.getErrorMessage());
                }

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

            incomingCallsView.onMeetingEnded(endMeetingDueToNoUserPublishingEvent.getMeetingId());
        }

        @Override
        public void meetingEndedDueToRejectionByAll(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToRejectionByAllEvent endMeetingDueToRejectionByAllEvent) {
            incomingCallsView.onMeetingEnded(endMeetingDueToRejectionByAllEvent.getMeetingId());

        }

        @Override
        public void meetingEnded(@NotNull Isometrik isometrik, @NotNull EndMeetingEvent endMeetingEvent) {
            incomingCallsView.onMeetingEnded(endMeetingEvent.getMeetingId());

        }
    };

    @Override
    public void checkIfMeetingStillActive(String meetingId) {
        isometrik.getRemoteUseCases().getMeetingUseCases().fetchMeetings(new FetchMeetingsQuery.Builder().setMeetingIds(Collections.singletonList(meetingId)).setUserToken(userToken).build(), (var1, var2) -> {
            if (var2 != null) {
                if (var2.getHttpResponseCode() == 404) {
                    //Assuming meeting already ended
                    incomingCallsView.onMeetingEnded(meetingId);
                }

            }
        });
    }
}
