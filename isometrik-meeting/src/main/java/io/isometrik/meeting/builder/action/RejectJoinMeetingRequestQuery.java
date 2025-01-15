package io.isometrik.meeting.builder.action;

public class RejectJoinMeetingRequestQuery {
    private final String meetingId;
    private final String userToken;

    private RejectJoinMeetingRequestQuery(Builder builder) {
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

        public RejectJoinMeetingRequestQuery build() {
            return new RejectJoinMeetingRequestQuery(this);
        }
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getUserToken() {
        return userToken;
    }
}
