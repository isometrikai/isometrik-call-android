package io.isometrik.ui.meetings.meeting.details;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.ui.utils.BasePresenter;


public interface MeetingDetailsContract {

    interface Presenter extends BasePresenter<View> {

        void fetchMembers(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag);

        void fetchMembersOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void initialize(String meetingId, boolean isUserAnAdmin);

        void kickOutMembers(String meetingId, List<String> memberIds);

        /**
         * Make admin.
         *
         * @param meetingId the meeting id
         * @param memberId  the member id
         */
        void makeAdmin(String meetingId, String memberId);

        /**
         * Revoke admin permissions.
         *
         * @param meetingId the meeting id
         * @param memberId  the member id
         */
        void revokeAdminPermissions(String meetingId, String memberId);
    }


    interface View {

        void onMembersReceived(ArrayList<MembersModel> members, boolean refreshRequest, int membersCount);

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         *                     operation
         */
        void onError(String errorMessage);

        /**
         * On member removed successfully.
         *
         * @param memberId the member id
         * @param membersCount the members count
         */
        void onMemberRemovedSuccessfully(String memberId, int membersCount);

        /**
         * On member admin permissions updated.
         *
         * @param memberId the member id
         * @param admin    the admin
         */
        void onMemberAdminPermissionsUpdated(String memberId, boolean admin);

    }
}