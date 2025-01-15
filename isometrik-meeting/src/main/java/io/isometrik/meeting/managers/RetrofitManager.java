package io.isometrik.meeting.managers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.enums.IMLogVerbosity;
import io.isometrik.meeting.services.ActionService;
import io.isometrik.meeting.services.MeetingService;
import io.isometrik.meeting.services.MemberService;
import io.isometrik.meeting.services.MessageService;
import io.isometrik.meeting.services.PublishService;
import io.isometrik.meeting.services.RecordingService;
import io.isometrik.meeting.services.UserService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The manager class for Retrofit and OkHttpClient client for Message, User, Conversation,
 * DeliveryStatus, MembershipControl and Reaction remote api calls.
 */
public class RetrofitManager {
    private final Isometrik isometrik;

    private final OkHttpClient transactionClientInstance;

    private final UserService userService;
    private final MeetingService meetingService;
    private final PublishService publishService;
    private final ActionService actionService;
    private final MessageService messageService;
    private final MemberService memberService;
    private final RecordingService recordingService;

    /**
     * Instantiates a new Retrofit manager.
     *
     * @param isometrikInstance the isometrik instance
     * @see io.isometrik.meeting.Isometrik
     */
    public RetrofitManager(Isometrik isometrikInstance) {
        this.isometrik = isometrikInstance;
        this.transactionClientInstance = createOkHttpClient(this.isometrik.getConfiguration().getRequestTimeout(), this.isometrik.getConfiguration().getConnectTimeout());
        Retrofit transactionInstance = createRetrofit(this.transactionClientInstance);

        this.userService = transactionInstance.create(UserService.class);
        this.meetingService = transactionInstance.create(MeetingService.class);
        this.publishService = transactionInstance.create(PublishService.class);
        this.actionService = transactionInstance.create(ActionService.class);
        this.messageService = transactionInstance.create(MessageService.class);
        this.memberService = transactionInstance.create(MemberService.class);
        this.recordingService = transactionInstance.create(RecordingService.class);

    }

    /**
     * Create OkHttpClient instance.
     *
     * @return OkHttpClient instance
     * @see okhttp3.OkHttpClient
     */
    private OkHttpClient createOkHttpClient(int requestTimeout, int connectTimeOut) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.retryOnConnectionFailure(false);
        httpClient.readTimeout(requestTimeout, TimeUnit.SECONDS);
        httpClient.connectTimeout(connectTimeOut, TimeUnit.SECONDS);

        if (isometrik.getConfiguration().getLogVerbosity() == IMLogVerbosity.BODY) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return httpClient.build();
    }

    /**
     * Create retrofit instance.
     *
     * @return retrofit instance
     * @see retrofit2.Retrofit
     */
    private Retrofit createRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().baseUrl(isometrik.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).client(client).build();
    }


    /**
     * Gets user service.
     *
     * @return the user service
     */
    public UserService getUserService() {
        return userService;
    }

    public MeetingService getMeetingService() {
        return meetingService;
    }

    public PublishService getPublishService() {
        return publishService;
    }

    public ActionService getActionService() {
        return actionService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public RecordingService getRecordingService() {
        return recordingService;
    }

    /**
     * Destroy.
     *
     * @param force whether to destroy forcibly
     */
    public void destroy(boolean force) {
        if (this.transactionClientInstance != null) {
            closeExecutor(this.transactionClientInstance, force);
        }
    }

    /**
     * Closes the OkHttpClient execute
     *
     * @param client OkHttpClient whose requests are to be canceled
     * @param force  whether to forcibly shutdown the OkHttpClient and evict all connection pool
     */
    private void closeExecutor(OkHttpClient client, boolean force) {
        try {
            new Thread(() -> {
                client.dispatcher().cancelAll();
                if (force) {

                    ExecutorService executorService = client.dispatcher().executorService();
                    executorService.shutdown();
                    client.connectionPool().evictAll();
                }
            }).start();
        } catch (Exception ignore) {
        }
    }
}

