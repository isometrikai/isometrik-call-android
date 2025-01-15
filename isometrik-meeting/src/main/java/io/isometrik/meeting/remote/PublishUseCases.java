package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.meetings.CreateMeetingQuery;
import io.isometrik.meeting.builder.publish.StartPublishingQuery;
import io.isometrik.meeting.builder.publish.StopPublishingQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.publish.StartPublishing;
import io.isometrik.meeting.models.publish.StopPublishing;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.meeting.CreateMeetingResult;
import io.isometrik.meeting.response.publish.StartPublishingResult;
import io.isometrik.meeting.response.publish.StopPublishingResult;

public class PublishUseCases {

    private final StartPublishing startPublishing;
    private final StopPublishing stopPublishing;

    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    public PublishUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
                           BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        startPublishing = new StartPublishing();
        stopPublishing = new StopPublishing();

    }

    public void startPublishing(@NotNull StartPublishingQuery startPublishingQuery, @NotNull CompletionHandler<StartPublishingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            startPublishing.validateParams(startPublishingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void stopPublishing(@NotNull StopPublishingQuery stopPublishingQuery, @NotNull CompletionHandler<StopPublishingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            stopPublishing.validateParams(stopPublishingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }
}
