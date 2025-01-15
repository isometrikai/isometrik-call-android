package io.isometrik.meeting.builder.recording;

/**
 * The query builder for fetching recordings request.
 */
public class FetchRecordingsQuery {
    private final Integer limit;
    private final Integer skip;
    private final String meetingId;
    private final String userToken;

    private FetchRecordingsQuery(Builder builder) {
        this.limit = builder.limit;
        this.skip = builder.skip;
        this.meetingId = builder.meetingId;
        this.userToken = builder.userToken;
    }

    /**
     * The builder class for building fetch messages query.
     */
    public static class Builder {
        private Integer limit;
        private Integer skip;
        private String meetingId;
        private String userToken;


        /**
         * Instantiates a new Builder.
         */
        public Builder() {
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
         * Build fetch messages query.
         *
         * @return the fetch messages query
         */
        public FetchRecordingsQuery build() {
            return new FetchRecordingsQuery(this);
        }
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
     * Gets user token.
     *
     * @return the user token
     */
    public String getUserToken() {
        return userToken;
    }


}
