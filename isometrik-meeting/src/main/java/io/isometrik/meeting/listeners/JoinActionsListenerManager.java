package io.isometrik.meeting.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.JoinActionsEventCallback;
import io.isometrik.meeting.events.action.JoinMeetingAcceptEvent;
import io.isometrik.meeting.events.action.JoinMeetingRejectEvent;


public  class JoinActionsListenerManager {


    private final List<JoinActionsEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new User listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public JoinActionsListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(JoinActionsEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(JoinActionsEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<JoinActionsEventCallback> getListeners() {
        List<JoinActionsEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    public void announce(JoinMeetingAcceptEvent joinMeetingAcceptEvent) {
        for (JoinActionsEventCallback joinActionsEventCallback : getListeners()) {
            joinActionsEventCallback.joinMeetingAccepted(this.isometrik, joinMeetingAcceptEvent);
        }
    }

    public void announce(JoinMeetingRejectEvent joinMeetingRejectEvent) {
        for (JoinActionsEventCallback joinActionsEventCallback : getListeners()) {
            joinActionsEventCallback.joinMeetingRejected(this.isometrik, joinMeetingRejectEvent);
        }
    }

}
