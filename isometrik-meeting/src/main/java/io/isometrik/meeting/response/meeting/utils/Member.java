package io.isometrik.meeting.response.meeting.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Member {

    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("online")
    @Expose
    private boolean online;
    @SerializedName("isAdmin")
    @Expose
    private boolean isAdmin;
    @SerializedName("lastSeen")
    @Expose
    private long lastSeen;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    @SerializedName("isPublishing")
    @Expose
    private boolean isPublishing;
    @SerializedName("isHost")
    @Expose
    private Boolean isHost;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("joinTime")
    @Expose
    private Long joinTime;

    @SerializedName("leaveTime")
    @Expose
    private Long leaveTime;


    /**
     * Gets user profile image url.
     *
     * @return the user profile image url
     */
    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets user identifier.
     *
     * @return the user identifier
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Is online boolean.
     *
     * @return the boolean
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Gets last seen.
     *
     * @return the last seen
     */
    public long getLastSeen() {
        return lastSeen;
    }

    /**
     * Gets meta data.
     *
     * @return the meta data
     */
    public JSONObject getMetaData() {

        try {
            return new JSONObject(new Gson().toJson(metaData));
        } catch (Exception ignore) {
            return new JSONObject();
        }
    }

    public boolean isPublishing() {
        return isPublishing;
    }

    public Boolean getHost() {
        return isHost;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }
}
