package io.isometrik.meeting.builder.message;

import java.util.List;

/**
 * The query builder for fetching messages count request.
 */
public class FetchMessagesCountQuery {

    private final String meetingId;
    private final Long lastMessageTimestamp;
    private final List<String> messageIds;
    private final List<Integer> messageTypes;
    private final List<String> customTypes;
    private final List<String> senderIds;
    private final String userToken;
    private final Boolean senderIdsExclusive;

    private FetchMessagesCountQuery(Builder builder) {
        this.meetingId = builder.meetingId;
        this.lastMessageTimestamp = builder.lastMessageTimestamp;
        this.messageIds = builder.messageIds;
        this.messageTypes = builder.messageTypes;
        this.customTypes = builder.customTypes;
        this.senderIds = builder.senderIds;
        this.userToken = builder.userToken;
        this.senderIdsExclusive = builder.senderIdsExclusive;
    }

    /**
     * The builder class for building fetch messages count query.
     */
    public static class Builder {
        private String meetingId;
        private Long lastMessageTimestamp;
        private List<String> messageIds;
        private List<Integer> messageTypes;
        private List<String> customTypes;
        private List<String> senderIds;
        private String userToken;
        private Boolean senderIdsExclusive;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
        }

        /**
         * Sets meeting id.
         *
         * @param meetingId the meeting id
         * @return the meeting id
         */
        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }


        /**
         * Sets last message timestamp.
         *
         * @param lastMessageTimestamp the last message timestamp
         * @return the last message timestamp
         */
        public Builder setLastMessageTimestamp(Long lastMessageTimestamp) {
            this.lastMessageTimestamp = lastMessageTimestamp;
            return this;
        }

        /**
         * Sets message ids.
         *
         * @param messageIds the message ids
         * @return the message ids
         */
        public Builder setMessageIds(List<String> messageIds) {
            this.messageIds = messageIds;
            return this;
        }

        /**
         * Sets message types.
         *
         * @param messageTypes the message types
         * @return the message types
         */
        public Builder setMessageTypes(List<Integer> messageTypes) {
            this.messageTypes = messageTypes;
            return this;
        }


        /**
         * Sets custom types.
         *
         * @param customTypes the custom types
         * @return the custom types
         */
        public Builder setCustomTypes(List<String> customTypes) {
            this.customTypes = customTypes;
            return this;
        }

        /**
         * Sets sender ids.
         *
         * @param senderIds the sender ids
         * @return the sender ids
         */
        public Builder setSenderIds(List<String> senderIds) {
            this.senderIds = senderIds;
            return this;
        }


        /**
         * Sets user token.
         *
         * @param userToken the user token
         * @return the user token
         */
        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        /**
         * Sets sender ids exclusive.
         *
         * @param senderIdsExclusive the sender ids exclusive
         * @return the sender ids exclusive
         */
        public Builder setSenderIdsExclusive(Boolean senderIdsExclusive) {
            this.senderIdsExclusive = senderIdsExclusive;
            return this;
        }

        /**
         * Build fetch messages count query.
         *
         * @return the fetch messages count query
         */
        public FetchMessagesCountQuery build() {
            return new FetchMessagesCountQuery(this);
        }
    }

    /**
     * Gets meeting id.
     *
     * @return the meeting id
     */
    public String getMeetingId() {
        return meetingId;
    }


    /**
     * Gets last message timestamp.
     *
     * @return the last message timestamp
     */
    public Long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    /**
     * Gets message ids.
     *
     * @return the message ids
     */
    public List<String> getMessageIds() {
        return messageIds;
    }

    /**
     * Gets message types.
     *
     * @return the message types
     */
    public List<Integer> getMessageTypes() {
        return messageTypes;
    }


    /**
     * Gets custom types.
     *
     * @return the custom types
     */
    public List<String> getCustomTypes() {
        return customTypes;
    }

    /**
     * Gets sender ids.
     *
     * @return the sender ids
     */
    public List<String> getSenderIds() {
        return senderIds;
    }


    /**
     * Gets user token.
     *
     * @return the user token
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * Gets sender ids exclusive.
     *
     * @return the sender ids exclusive
     */
    public Boolean getSenderIdsExclusive() {
        return senderIdsExclusive;
    }
}
