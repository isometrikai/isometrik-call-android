package io.isometrik.meeting.response.member;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch members result of fetching members list in a meeting query
 * FetchMembersQuery{@link io.isometrik.meeting.builder.member.FetchMembersQuery}.
 *
 * @see io.isometrik.meeting.builder.member.FetchMembersQuery
 */
public class FetchMembersResult implements Serializable {
    @SerializedName("msg")
    @Expose
    private String message;

    @SerializedName("membersCount")
    @Expose
    private int membersCount;

    @SerializedName("meetingMembers")
    @Expose
    private ArrayList<MeetingMember> meetingMembers;

    public static class MeetingMember {

        @SerializedName("isPublishing")
        @Expose
        private boolean isPublishing;
        @SerializedName("isAdmin")
        @Expose
        private boolean isAdmin;

        @SerializedName("isHost")
        @Expose
        private boolean isHost;
        @SerializedName("joinTime")
        @Expose
        private Long joinTime;
        @SerializedName("leaveTime")
        @Expose
        private Long leaveTime;
        @SerializedName("accepted")
        @Expose
        private boolean accepted;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
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
        @SerializedName("lastSeen")
        @Expose
        private long lastSeen;
        @SerializedName("metaData")
        @Expose
        private Object metaData;

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

        public boolean isAdmin() {
            return isAdmin;
        }

        public boolean isHost() {
            return isHost;
        }

        public Long getJoinTime() {
            return joinTime;
        }

        public Long getLeaveTime() {
            return leaveTime;
        }

        public boolean isAccepted() {
            return accepted;
        }

        public String getDeviceId() {
            return deviceId;
        }
    }

    /**
     * Gets message.
     *
     * @return the response message received
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets meeting members.
     *
     * @return the meeting members
     */
    public ArrayList<MeetingMember> getMeetingMembers() {
        return meetingMembers;
    }

    /**
     * Gets members count.
     *
     * @return the members count
     */
    public int getMembersCount() {
        return membersCount;
    }
}
