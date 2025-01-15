package io.isometrik.ui.meetings.create.onetoone.enums;

/**
 * The enum for the meeting types.
 */
public enum CallType {

    /**
     * Normal meeting type.
     */
    AudioCall("AudioCall"),

    /**
     * Public meeting type.
     */
    VideoCall("VideoCall");
    private final String value;

    CallType(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public final String getValue() {
        return this.value;
    }
}
