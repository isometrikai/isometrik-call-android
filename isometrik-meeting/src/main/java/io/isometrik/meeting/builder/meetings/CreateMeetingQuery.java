package io.isometrik.meeting.builder.meetings;

import org.json.JSONObject;

import java.util.List;

public class CreateMeetingQuery {

    private final String meetingImageUrl;
    private final String meetingDescription;
    private final List<String> members;
    private final Boolean audioOnly;
    private final Boolean enableRecording;
    private final Boolean hdMeeting;
    private final JSONObject metaData;
    private final List<String> searchableTags;
    private final String customType;
    private final Boolean selfHosted;
    private final Boolean pushNotifications;
    private final Integer meetingType;
    private final String meetingCreationNotificationTitle;
    private final String meetingCreationNotificationBody;
    private final String meetingEndNotificationTitle;
    private final String meetingEndNotificationBody;
    private final String conversationId;
    private final String deviceId;
    private final Boolean autoTerminate;
    private final String userToken;

    private CreateMeetingQuery(Builder builder) {
        this.meetingImageUrl = builder.meetingImageUrl;
        this.meetingDescription = builder.meetingDescription;
        this.members = builder.members;
        this.audioOnly = builder.audioOnly;
        this.enableRecording = builder.enableRecording;
        this.hdMeeting = builder.hdMeeting;
        this.metaData = builder.metaData;
        this.searchableTags = builder.searchableTags;
        this.customType = builder.customType;
        this.selfHosted = builder.selfHosted;
        this.pushNotifications = builder.pushNotifications;
        this.meetingType = builder.meetingType;
        this.meetingCreationNotificationTitle = builder.meetingCreationNotificationTitle;
        this.meetingCreationNotificationBody = builder.meetingCreationNotificationBody;
        this.meetingEndNotificationTitle = builder.meetingEndNotificationTitle;
        this.meetingEndNotificationBody = builder.meetingEndNotificationBody;
        this.conversationId = builder.conversationId;
        this.deviceId = builder.deviceId;
        this.autoTerminate = builder.autoTerminate;
        this.userToken = builder.userToken;
    }

    public static class Builder {


        private String meetingImageUrl;
        private String meetingDescription;
        private List<String> members;
        private Boolean audioOnly;
        private Boolean enableRecording;
        private Boolean hdMeeting;
        private JSONObject metaData;
        private List<String> searchableTags;
        private String customType;
        private Boolean selfHosted;
        private Boolean pushNotifications;
        private Integer meetingType;
        private String meetingCreationNotificationTitle;
        private String meetingCreationNotificationBody;
        private String meetingEndNotificationTitle;
        private String meetingEndNotificationBody;
        private String conversationId;
        private String deviceId;
        private Boolean autoTerminate;
        private String userToken;

        public Builder setMeetingImageUrl(String meetingImageUrl) {
            this.meetingImageUrl = meetingImageUrl;
            return this;
        }

        public Builder setMeetingDescription(String meetingDescription) {
            this.meetingDescription = meetingDescription;
            return this;
        }

        public Builder setMembers(List<String> members) {
            this.members = members;
            return this;
        }

        public Builder setAudioOnly(Boolean audioOnly) {
            this.audioOnly = audioOnly;
            return this;
        }

        public Builder setEnableRecording(Boolean enableRecording) {
            this.enableRecording = enableRecording;
            return this;
        }

        public Builder setHdMeeting(Boolean hdMeeting) {
            this.hdMeeting = hdMeeting;
            return this;
        }

        public Builder setMetaData(JSONObject metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder setSearchableTags(List<String> searchableTags) {
            this.searchableTags = searchableTags;
            return this;
        }

        public Builder setCustomType(String customType) {
            this.customType = customType;
            return this;
        }

        public Builder setSelfHosted(Boolean selfHosted) {
            this.selfHosted = selfHosted;
            return this;
        }

        public Builder setPushNotifications(Boolean pushNotifications) {
            this.pushNotifications = pushNotifications;
            return this;
        }

        public Builder setMeetingType(Integer meetingType) {
            this.meetingType = meetingType;
            return this;
        }

        public Builder setMeetingCreationNotificationTitle(String meetingCreationNotificationTitle) {
            this.meetingCreationNotificationTitle = meetingCreationNotificationTitle;
            return this;
        }

        public Builder setMeetingCreationNotificationBody(String meetingCreationNotificationBody) {
            this.meetingCreationNotificationBody = meetingCreationNotificationBody;
            return this;
        }

        public Builder setMeetingEndNotificationTitle(String meetingEndNotificationTitle) {
            this.meetingEndNotificationTitle = meetingEndNotificationTitle;
            return this;
        }

        public Builder setMeetingEndNotificationBody(String meetingEndNotificationBody) {
            this.meetingEndNotificationBody = meetingEndNotificationBody;
            return this;
        }

        public Builder setConversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setAutoTerminate(Boolean autoTerminate) {
            this.autoTerminate = autoTerminate;
            return this;
        }

        public Builder setUserToken(String userToken) {
            this.userToken = userToken;
            return this;
        }

        public CreateMeetingQuery build() {
            return new CreateMeetingQuery(this);
        }
    }

    public String getMeetingImageUrl() {
        return meetingImageUrl;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public List<String> getMembers() {
        return members;
    }

    public Boolean getAudioOnly() {
        return audioOnly;
    }

    public Boolean getEnableRecording() {
        return enableRecording;
    }

    public Boolean getHdMeeting() {
        return hdMeeting;
    }

    public JSONObject getMetaData() {
        return metaData;
    }

    public List<String> getSearchableTags() {
        return searchableTags;
    }

    public String getCustomType() {
        return customType;
    }

    public Boolean getSelfHosted() {
        return selfHosted;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public String getMeetingCreationNotificationTitle() {
        return meetingCreationNotificationTitle;
    }

    public String getMeetingCreationNotificationBody() {
        return meetingCreationNotificationBody;
    }

    public String getMeetingEndNotificationTitle() {
        return meetingEndNotificationTitle;
    }

    public String getMeetingEndNotificationBody() {
        return meetingEndNotificationBody;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Boolean getAutoTerminate() {
        return autoTerminate;
    }

    public String getUserToken() {
        return userToken;
    }
}
