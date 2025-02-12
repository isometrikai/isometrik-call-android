package io.isometrik.meeting.response.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the add members request.
 */
public class AddMembersResult {
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

    /**
     * Gets members count.
     *
     * @return the members count
     */
    public int getMembersCount() {
        return membersCount;
    }
}
