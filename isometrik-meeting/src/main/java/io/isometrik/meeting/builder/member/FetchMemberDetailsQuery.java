package io.isometrik.meeting.builder.member;

/**
 * Query builder class for creating the request for fetching the details of a member by uid in
 * meeting.
 */
public class FetchMemberDetailsQuery {

    private final String meetingId;
    private final String memberId;
    private final String userToken;

    private FetchMemberDetailsQuery(Builder builder) {

        this.meetingId = builder.meetingId;
        this.userToken = builder.userToken;
        this.memberId = builder.memberId;
    }

    /**
     * The Builder class for the FetchMemberDetailsByUidQuery.
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

        public Builder setMemberId(String memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }


        /**
         * Sets meeting id.
         *
         * @param meetingId the id of the meeting whose member details are to be fetched
         * @return the FetchMemberDetailsByUidQuery.Builder{@link Builder}
         * instance
         * @see Builder
         */
        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        /**
         * Build fetch user details query.
         *
         * @return the FetchMemberDetailsByUidQuery{@link FetchMemberDetailsQuery} instance
         * @see FetchMemberDetailsQuery
         */
        public FetchMemberDetailsQuery build() {
            return new FetchMemberDetailsQuery(this);
        }
    }


    /**
     * Gets meetingId.
     *
     * @return the id of the meeting whose member details are to be fetched
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

    public String getMemberId() {
        return memberId;
    }
}
