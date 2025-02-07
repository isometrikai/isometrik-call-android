package io.isometrik.meeting.remote;

import io.isometrik.meeting.builder.upload.CancelUserImageUploadQuery;
import io.isometrik.meeting.builder.upload.UploadUserImageQuery;
import io.isometrik.meeting.managers.MediaTransferManager;
import io.isometrik.meeting.models.upload.UploadUserImage;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.upload.CancelUserImageUploadResult;
import io.isometrik.meeting.response.upload.UploadUserImageResult;

import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for upload operations-
 * UploadMedia, UploadConversationImage and UploadUserImage.
 */
public class UploadUseCases {

    private final UploadUserImage uploadUserImage;

    private final MediaTransferManager mediaTransferManager;
    private final BaseResponse baseResponse;

    /**
     * Instantiates a new Upload use cases.
     *
     * @param mediaTransferManager the media transfer manager
     * @param baseResponse         the base response
     */
    public UploadUseCases(MediaTransferManager mediaTransferManager, BaseResponse baseResponse) {

        this.mediaTransferManager = mediaTransferManager;
        this.baseResponse = baseResponse;

        uploadUserImage = new UploadUserImage();
    }

    /**
     * Upload user image.
     *
     * @param uploadUserImageQuery the upload user image query
     * @param completionHandler    the completion handler
     */
    public void uploadUserImage(@NotNull UploadUserImageQuery uploadUserImageQuery,
                                @NotNull CompletionHandler<UploadUserImageResult> completionHandler) {

        uploadUserImage.uploadUserImage(uploadUserImageQuery, completionHandler, mediaTransferManager,
                baseResponse);
    }

    /**
     * Cancel user image upload.
     *
     * @param cancelUserImageUploadQuery the cancel user image upload query
     * @param completionHandler          the completion handler
     */
    public void cancelUserImageUpload(@NotNull CancelUserImageUploadQuery cancelUserImageUploadQuery,
                                      @NotNull CompletionHandler<CancelUserImageUploadResult> completionHandler) {

        uploadUserImage.cancelUserImageUpload(cancelUserImageUploadQuery, completionHandler);
    }
}
