package io.isometrik.meeting.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.MeetingEventCallback;
import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToNoUserPublishingEvent;
import io.isometrik.meeting.events.meeting.EndMeetingDueToRejectionByAllEvent;
import io.isometrik.meeting.events.meeting.EndMeetingEvent;


public class MeetingListenerManager {
    private final List<MeetingEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new User listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public MeetingListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(MeetingEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(MeetingEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<MeetingEventCallback> getListeners() {
        List<MeetingEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    public void announce(CreateMeetingEvent createMeetingEvent) {
        for (MeetingEventCallback meetingEventCallback : getListeners()) {
            meetingEventCallback.meetingCreated(this.isometrik, createMeetingEvent);
        }
    }

    public void announce(EndMeetingDueToNoUserPublishingEvent endMeetingDueToNoUserPublishingEvent) {
        for (MeetingEventCallback meetingEventCallback : getListeners()) {
            meetingEventCallback.meetingEndedDueToNoUserPublishing(this.isometrik, endMeetingDueToNoUserPublishingEvent);
        }
    }

    public void announce(EndMeetingDueToRejectionByAllEvent endMeetingDueToRejectionByAllEvent) {
        for (MeetingEventCallback meetingEventCallback : getListeners()) {
            meetingEventCallback.meetingEndedDueToRejectionByAll(this.isometrik, endMeetingDueToRejectionByAllEvent);
        }
    }

    public void announce(EndMeetingEvent endMeetingEvent) {
        for (MeetingEventCallback meetingEventCallback : getListeners()) {
            meetingEventCallback.meetingEnded(this.isometrik, endMeetingEvent);
        }
    }
}
