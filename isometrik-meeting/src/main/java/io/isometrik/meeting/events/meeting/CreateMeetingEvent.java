package io.isometrik.meeting.events.meeting;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.events.meeting.utils.MeetingConfig;
import io.isometrik.meeting.events.meeting.utils.MeetingMembers;

public class CreateMeetingEvent {

    @SerializedName("searchableTags")
    @Expose
    private ArrayList<String> searchableTags;

    @SerializedName("metaData")
    @Expose
    private Object metaData;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("customType")
    @Expose
    private String customType;

    @SerializedName("meetingId")
    @Expose
    private String meetingId;

    @SerializedName("meetingDescription")
    @Expose
    private String meetingDescription;

    @SerializedName("meetingImageUrl")
    @Expose
    private String meetingImageUrl;

    @SerializedName("creationTime")
    @Expose
    private long creationTime;


    @SerializedName("createdBy")
    @Expose
    private String createdBy;

    @SerializedName("initiatorName")
    @Expose
    private String initiatorName;

    @SerializedName("initiatorIdentifier")
    @Expose
    private String initiatorIdentifier;

    @SerializedName("initiatorImageUrl")
    @Expose
    private String initiatorImageUrl;

    @SerializedName("members")
    @Expose
    private List<MeetingMembers> members;


    @SerializedName("audioOnly")
    @Expose
    private boolean audioOnly;

    @SerializedName("enableRecording")
    @Expose
    private boolean enableRecording;


    @SerializedName("hdMeeting")
    @Expose
    private boolean hdMeeting;

    @SerializedName("active")
    @Expose
    private boolean active;


    @SerializedName("selfHosted")
    @Expose
    private boolean selfHosted;

    @SerializedName("adminCount")
    @Expose
    private int adminCount;

    @SerializedName("meetingType")
    @Expose
    private int meetingType;

    @SerializedName("config")
    @Expose
    private MeetingConfig config;

    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    @SerializedName("privateOneToOne")
    @Expose
    private Boolean privateOneToOne;

    @SerializedName("autoTerminate")
    @Expose
    private boolean autoTerminate;

    public JSONObject getMetaData() {

        try {
            return new JSONObject(new Gson().toJson(metaData));
        } catch (Exception ignore) {
            return new JSONObject();
        }
    }

    public String getAction() {
        return action;
    }

    public String getCustomType() {
        return customType;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public String getMeetingImageUrl() {
        return meetingImageUrl;
    }

    public long getCreationTime() {
        return creationTime;
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

    public String getInitiatorImageUrl() {
        return initiatorImageUrl;
    }

    public List<MeetingMembers> getMembers() {
        return members;
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

    public boolean isActive() {
        return active;
    }

    public boolean isSelfHosted() {
        return selfHosted;
    }

    public int getAdminCount() {
        return adminCount;
    }

    public int getMeetingType() {
        return meetingType;
    }

    public MeetingConfig getConfig() {
        return config;
    }

    public String getConversationId() {
        return conversationId;
    }

    public Boolean getPrivateOneToOne() {
        return privateOneToOne;
    }

    public boolean isAutoTerminate() {
        return autoTerminate;
    }
}
