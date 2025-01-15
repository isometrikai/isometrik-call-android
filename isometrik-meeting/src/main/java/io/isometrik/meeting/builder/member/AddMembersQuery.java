package io.isometrik.meeting.builder.member;

import java.util.List;

/**
 * Query builder class for creating the request for adding a member to a meeting.
 */
public class AddMembersQuery {

    private final String meetingId;
    private final List<String> members;
    private final String userToken;

    private AddMembersQuery(Builder builder) {
        this.meetingId = builder.meetingId;
        this.members = builder.members;
        this.userToken = builder.userToken;
    }

    /**
     * The Builder class for the AddMemberQuery.
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
         * @param meetingId the id of the meeting, in which to add member
         * @return the AddMemberQuery.Builder{@link AddMembersQuery.Builder}
         * instance
         * @see AddMembersQuery.Builder
         */
        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        /**
         * Sets user token.
         *
         * @param userToken the token of the member adding other member to the meeting
         * @return the AddMemberQuery.Builder{@link AddMembersQuery.Builder}
         * instance
         * @see AddMembersQuery.Builder
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
         * Build add member query.
         *
         * @return the AddMemberQuery{@link AddMembersQuery} instance
         * @see AddMembersQuery
         */
        public AddMembersQuery build() {
            return new AddMembersQuery(this);
        }
    }

    /**
     * Gets meeting id.
     *
     * @return the id of the meeting, in which to add member
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
     * @return the token of the member adding other member to the meeting
     */
    public String getUserToken() {
        return userToken;
    }
}
