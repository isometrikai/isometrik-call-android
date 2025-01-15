package io.isometrik.meeting.events.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinMeetingAcceptEvent {

    @SerializedName("meetingId")
    @Expose
    private String meetingId;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("userId")
    @Expose
    private String userId;

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

    public String getAction() {
        return action;
    }

    public String getUserId() {
        return userId;
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
