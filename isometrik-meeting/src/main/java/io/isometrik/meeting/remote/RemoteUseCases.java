package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.managers.MediaTransferManager;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.response.error.BaseResponse;

/**
 * The remote use case class containing methods for accessing use cases containing api calls for
 * operations-
 */
public class RemoteUseCases {

    private final UploadUseCases uploadUseCases;
    private final UserUseCases userUseCases;
    private final MeetingUseCases meetingUseCases;
    private final PublishUseCases publishUseCases;
    private final ActionUseCases actionUseCases;
    private final MessageUseCases messageUseCases;
    private final MemberUseCases memberUseCases;

    private final RecordingUseCases recordingUseCases;

    /**
     * Instantiates a new Remote use cases.
     *
     * @param imConfiguration      the im configuration
     * @param retrofitManager      the retrofit manager
     * @param baseResponse         the base response
     * @param gson                 the gson
     * @param isometrik            the isometrik
     * @param mediaTransferManager the media transfer manager
     */
    public RemoteUseCases(IMConfiguration imConfiguration, RetrofitManager retrofitManager, BaseResponse baseResponse, Gson gson, Isometrik isometrik, MediaTransferManager mediaTransferManager) {

        uploadUseCases = new UploadUseCases(mediaTransferManager, baseResponse);
        userUseCases = new UserUseCases(imConfiguration, retrofitManager, baseResponse, gson);
        meetingUseCases = new MeetingUseCases(imConfiguration, retrofitManager, baseResponse, gson);
        publishUseCases = new PublishUseCases(imConfiguration, retrofitManager, baseResponse, gson);
        actionUseCases = new ActionUseCases(imConfiguration, retrofitManager, baseResponse, gson);
        messageUseCases = new MessageUseCases(imConfiguration, retrofitManager, baseResponse, gson);
        memberUseCases = new MemberUseCases(imConfiguration, retrofitManager, baseResponse, gson);
        recordingUseCases = new RecordingUseCases(imConfiguration, retrofitManager, baseResponse, gson);
    }

    /**
     * Gets upload use cases.
     *
     * @return the upload use cases
     */
    public UploadUseCases getUploadUseCases() {
        return uploadUseCases;
    }

    /**
     * Gets user use cases.
     *
     * @return the user use cases
     */
    public UserUseCases getUserUseCases() {
        return userUseCases;
    }

    public MeetingUseCases getMeetingUseCases() {
        return meetingUseCases;
    }

    public PublishUseCases getPublishUseCases() {
        return publishUseCases;
    }

    public ActionUseCases getActionUseCases() {
        return actionUseCases;
    }

    public MessageUseCases getMessageUseCases() {
        return messageUseCases;
    }

    public MemberUseCases getMemberUseCases() {
        return memberUseCases;
    }

    public RecordingUseCases getRecordingUseCases() {
        return recordingUseCases;
    }
}
