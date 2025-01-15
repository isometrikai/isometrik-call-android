package io.isometrik.ui.meetings.create;

import java.util.ArrayList;
import java.util.Collections;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.meetings.CreateMeetingQuery;
import io.isometrik.meeting.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.meeting.enums.MeetingType;
import io.isometrik.meeting.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.meetings.create.onetoone.enums.CallType;
import io.isometrik.ui.utils.Constants;


public class CreateMeetingPresenter implements CreateMeetingContract.Presenter {

    /**
     * Instantiates a new users presenter.
     *
     * @param createMeetingView the users view
     */
    public CreateMeetingPresenter(CreateMeetingContract.View createMeetingView) {
        this.createMeetingView = createMeetingView;
    }

    private final CreateMeetingContract.View createMeetingView;
    private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
    private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
    private int offset;
    private boolean isLastPage;
    private boolean isLoading;
    private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
    private boolean isSearchRequest;
    private String searchTag;

    @Override
    public void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag) {
        isLoading = true;

        if (refreshRequest) {
            this.offset = 0;
            isLastPage = false;
        }
        this.isSearchRequest = isSearchRequest;
        this.searchTag = isSearchRequest ? searchTag : null;

        FetchNonBlockedUsersQuery.Builder fetchNonBlockedUsersQuery = new FetchNonBlockedUsersQuery.Builder().setLimit(PAGE_SIZE).setSkip(offset).setSort(Constants.SORT_ORDER_ASC).setUserToken(userToken);
        if (isSearchRequest && searchTag != null) {
            fetchNonBlockedUsersQuery.setSearchTag(searchTag);
        }
        isometrik.getRemoteUseCases().getUserUseCases().fetchNonBlockedUsers(fetchNonBlockedUsersQuery.build(), (var1, var2) -> {

            isLoading = false;

            if (var1 != null) {

                ArrayList<UsersModel> usersModels = new ArrayList<>();
                ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
                int size = users.size();

                for (int i = 0; i < size; i++) {

                    usersModels.add(new UsersModel(users.get(i)));
                }
                if (size < PAGE_SIZE) {

                    isLastPage = true;
                }

                createMeetingView.onNonBlockedUsersFetchedSuccessfully(usersModels, refreshRequest);

            } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

                    if (refreshRequest) {
                        //No users found
                        createMeetingView.onNonBlockedUsersFetchedSuccessfully(new ArrayList<>(), true);

                    } else {
                        isLastPage = true;
                    }
                } else {
                    createMeetingView.onError(var2.getErrorMessage());

                }
            }
        });
    }

    @Override
    public void fetchNonBlockedUsersOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {

            if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount) && firstVisibleItemPosition >= 0 && (totalItemCount) >= PAGE_SIZE) {

                offset++;
                fetchNonBlockedUsers(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
            }
        }
    }

    @Override
    public void createMeeting(String memberId, String meetingDescription, String opponentName, String opponentImageUrl, String customType) {
        isometrik.getRemoteUseCases().getMeetingUseCases().createMeeting(new CreateMeetingQuery.Builder().setHdMeeting(true).setAudioOnly(customType.equals(CallType.AudioCall.getValue())).setAutoTerminate(true).setMeetingDescription(meetingDescription).setMeetingType(MeetingType.NormalMeeting.getValue()).setUserToken(userToken).setSelfHosted(true).setEnableRecording(false).setDeviceId(IsometrikUiSdk.getInstance().getUserSession().getDeviceId()).setMembers(Collections.singletonList(memberId)).setPushNotifications(true).setSearchableTags(Collections.singletonList(meetingDescription)).setCustomType(customType).build(), (var1, var2) -> {
            if (var1 != null) {
                createMeetingView.onMeetingCreated(var1.getMeetingId(), var1.getRtcToken(), opponentName, opponentImageUrl, var1.getCreationTime(), customType);
            } else {
                createMeetingView.onError(var2.getErrorMessage());
            }
        });
    }
}
