package io.isometrik.ui.meetings.list;

import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.meetings.FetchMeetingsQuery;
import io.isometrik.meeting.builder.publish.StartPublishingQuery;
import io.isometrik.meeting.callbacks.MeetingEventCallback;
import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToNoUserPublishingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToRejectionByAllEvent;
import io.isometrik.meeting.events.meeting.EndMeetingEvent;
import io.isometrik.meeting.events.meeting.utils.MeetingMembers;
import io.isometrik.meeting.response.meeting.utils.Meeting;
import io.isometrik.ui.IsometrikCallSdk;
import io.isometrik.ui.notifications.NotificationGenerator;
import io.isometrik.ui.utils.ApplicationStateHelper;
import io.isometrik.ui.utils.Constants;
import io.isometrik.ui.utils.MeetingsNotifiedToUsers;


public class MeetingsPresenter implements MeetingsContract.Presenter {

    MeetingsPresenter(MeetingsContract.View meetingsView) {
        this.meetingsView = meetingsView;
    }

    private final MeetingsContract.View meetingsView;
    private int offset;
    private boolean isLastPage;
    private boolean isLoading;
    private final int PAGE_SIZE = Constants.MEETINGS_PAGE_SIZE;

    private boolean isSearchRequest;
    private String searchTag;
    private final Isometrik isometrik = IsometrikCallSdk.getInstance().getIsometrik();
    private final String userToken = IsometrikCallSdk.getInstance().getUserSession().getUserToken();
    private final String userId = IsometrikCallSdk.getInstance().getUserSession().getUserId();

    @Override
    public void requestMeetingsData(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag) {
        isLoading = true;

        if (refreshRequest) {
            this.offset = 0;
            isLastPage = false;
        }
        this.isSearchRequest = isSearchRequest;
        this.searchTag = isSearchRequest ? searchTag : null;

        FetchMeetingsQuery.Builder fetchMeetingsQuery = new FetchMeetingsQuery.Builder().setIncludeMembers(true).setMembersLimit(Constants.MEETING_MEMBERS_PAGE_SIZE).setLimit(PAGE_SIZE).setSkip(offset).setSort(Constants.SORT_ORDER_ASC).setUserToken(userToken);
        if (isSearchRequest && searchTag != null) {
            fetchMeetingsQuery.setSearchTag(searchTag);
        }
        isometrik.getRemoteUseCases().getMeetingUseCases().fetchMeetings(fetchMeetingsQuery.build(), (var1, var2) -> {

            isLoading = false;

            if (var1 != null) {

                ArrayList<MeetingsModel> meetingsModels = new ArrayList<>();
                ArrayList<Meeting> meetings = var1.getMeetings();
                int size = meetings.size();

                for (int i = 0; i < size; i++) {

                    meetingsModels.add(new MeetingsModel(meetings.get(i)));
                }
                if (size < PAGE_SIZE) {

                    isLastPage = true;
                }
                meetingsView.onMeetingsDataReceived(meetingsModels, refreshRequest);

            } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                    if (refreshRequest) {
                        //No meetings found

                        meetingsView.onMeetingsDataReceived(new ArrayList<>(), true);

                    } else {
                        isLastPage = true;
                    }
                } else {

                    meetingsView.onError(var2.getErrorMessage());

                }
            }
        });
    }

    /**
     * {@link MeetingsContract.Presenter#requestMeetingsDataOnScroll(int, int, int)}
     */
    @Override
    public void requestMeetingsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

        if (!isLoading && !isLastPage) {

            if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount) && firstVisibleItemPosition >= 0 && (totalItemCount) >= PAGE_SIZE) {

                offset++;
                requestMeetingsData(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
            }
        }
    }

    @Override
    public void joinMeeting(String meetingId, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean audioOnly, boolean hdMeeting) {
        isometrik.getRemoteUseCases().getPublishUseCases().startPublishing(new StartPublishingQuery.Builder().setDeviceId(IsometrikCallSdk.getInstance().getUserSession().getDeviceId()).setMeetingId(meetingId).setUserToken(userToken).build(), (var1, var2) -> {
            if (var1 != null) {
                meetingsView.onMeetingJoined(meetingId, var1.getRtcToken(), opponentName, opponentImageUrl, meetingCreationTime, audioOnly, hdMeeting, var1.getJoinTime());
            } else {
                meetingsView.onError(var2.getErrorMessage());
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

            String opponentName, opponentImageUrl;

            List<MeetingMembers> members = createMeetingEvent.getMembers();

            if (members.get(0).getMemberId().equals(userId)) {
                opponentName = members.get(1).getMemberName();
                opponentImageUrl = members.get(1).getMemberProfileImageUrl();
            } else {
                opponentName = members.get(0).getMemberName();
                opponentImageUrl = members.get(0).getMemberProfileImageUrl();
            }
            meetingsView.onMeetingCreated(new MeetingsModel(createMeetingEvent));
            if (!userId.equals(createMeetingEvent.getCreatedBy())) {
                Intent intent = MeetingsNotifiedToUsers.getInstance().addMeetingNotified(IsometrikCallSdk.getInstance().getContext(), createMeetingEvent.getMeetingId(), createMeetingEvent.isAudioOnly(), createMeetingEvent.isHdMeeting(), opponentName, opponentImageUrl, createMeetingEvent.getCreationTime(), false);

                if (intent != null) {

                    boolean isApplicationInForeground = ApplicationStateHelper.isApplicationInForeground();
                    if (IsometrikCallSdk.getInstance().getUserSession().isUserBusy() || !isApplicationInForeground) {

                        NotificationGenerator.generateIncomingCallNotification(createMeetingEvent.getCustomType(), createMeetingEvent.getCreatedBy(), createMeetingEvent.getMeetingId(), createMeetingEvent.getInitiatorName(), createMeetingEvent.getInitiatorImageUrl(), createMeetingEvent.getCreationTime(), createMeetingEvent.isHdMeeting(), createMeetingEvent.isAudioOnly(), intent, IsometrikCallSdk.getInstance().getContext());
                    } else {

                        meetingsView.onNewIncomingCall(intent);
                    }
                }
            }
        }

        @Override
        public void meetingEndedDueToNoUserPublishing(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToNoUserPublishingEvent endMeetingDueToNoUserPublishingEvent) {
            meetingsView.onMeetingEnded(endMeetingDueToNoUserPublishingEvent.getMeetingId());
        }

        @Override
        public void meetingEndedDueToRejectionByAll(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToRejectionByAllEvent endMeetingDueToRejectionByAllEvent) {
            meetingsView.onMeetingEnded(endMeetingDueToRejectionByAllEvent.getMeetingId());
        }

        @Override
        public void meetingEnded(@NotNull Isometrik isometrik, @NotNull EndMeetingEvent endMeetingEvent) {
            meetingsView.onMeetingEnded(endMeetingEvent.getMeetingId());
        }
    };

}
