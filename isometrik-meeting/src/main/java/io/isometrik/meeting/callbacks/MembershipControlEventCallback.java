package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.membershipcontrol.AddAdminEvent;
import io.isometrik.meeting.events.membershipcontrol.AddMembersEvent;
import io.isometrik.meeting.events.membershipcontrol.JoinPublicMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.LeaveMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.meeting.events.membershipcontrol.RemoveMembersEvent;


public abstract class MembershipControlEventCallback {

    public abstract void meetingLeft(@NotNull Isometrik isometrik, @NotNull LeaveMeetingEvent leaveMeetingEvent);

    public abstract void adminAdded(@NotNull Isometrik isometrik, @NotNull AddAdminEvent addAdminEvent);

    public abstract void adminRemoved(@NotNull Isometrik isometrik, @NotNull RemoveAdminEvent removeAdminEvent);

    public abstract void joinedPublicMeeting(@NotNull Isometrik isometrik, @NotNull JoinPublicMeetingEvent joinPublicMeetingEvent);

    public abstract void membersAdded(@NotNull Isometrik isometrik, @NotNull AddMembersEvent addMembersEvent);

    public abstract void membersRemoved(@NotNull Isometrik isometrik, @NotNull RemoveMembersEvent removeMembersEvent);

}
