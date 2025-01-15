package io.isometrik.meeting.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.MessageEventCallback;
import io.isometrik.meeting.events.message.PublishMessageEvent;


public  class MessageListenerManager {
    private final List<MessageEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new User listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public MessageListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(MessageEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(MessageEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<MessageEventCallback> getListeners() {
        List<MessageEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    public void announce(PublishMessageEvent publishMessageEvent) {
        for (MessageEventCallback messageEventCallback : getListeners()) {
            messageEventCallback.messagePublished(this.isometrik, publishMessageEvent);
        }
    }

}
