package io.isometrik.meeting.events.recording;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordingStartEvent {
    @SerializedName("meetingId")
    @Expose
    private String meetingId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;

    @SerializedName("sentAt")
    @Expose
    private long sentAt;

    public String getMeetingId() {
        return meetingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public long getSentAt() {
        return sentAt;
    }
}