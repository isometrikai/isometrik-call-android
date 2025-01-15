package io.isometrik.meeting.response.member;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

import io.isometrik.meeting.builder.member.FetchMemberDetailsQuery;

/**
 * The class to parse fetch user details result of fetching the user details query
 * FetchUserDetailsQuery{@link FetchMemberDetailsQuery}.
 *
 * @see FetchMemberDetailsQuery
 */
public class FetchMemberDetailsResult implements Serializable {
    @SerializedName("msg")
    @Expose
    private String message;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("metaData")
    @Expose
    private Object metaData;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("online")
    @Expose
    private boolean online;
    @SerializedName("notification")
    @Expose
    private boolean notification;
    @SerializedName("visibility")
    @Expose
    private boolean visibility;
    @SerializedName("lastSeen")
    @Expose
    private long lastSeen;
    @SerializedName("updatedAt")
    @Expose
    private long updatedAt;
    @SerializedName("createdAt")
    @Expose
    private long createdAt;

    public String getUserName() {
        return userName;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isNotification() {
        return notification;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public long getCreatedAt() {
        return createdAt;
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

    public String getMessage() {
        return message;
    }
}
