package io.isometrik.meeting.response.recording;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopRecordingResult {
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
