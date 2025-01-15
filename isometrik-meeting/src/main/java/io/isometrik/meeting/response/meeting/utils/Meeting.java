package io.isometrik.meeting.response.meeting.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Meeting {


    @SerializedName("members")
    @Expose
    private ArrayList<Member> members;

    @SerializedName("meetingId")
    @Expose
    private String meetingId;
    @SerializedName("meetingImageUrl")
    @Expose
    private String meetingImageUrl;
    @SerializedName("meetingDescription")
    @Expose
    private String meetingDescription;
    @SerializedName("creationTime")
    @Expose
    private long creationTime;
    @SerializedName("audioOnly")
    @Expose
    private boolean audioOnly;
    @SerializedName("enableRecording")
    @Expose
    private boolean enableRecording;
    @SerializedName("hdMeeting")
    @Expose
    private boolean hdMeeting;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    @SerializedName("searchableTags")
    @Expose
    private List<String> searchableTags;

    @SerializedName("customType")
    @Expose
    private String customType;

    @SerializedName("selfHosted")
    @Expose
    private boolean selfHosted;


    @SerializedName("config")
    @Expose
    private Config config;
    @SerializedName("meetingType")
    @Expose
    private int meetingType;

    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("initiatorName")
    @Expose
    private String initiatorName;
    @SerializedName("initiatorIdentifier")
    @Expose
    private String initiatorIdentifier;
    @SerializedName("initiatorImage")
    @Expose
    private String initiatorImage;

    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("autoTerminate")
    @Expose
    private boolean autoTerminate;
    @SerializedName("isHost")
    @Expose
    private boolean isHost;

    @SerializedName("membersCount")
    @Expose
    private int membersCount;
    @SerializedName("membersPublishingCount")
    @Expose
    private int membersPublishingCount;
    @SerializedName("adminCount")
    @Expose
    private int adminCount;

    public static class Config {

        @SerializedName("pushNotifications")
        @Expose
        private boolean pushNotifications;

        public boolean isPushNotifications() {
            return pushNotifications;
        }
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getMeetingImageUrl() {
        return meetingImageUrl;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    public boolean isEnableRecording() {
        return enableRecording;
    }

    public boolean isHdMeeting() {
        return hdMeeting;
    }


    public List<String> getSearchableTags() {
        return searchableTags;
    }

    public String getCustomType() {
        return customType;
    }

    public boolean isSelfHosted() {
        return selfHosted;
    }

    public Config getConfig() {
        return config;
    }

    public int getMeetingType() {
        return meetingType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public String getInitiatorIdentifier() {
        return initiatorIdentifier;
    }

    public String getInitiatorImage() {
        return initiatorImage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isAutoTerminate() {
        return autoTerminate;
    }

    public boolean isHost() {
        return isHost;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public int getMembersPublishingCount() {
        return membersPublishingCount;
    }

    public int getAdminCount() {
        return adminCount;
    }

    public JSONObject getMetaData() {

        try {
            return new JSONObject(new Gson().toJson(metaData));
        } catch (Exception ignore) {
            return new JSONObject();
        }
    }
}
