package io.isometrik.meeting.response.meeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveMeetingResult {

    @SerializedName("msg")
    @Expose
    private String message;

    @SerializedName("membersCount")
    @Expose
    private int membersCount;

    /**
     * Gets message.
     *
     * @return the response message received
     */
    public String getMessage() {
        return message;
    }

    public int getMembersCount() {
        return membersCount;
    }
}
