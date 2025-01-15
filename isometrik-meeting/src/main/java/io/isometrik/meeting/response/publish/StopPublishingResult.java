package io.isometrik.meeting.response.publish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopPublishingResult {
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
