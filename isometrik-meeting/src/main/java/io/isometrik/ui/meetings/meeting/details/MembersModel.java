package io.isometrik.ui.meetings.meeting.details;

public class MembersModel {

    private final String memberId, memberName, memberProfileImageUrl;
    private final boolean isUserAnAdmin, isOnline, isSelf;
    private boolean isAdmin;

    public MembersModel(String memberId, String memberName, String memberProfileImageUrl, boolean isUserAnAdmin, boolean isOnline, boolean isSelf, boolean isAdmin) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberProfileImageUrl = memberProfileImageUrl;
        this.isUserAnAdmin = isUserAnAdmin;
        this.isOnline = isOnline;
        this.isSelf = isSelf;
        this.isAdmin = isAdmin;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberProfileImageUrl() {
        return memberProfileImageUrl;
    }

    public boolean isUserAnAdmin() {
        return isUserAnAdmin;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
