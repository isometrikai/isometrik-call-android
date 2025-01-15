package io.isometrik.meeting.services;

import java.util.Map;

import io.isometrik.meeting.response.publish.StartPublishingResult;
import io.isometrik.meeting.response.publish.StopPublishingResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;


public interface PublishService {

    @POST("/meetings/v1/publish/start")
    Call<StartPublishingResult> startPublishing(@HeaderMap Map<String, String> headers,
                                                @Body Map<String, Object> bodyParams);

    @POST("/meetings/v1/publish/stop")
    Call<StopPublishingResult> stopPublishing(@HeaderMap Map<String, String> headers,
                                              @Body Map<String, Object> bodyParams);


}
