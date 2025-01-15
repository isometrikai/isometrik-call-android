package io.isometrik.meeting.services;

import java.util.Map;

import io.isometrik.meeting.response.message.FetchMessagesCountResult;
import io.isometrik.meeting.response.message.FetchMessagesResult;
import io.isometrik.meeting.response.message.SendMessageResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface MessageService {
    @POST("/meetings/v1/publish/message")
    Call<SendMessageResult> sendMessage(@HeaderMap Map<String, String> headers,
                                        @Body Map<String, Object> bodyParams);

    /**
     * Fetch messages call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @GET("/meetings/v1/messages")
    Call<FetchMessagesResult> fetchMessages(@HeaderMap Map<String, String> headers,
                                            @QueryMap Map<String, Object> queryParams);

    /**
     * Fetch messages count call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @GET("/meetings/v1/messages/count")
    Call<FetchMessagesCountResult> fetchMessagesCount(@HeaderMap Map<String, String> headers,
                                                      @QueryMap Map<String, Object> queryParams);
}
