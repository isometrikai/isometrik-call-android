package io.isometrik.meeting.events.meeting.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetingMembers {

    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("memberIdentifier")
    @Expose
    private String memberIdentifier;
    @SerializedName("memberName")
    @Expose
    private String memberName;

    @SerializedName("memberProfileImageUrl")
    @Expose
    private String memberProfileImageUrl;

    @SerializedName("isAdmin")
    @Expose
    private Boolean isAdmin;

    @SerializedName("isPublishing")
    @Expose
    private Boolean isPublishing;

    public String getMemberId() {
        return memberId;
    }

    public String getMemberIdentifier() {
        return memberIdentifier;
    }

    public String getMemberName() {
        return memberName;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Boolean getPublishing() {
        return isPublishing;
    }

    public String getMemberProfileImageUrl() {
        return memberProfileImageUrl;
    }
}