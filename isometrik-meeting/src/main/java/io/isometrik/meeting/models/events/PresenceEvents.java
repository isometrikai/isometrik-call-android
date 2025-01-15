package io.isometrik.meeting.models.events;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.action.JoinMeetingAcceptEvent;
import io.isometrik.meeting.events.action.JoinMeetingRejectEvent;
import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToNoUserPublishingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToRejectionByAllEvent;
import io.isometrik.meeting.events.meeting.EndMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.AddAdminEvent;
import io.isometrik.meeting.events.membershipcontrol.AddMembersEvent;
import io.isometrik.meeting.events.membershipcontrol.JoinPublicMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.LeaveMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.meeting.events.membershipcontrol.RemoveMembersEvent;
import io.isometrik.meeting.events.publish.PublishStartEvent;
import io.isometrik.meeting.events.publish.PublishStopEvent;
import io.isometrik.meeting.events.recording.RecordingStartEvent;
import io.isometrik.meeting.events.recording.RecordingStopEvent;

/**
 * The helper class to parse and announce presence events on respective registered realtime event
 * listeners for-
 * events.
 */
public class PresenceEvents {
    /**
     * Handle presence event.
     *
     * @param jsonObject        the json object
     * @param isometrikInstance the isometrik instance
     * @throws JSONException the json exception
     */
    public void handlePresenceEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance) throws JSONException {

        String action = jsonObject.getString("action");

        switch (action) {

            case "joinRequestAccept":

                isometrikInstance.getRealtimeEventsListenerManager().getJoinActionsListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), JoinMeetingAcceptEvent.class));
                break;

            case "joinRequestReject":

                isometrikInstance.getRealtimeEventsListenerManager().getJoinActionsListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), JoinMeetingRejectEvent.class));
                break;

            case "meetingCreated":

                isometrikInstance.getRealtimeEventsListenerManager().getMeetingListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), CreateMeetingEvent.class));
                break;

            case "meetingEndedDueToNoUserPublishing":

                isometrikInstance.getRealtimeEventsListenerManager().getMeetingListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), EndMeetingDueToNoUserPublishingEvent.class));
                break;

            case "meetingEndedDueToRejectionByAll":

                isometrikInstance.getRealtimeEventsListenerManager().getMeetingListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), EndMeetingDueToRejectionByAllEvent.class));
                break;

            case "meetingEndedByHost":

                isometrikInstance.getRealtimeEventsListenerManager().getMeetingListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), EndMeetingEvent.class));
                break;

            case "addAdmin":

                isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), AddAdminEvent.class));
                break;

            case "membersAdd":

                isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), AddMembersEvent.class));
                break;

            case "memberJoin":

                isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), JoinPublicMeetingEvent.class));
                break;

            case "memberLeave":

                isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), LeaveMeetingEvent.class));
                break;

            case "removeAdmin":

                isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), RemoveAdminEvent.class));
                break;

            case "membersRemove":

                isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), RemoveMembersEvent.class));
                break;

            case "publishingStarted":

                isometrikInstance.getRealtimeEventsListenerManager().getPublishListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), PublishStartEvent.class));
                break;

            case "publishingStopped":

                isometrikInstance.getRealtimeEventsListenerManager().getPublishListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), PublishStopEvent.class));
                break;

            case "recordingStarted":

                isometrikInstance.getRealtimeEventsListenerManager().getRecordingListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), RecordingStartEvent.class));
                break;

            case "recordingStopped":

                isometrikInstance.getRealtimeEventsListenerManager().getRecordingListenerManager().announce(isometrikInstance.getGson().fromJson(jsonObject.toString(), RecordingStopEvent.class));
                break;

        }
    }
}
