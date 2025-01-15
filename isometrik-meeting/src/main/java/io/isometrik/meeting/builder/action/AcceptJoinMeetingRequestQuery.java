package io.isometrik.meeting.builder.action;

public class AcceptJoinMeetingRequestQuery {
    private final String meetingId;
    private final String deviceId;
    private final String userToken;

    private AcceptJoinMeetingRequestQuery(Builder builder) {
        this.meetingId = builder.meetingId;
        this.deviceId = builder.deviceId;
        this.userToken = builder.userToken;
    }

    public static class Builder {
        private String meetingId;
        private String deviceId;
        private String userToken;

        public Builder setMeetingId(String meetingId) {
            this.meetingId = meetingId;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        public AcceptJoinMeetingRequestQuery build() {
            return new AcceptJoinMeetingRequestQuery(this);
        }
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserToken() {
        return userToken;
    }
}
