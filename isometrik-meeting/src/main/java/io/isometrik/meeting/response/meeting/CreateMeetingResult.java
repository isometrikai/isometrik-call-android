package io.isometrik.meeting.response.meeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateMeetingResult {

    @SerializedName("uid")
    @Expose
    private long uid;

    @SerializedName("creationTime")
    @Expose
    private long creationTime;

    @SerializedName("rtcToken")
    @Expose
    private String rtcToken;

    @SerializedName("meetingId")
    @Expose
    private String meetingId;

    @SerializedName("msg")
    @Expose
    private String message;

    /**
     * Gets message.
     *
     * @return the response message received
     */
    public String getMessage() {
        return message;
    }

    public long getUid() {
        return uid;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getRtcToken() {
        return rtcToken;
    }

    public String getMeetingId() {
        return meetingId;
    }
}
