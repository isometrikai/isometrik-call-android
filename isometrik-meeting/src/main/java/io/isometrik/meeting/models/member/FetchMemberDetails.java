package io.isometrik.meeting.models.member;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.member.FetchMemberDetailsQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.ErrorResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;
import io.isometrik.meeting.response.member.FetchMemberDetailsResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the fetch meeting member details request and expose the parsed successful or error response.
 */
public class FetchMemberDetails {
    /**
     * Validate params.
     *
     * @param fetchMemberDetailsQuery the fetch meeting member details query
     * @param completionHandler       the completion handler
     * @param retrofitManager         the retrofit manager
     * @param imConfiguration         the im configuration
     * @param baseResponse            the base response
     * @param gson                    the gson
     */
    public void validateParams(
            @NotNull FetchMemberDetailsQuery fetchMemberDetailsQuery,
            @NotNull final CompletionHandler<FetchMemberDetailsResult> completionHandler,
            @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
            @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
        String userToken = fetchMemberDetailsQuery.getUserToken();
        String meetingId = fetchMemberDetailsQuery.getMeetingId();
        String memberId = fetchMemberDetailsQuery.getMemberId();

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

            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("meetingId", meetingId);
            queryParams.put("memberId", memberId);

            Call<FetchMemberDetailsResult> call =
                    retrofitManager.getMemberService()
                            .fetchMemberDetails(headers, queryParams);

            call.enqueue(new Callback<FetchMemberDetailsResult>() {
                @Override
                public void onResponse(@NotNull Call<FetchMemberDetailsResult> call,
                                       @NotNull Response<FetchMemberDetailsResult> response) {

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
                                new IsometrikError.Builder().setHttpResponseCode(response.code()).setRemoteError(true);

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
                public void onFailure(@NotNull Call<FetchMemberDetailsResult> call, @NotNull Throwable t) {
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