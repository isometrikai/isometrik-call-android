package io.isometrik.ui.meetings.meeting.add;

import java.util.ArrayList;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.member.AddMembersQuery;
import io.isometrik.meeting.builder.member.FetchEligibleMembersQuery;
import io.isometrik.meeting.response.member.FetchEligibleMembersResult;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.utils.Constants;

/**
 * The presenter to fetch list of users that can be added as participants to a meeting with
 * paging, search and pull to refresh option.Api call to add participant to a meeting.
 */
public class AddParticipantsPresenter implements AddParticipantsContract.Presenter {

    AddParticipantsPresenter() {
    }

    @Override
    public void initialize(String meetingId) {
        this.meetingId = meetingId;

    }

    @Override
    public void attachView(AddParticipantsContract.View addParticipantsView) {
        this.addParticipantsView = addParticipantsView;
    }

    @Override
    public void detachView() {
        this.addParticipantsView = null;
    }

    private String meetingId;

    private AddParticipantsContract.View addParticipantsView;
    private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

    private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();

    private int offset;
    private boolean isLastPage;
    private boolean isLoading;
    private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

    private boolean isSearchRequest;
    private String searchTag;

    @Override
    public void addMembers(ArrayList<ParticipantsModel> participantsModels) {
        ArrayList<String> memberIds = new ArrayList<>();
        int size = participantsModels.size();
        for (int i = 0; i < size; i++) {

            memberIds.add(participantsModels.get(i).getUserId());
        }

        isometrik.getRemoteUseCases().getMemberUseCases().addMembers(new AddMembersQuery.Builder().setMeetingId(meetingId).setUserToken(userToken).setMembers(memberIds).build(), (var1, var2) -> {
            if (var1 != null) {
                if (addParticipantsView != null) {
                    addParticipantsView.onMembersAddedSuccessfully();
                }
            } else {
                if (addParticipantsView != null) {
                    addParticipantsView.onError(var2.getErrorMessage());
                }
            }
        });
    }

    @Override
    public void fetchMembersToAddToMeeting(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag) {
        isLoading = true;

        if (refreshRequest) {
            this.offset = 0;
            isLastPage = false;
        }
        this.isSearchRequest = isSearchRequest;
        this.searchTag = isSearchRequest ? searchTag : null;

        FetchEligibleMembersQuery.Builder fetchEligibleMembersQuery = new FetchEligibleMembersQuery.Builder().setLimit(PAGE_SIZE).setSkip(offset).setMeetingId(meetingId).setUserToken(userToken);
        if (isSearchRequest && searchTag != null) {
            fetchEligibleMembersQuery.setSearchTag(searchTag);
        }

        isometrik.getRemoteUseCases().getMemberUseCases().fetchEligibleMembers(fetchEligibleMembersQuery.build(), (var1, var2) -> {

            isLoading = false;

            if (var1 != null) {

                ArrayList<ParticipantsModel> participantsModels = new ArrayList<>();
                ArrayList<FetchEligibleMembersResult.MeetingEligibleMember> users = var1.getMeetingEligibleMembers();
                int size = users.size();

                for (int i = 0; i < size; i++) {

                    participantsModels.add(new ParticipantsModel(users.get(i)));
                }
                if (size < PAGE_SIZE) {

                    isLastPage = true;
                }
                if (addParticipantsView != null) {
                    addParticipantsView.onMembersToAddToMeetingFetched(participantsModels, refreshRequest, isSearchRequest);
                }
            } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

                    if (refreshRequest) {
                        //No participants to add found
                        if (addParticipantsView != null) {
                            addParticipantsView.onMembersToAddToMeetingFetched(new ArrayList<>(), true, isSearchRequest);
                        }
                    } else {
                        isLastPage = true;
                    }
                } else {
                    if (addParticipantsView != null) {
                        addParticipantsView.onError(var2.getErrorMessage());
                    }
                }
            }
        });
    }

    @Override
    public void requestMembersToAddToMeetingOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

        if (!isLoading && !isLastPage) {

            if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount) && firstVisibleItemPosition >= 0 && (totalItemCount) >= PAGE_SIZE) {

                offset++;
                fetchMembersToAddToMeeting(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
            }
        }
    }
}
