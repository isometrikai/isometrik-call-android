package io.isometrik.meeting.models.meeting;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.meetings.CreateMeetingQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.ErrorResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;
import io.isometrik.meeting.response.meeting.CreateMeetingResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the create meeting request and expose the parsed successful or error response.
 */
public class CreateMeeting {
    /**
     * Validate params.
     *
     * @param createMeetingQuery the create meeting query
     * @param completionHandler  the completion handler
     * @param retrofitManager    the retrofit manager
     * @param imConfiguration    the im configuration
     * @param baseResponse       the base response
     * @param gson               the gson
     */
    public void validateParams(@NotNull CreateMeetingQuery createMeetingQuery, @NotNull final CompletionHandler<CreateMeetingResult> completionHandler, @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration, @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

        String userToken = createMeetingQuery.getUserToken();
        List<String> members = createMeetingQuery.getMembers();
        Boolean audioOnly = createMeetingQuery.getAudioOnly();
        Boolean enableRecording = createMeetingQuery.getEnableRecording();
        Boolean hdMeeting = createMeetingQuery.getHdMeeting();
        Boolean selfHosted = createMeetingQuery.getSelfHosted();
        Boolean pushNotifications = createMeetingQuery.getPushNotifications();
        Integer meetingType = createMeetingQuery.getMeetingType();
        Boolean autoTerminate = createMeetingQuery.getAutoTerminate();
        String deviceId = createMeetingQuery.getDeviceId();

        String meetingImageUrl = createMeetingQuery.getMeetingImageUrl();
        String meetingDescription = createMeetingQuery.getMeetingDescription();
        String conversationId = createMeetingQuery.getConversationId();
        JSONObject metaData = createMeetingQuery.getMetaData();
        String customType = createMeetingQuery.getCustomType();
        List<String> searchableTags = createMeetingQuery.getSearchableTags();
        String meetingCreationNotificationTitle = createMeetingQuery.getMeetingCreationNotificationTitle();
        String meetingCreationNotificationBody = createMeetingQuery.getMeetingCreationNotificationBody();
        String meetingEndNotificationTitle = createMeetingQuery.getMeetingEndNotificationTitle();
        String meetingEndNotificationBody = createMeetingQuery.getMeetingEndNotificationBody();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else if (audioOnly == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_AUDIO_ONLY_MISSING);
        } else if (enableRecording == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_ENABLE_RECORDING_MISSING);
        } else if (hdMeeting == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_HD_MEETING_MISSING);
        } else if (selfHosted == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SELF_HOSTED_MISSING);
        } else if (pushNotifications == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PUSH_NOTIFICATIONS_MISSING);
        } else if (meetingType == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEETING_TYPE_MISSING);
        } else if (autoTerminate == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_AUTO_TERMINATE_MISSING);
        } else if (deviceId == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_DEVICEID_MISSING);
        } else if (members == null || members.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEMBERS_MISSING);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userToken", userToken);

            Map<String, Object> bodyParams = new HashMap<>();

            bodyParams.put("members", members);
            bodyParams.put("audioOnly", audioOnly);
            bodyParams.put("enableRecording", enableRecording);
            bodyParams.put("hdMeeting", hdMeeting);
            bodyParams.put("selfHosted", selfHosted);
            bodyParams.put("pushNotifications", pushNotifications);
            bodyParams.put("meetingType", meetingType);
            bodyParams.put("autoTerminate", autoTerminate);
            bodyParams.put("deviceId", deviceId);

            if (meetingImageUrl != null) {
                bodyParams.put("meetingImageUrl", meetingImageUrl);
            }
            if (meetingDescription != null) {
                bodyParams.put("meetingDescription", meetingDescription);
            }
            if (conversationId != null) {
                bodyParams.put("conversationId", conversationId);
            }
            if (metaData != null)
                bodyParams.put("metaData", JsonParser.parseString(metaData.toString()));
            if (customType != null) bodyParams.put("customType", customType);
            if (searchableTags != null) bodyParams.put("searchableTags", searchableTags);
            if (meetingCreationNotificationTitle != null) {
                bodyParams.put("meetingCreationNotificationTitle", meetingCreationNotificationTitle);
            }
            if (meetingCreationNotificationBody != null) {
                bodyParams.put("meetingCreationNotificationBody", meetingCreationNotificationBody);
            }
            if (meetingEndNotificationTitle != null) {
                bodyParams.put("meetingEndNotificationTitle", meetingEndNotificationTitle);
            }
            if (meetingEndNotificationBody != null) {
                bodyParams.put("meetingEndNotificationBody", meetingEndNotificationBody);
            }

            Call<CreateMeetingResult> call = retrofitManager.getMeetingService().createMeeting(headers, bodyParams);

            call.enqueue(new Callback<CreateMeetingResult>() {
                @Override
                public void onResponse(@NotNull Call<CreateMeetingResult> call, @NotNull Response<CreateMeetingResult> response) {

                    if (response.isSuccessful()) {

                        if (response.code() == 200) {
                            completionHandler.onComplete(response.body(), null);
                        }
                    } else {

                        ErrorResponse errorResponse;
                        IsometrikError.Builder isometrikErrorBuilder;
                        try {

                            if (response.errorBody() != null) {
                                errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                            } else {
                                errorResponse = new ErrorResponse();
                            }
                        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
                            // handle failure to read error
                            errorResponse = new ErrorResponse();
                        }

                        isometrikErrorBuilder = new IsometrikError.Builder().setHttpResponseCode(response.code()).setRemoteError(true);

                        switch (response.code()) {
                            case 400:

                                isometrikErrorBuilder =
                                        baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse, false);
                                break;
                            case 401:

                                isometrikErrorBuilder = baseResponse.handle401responseCode(isometrikErrorBuilder, errorResponse, false);
                                break;

                            case 403:

                                isometrikErrorBuilder = baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            case 404:

                                isometrikErrorBuilder = baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
                                break;


                            case 422:

                                isometrikErrorBuilder = baseResponse.handle422responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            case 502:

                                isometrikErrorBuilder = baseResponse.handle502responseCode(isometrikErrorBuilder);
                                break;

                            case 503:
                                isometrikErrorBuilder = baseResponse.handle503responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            default:
                                //500 response code
                                isometrikErrorBuilder = baseResponse.handle500responseCode(isometrikErrorBuilder);
                        }

                        completionHandler.onComplete(null, isometrikErrorBuilder.build());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<CreateMeetingResult> call, @NotNull Throwable t) {
                    if (t instanceof IOException) {
                        // Network failure
                        completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
                    } else {
                        // Parsing error
                        completionHandler.onComplete(null, baseResponse.handleParsingError(t));
                    }
                }
            });
        }
    }
}
