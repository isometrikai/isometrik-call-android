package io.isometrik.meeting.events.message;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

public class PublishMessageEvent {


    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("meetingId")
    @Expose
    private String meetingId;

    @SerializedName("messageId")
    @Expose
    private String messageId;

    @SerializedName("sentAt")
    @Expose
    private long sentAt;

    @SerializedName("senderId")
    @Expose
    private String senderId;

    @SerializedName("senderIdentifier")
    @Expose
    private String senderIdentifier;

    @SerializedName("senderProfileImageUrl")
    @Expose
    private String senderProfileImageUrl;

    @SerializedName("senderName")
    @Expose
    private String senderName;

    @SerializedName("body")
    @Expose
    private String message;

    @SerializedName("messageType")
    @Expose
    private int messageType;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("customType")
    @Expose
    private String customType;

    @SerializedName("repliesCount")
    @Expose
    private int repliesCount;

    @SerializedName("replyMessage")
    @Expose
    private boolean replyMessage;
    @SerializedName("searchableTags")
    @Expose
    private ArrayList<String> searchableTags;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    public String getAction() {
        return action;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getMessageId() {
        return messageId;
    }

    public long getSentAt() {
        return sentAt;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderIdentifier() {
        return senderIdentifier;
    }

    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageType() {
        return messageType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getCustomType() {
        return customType;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public boolean isReplyMessage() {
        return replyMessage;
    }

    public ArrayList<String> getSearchableTags() {
        return searchableTags;
    }
    public JSONObject getMetaData() {

        try {
            return new JSONObject(new Gson().toJson(metaData));
        } catch (Exception ignore) {
            return new JSONObject();
        }
    }
}
