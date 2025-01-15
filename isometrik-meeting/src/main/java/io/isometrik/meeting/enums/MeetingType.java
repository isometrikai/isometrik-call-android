package io.isometrik.meeting.enums;

/**
 * The enum for the meeting types.
 */
public enum MeetingType {

    /**
     * Normal meeting type.
     */
    NormalMeeting(0),

    /**
     * Public meeting type.
     */
    PublicMeeting(1);
    private final int value;

    MeetingType(int value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public final int getValue() {
        return this.value;
    }
}
