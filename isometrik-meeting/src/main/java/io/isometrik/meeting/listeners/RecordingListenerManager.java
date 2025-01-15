package io.isometrik.meeting.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.RecordingEventCallback;
import io.isometrik.meeting.events.recording.RecordingStartEvent;
import io.isometrik.meeting.events.recording.RecordingStopEvent;


public class RecordingListenerManager {
    private final List<RecordingEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new recording listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public RecordingListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(RecordingEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(RecordingEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<RecordingEventCallback> getListeners() {
        List<RecordingEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    public void announce(RecordingStartEvent publishStartEvent) {
        for (RecordingEventCallback recordingEventCallback : getListeners()) {
            recordingEventCallback.recordingStarted(this.isometrik, publishStartEvent);
        }
    }

    public void announce(RecordingStopEvent recordingStopEvent) {
        for (RecordingEventCallback recordingEventCallback : getListeners()) {
            recordingEventCallback.recordingStopped(this.isometrik, recordingStopEvent);
        }
    }
}
