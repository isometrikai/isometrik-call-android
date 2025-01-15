package io.isometrik.meeting.builder.member;

import java.util.List;

/**
 * Query builder class for creating the request for removing a member from a meeting.
 */
public class RemoveMembersQuery {

    private final String meetingId;
    private final List<String> members;
    private final String userToken;

    private RemoveMembersQuery(Builder builder) {
        this.meetingId = builder.meetingId;
        this.members = builder.members;
        this.userToken = builder.userToken;
    }

    /**
     * The Builder class for the RemoveMemberQuery.
     */
    public static class Builder {
        private String meetingId;
        private List<String> members;
        private String userToken;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
        }

        /**
         * Sets meeting id.
         *
         * @param meetingId the id of the meeting from which to remove a member
         * @return the RemoveMemberQuery.Builder{@link RemoveMembersQuery.Builder}
         * instance
         * @see RemoveMembersQuery.Builder
         */
        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        /**
         * Sets user token.
         *
         * @param userToken the token of the member who is removing another member from a meeting
         * @return the RemoveMemberQuery.Builder{@link RemoveMembersQuery.Builder}
         * instance
         * @see RemoveMembersQuery.Builder
         */
        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        /**
         * Sets members.
         *
         * @param members the members
         * @return the members
         */
        public Builder setMembers(List<String> members) {
            this.members = members;
            return this;
        }

        /**
         * Build remove member query.
         *
         * @return the RemoveMemberQuery{@link RemoveMembersQuery} instance
         * @see RemoveMembersQuery
         */
        public RemoveMembersQuery build() {
            return new RemoveMembersQuery(this);
        }
    }

    /**
     * Gets meeting id.
     *
     * @return the id of the meeting from which to remove a member
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * Gets members.
     *
     * @return the members
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * Gets user token.
     *
     * @return the token of the member who is removing another member from a meeting
     */
    public String getUserToken() {
        return userToken;
    }
}
