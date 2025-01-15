package io.isometrik.meeting.response.recording;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * The helper class to parse the response of the fetch recordings request.
 */
public class FetchRecordingsResult {
    @SerializedName("msg")
    @Expose
    private String message;
    @SerializedName("segmentRecordings")
    @Expose
    private ArrayList<Recording> recordings;

    public static class Recording {

        @SerializedName("userProfileImageUrl")
        @Expose
        private String userProfileImageUrl;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("userIdentifier")
        @Expose
        private String userIdentifier;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("online")
        @Expose
        private boolean online;
        @SerializedName("lastSeen")
        @Expose
        private long lastSeen;
        @SerializedName("metaData")
        @Expose
        private Object metaData;
        @SerializedName("recordingUrl")
        @Expose
        private String recordingUrl;
        @SerializedName("startedAt")
        @Expose
        private Long startedAt;
        @SerializedName("endedAt")
        @Expose
        private Long endedAt;

        public String getUserProfileImageUrl() {
            return userProfileImageUrl;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserIdentifier() {
            return userIdentifier;
        }

        public String getUserId() {
            return userId;
        }

        public boolean isOnline() {
            return online;
        }

        public long getLastSeen() {
            return lastSeen;
        }

        public Object getMetaData() {
            return metaData;
        }

        public String getRecordingUrl() {
            return recordingUrl;
        }

        public Long getStartedAt() {
            return startedAt;
        }

        public Long getEndedAt() {
            return endedAt;
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

    public ArrayList<Recording> getRecordings() {
        return recordings;
    }
}
