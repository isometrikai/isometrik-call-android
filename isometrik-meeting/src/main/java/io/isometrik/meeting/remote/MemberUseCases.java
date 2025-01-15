package io.isometrik.meeting.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.IMConfiguration;
import io.isometrik.meeting.builder.member.AddAdminQuery;
import io.isometrik.meeting.builder.member.AddMembersQuery;
import io.isometrik.meeting.builder.member.FetchEligibleMembersQuery;
import io.isometrik.meeting.builder.member.FetchMemberDetailsQuery;
import io.isometrik.meeting.builder.member.FetchMembersQuery;
import io.isometrik.meeting.builder.member.RemoveAdminQuery;
import io.isometrik.meeting.builder.member.RemoveMembersQuery;
import io.isometrik.meeting.managers.RetrofitManager;
import io.isometrik.meeting.models.member.AddAdmin;
import io.isometrik.meeting.models.member.AddMembers;
import io.isometrik.meeting.models.member.FetchEligibleMembers;
import io.isometrik.meeting.models.member.FetchMemberDetails;
import io.isometrik.meeting.models.member.FetchMembers;
import io.isometrik.meeting.models.member.RemoveAdmin;
import io.isometrik.meeting.models.member.RemoveMembers;
import io.isometrik.meeting.response.CompletionHandler;
import io.isometrik.meeting.response.error.BaseResponse;
import io.isometrik.meeting.response.error.IsometrikError;
import io.isometrik.meeting.response.member.AddAdminResult;
import io.isometrik.meeting.response.member.AddMembersResult;
import io.isometrik.meeting.response.member.FetchEligibleMembersResult;
import io.isometrik.meeting.response.member.FetchMemberDetailsResult;
import io.isometrik.meeting.response.member.FetchMembersResult;
import io.isometrik.meeting.response.member.RemoveAdminResult;
import io.isometrik.meeting.response.member.RemoveMembersResult;

/**
 * The remote use case class containing methods for api calls for membership control operations-
 * AddAdmin, AddMembers, RemoveAdmin,RemoveMembers,
 * FetchMeetingMemberDetails, fetch meeting members and eligible members to add to meeting.
 */
public class MemberUseCases {

    private final AddAdmin addAdmin;
    private final AddMembers addMembers;
    private final RemoveAdmin removeAdmin;
    private final RemoveMembers removeMembers;
    private final FetchMemberDetails fetchMemberDetails;
    private final FetchMembers fetchMembers;
    private final FetchEligibleMembers fetchEligibleMembers;

    private final IMConfiguration configuration;
    private final RetrofitManager retrofitManager;
    private final BaseResponse baseResponse;
    private final Gson gson;

    /**
     * Instantiates a new Membership control use cases.
     *
     * @param configuration   the configuration
     * @param retrofitManager the retrofit manager
     * @param baseResponse    the base response
     * @param gson            the gson
     */
    public MemberUseCases(IMConfiguration configuration, RetrofitManager retrofitManager, BaseResponse baseResponse, Gson gson) {
        this.configuration = configuration;
        this.retrofitManager = retrofitManager;
        this.baseResponse = baseResponse;
        this.gson = gson;

        addAdmin = new AddAdmin();
        addMembers = new AddMembers();
        removeAdmin = new RemoveAdmin();
        removeMembers = new RemoveMembers();
        fetchMemberDetails = new FetchMemberDetails();
        fetchMembers = new FetchMembers();
        fetchEligibleMembers = new FetchEligibleMembers();
    }

    /**
     * Add admin.
     *
     * @param addAdminQuery     the add admin query
     * @param completionHandler the completion handler
     */
    public void addAdmin(@NotNull AddAdminQuery addAdminQuery, @NotNull CompletionHandler<AddAdminResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            addAdmin.validateParams(addAdminQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Add members.
     *
     * @param addMembersQuery   the add members query
     * @param completionHandler the completion handler
     */
    public void addMembers(@NotNull AddMembersQuery addMembersQuery, @NotNull CompletionHandler<AddMembersResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            addMembers.validateParams(addMembersQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Remove admin.
     *
     * @param removeAdminQuery  the remove admin query
     * @param completionHandler the completion handler
     */
    public void removeAdmin(@NotNull RemoveAdminQuery removeAdminQuery, @NotNull CompletionHandler<RemoveAdminResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            removeAdmin.validateParams(removeAdminQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Remove members.
     *
     * @param removeMembersQuery the remove members query
     * @param completionHandler  the completion handler
     */
    public void removeMembers(@NotNull RemoveMembersQuery removeMembersQuery, @NotNull CompletionHandler<RemoveMembersResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            removeMembers.validateParams(removeMembersQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch meeting member details.
     *
     * @param fetchMemberDetailsQuery the fetch member details query
     * @param completionHandler       the completion handler
     */
    public void fetchMemberDetails(@NotNull FetchMemberDetailsQuery fetchMemberDetailsQuery, @NotNull CompletionHandler<FetchMemberDetailsResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchMemberDetails.validateParams(fetchMemberDetailsQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }


    /**
     * Fetch meeting members.
     *
     * @param fetchMembersQuery the fetch members query
     * @param completionHandler the completion handler
     */
    public void fetchMembers(@NotNull FetchMembersQuery fetchMembersQuery, @NotNull CompletionHandler<FetchMembersResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchMembers.validateParams(fetchMembersQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

    /**
     * Fetch eligible meeting members.
     *
     * @param fetchEligibleMembersQuery the fetch eligible members query
     * @param completionHandler         the completion handler
     */
    public void fetchEligibleMembers(@NotNull FetchEligibleMembersQuery fetchEligibleMembersQuery, @NotNull CompletionHandler<FetchEligibleMembersResult> completionHandler) {

        IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
        if (isometrikError == null) {
            fetchEligibleMembers.validateParams(fetchEligibleMembersQuery, completionHandler, retrofitManager, configuration, baseResponse, gson);
        } else {
            completionHandler.onComplete(null, isometrikError);
        }
    }

}
