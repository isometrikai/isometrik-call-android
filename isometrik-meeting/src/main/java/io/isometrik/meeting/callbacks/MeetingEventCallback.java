package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToNoUserPublishingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToRejectionByAllEvent;
import io.isometrik.meeting.events.meeting.EndMeetingEvent;


public abstract class MeetingEventCallback {

    public abstract void meetingCreated(@NotNull Isometrik isometrik, @NotNull CreateMeetingEvent createMeetingEvent);

    public abstract void meetingEndedDueToNoUserPublishing(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToNoUserPublishingEvent endMeetingDueToNoUserPublishingEvent);

    public abstract void meetingEndedDueToRejectionByAll(@NotNull Isometrik isometrik, @NotNull EndMeetingDueToRejectionByAllEvent endMeetingDueToRejectionByAllEvent);

    public abstract void meetingEnded(@NotNull Isometrik isometrik, @NotNull EndMeetingEvent endMeetingEvent);
}
