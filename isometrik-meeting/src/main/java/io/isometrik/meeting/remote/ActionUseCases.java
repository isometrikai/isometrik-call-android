package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.action.AcceptJoinMeetingRequestQuery;
import io.isometrik.meeting.builder.action.RejectJoinMeetingRequestQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.action.AcceptJoinMeetingRequest;
import io.isometrik.meeting.models.action.RejectJoinMeetingRequest;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.action.AcceptJoinMeetingRequestResult;
import io.isometrik.meeting.response.action.RejectJoinMeetingRequestResult;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;

public class ActionUseCases {

    private final AcceptJoinMeetingRequest acceptJoinMeetingRequest;
    private final RejectJoinMeetingRequest rejectJoinMeetingRequest;

    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    public ActionUseCases(IMConfiguration configuration, RetrofitManager retrofitManager, BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        acceptJoinMeetingRequest = new AcceptJoinMeetingRequest();
        rejectJoinMeetingRequest = new RejectJoinMeetingRequest();

    }

    public void acceptJoinMeetingRequest(@NotNull AcceptJoinMeetingRequestQuery acceptJoinMeetingRequestQuery, @NotNull CompletionHandler<AcceptJoinMeetingRequestResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            acceptJoinMeetingRequest.validateParams(acceptJoinMeetingRequestQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void rejectJoinMeetingRequest(@NotNull RejectJoinMeetingRequestQuery rejectJoinMeetingRequestQuery, @NotNull CompletionHandler<RejectJoinMeetingRequestResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            rejectJoinMeetingRequest.validateParams(rejectJoinMeetingRequestQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }
}
