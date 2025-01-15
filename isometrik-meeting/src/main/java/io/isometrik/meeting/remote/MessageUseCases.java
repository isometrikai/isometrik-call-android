package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.message.FetchMessagesCountQuery;
import io.isometrik.meeting.builder.message.FetchMessagesQuery;
import io.isometrik.meeting.builder.message.SendMessageQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.message.FetchMessages;
import io.isometrik.meeting.models.message.FetchMessagesCount;
import io.isometrik.meeting.models.message.SendMessage;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.message.FetchMessagesCountResult;
import io.isometrik.meeting.response.message.FetchMessagesResult;
import io.isometrik.meeting.response.message.SendMessageResult;

public class MessageUseCases {

    private final SendMessage sendMessage;
    private final FetchMessages fetchMessages;
    private final FetchMessagesCount fetchMessagesCount;
    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    public MessageUseCases(IMConfiguration configuration, RetrofitManager retrofitManager, BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        sendMessage = new SendMessage();
        fetchMessages = new FetchMessages();
        fetchMessagesCount = new FetchMessagesCount();

    }

    public void sendMessage(@NotNull SendMessageQuery sendMessageQuery, @NotNull CompletionHandler<SendMessageResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            sendMessage.validateParams(sendMessageQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void fetchMessages(@NotNull FetchMessagesQuery fetchMessagesQuery, @NotNull CompletionHandler<FetchMessagesResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchMessages.validateParams(fetchMessagesQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    public void fetchMessagesCount(@NotNull FetchMessagesCountQuery fetchMessagesCountQuery, @NotNull CompletionHandler<FetchMessagesCountResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchMessagesCount.validateParams(fetchMessagesCountQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }
}
