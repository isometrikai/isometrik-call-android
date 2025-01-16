package io.isometrik.ui.meetings.list;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.meeting.events.meeting.CreateMeetingEvent;
import io.isometrik.meeting.events.meeting.utils.MeetingMembers;
import io.isometrik.meeting.response.meeting.utils.Meeting;
import io.isometrik.meeting.response.meeting.utils.Member;
import io.isometrik.ui.IsometrikCallSdk;
import io.isometrik.ui.utils.TimeUtil;

/**
 * The helper class for inflating items in the meetings list.
 */
public class MeetingsModel {

    private final String meetingDescription;

    private final String duration;

    private final boolean selfHosted;

    private final String initiatorName;

    private final String meetingId;

    private final int membersCount;

    private final String opponentName;
    private final String opponentImageUrl;

    private final long meetingCreationTime;

    private final boolean audioOnly, hdMeeting;

    MeetingsModel(Meeting meeting) {
        meetingDescription = meeting.getMeetingDescription();
        selfHosted = meeting.isSelfHosted();
        initiatorName = meeting.getInitiatorName();
        meetingId = meeting.getMeetingId();
        duration = TimeUtil.getDurationString(TimeUtil.getDuration(meeting.getCreationTime()));
        membersCount = meeting.getMembersCount();
        audioOnly = meeting.isAudioOnly();
        hdMeeting = meeting.isHdMeeting();
        ArrayList<Member> members = meeting.getMembers();
        String userId = IsometrikCallSdk.getInstance().getUserSession().getUserId();
        if (members.get(0).getUserId().equals(userId)) {
            opponentName = members.get(1).getUserName();
            opponentImageUrl = members.get(1).getUserProfileImageUrl();
        } else {
            opponentName = members.get(0).getUserName();
            opponentImageUrl = members.get(0).getUserProfileImageUrl();
        }
        meetingCreationTime = meeting.getCreationTime();
    }

    MeetingsModel(CreateMeetingEvent createMeetingEvent) {
        meetingDescription = createMeetingEvent.getMeetingDescription();
        selfHosted = createMeetingEvent.isSelfHosted();
        initiatorName = createMeetingEvent.getInitiatorName();
        meetingId = createMeetingEvent.getMeetingId();
        duration = TimeUtil.getDurationString(TimeUtil.getDuration(createMeetingEvent.getCreationTime()));
        audioOnly = createMeetingEvent.isAudioOnly();
        hdMeeting = createMeetingEvent.isHdMeeting();

        List<MeetingMembers> members = createMeetingEvent.getMembers();
        String userId = IsometrikCallSdk.getInstance().getUserSession().getUserId();
        if (members.get(0).getMemberId().equals(userId)) {
            opponentName = members.get(1).getMemberName();
            opponentImageUrl = members.get(1).getMemberProfileImageUrl();
        } else {
            opponentName = members.get(0).getMemberName();
            opponentImageUrl = members.get(0).getMemberProfileImageUrl();
        }

        membersCount = members.size();
        meetingCreationTime = createMeetingEvent.getCreationTime();
    }


    public String getMeetingDescription() {
        return meetingDescription;
    }

    public String getDuration() {
        return duration;
    }

    public boolean isSelfHosted() {
        return selfHosted;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getOpponentImageUrl() {
        return opponentImageUrl;
    }

    public long getMeetingCreationTime() {
        return meetingCreationTime;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    public boolean isHdMeeting() {
        return hdMeeting;
    }
}
