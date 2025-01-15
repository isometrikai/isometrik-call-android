package io.isometrik.meeting.response.meeting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.isometrik.meeting.response.meeting.utils.Meeting;

public class FetchMeetingsResult {
    @SerializedName("msg")
    @Expose
    private String message;

    @SerializedName("meetings")
    @Expose
    private ArrayList<Meeting> meetings;

    /**
     * Gets message.
     *
     * @return the response message received
     */
    public String getMessage() {
        return message;
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }
}
