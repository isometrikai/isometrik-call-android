package io.isometrik.ui.meetings.meeting.details;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.member.AddAdminQuery;
import io.isometrik.meeting.builder.member.FetchMembersQuery;
import io.isometrik.meeting.builder.member.RemoveAdminQuery;
import io.isometrik.meeting.builder.member.RemoveMembersQuery;
import io.isometrik.meeting.response.member.FetchMembersResult;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.utils.Constants;


public class MeetingDetailsPresenter implements MeetingDetailsContract.Presenter {


    MeetingDetailsPresenter() {
    }

    private MeetingDetailsContract.View meetingDetailsView;


    private boolean isLoading;
    private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
    private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
    private boolean isSearchRequest;
    private String searchTag, meetingId;
    private final int PAGE_SIZE = Constants.MEMBERS_PAGE_SIZE;
    private boolean isLastPage, isUserAnAdmin;
    private int offset;

    private final String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

    @Override
    public void attachView(MeetingDetailsContract.View meetingDetailsView) {
        this.meetingDetailsView = meetingDetailsView;
    }

    @Override
    public void detachView() {
        this.meetingDetailsView = null;
    }


    @Override
    public void initialize(String meetingId, boolean isUserAnAdmin) {
        this.meetingId = meetingId;
        this.isUserAnAdmin = isUserAnAdmin;
    }

    @Override
    public void fetchMembers(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag) {
        isLoading = true;

        if (refreshRequest) {
            this.offset = 0;
            isLastPage = false;
        }
        this.isSearchRequest = isSearchRequest;
        this.searchTag = isSearchRequest ? searchTag : null;

        FetchMembersQuery.Builder fetchMembersQuery = new FetchMembersQuery.Builder().setUserToken(userToken).setMeetingId(meetingId).setLimit(PAGE_SIZE).setSkip(offset);
        if (isSearchRequest && searchTag != null) {
            fetchMembersQuery.setSearchTag(searchTag);
        }
        isometrik.getRemoteUseCases().getMemberUseCases().fetchMembers(fetchMembersQuery.build(), (var1, var2) -> {

            if (var1 != null) {

                ArrayList<MembersModel> membersModels = new ArrayList<>();

                ArrayList<FetchMembersResult.MeetingMember> members = var1.getMeetingMembers();
                int size = members.size();
                if (size < PAGE_SIZE) {

                    isLastPage = true;
                }
                for (int i = 0; i < size; i++) {
                    FetchMembersResult.MeetingMember member = members.get(i);
                    membersModels.add(new MembersModel(member.getUserId(), member.getUserName(), member.getUserProfileImageUrl(), isUserAnAdmin, member.isOnline(), member.getUserId().equals(userId), member.isAdmin()));
                }

                if (meetingDetailsView != null) {
                    meetingDetailsView.onMembersReceived(membersModels, refreshRequest, var1.getMembersCount());
                }
            } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                    if (refreshRequest) {
                        //No members found
                        if (meetingDetailsView != null) {
                            meetingDetailsView.onMembersReceived(new ArrayList<>(), true, offset == 0 ? 0 : -1);
                        }
                    } else {
                        isLastPage = true;
                    }
                } else {

                    if (meetingDetailsView != null) {
                        meetingDetailsView.onError(var2.getErrorMessage());
                    }
                }
            }
            isLoading = false;
        });
    }


    @Override
    public void fetchMembersOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

        if (!isLoading && !isLastPage) {

            if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount) && firstVisibleItemPosition >= 0 && (totalItemCount) >= PAGE_SIZE) {

                offset++;
                fetchMembers(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
            }
        }
    }


    @Override
    public void kickOutMembers(String meetingId, List<String> memberIds) {
        isometrik.getRemoteUseCases().getMemberUseCases().removeMembers(new RemoveMembersQuery.Builder().setMeetingId(meetingId).setUserToken(userToken).setMembers(memberIds).build(), (var1, var2) -> {
            if (var1 != null) {
                meetingDetailsView.onMemberRemovedSuccessfully(memberIds.get(0), var1.getMembersCount());
            } else {
                meetingDetailsView.onError(var2.getErrorMessage());
            }
        });
    }

    @Override
    public void makeAdmin(String meetingId, String memberId) {
        isometrik.getRemoteUseCases().getMemberUseCases().addAdmin(new AddAdminQuery.Builder().setMeetingId(meetingId).setUserToken(userToken).setMemberId(memberId).build(), (var1, var2) -> {
            if (var1 != null) {
                meetingDetailsView.onMemberAdminPermissionsUpdated(memberId, true);
            } else {
                meetingDetailsView.onError(var2.getErrorMessage());
            }
        });
    }

    @Override
    public void revokeAdminPermissions(String setMeetingId, String memberId) {
        isometrik.getRemoteUseCases().getMemberUseCases().removeAdmin(new RemoveAdminQuery.Builder().setMeetingId(setMeetingId).setUserToken(userToken).setMemberId(memberId).build(), (var1, var2) -> {
            if (var1 != null) {
                meetingDetailsView.onMemberAdminPermissionsUpdated(memberId, false);
            } else {
                meetingDetailsView.onError(var2.getErrorMessage());
            }
        });
    }
}