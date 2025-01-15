package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.recording.FetchRecordingsQuery;
import io.isometrik.meeting.builder.recording.StartRecordingQuery;
import io.isometrik.meeting.builder.recording.StopRecordingQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.recording.FetchRecordings;
import io.isometrik.meeting.models.recording.StartRecording;
import io.isometrik.meeting.models.recording.StopRecording;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.recording.FetchRecordingsResult;
import io.isometrik.meeting.response.recording.StartRecordingResult;
import io.isometrik.meeting.response.recording.StopRecordingResult;

public class RecordingUseCases {
    private final StartRecording startRecording;
    private final StopRecording stopRecording;
    private final FetchRecordings fetchRecordings;

    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    public RecordingUseCases(IMConfiguration configuration, RetrofitManager retrofitManager, BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        startRecording = new StartRecording();
        stopRecording = new StopRecording();
        fetchRecordings = new FetchRecordings();
    }


    public void startRecording(@NotNull StartRecordingQuery startRecordingQuery, @NotNull CompletionHandler<StartRecordingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            startRecording.validateParams(startRecordingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void stopRecording(@NotNull StopRecordingQuery stopRecordingQuery, @NotNull CompletionHandler<StopRecordingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            stopRecording.validateParams(stopRecordingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void fetchRecordings(@NotNull FetchRecordingsQuery fetchRecordingsQuery, @NotNull CompletionHandler<FetchRecordingsResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchRecordings.validateParams(fetchRecordingsQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

}
