package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.action.JoinMeetingAcceptEvent;
import io.isometrik.meeting.events.action.JoinMeetingRejectEvent;


public abstract class JoinActionsEventCallback {

    public abstract void joinMeetingAccepted(@NotNull Isometrik isometrik, @NotNull JoinMeetingAcceptEvent joinMeetingAcceptEvent);

    public abstract void joinMeetingRejected(@NotNull Isometrik isometrik, @NotNull JoinMeetingRejectEvent joinMeetingRejectEvent);
}
