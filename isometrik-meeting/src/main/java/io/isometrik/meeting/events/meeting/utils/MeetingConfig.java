package io.isometrik.meeting.events.meeting.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetingConfig {
    @SerializedName("pushNotifications")
    @Expose
    private boolean pushNotifications;

    public boolean isPushNotifications() {
        return pushNotifications;
    }
}
