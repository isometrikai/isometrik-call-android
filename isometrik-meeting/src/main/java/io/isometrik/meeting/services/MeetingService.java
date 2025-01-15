package io.isometrik.meeting.services;

import java.util.Map;

import io.isometrik.meeting.response.meeting.CreateMeetingResult;
import io.isometrik.meeting.response.meeting.EndMeetingResult;
import io.isometrik.meeting.response.meeting.FetchMeetingsResult;
import io.isometrik.meeting.response.meeting.LeaveMeetingResult;
import io.isometrik.meeting.response.meeting.JoinMeetingResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;


public interface MeetingService {

    @POST("/meetings/v1/meeting")
    Call<CreateMeetingResult> createMeeting(@HeaderMap Map<String, String> headers,
                                            @Body Map<String, Object> bodyParams);

    @PUT("/meetings/v1/meeting")
    Call<EndMeetingResult> endMeeting(@HeaderMap Map<String, String> headers,
                                      @Body Map<String, Object> bodyParams);

    @GET("/meetings/v1/meetings")
    Call<FetchMeetingsResult> fetchMeetings(
            @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

    /**
     * Join meeting call.
     *
     * @param headers    the headers
     * @param bodyParams the body params
     * @return the call
     */
    @POST("/meetings/v1/join")
    Call<JoinMeetingResult> joinMeeting(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

    @DELETE("/meetings/v1/leave")
    Call<LeaveMeetingResult> leaveMeeting(
            @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);
}
