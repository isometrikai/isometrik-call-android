package io.isometrik.meeting.response.publish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartPublishingResult {

    @SerializedName("uid")
    @Expose
    private long uid;

    @SerializedName("joinTime")
    @Expose
    private long joinTime;

    @SerializedName("rtcToken")
    @Expose
    private String rtcToken;

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

    public long getJoinTime() {
        return joinTime;
    }

    public String getRtcToken() {
        return rtcToken;
    }
}
