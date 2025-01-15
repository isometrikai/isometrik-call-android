package io.isometrik.meeting.events.membershipcontrol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAdminEvent {
    @SerializedName("meetingId")
    @Expose
    private String meetingId;
    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("initiatorId")
    @Expose
    private String initiatorId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("memberName")
    @Expose
    private String memberName;
    @SerializedName("memberIdentifier")
    @Expose
    private String memberIdentifier;
    @SerializedName("memberProfileImageUrl")
    @Expose
    private String memberProfileImageUrl;
    @SerializedName("initiatorName")
    @Expose
    private String initiatorName;
    @SerializedName("initiatorIdentifier")
    @Expose
    private String initiatorIdentifier;
    @SerializedName("initiatorProfileImageUrl")
    @Expose
    private String initiatorProfileImageUrl;
    @SerializedName("meetingDescription")
    @Expose
    private String meetingDescription;
    @SerializedName("meetingImageUrl")
    @Expose
    private String meetingImageUrl;

    @SerializedName("sentAt")
    @Expose
    private long sentAt;

    public String getMeetingId() {
        return meetingId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public String getAction() {
        return action;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberIdentifier() {
        return memberIdentifier;
    }

    public String getMemberProfileImageUrl() {
        return memberProfileImageUrl;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public String getInitiatorIdentifier() {
        return initiatorIdentifier;
    }

    public String getInitiatorProfileImageUrl() {
        return initiatorProfileImageUrl;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public String getMeetingImageUrl() {
        return meetingImageUrl;
    }

    public long getSentAt() {
        return sentAt;
    }
}
