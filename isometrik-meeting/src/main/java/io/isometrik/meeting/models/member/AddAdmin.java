package io.isometrik.meeting.models.member;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.member.AddAdminQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.ErrorResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;
import io.isometrik.meeting.response.member.AddAdminResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the add admin request and expose the parsed successful or error response.
 */
public class AddAdmin {
    /**
     * Validate params.
     *
     * @param addAdminQuery     the add admin query
     * @param completionHandler the completion handler
     * @param retrofitManager   the retrofit manager
     * @param imConfiguration   the im configuration
     * @param baseResponse      the base response
     * @param gson              the gson
     */
    public void validateParams(@NotNull AddAdminQuery addAdminQuery,
                               @NotNull final CompletionHandler<AddAdminResult> completionHandler,
                               @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
                               @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
        String meetingId = addAdminQuery.getMeetingId();
        String memberId = addAdminQuery.getMemberId();
        String userToken = addAdminQuery.getUserToken();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else if (meetingId == null || meetingId.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEETING_ID_MISSING);
        } else if (memberId == null || memberId.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEMBER_ID_MISSING);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userToken", userToken);

            Map<String, Object> bodyParams = new HashMap<>();
            bodyParams.put("meetingId", meetingId);
            bodyParams.put("memberId", memberId);

            Call<AddAdminResult> call =
                    retrofitManager.getMemberService().addAdmin(headers, bodyParams);

            call.enqueue(new Callback<AddAdminResult>() {
                @Override
                public void onResponse(@NotNull Call<AddAdminResult> call,
                                       @NotNull Response<AddAdminResult> response) {

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

                        isometrikErrorBuilder =
                                new IsometrikError.Builder().setHttpResponseCode(response.code())
                                        .setRemoteError(true);

                        switch (response.code()) {

                            case 400:

                                isometrikErrorBuilder =
                                        baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse, false);
                                break;
                            case 401:

                                isometrikErrorBuilder = baseResponse.handle401responseCode(isometrikErrorBuilder, errorResponse, false);
                                break;

                            case 403:

                                isometrikErrorBuilder =
                                        baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            case 404:

                                isometrikErrorBuilder =
                                        baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            case 422:

                                isometrikErrorBuilder =
                                        baseResponse.handle422responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            case 502:

                                isometrikErrorBuilder = baseResponse.handle502responseCode(isometrikErrorBuilder);
                                break;

                            case 503:
                                isometrikErrorBuilder =
                                        baseResponse.handle503responseCode(isometrikErrorBuilder, errorResponse);
                                break;

                            default:
                                //500 response code
                                isometrikErrorBuilder = baseResponse.handle500responseCode(isometrikErrorBuilder);
                        }

                        completionHandler.onComplete(null, isometrikErrorBuilder.build());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AddAdminResult> call, @NotNull Throwable t) {
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
