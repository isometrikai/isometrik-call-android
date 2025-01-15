package io.isometrik.ui.utils;

import android.content.Context;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

import io.isometrik.meeting.builder.message.SendMessageQuery;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.meeting.R;
import io.isometrik.ui.meetings.incoming.IncomingCallActivity;

public class MeetingsNotifiedToUsers {

    private static MeetingsNotifiedToUsers instance;
    private final Set<String> meetingsAlreadyNotified;

    private MeetingsNotifiedToUsers() {
        meetingsAlreadyNotified = new HashSet<>();
    }

    public static synchronized MeetingsNotifiedToUsers getInstance() {
        if (instance == null) {
            instance = new MeetingsNotifiedToUsers();
        }
        return instance;
    }

    public boolean checkIfMeetingAlreadyNotified(String meetingId) {
        return meetingsAlreadyNotified.contains(meetingId);
    }

    public Intent addMeetingNotified(Context context, String meetingId, boolean audioOnly, boolean hdMeeting, String opponentName, String opponentImageUrl, long meetingCreationTime, boolean fromNotification) {
        if (meetingsAlreadyNotified.contains(meetingId)) {
            return null;
            //Meeting already notified
        } else {

            try {

                IsometrikUiSdk.getInstance().getIsometrik().getExecutor().execute(() -> {

                    IsometrikUiSdk.getInstance().getIsometrik().getRemoteUseCases().getMessageUseCases().sendMessage(new SendMessageQuery.Builder().setMessageType(1).setDeviceId(IsometrikUiSdk.getInstance().getUserSession().getDeviceId()).setMeetingId(meetingId).setBody(context.getString(R.string.ism_call_ringing_event)).setUserToken(IsometrikUiSdk.getInstance().getUserSession().getUserToken()).build(), (var1, var2) -> {

                    });
                });
            } catch (Exception ignore) {

            }
            Intent intent = new Intent(context, IncomingCallActivity.class);
            intent.putExtra("audioOnly", audioOnly);
            intent.putExtra("hdMeeting", hdMeeting);
            intent.putExtra("meetingTitle", opponentName);
            intent.putExtra("meetingImageUrl", opponentImageUrl);
            intent.putExtra("meetingId", meetingId);
            intent.putExtra("meetingCreationTime", meetingCreationTime);
            intent.putExtra("fromNotification", fromNotification);

            meetingsAlreadyNotified.add(meetingId);
            return intent;
            //Meeting not notified already
        }

    }

}