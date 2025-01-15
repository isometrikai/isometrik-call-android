package io.isometrik.meeting.services;

import java.util.Map;

import io.isometrik.meeting.response.recording.FetchRecordingsResult;
import io.isometrik.meeting.response.recording.StartRecordingResult;
import io.isometrik.meeting.response.recording.StopRecordingResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RecordingService {
    @POST("/meetings/v1/recording/start")
    Call<StartRecordingResult> startRecording(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

    @POST("/meetings/v1/recording/stop")
    Call<StopRecordingResult> stopRecording(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

    /**
     * Fetch recordings call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @GET("/meetings/v1/recordings")
    Call<FetchRecordingsResult> fetchRecordings(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);
}
