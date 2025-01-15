package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.user.AuthenticateUserQuery;
import io.isometrik.meeting.builder.user.CreateUserQuery;
import io.isometrik.meeting.builder.user.DeleteUserQuery;
import io.isometrik.meeting.builder.user.FetchCreateUserPresignedUrlQuery;
import io.isometrik.meeting.builder.user.FetchUpdateUserPresignedUrlQuery;
import io.isometrik.meeting.builder.user.FetchUserDetailsQuery;
import io.isometrik.meeting.builder.user.FetchUsersQuery;
import io.isometrik.meeting.builder.user.UpdateUserQuery;
import io.isometrik.meeting.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.user.AuthenticateUser;
import io.isometrik.meeting.models.user.CreateUser;
import io.isometrik.meeting.models.user.DeleteUser;
import io.isometrik.meeting.models.user.FetchCreateUserPresignedUrl;
import io.isometrik.meeting.models.user.FetchUpdateUserPresignedUrl;
import io.isometrik.meeting.models.user.FetchUserDetails;
import io.isometrik.meeting.models.user.FetchUsers;
import io.isometrik.meeting.models.user.UpdateUser;
import io.isometrik.meeting.models.user.block.FetchNonBlockedUsers;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.user.AuthenticateUserResult;
import io.isometrik.meeting.response.user.CreateUserResult;
import io.isometrik.meeting.response.user.DeleteUserResult;
import io.isometrik.meeting.response.user.FetchCreateUserPresignedUrlResult;
import io.isometrik.meeting.response.user.FetchUpdateUserPresignedUrlResult;
import io.isometrik.meeting.response.user.FetchUserDetailsResult;
import io.isometrik.meeting.response.user.FetchUsersResult;
import io.isometrik.meeting.response.user.UpdateUserResult;
import io.isometrik.meeting.response.user.block.FetchNonBlockedUsersResult;

/**
 * The remote use case class containing methods for api calls for user operations- BlockUser,
 * FetchBlockedUsers, FetchBlockedUsersCount, FetchNonBlockedUsers,
 * UnblockUser, AuthenticateUser, CreateUser, DeleteUser, FetchUserDetails,
 * FetchUsers, UpdateUser, FetchCreateUserPresignedUrl and FetchUpdateUserPresignedUrl.
 */
public class UserUseCases {

    private final AuthenticateUser authenticateUser;
    private final CreateUser createUser;
    private final DeleteUser deleteUser;
    private final FetchUserDetails fetchUserDetails;
    private final FetchUsers fetchUsers;
    private final UpdateUser updateUser;
    private final FetchCreateUserPresignedUrl fetchCreateUserPresignedUrl;
    private final FetchUpdateUserPresignedUrl fetchUpdateUserPresignedUrl;

    private final FetchNonBlockedUsers fetchNonBlockedUsers;

    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    /**
     * Instantiates a new User use cases.
     *
     * @param configuration   the configuration
     * @param retrofitManager the retrofit manager
     * @param baseResponse    the base response
     * @param gson            the gson
     */
    public UserUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
                        BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        authenticateUser = new AuthenticateUser();
        createUser = new CreateUser();
        deleteUser = new DeleteUser();
        fetchUserDetails = new FetchUserDetails();
        fetchUsers = new FetchUsers();
        updateUser = new UpdateUser();
        fetchCreateUserPresignedUrl = new FetchCreateUserPresignedUrl();
        fetchUpdateUserPresignedUrl = new FetchUpdateUserPresignedUrl();
        fetchNonBlockedUsers = new FetchNonBlockedUsers();
    }

    /**
     * Authenticate user.
     *
     * @param authenticateUserQuery the authenticate user query
     * @param completionHandler     the completion handler
     */
    public void authenticateUser(@NotNull AuthenticateUserQuery authenticateUserQuery,
                                 @NotNull CompletionHandler<AuthenticateUserResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
        if (isometrikError == null) {
            authenticateUser.validateParams(authenticateUserQuery, completionHandler, retrofitManager,
                    configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Create user.
     *
     * @param createUserQuery   the create user query
     * @param completionHandler the completion handler
     */
    public void createUser(@NotNull CreateUserQuery createUserQuery,
                           @NotNull CompletionHandler<CreateUserResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
        if (isometrikError == null) {
            createUser.validateParams(createUserQuery, completionHandler, retrofitManager, configuration,
                    baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Delete user.
     *
     * @param deleteUserQuery   the delete user query
     * @param completionHandler the completion handler
     */
    public void deleteUser(@NotNull DeleteUserQuery deleteUserQuery,
                           @NotNull CompletionHandler<DeleteUserResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            deleteUser.validateParams(deleteUserQuery, completionHandler, retrofitManager, configuration,
                    baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch user details.
     *
     * @param fetchUserDetailsQuery the fetch user details query
     * @param completionHandler     the completion handler
     */
    public void fetchUserDetails(@NotNull FetchUserDetailsQuery fetchUserDetailsQuery,
                                 @NotNull CompletionHandler<FetchUserDetailsResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchUserDetails.validateParams(fetchUserDetailsQuery, completionHandler, retrofitManager,
                    configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch users.
     *
     * @param fetchUsersQuery   the fetch users query
     * @param completionHandler the completion handler
     */
    public void fetchUsers(@NotNull FetchUsersQuery fetchUsersQuery,
                           @NotNull CompletionHandler<FetchUsersResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(false);
        if (isometrikError == null) {
            fetchUsers.validateParams(fetchUsersQuery, completionHandler, retrofitManager, configuration,
                    baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Update user.
     *
     * @param updateUserQuery   the update user query
     * @param completionHandler the completion handler
     */
    public void updateUser(@NotNull UpdateUserQuery updateUserQuery,
                           @NotNull CompletionHandler<UpdateUserResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            updateUser.validateParams(updateUserQuery, completionHandler, retrofitManager, configuration,
                    baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch create user presigned url.
     *
     * @param fetchCreateUserPresignedUrlQuery the fetch create user presigned url query
     * @param completionHandler                the completion handler
     */
    public void fetchCreateUserPresignedUrl(
            @NotNull FetchCreateUserPresignedUrlQuery fetchCreateUserPresignedUrlQuery,
            @NotNull CompletionHandler<FetchCreateUserPresignedUrlResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchCreateUserPresignedUrl.validateParams(fetchCreateUserPresignedUrlQuery,
                    completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch update user presigned url.
     *
     * @param fetchUpdateUserPresignedUrlQuery the fetch update user presigned url query
     * @param completionHandler                the completion handler
     */
    public void fetchUpdateUserPresignedUrl(
            @NotNull FetchUpdateUserPresignedUrlQuery fetchUpdateUserPresignedUrlQuery,
            @NotNull CompletionHandler<FetchUpdateUserPresignedUrlResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchUpdateUserPresignedUrl.validateParams(fetchUpdateUserPresignedUrlQuery,
                    completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch non blocked users.
     *
     * @param fetchNonBlockedUsersQuery the fetch non blocked users query
     * @param completionHandler         the completion handler
     */
    public void fetchNonBlockedUsers(@NotNull FetchNonBlockedUsersQuery fetchNonBlockedUsersQuery,
                                     @NotNull CompletionHandler<FetchNonBlockedUsersResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchNonBlockedUsers.validateParams(fetchNonBlockedUsersQuery, completionHandler,
                    retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }
}
