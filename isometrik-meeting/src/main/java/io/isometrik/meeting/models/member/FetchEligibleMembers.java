package io.isometrik.meeting.models.member;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.member.FetchEligibleMembersQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.ErrorResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;
import io.isometrik.meeting.response.member.FetchEligibleMembersResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the fetch members to add to meeting request and expose the parsed successful or error response.
 */
public class FetchEligibleMembers {
    /**
     * Validate params.
     *
     * @param fetchEligibleMembersQuery the fetch members to add to meeting query
     * @param completionHandler         the completion handler
     * @param retrofitManager           the retrofit manager
     * @param imConfiguration           the im configuration
     * @param baseResponse              the base response
     * @param gson                      the gson
     */
    public void validateParams(@NotNull FetchEligibleMembersQuery fetchEligibleMembersQuery, @NotNull final CompletionHandler<FetchEligibleMembersResult> completionHandler, @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration, @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

        String meetingId = fetchEligibleMembersQuery.getMeetingId();
        Integer skip = fetchEligibleMembersQuery.getSkip();
        Integer limit = fetchEligibleMembersQuery.getLimit();

        String searchTag = fetchEligibleMembersQuery.getSearchTag();

        String userToken = fetchEligibleMembersQuery.getUserToken();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else if (meetingId == null || meetingId.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEETING_ID_MISSING);
        } else {
            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userToken", userToken);

            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("meetingId", meetingId);

            if (skip != null) queryParams.put("skip", skip);
            if (limit != null) queryParams.put("limit", limit);
            if (searchTag != null) queryParams.put("searchTag", searchTag);

            Call<FetchEligibleMembersResult> call = retrofitManager.getMemberService().fetchEligibleMembers(headers, queryParams);

            call.enqueue(new Callback<FetchEligibleMembersResult>() {
                @Override
                public void onResponse(@NotNull Call<FetchEligibleMembersResult> call, @NotNull Response<FetchEligibleMembersResult> response) {

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
                public void onFailure(@NotNull Call<FetchEligibleMembersResult> call, @NotNull Throwable t) {
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
