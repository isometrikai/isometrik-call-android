package io.isometrik.meeting.response.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RejectJoinMeetingRequestResult {
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
}
