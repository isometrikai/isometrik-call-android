package io.isometrik.meeting.builder.member;

/**
 * The query builder for removing admin request.
 */
public class RemoveAdminQuery {
    private final String meetingId;
    private final String memberId;
    private final String userToken;

    private RemoveAdminQuery(Builder builder) {
        this.meetingId = builder.meetingId;
        this.memberId = builder.memberId;
        this.userToken = builder.userToken;
    }

    /**
     * The builder class for building remove admin query.
     */
    public static class Builder {
        private String meetingId;
        private String memberId;
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
        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        /**
         * Sets member id.
         *
         * @param memberId the member id
         * @return the member id
         */
        public Builder setMemberId(String memberId) {
            this.memberId = memberId;
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
         * Build remove admin query.
         *
         * @return the remove admin query
         */
        public RemoveAdminQuery build() {
            return new RemoveAdminQuery(this);
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
     * Gets member id.
     *
     * @return the member id
     */
    public String getMemberId() {
        return memberId;
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
