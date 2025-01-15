package io.isometrik.meeting.events.membershipcontrol.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MembersAddedOrRemoved {

    @SerializedName("memberId")
    @Expose
    private String memberId;

    @SerializedName("memberName")
    @Expose
    private String memberName;

    @SerializedName("memberIdentifier")
    @Expose
    private String memberIdentifier;

    @SerializedName("memberProfileImageUrl")
    @Expose
    private String memberProfileImageUrl;

    public String getMemberId() {
        return memberId;
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
}
