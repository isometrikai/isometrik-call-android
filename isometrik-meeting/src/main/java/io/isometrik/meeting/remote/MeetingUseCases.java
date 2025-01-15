package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.meetings.CreateMeetingQuery;
import io.isometrik.meeting.builder.meetings.EndMeetingQuery;
import io.isometrik.meeting.builder.meetings.FetchMeetingsQuery;
import io.isometrik.meeting.builder.meetings.JoinMeetingQuery;
import io.isometrik.meeting.builder.meetings.LeaveMeetingQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.meeting.CreateMeeting;
import io.isometrik.meeting.models.meeting.EndMeeting;
import io.isometrik.meeting.models.meeting.FetchMeetings;
import io.isometrik.meeting.models.meeting.JoinMeeting;
import io.isometrik.meeting.models.meeting.LeaveMeeting;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.meeting.CreateMeetingResult;
import io.isometrik.meeting.response.meeting.EndMeetingResult;
import io.isometrik.meeting.response.meeting.FetchMeetingsResult;
import io.isometrik.meeting.response.meeting.JoinMeetingResult;
import io.isometrik.meeting.response.meeting.LeaveMeetingResult;

public class MeetingUseCases {
    private final CreateMeeting createMeeting;
    private final EndMeeting endMeeting;
    private final FetchMeetings fetchMeetings;
    private final JoinMeeting joinMeeting;
    private final LeaveMeeting leaveMeeting;

    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    public MeetingUseCases(IMConfiguration configuration, RetrofitManager retrofitManager, BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        createMeeting = new CreateMeeting();
        endMeeting = new EndMeeting();
        fetchMeetings = new FetchMeetings();
        leaveMeeting = new LeaveMeeting();
        joinMeeting = new JoinMeeting();
    }


    public void createMeeting(@NotNull CreateMeetingQuery createMeetingQuery, @NotNull CompletionHandler<CreateMeetingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            createMeeting.validateParams(createMeetingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void endMeeting(@NotNull EndMeetingQuery endMeetingQuery, @NotNull CompletionHandler<EndMeetingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            endMeeting.validateParams(endMeetingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void fetchMeetings(@NotNull FetchMeetingsQuery fetchMeetingsQuery, @NotNull CompletionHandler<FetchMeetingsResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchMeetings.validateParams(fetchMeetingsQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void leaveMeeting(@NotNull LeaveMeetingQuery leaveMeetingQuery, @NotNull CompletionHandler<LeaveMeetingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            leaveMeeting.validateParams(leaveMeetingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void joinMeeting(@NotNull JoinMeetingQuery joinMeetingQuery, @NotNull CompletionHandler<JoinMeetingResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            joinMeeting.validateParams(joinMeetingQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }
}
