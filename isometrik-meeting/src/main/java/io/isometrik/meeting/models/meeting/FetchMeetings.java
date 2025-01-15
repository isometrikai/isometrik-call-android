package io.isometrik.meeting.models.meeting;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.meetings.FetchMeetingsQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.ErrorResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.error.IsometrikErrorBuilder;
import io.isometrik.meeting.response.meeting.FetchMeetingsResult;
import io.isometrik.meeting.utils.MapUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the fetch meetings request and expose the parsed successful or error response.
 */
public class FetchMeetings {
    /**
     * Validate params.
     *
     * @param fetchMeetingsQuery the fetch meetings query
     * @param completionHandler  the completion handler
     * @param retrofitManager    the retrofit manager
     * @param imConfiguration    the im configuration
     * @param baseResponse       the base response
     * @param gson               the gson
     */
    public void validateParams(@NotNull FetchMeetingsQuery fetchMeetingsQuery,
                               @NotNull final CompletionHandler<FetchMeetingsResult> completionHandler,
                               @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
                               @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
        String userToken = fetchMeetingsQuery.getUserToken();

        if (userToken == null || userToken.isEmpty()) {
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
        } else {
            Integer sort = fetchMeetingsQuery.getSort();
            Integer meetingType = fetchMeetingsQuery.getMeetingType();
            Integer limit = fetchMeetingsQuery.getLimit();
            Integer skip = fetchMeetingsQuery.getSkip();
            Boolean includeMembers = fetchMeetingsQuery.getIncludeMembers();
            List<String> membersIncluded = fetchMeetingsQuery.getMembersIncluded();
            List<String> membersExactly = fetchMeetingsQuery.getMembersExactly();
            List<String> meetingIds = fetchMeetingsQuery.getMeetingIds();
            String customType = fetchMeetingsQuery.getCustomType();
            String searchTag = fetchMeetingsQuery.getSearchTag();
            Boolean hdMeeting = fetchMeetingsQuery.getHdMeeting();
            Boolean recorded = fetchMeetingsQuery.getRecorded();
            Boolean audioOnly = fetchMeetingsQuery.getAudioOnly();

            Map<String, String> headers = new HashMap<>();
            headers.put("licenseKey", imConfiguration.getLicenseKey());
            headers.put("appSecret", imConfiguration.getAppSecret());
            headers.put("userToken", userToken);

            Map<String, Object> queryParams = new HashMap<>();
            if (membersIncluded != null) {
                queryParams.put("membersIncluded", MapUtils.stringListToCsv(membersIncluded));
            }
            if (membersExactly != null) {
                queryParams.put("membersExactly", MapUtils.stringListToCsv(membersExactly));
            }
            if (meetingIds != null)
                queryParams.put("ids", MapUtils.stringListToCsv(meetingIds));
            if (customType != null) queryParams.put("customType", customType);
            if (searchTag != null) queryParams.put("searchTag", searchTag);
            if (meetingType != null) queryParams.put("meetingType", meetingType);

            if (includeMembers != null) {
                queryParams.put("includeMembers", includeMembers);
                if (includeMembers) {
                    Integer membersSkip = fetchMeetingsQuery.getMembersSkip();
                    Integer membersLimit = fetchMeetingsQuery.getMembersLimit();


                    if (membersSkip != null) queryParams.put("membersSkip", membersSkip);
                    if (membersLimit != null) queryParams.put("membersLimit", membersLimit);
                }
            }

            if (skip != null) queryParams.put("skip", skip);
            if (limit != null) queryParams.put("limit", limit);
            if (sort != null) queryParams.put("sort", sort);

            if (hdMeeting != null) queryParams.put("hdMeeting", hdMeeting);
            if (recorded != null) queryParams.put("recorded", recorded);
            if (audioOnly != null) queryParams.put("audioOnly", audioOnly);

            Call<FetchMeetingsResult> call =
                    retrofitManager.getMeetingService().fetchMeetings(headers, queryParams);

            call.enqueue(new Callback<FetchMeetingsResult>() {
                @Override
                public void onResponse(@NotNull Call<FetchMeetingsResult> call,
                                       @NotNull Response<FetchMeetingsResult> response) {

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
                public void onFailure(@NotNull Call<FetchMeetingsResult> call, @NotNull Throwable t) {
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
