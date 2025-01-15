package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.publish.PublishStartEvent;
import io.isometrik.meeting.events.publish.PublishStopEvent;


public abstract class PublishEventCallback {

    public abstract void publishStarted(@NotNull Isometrik isometrik, @NotNull PublishStartEvent publishStartEvent);


    public abstract void publishStopped(@NotNull Isometrik isometrik, @NotNull PublishStopEvent publishStopEvent);
}
