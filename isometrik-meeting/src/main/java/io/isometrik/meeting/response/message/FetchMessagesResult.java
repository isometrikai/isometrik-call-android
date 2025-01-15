package io.isometrik.meeting.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * The helper class to parse the response of the fetch messages request.
 */
public class FetchMessagesResult {
    @SerializedName("msg")
    @Expose
    private String message;
    @SerializedName("messages")
    @Expose
    private ArrayList<Message> messages;

    public static class Message {

        @SerializedName("senderId")
        @Expose
        private String senderId;
        @SerializedName("senderName")
        @Expose
        private String senderName;
        @SerializedName("senderIdentifier")
        @Expose
        private String senderIdentifier;
        @SerializedName("senderProfileImageUrl")
        @Expose
        private String senderProfileImageUrl;
        @SerializedName("metaData")
        @Expose
        private Object metaData;
        @SerializedName("messageType")
        @Expose
        private Integer messageType;
        @SerializedName("customType")
        @Expose
        private String customType;
        @SerializedName("body")
        @Expose
        private String body;
        @SerializedName("sentAt")
        @Expose
        private long sentAt;
        @SerializedName("messageId")
        @Expose
        private String messageId;
        @SerializedName("searchableTags")
        @Expose
        private ArrayList<String> searchableTags;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;

        public String getSenderId() {
            return senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public String getSenderIdentifier() {
            return senderIdentifier;
        }

        public String getSenderProfileImageUrl() {
            return senderProfileImageUrl;
        }

        public Object getMetaData() {
            return metaData;
        }

        public Integer getMessageType() {
            return messageType;
        }

        public String getCustomType() {
            return customType;
        }

        public String getBody() {
            return body;
        }

        public long getSentAt() {
            return sentAt;
        }

        public String getMessageId() {
            return messageId;
        }

        public ArrayList<String> getSearchableTags() {
            return searchableTags;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }

    /**
     * Gets message.
     *
     * @return the response message received
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets messages.
     *
     * @return the messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }
}
