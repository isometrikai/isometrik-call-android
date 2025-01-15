package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.message.PublishMessageEvent;
import io.isometrik.meeting.events.publish.PublishStartEvent;
import io.isometrik.meeting.events.publish.PublishStopEvent;


public abstract class MessageEventCallback {

    public abstract void messagePublished(@NotNull Isometrik isometrik, @NotNull PublishMessageEvent publishMessageEvent);

}
