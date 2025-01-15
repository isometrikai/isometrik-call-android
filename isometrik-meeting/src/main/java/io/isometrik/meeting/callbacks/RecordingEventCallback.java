package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.recording.RecordingStartEvent;
import io.isometrik.meeting.events.recording.RecordingStopEvent;


public abstract class RecordingEventCallback {

    public abstract void recordingStarted(@NotNull Isometrik isometrik, @NotNull RecordingStartEvent recordingStartEvent);


    public abstract void recordingStopped(@NotNull Isometrik isometrik, @NotNull RecordingStopEvent recordingStopEvent);
}
