package io.isometrik.meeting.builder.meetings;

public class EndMeetingQuery {
    private final String meetingId;
    private final String userToken;

    private EndMeetingQuery(Builder builder) {
        this.meetingId = builder.meetingId;
        this.userToken = builder.userToken;
    }

    public static class Builder {
        private String meetingId;
        private String userToken;

        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        public EndMeetingQuery build() {
            return new EndMeetingQuery(this);
        }
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getUserToken() {
        return userToken;
    }
}
