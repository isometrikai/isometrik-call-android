package io.isometrik.ui.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.builder.action.RejectJoinMeetingRequestQuery;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.meeting.R;
import io.isometrik.ui.utils.NotificationUtil;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {

            String meetingId = intent.getStringExtra("meetingId");

            if (meetingId != null) {
                if (intent.getAction().equals(context.getString(R.string.ism_action_reject))) {
                    isometrik.getRemoteUseCases().getActionUseCases().rejectJoinMeetingRequest(new RejectJoinMeetingRequestQuery.Builder().setMeetingId(intent.getStringExtra("meetingId")).setUserToken(IsometrikUiSdk.getInstance().getUserSession().getUserToken()).build(), (var1, var2) -> {

                    });
                }
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.cancel(meetingId, NotificationUtil.getNotificationId(meetingId));
            }
        }
    }
}
