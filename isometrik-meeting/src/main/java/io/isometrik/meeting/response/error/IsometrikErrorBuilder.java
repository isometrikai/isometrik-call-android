package io.isometrik.meeting.response.error;

/**
 * The helper class for building Isometrik errors for local validations.
 */
public final class IsometrikErrorBuilder {

    // Error Codes

    private static final int IMERR_LICENSE_KEY_MISSING = 101;

    /**
     * The constant IMERROBJ_LICENSE_KEY_MISSING.
     */
    public static final IsometrikError IMERROBJ_LICENSE_KEY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_LICENSE_KEY_MISSING).setErrorMessage("License key not configured.").setRemoteError(false).build();

    private static final int IMERR_APP_SECRET_MISSING = 102;

    /**
     * The constant IMERROBJ_APP_SECRET_MISSING.
     */
    public static final IsometrikError IMERROBJ_APP_SECRET_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_APP_SECRET_MISSING).setErrorMessage("App secret not configured.").setRemoteError(false).build();

    private static final int IMERR_USER_SECRET_MISSING = 103;

    /**
     * The constant IMERROBJ_USER_SECRET_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_SECRET_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_SECRET_MISSING).setErrorMessage("User secret not configured.").setRemoteError(false).build();

    private static final int IMERR_CLIENT_ID_MISSING = 104;

    /**
     * The constant IMERROBJ_CLIENT_ID_MISSING.
     */
    public static final IsometrikError IMERROBJ_CLIENT_ID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CLIENT_ID_MISSING).setErrorMessage("Client id not configured.").setRemoteError(false).build();

    private static final int IMERR_USER_IDENTIFIER_MISSING = 105;

    /**
     * The constant IMERROBJ_USER_IDENTIFIER_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_IDENTIFIER_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IDENTIFIER_MISSING).setErrorMessage("userIdentifier is missing.").setRemoteError(false).build();

    private static final int IMERR_USER_PASSWORD_MISSING = 106;

    /**
     * The constant IMERROBJ_USER_PASSWORD_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_PASSWORD_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PASSWORD_MISSING).setErrorMessage("userPassword is missing.").setRemoteError(false).build();

    private static final int IMERR_UPDATE_USER_DETAILS_MISSING = 107;

    /**
     * The constant IMERROBJ_UPDATE_USER_DETAILS_MISSING.
     */
    public static final IsometrikError IMERROBJ_UPDATE_USER_DETAILS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UPDATE_USER_DETAILS_MISSING).setErrorMessage("Atleast one of userIdentifier, userProfileImageUrl, userName, notification and metadata should be specified.").setRemoteError(false).build();

    private static final int IMERR_USER_PROFILE_IMAGEURL_MISSING = 108;

    /**
     * The constant IMERROBJ_USER_PROFILE_IMAGEURL_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_PROFILE_IMAGEURL_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_PROFILE_IMAGEURL_MISSING).setErrorMessage("userProfileImageUrl is missing.").setRemoteError(false).build();

    private static final int IMERR_USER_NAME_MISSING = 109;

    /**
     * The constant IMERROBJ_USER_NAME_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_NAME_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_NAME_MISSING).setErrorMessage("userName is missing.").setRemoteError(false).build();

    private static final int IMERR_CONNECTION_STRING_MISSING = 110;

    /**
     * The constant IMERROBJ_CONNECTION_STRING_MISSING.
     */
    public static final IsometrikError IMERROBJ_CONNECTION_STRING_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONNECTION_STRING_MISSING).setErrorMessage("Connection string not configured.").setRemoteError(false).build();

    private static final int IMERR_CONNECTION_STRING_INVALID_VALUE = 111;

    /**
     * The constant IMERROBJ_CONNECTION_STRING_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_CONNECTION_STRING_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CONNECTION_STRING_INVALID_VALUE).setErrorMessage("Connection string has invalid value.").setRemoteError(false).build();

    private static final int IMERR_USER_TOKEN_MISSING = 112;

    /**
     * The constant IMERROBJ_USER_TOKEN_MISSING.
     */
    public static final IsometrikError IMERROBJ_USER_TOKEN_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_TOKEN_MISSING).setErrorMessage("userToken is missing.").setRemoteError(false).build();

    private static final int IMERR_PRESIGNED_URL_REQUESTS_INVALID_VALUE = 113;

    /**
     * The constant IMERROBJ_PRESIGNED_URL_REQUESTS_INVALID_VALUE.
     */
    public static final IsometrikError IMERROBJ_PRESIGNED_URL_REQUESTS_INVALID_VALUE = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRESIGNED_URL_REQUESTS_INVALID_VALUE).setErrorMessage("Presigned urls has invalid value.").setRemoteError(false).build();


    private static final int IMERR_MEDIA_PATH_MISSING = 114;

    /**
     * The constant IMERROBJ_MEDIA_PATH_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEDIA_PATH_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_PATH_MISSING).setErrorMessage("media path is missing.").setRemoteError(false).build();
    private static final int IMERR_UPLOAD_MEDIA_FAILED = 115;

    /**
     * The constant IMERROBJ_UPLOAD_MEDIA_FAILED.
     */
    public static final IsometrikError IMERROBJ_UPLOAD_MEDIA_FAILED = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_UPLOAD_MEDIA_FAILED).setErrorMessage("media upload failed.").setRemoteError(false).build();

    private static final int IMERR_CANCEL_UPLOAD_REQUEST_NOT_FOUND = 116;

    /**
     * The constant IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND.
     */
    public static final IsometrikError IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_CANCEL_UPLOAD_REQUEST_NOT_FOUND).setErrorMessage("mediaUpload request to be canceled not found.").setRemoteError(false).build();

    private static final int IMERR_MEDIAID_MISSING = 117;

    /**
     * The constant IMERROBJ_MEDIAID_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEDIAID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIAID_MISSING).setErrorMessage("mediaId is missing.").setRemoteError(false).build();

    private static final int IMERR_MEDIA_UPLOAD_CANCELED = 118;

    /**
     * The constant IMERROBJ_MEDIA_UPLOAD_CANCELED.
     */
    public static final IsometrikError IMERROBJ_MEDIA_UPLOAD_CANCELED = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_UPLOAD_CANCELED).setErrorMessage("Media upload has been canceled.").setRemoteError(false).build();

    private static final int IMERR_PRESIGNED_URL_MISSING = 119;
    /**
     * The constant IMERROBJ_PRESIGNED_URL_MISSING.
     */
    public static final IsometrikError IMERROBJ_PRESIGNED_URL_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PRESIGNED_URL_MISSING).setErrorMessage("presignedUrl is missing.").setRemoteError(false).build();

    private static final int IMERR_REQUESTID_MISSING = 120;

    /**
     * The constant IMERROBJ_REQUESTID_MISSING.
     */
    public static final IsometrikError IMERROBJ_REQUESTID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_REQUESTID_MISSING).setErrorMessage("requestId is missing.").setRemoteError(false).build();
    private static final int IMERR_DEVICEID_MISSING = 121;

    /**
     * The constant IMERROBJ_DEVICEID_MISSING.
     */
    public static final IsometrikError IMERROBJ_DEVICEID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_DEVICEID_MISSING).setErrorMessage("deviceId is missing.").setRemoteError(false).build();
    private static final int IMERR_MEDIA_EXTENSION_MISSING = 122;

    /**
     * The constant IMERROBJ_MEDIA_EXTENSION_MISSING.
     */
    public static final IsometrikError IMERROBJ_MEDIA_EXTENSION_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEDIA_EXTENSION_MISSING).setErrorMessage("mediaExtension is missing.").setRemoteError(false).build();
    private static final int IMERR_USER_IMAGE_UPLOAD_CANCELED = 123;

    /**
     * The constant IMERROBJ_USER_IMAGE_UPLOAD_CANCELED.
     */
    public static final IsometrikError IMERROBJ_USER_IMAGE_UPLOAD_CANCELED = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_USER_IMAGE_UPLOAD_CANCELED).setErrorMessage("User image upload has been canceled.").setRemoteError(false).build();

    private static final int IMERR_AUDIO_ONLY_MISSING = 124;


    public static final IsometrikError IMERROBJ_AUDIO_ONLY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_AUDIO_ONLY_MISSING).setErrorMessage("audioOnly is missing.").setRemoteError(false).build();

    private static final int IMERR_ENABLE_RECORDING_MISSING = 125;


    public static final IsometrikError IMERROBJ_ENABLE_RECORDING_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_ENABLE_RECORDING_MISSING).setErrorMessage("enableRecording is missing.").setRemoteError(false).build();

    private static final int IMERR_HD_MEETING_MISSING = 126;


    public static final IsometrikError IMERROBJ_HD_MEETING_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_HD_MEETING_MISSING).setErrorMessage("hdMeeting is missing.").setRemoteError(false).build();

    private static final int IMERR_SELF_HOSTED_MISSING = 127;


    public static final IsometrikError IMERROBJ_SELF_HOSTED_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_SELF_HOSTED_MISSING).setErrorMessage("selfHosted is missing.").setRemoteError(false).build();

    private static final int IMERR_PUSH_NOTIFICATIONS_MISSING = 128;


    public static final IsometrikError IMERROBJ_PUSH_NOTIFICATIONS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_PUSH_NOTIFICATIONS_MISSING).setErrorMessage("pushNotifications is missing.").setRemoteError(false).build();

    private static final int IMERR_MEETING_TYPE_MISSING = 129;


    public static final IsometrikError IMERROBJ_MEETING_TYPE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEETING_TYPE_MISSING).setErrorMessage("meetingType is missing.").setRemoteError(false).build();

    private static final int IMERR_AUTO_TERMINATE_MISSING = 130;


    public static final IsometrikError IMERROBJ_AUTO_TERMINATE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_AUTO_TERMINATE_MISSING).setErrorMessage("autoTerminate is missing.").setRemoteError(false).build();

    private static final int IMERR_MEETING_ID_MISSING = 131;


    public static final IsometrikError IMERROBJ_MEETING_ID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEETING_ID_MISSING).setErrorMessage("meetingId is missing.").setRemoteError(false).build();

    private static final int IMERR_MEMBERS_MISSING = 132;


    public static final IsometrikError IMERROBJ_MEMBERS_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBERS_MISSING).setErrorMessage("members is missing.").setRemoteError(false).build();


    /**
     * Not Found
     */
    private static final int IMERR_NOT_FOUND = 133;

    /**
     * Forbidden
     */
    private static final int IMERR_FORBIDDEN = 134;

    /**
     * Service unavailable
     */
    private static final int IMERR_SERVICE_UNAVAILABLE = 135;

    /**
     * Conflict
     */
    private static final int IMERR_CONFLICT = 136;

    /**
     * Unprocessable entity
     */
    private static final int IMERR_UNPROCESSABLE_ENTITY = 137;

    /**
     * Bad gateway
     */
    private static final int IMERR_BAD_GATEWAY = 138;

    /**
     * Internal server error
     */

    private static final int IMERR_INTERNAL_SERVER_ERROR = 139;

    /**
     * Parsing error
     */

    private static final int IMERR_PARSING_ERROR = 140;

    /**
     * Network error
     */

    private static final int IMERR_NETWORK_ERROR = 141;

    /**
     * Bad Request error
     */

    private static final int IMERR_BAD_REQUEST_ERROR = 142;

    /**
     * Unauthorized error
     */

    private static final int IMERR_UNAUTHORIZED_ERROR = 143;

    /**
     * Gets imerr not found.
     *
     * @return the imerr not found
     */
    static int getImerrNotFound() {
        return IMERR_NOT_FOUND;
    }

    /**
     * Gets imerr forbidden.
     *
     * @return the imerr forbidden
     */
    static int getImerrForbidden() {
        return IMERR_FORBIDDEN;
    }

    /**
     * Gets imerr service unavailable.
     *
     * @return the imerr service unavailable
     */
    static int getImerrServiceUnavailable() {
        return IMERR_SERVICE_UNAVAILABLE;
    }

    /**
     * Gets imerr conflict.
     *
     * @return the imerr conflict
     */
    static int getImerrConflict() {
        return IMERR_CONFLICT;
    }

    /**
     * Gets imerr unprocessable entity.
     *
     * @return the imerr unprocessable entity
     */
    static int getImerrUnprocessableEntity() {
        return IMERR_UNPROCESSABLE_ENTITY;
    }

    /**
     * Gets imerr bad gateway.
     *
     * @return the imerr bad gateway
     */
    static int getImerrBadGateway() {
        return IMERR_BAD_GATEWAY;
    }

    /**
     * Gets imerr internal server error.
     *
     * @return the imerr internal server error
     */
    static int getImerrInternalServerError() {
        return IMERR_INTERNAL_SERVER_ERROR;
    }

    /**
     * Gets imerr parsing error.
     *
     * @return the imerr parsing error
     */
    static int getImerrParsingError() {
        return IMERR_PARSING_ERROR;
    }

    /**
     * Gets imerr network error.
     *
     * @return the imerr network error
     */
    static int getImerrNetworkError() {
        return IMERR_NETWORK_ERROR;
    }

    /**
     * Gets imerr bad request error.
     *
     * @return the imerr bad request error
     */
    static int getImerrBadRequestError() {
        return IMERR_BAD_REQUEST_ERROR;
    }

    /**
     * Gets imerr unauthorized error.
     *
     * @return the imerr unauthorized error
     */
    public static int getImerrUnauthorizedError() {
        return IMERR_UNAUTHORIZED_ERROR;
    }

    private static final int IMERR_MESSAGE_TYPE_MISSING = 144;

    public static final IsometrikError IMERROBJ_MESSAGE_TYPE_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_TYPE_MISSING).setErrorMessage("messageType is missing.").setRemoteError(false).build();

    private static final int IMERR_MESSAGE_BODY_MISSING = 145;

    /**
     * The constant IMERROBJ_MESSAGE_BODY_MISSING.
     */
    public static final IsometrikError IMERROBJ_MESSAGE_BODY_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MESSAGE_BODY_MISSING).setErrorMessage("Message body is missing.").setRemoteError(false).build();


    private static final int IMERR_MEMBER_ID_MISSING = 146;


    public static final IsometrikError IMERROBJ_MEMBER_ID_MISSING = new IsometrikError.Builder().setIsometrikErrorCode(IMERR_MEMBER_ID_MISSING).setErrorMessage("memberId is missing.").setRemoteError(false).build();


}
