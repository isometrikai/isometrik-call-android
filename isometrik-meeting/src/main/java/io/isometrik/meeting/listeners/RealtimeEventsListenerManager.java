package io.isometrik.meeting.listeners;

import io.isometrik.meeting.Isometrik;

/**
 * Class for registering/unregistering listener for realtime events for connection, conversation,
 * membership control, message, reaction and user.
 */
public class RealtimeEventsListenerManager {

    private final ConnectionListenerManager connectionListenerManager;
    private final UserListenerManager userListenerManager;
    private final JoinActionsListenerManager joinActionsListenerManager;
    private final MeetingListenerManager meetingListenerManager;
    private final MembershipControlListenerManager membershipControlListenerManager;
    private final MessageListenerManager messageListenerManager;
    private final PublishListenerManager publishListenerManager;
    private final RecordingListenerManager recordingListenerManager;

    /**
     * Instantiates a new Realtime events listener manager.
     *
     * @param isometrik the isometrik
     */
    public RealtimeEventsListenerManager(Isometrik isometrik) {
        connectionListenerManager = new ConnectionListenerManager(isometrik);
        userListenerManager = new UserListenerManager(isometrik);
        joinActionsListenerManager = new JoinActionsListenerManager(isometrik);
        meetingListenerManager = new MeetingListenerManager(isometrik);
        membershipControlListenerManager = new MembershipControlListenerManager(isometrik);
        messageListenerManager = new MessageListenerManager(isometrik);
        publishListenerManager = new PublishListenerManager(isometrik);
        recordingListenerManager = new RecordingListenerManager(isometrik);
    }

    /**
     * Gets connection listener manager.
     *
     * @return the connection listener manager
     */
    public ConnectionListenerManager getConnectionListenerManager() {
        return connectionListenerManager;
    }

    /**
     * Gets user listener manager.
     *
     * @return the user listener manager
     */
    public UserListenerManager getUserListenerManager() {
        return userListenerManager;
    }

    public JoinActionsListenerManager getJoinActionsListenerManager() {
        return joinActionsListenerManager;
    }

    public MeetingListenerManager getMeetingListenerManager() {
        return meetingListenerManager;
    }

    public MembershipControlListenerManager getMembershipControlListenerManager() {
        return membershipControlListenerManager;
    }

    public MessageListenerManager getMessageListenerManager() {
        return messageListenerManager;
    }

    public PublishListenerManager getPublishListenerManager() {
        return publishListenerManager;
    }

    public RecordingListenerManager getRecordingListenerManager() {
        return recordingListenerManager;
    }
}
