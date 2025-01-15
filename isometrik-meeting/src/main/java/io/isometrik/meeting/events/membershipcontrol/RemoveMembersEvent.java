package io.isometrik.meeting.events.membershipcontrol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.isometrik.meeting.events.membershipcontrol.util.MembersAddedOrRemoved;

public class RemoveMembersEvent {

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
    @SerializedName("meetingDescription")
    @Expose
    private String meetingDescription;
    @SerializedName("meetingImageUrl")
    @Expose
    private String meetingImageUrl;
    @SerializedName("membersCount")
    @Expose
    private int membersCount;
    @SerializedName("sentAt")
    @Expose
    private long sentAt;

    @SerializedName("members")
    @Expose
    private ArrayList<MembersAddedOrRemoved> members;

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

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public String getMeetingImageUrl() {
        return meetingImageUrl;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public long getSentAt() {
        return sentAt;
    }

    public ArrayList<MembersAddedOrRemoved> getMembers() {
        return members;
    }
}
