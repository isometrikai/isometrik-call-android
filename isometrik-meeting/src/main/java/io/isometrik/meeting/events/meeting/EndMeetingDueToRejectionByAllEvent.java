package io.isometrik.meeting.events.meeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.isometrik.meeting.events.meeting.utils.CallDuration;

public class EndMeetingDueToRejectionByAllEvent {
    @SerializedName("meetingId")
    @Expose
    private String meetingId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("initiatorId")
    @Expose
    private String initiatorId;
    @SerializedName("initiatorName")
    @Expose
    private String initiatorName;
    @SerializedName("initiatorIdentifier")
    @Expose
    private String initiatorIdentifier;
    @SerializedName("initiatorImageUrl")
    @Expose
    private String initiatorImageUrl;
    @SerializedName("meetingDescription")
    @Expose
    private String meetingDescription;
    @SerializedName("meetingImageUrl")
    @Expose
    private String meetingImageUrl;

    @SerializedName("sentAt")
    @Expose
    private long sentAt;

    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    @SerializedName("privateOneToOne")
    @Expose
    private Boolean privateOneToOne;

    @SerializedName("recordingUrl")
    @Expose
    private String recordingUrl;

    @SerializedName("missedByMembers")
    @Expose
    private ArrayList<String> missedByMembers;

    @SerializedName("callDurations")
    @Expose
    private ArrayList<CallDuration> callDurations;


    public String getMeetingId() {
        return meetingId;
    }

    public String getAction() {
        return action;
    }

    public String getInitiatorId() {
        return initiatorId;
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

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public String getMeetingImageUrl() {
        return meetingImageUrl;
    }

    public long getSentAt() {
        return sentAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public Boolean getPrivateOneToOne() {
        return privateOneToOne;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public ArrayList<String> getMissedByMembers() {
        return missedByMembers;
    }

    public ArrayList<CallDuration> getCallDurations() {
        return callDurations;
    }
}