package io.isometrik.meeting.builder.meetings;

public class LeaveMeetingQuery {
    private final String meetingId;
    private final String userToken;

    private LeaveMeetingQuery(Builder builder) {
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

        public LeaveMeetingQuery build() {
            return new LeaveMeetingQuery(this);
        }
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getUserToken() {
        return userToken;
    }
}
