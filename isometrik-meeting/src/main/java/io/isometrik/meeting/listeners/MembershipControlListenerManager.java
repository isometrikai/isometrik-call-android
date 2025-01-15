package io.isometrik.meeting.listeners;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.MembershipControlEventCallback;
import io.isometrik.meeting.events.membershipcontrol.AddAdminEvent;
import io.isometrik.meeting.events.membershipcontrol.AddMembersEvent;
import io.isometrik.meeting.events.membershipcontrol.JoinPublicMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.LeaveMeetingEvent;
import io.isometrik.meeting.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.meeting.events.membershipcontrol.RemoveMembersEvent;


public class MembershipControlListenerManager {
    private final List<MembershipControlEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new User listener manager.
     *
     * @param isometrikInstance the isometrik instance
     */
    public MembershipControlListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(MembershipControlEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(MembershipControlEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private List<MembershipControlEventCallback> getListeners() {
        List<MembershipControlEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }


    public void announce(LeaveMeetingEvent leaveMeetingEvent) {
        for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
            membershipControlEventCallback.meetingLeft(this.isometrik, leaveMeetingEvent);
        }
    }


    public void announce(AddAdminEvent addAdminEvent) {
        for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
            membershipControlEventCallback.adminAdded(this.isometrik, addAdminEvent);
        }
    }

    public void announce(RemoveAdminEvent removeAdminEvent) {
        for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
            membershipControlEventCallback.adminRemoved(this.isometrik, removeAdminEvent);
        }
    }

    public void announce(JoinPublicMeetingEvent joinPublicMeetingEvent) {
        for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
            membershipControlEventCallback.joinedPublicMeeting(this.isometrik, joinPublicMeetingEvent);
        }
    }

    public void announce(AddMembersEvent addMembersEvent) {
        for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
            membershipControlEventCallback.membersAdded(this.isometrik, addMembersEvent);
        }
    }

    public void announce(RemoveMembersEvent removeMembersEvent) {
        for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
            membershipControlEventCallback.membersRemoved(this.isometrik, removeMembersEvent);
        }
    }
}
