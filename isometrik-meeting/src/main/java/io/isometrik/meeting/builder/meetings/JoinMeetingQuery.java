package io.isometrik.meeting.builder.meetings;

/**
 * The query builder for joining meeting request.
 */
public class JoinMeetingQuery {
    private final String meetingId;
    private final String userToken;

    private JoinMeetingQuery(JoinMeetingQuery.Builder builder) {
        this.meetingId = builder.meetingId;
        this.userToken = builder.userToken;
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

    /**
     * The builder class for building join meeting query.
     */
    public static class Builder {
        private String meetingId;
        private String userToken;

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
        public JoinMeetingQuery.Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        /**
         * Sets user token.
         *
         * @param userToken the user token
         * @return the user token
         */
        public JoinMeetingQuery.Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        /**
         * Build join meeting query.
         *
         * @return the join meeting query
         */
        public JoinMeetingQuery build() {
            return new JoinMeetingQuery(this);
        }
    }
}
