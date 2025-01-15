package io.isometrik.meeting.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.PublishEventCallback;
import io.isometrik.meeting.events.publish.PublishStartEvent;
import io.isometrik.meeting.events.publish.PublishStopEvent;


public  class PublishListenerManager {
    private final List<PublishEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new publish listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public PublishListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(PublishEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(PublishEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<PublishEventCallback> getListeners() {
        List<PublishEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    public void announce(PublishStartEvent publishStartEvent) {
        for (PublishEventCallback publishEventCallback : getListeners()) {
            publishEventCallback.publishStarted(this.isometrik, publishStartEvent);
        }
    }

    public void announce(PublishStopEvent publishStopEvent) {
        for (PublishEventCallback publishEventCallback : getListeners()) {
            publishEventCallback.publishStopped(this.isometrik, publishStopEvent);
        }
    }
}
