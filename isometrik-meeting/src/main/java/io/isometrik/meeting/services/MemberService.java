package io.isometrik.meeting.services;

import java.util.Map;

import io.isometrik.meeting.response.member.AddAdminResult;
import io.isometrik.meeting.response.member.AddMembersResult;
import io.isometrik.meeting.response.member.FetchEligibleMembersResult;
import io.isometrik.meeting.response.member.FetchMemberDetailsResult;
import io.isometrik.meeting.response.member.FetchMembersResult;
import io.isometrik.meeting.response.member.RemoveAdminResult;
import io.isometrik.meeting.response.member.RemoveMembersResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

/**
 * The interface member service to add,remove member,fetch members in meeting and leave a
 * meeting by a member.
 */
public interface MemberService {
    /**
     * Add admin call.
     *
     * @param headers    the headers
     * @param bodyParams the body params
     * @return the call
     */
    @POST("/meetings/v1/admin")
    Call<AddAdminResult> addAdmin(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

    /**
     * Add members call.
     *
     * @param headers    the headers
     * @param bodyParams the body params
     * @return the call
     */
    @PUT("/meetings/v1/members")
    Call<AddMembersResult> addMembers(@HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);


    /**
     * Remove members call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @DELETE("/meetings/v1/members")
    Call<RemoveMembersResult> removeMembers(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

    /**
     * Remove admin call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @DELETE("/meetings/v1/admin")
    Call<RemoveAdminResult> removeAdmin(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);


    /**
     * Fetch meeting member details call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @GET("/meetings/v1/member/details")
    Call<FetchMemberDetailsResult> fetchMemberDetails(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);


    /**
     * Fetch members in a meeting call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @GET("/meetings/v1/members")
    Call<FetchMembersResult> fetchMembers(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);


    /**
     * Fetch members to add to meeting call.
     *
     * @param headers     the headers
     * @param queryParams the query params
     * @return the call
     */
    @GET("/meetings/v1/eligible/members")
    Call<FetchEligibleMembersResult> fetchEligibleMembers(@HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);
}
