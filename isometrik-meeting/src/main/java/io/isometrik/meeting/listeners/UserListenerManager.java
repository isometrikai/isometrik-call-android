package io.isometrik.meeting.listeners;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.UserEventCallback;
import io.isometrik.meeting.events.user.DeleteUserEvent;
import io.isometrik.meeting.events.user.UpdateUserEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to add/remove listeners which are announced on user realtime events and
 * to announce added listeners on user events for block, unblock, update and delete user.
 */
public class UserListenerManager {
    private final List<UserEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new User listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public UserListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(UserEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(UserEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<UserEventCallback> getListeners() {
        List<UserEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    /**
     * Announce update user event.
     *
     * @param updateUserEvent the update user event
     */
    public void announce(UpdateUserEvent updateUserEvent) {
        for (UserEventCallback userEventCallback : getListeners()) {
            userEventCallback.userUpdated(this.isometrik, updateUserEvent);
        }
    }

    /**
     * Announce delete user event.
     *
     * @param deleteUserEvent the delete user event
     */
    public void announce(DeleteUserEvent deleteUserEvent) {
        for (UserEventCallback userEventCallback : getListeners()) {
            userEventCallback.userDeleted(this.isometrik, deleteUserEvent);
        }
    }
}
