package io.isometrik.meeting.response.meeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EndMeetingResult {

    @SerializedName("msg")
    @Expose
    private String message;

    @SerializedName("recordingUrl")
    @Expose
    private String recordingUrl;

    /**
     * Gets message.
     *
     * @return the response message received
     */
    public String getMessage() {
        return message;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }
}
