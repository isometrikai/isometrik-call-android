package io.isometrik.meeting.services;

import java.util.Map;

import io.isometrik.meeting.response.action.AcceptJoinMeetingRequestResult;
import io.isometrik.meeting.response.action.RejectJoinMeetingRequestResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;


public interface ActionService {

    @POST("/meetings/v1/accept")
    Call<AcceptJoinMeetingRequestResult> acceptJoinMeetingRequest(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

    @POST("/meetings/v1/reject")
    Call<RejectJoinMeetingRequestResult> rejectJoinMeetingRequest(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);


}
