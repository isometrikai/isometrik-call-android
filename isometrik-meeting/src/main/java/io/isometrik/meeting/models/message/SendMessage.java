package io.isometrik.meeting.models.message;

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
import io.isometrik.meeting.builder.message.SendMessageQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.ErrorResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;
import io.isometrik.meeting.response.message.SendMessageResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the send a message in a meeting SendMessageQuery{@link
 * io.isometrik.meeting.builder.message.SendMessageQuery} request and parse the response(both success or
 * error) to return send message result
 * SendMessageResult{@link io.isometrik.meeting.response.message.SendMessageResult}
 * or error received.
 *
 * @see io.isometrik.meeting.builder.message.SendMessageQuery
 * @see io.isometrik.meeting.response.message.SendMessageResult
 */
public class SendMessage {

    /**
     * Validate params.
     *
     * @param sendMessageQuery  the send message request query
     * @param completionHandler the send message request completion handler
     * @param retrofitManager   the retrofit manager to make remote api calls
     * @param imConfiguration   the isometrik configuration instance
     * @param baseResponse      the base response instance to handle non 200 http response code messages
     * @param gson              the gson instance
     * @see io.isometrik.meeting.builder.message.SendMessageQuery
     * @see io.isometrik.meeting.response.CompletionHandler
     * @see io.isometrik.meeting.managers.RetrofitManager
     * @see io.isometrik.meeting.IMConfiguration
     * @see io.isometrik.meeting.response.error.BaseResponse
     * @see Gson
     */
    public void validateParams(@NotNull SendMessageQuery sendMessageQuery, @NotNull final CompletionHandler<SendMessageResult> completionHandler, @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration, @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

        String meetingId = sendMessageQuery.getMeetingId();
        String userToken = sendMessageQuery.getUserToken();
        Integer messageType = sendMessageQuery.getMessageType();
        String body = sendMessageQuery.getBody();
        String deviceId = sendMessageQuery.getDeviceId();
        String customType = sendMessageQuery.getCustomType();
        JSONObject metaData = sendMessageQuery.getMetaData();
        List<String> searchableTags = sendMessageQuery.getSearchableTags();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else if (meetingId == null || meetingId.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEETING_ID_MISSING);
        } else if (deviceId == null || deviceId.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_DEVICEID_MISSING);
        } else if (messageType == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_TYPE_MISSING);
        } else if (body == null) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_BODY_MISSING);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userToken", userToken);

            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("meetingId", meetingId);
            bodyParams.put("messageType", messageType);
            bodyParams.put("body", body);
            bodyParams.put("deviceId", deviceId);

            if (customType != null) bodyParams.put("customType", customType);
            if (metaData != null)
                bodyParams.put("metaData", JsonParser.parseString(metaData.toString()));
            if (searchableTags != null) bodyParams.put("searchableTags", searchableTags);

            Call<SendMessageResult> call = retrofitManager.getMessageService().sendMessage(headers, bodyParams);
            call.enqueue(new Callback<SendMessageResult>() {
                @Override
                public void onResponse(@NotNull Call<SendMessageResult> call, @NotNull Response<SendMessageResult> response) {

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

                                isometrikErrorBuilder = baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse, false);
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
                public void onFailure(@NotNull Call<SendMessageResult> call, @NotNull Throwable t) {
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
