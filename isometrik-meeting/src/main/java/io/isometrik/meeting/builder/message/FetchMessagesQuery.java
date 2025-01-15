package io.isometrik.meeting.builder.message;

import java.util.List;

/**
 * The query builder for fetching messages request.
 */
public class FetchMessagesQuery {
    private final Integer sort;
    private final Integer limit;
    private final Integer skip;
    private final String meetingId;
    private final Long lastMessageTimestamp;
    private final List<String> messageIds;
    private final List<Integer> messageTypes;
    private final List<String> customTypes;
    private final List<String> senderIds;
    private final String userToken;
    private final String searchTag;
    private final Boolean senderIdsExclusive;

    private FetchMessagesQuery(Builder builder) {
        this.sort = builder.sort;
        this.limit = builder.limit;
        this.skip = builder.skip;
        this.meetingId = builder.meetingId;
        this.lastMessageTimestamp = builder.lastMessageTimestamp;
        this.messageIds = builder.messageIds;
        this.messageTypes = builder.messageTypes;
        this.customTypes = builder.customTypes;
        this.senderIds = builder.senderIds;
        this.userToken = builder.userToken;
        this.searchTag = builder.searchTag;
        this.senderIdsExclusive = builder.senderIdsExclusive;
    }

    /**
     * The builder class for building fetch messages query.
     */
    public static class Builder {
        private Integer sort;
        private Integer limit;
        private Integer skip;
        private String meetingId;
        private Long lastMessageTimestamp;
        private List<String> messageIds;
        private List<Integer> messageTypes;
        private List<String> customTypes;
        private List<String> senderIds;
        private String userToken;
        private String searchTag;
        private Boolean senderIdsExclusive;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
        }

        /**
         * Sets sort.
         *
         * @param sort the sort
         * @return the sort
         */
        public Builder setSort(Integer sort) {
            this.sort = sort;
            return this;
        }

        /**
         * Sets limit.
         *
         * @param limit the limit
         * @return the limit
         */
        public Builder setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Sets skip.
         *
         * @param skip the skip
         * @return the skip
         */
        public Builder setSkip(Integer skip) {
            this.skip = skip;
            return this;
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
         * Sets search tag.
         *
         * @param searchTag the search tag
         * @return the search tag
         */
        public Builder setSearchTag(String searchTag) {
            this.searchTag = searchTag;
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
         * Build fetch messages query.
         *
         * @return the fetch messages query
         */
        public FetchMessagesQuery build() {
            return new FetchMessagesQuery(this);
        }
    }


    /**
     * Gets sort.
     *
     * @return the sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * Gets limit.
     *
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Gets skip.
     *
     * @return the skip
     */
    public Integer getSkip() {
        return skip;
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
     * Gets search tag.
     *
     * @return the search tag
     */
    public String getSearchTag() {
        return searchTag;
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
