package io.isometrik.meeting.events.meeting.utils;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallDuration {
    @SerializedName("memberId")
    @Expose
    private String memberId;

    @SerializedName("durationInMilliseconds")
    @Expose
    private long durationInMilliseconds;

    public String getMemberId() {
        return memberId;
    }

    public long getDurationInMilliseconds() {
        return durationInMilliseconds;
    }
}
