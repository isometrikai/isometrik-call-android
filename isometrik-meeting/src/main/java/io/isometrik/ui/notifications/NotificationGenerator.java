package io.isometrik.ui.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;

import io.isometrik.meeting.R;
import io.isometrik.ui.meetings.create.onetoone.enums.CallType;
import io.isometrik.ui.meetings.incoming.accept.AcceptCallFromNotificationActivity;
import io.isometrik.ui.notifications.NotificationBroadcastReceiver;
import io.isometrik.ui.utils.Constants;
import com.bumptech.glide.Glide;
import io.isometrik.ui.utils.NotificationUtil;
import io.isometrik.ui.utils.PlaceholderUtils;

public class NotificationGenerator {

    @SuppressLint("NotificationTrampoline")
    public static void generateIncomingCallNotification(String customType, String initiatorId, String meetingId, String initiatorName, String initiatorImageUrl, long meetingCreationTime, boolean hdMeeting, boolean audioOnly, Intent intent, Context context) {
        String message;

        if (customType != null) {
            if (customType.equals(CallType.AudioCall.getValue())) {
                message = context.getString(R.string.ism_incoming_call_type, context.getString(R.string.ism_audio));
            } else if (customType.equals(CallType.VideoCall.getValue())) {
                message = context.getString(R.string.ism_incoming_call_type, context.getString(R.string.ism_video));
            } else {
                message = context.getString(R.string.ism_incoming_call);
            }

        } else {
            message = context.getString(R.string.ism_incoming_call);
        }

        try {
            // Intent for accept action
            Intent acceptIntent = new Intent(context, AcceptCallFromNotificationActivity.class);
            acceptIntent.setAction(context.getString(R.string.ism_action_accept));
            acceptIntent.putExtra("meetingId", meetingId);
            acceptIntent.putExtra("meetingTitle", initiatorName);
            acceptIntent.putExtra("meetingImageUrl", initiatorImageUrl);
            acceptIntent.putExtra("meetingCreationTime", meetingCreationTime);
            acceptIntent.putExtra("hdMeeting", hdMeeting);
            acceptIntent.putExtra("audioOnly", audioOnly);

            PendingIntent acceptPendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                acceptPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                acceptPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            // Intent for reject action
            Intent rejectIntent = new Intent(context, NotificationBroadcastReceiver.class);
            rejectIntent.setAction(context.getString(R.string.ism_action_reject));
            rejectIntent.putExtra("meetingId", meetingId);
            acceptIntent.putExtra("meetingTitle", initiatorName);
            acceptIntent.putExtra("meetingImageUrl", initiatorImageUrl);
            rejectIntent.putExtra("meetingCreationTime", meetingCreationTime);
            rejectIntent.putExtra("hdMeeting", hdMeeting);
            rejectIntent.putExtra("audioOnly", audioOnly);

            PendingIntent rejectPendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                rejectPendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                rejectPendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            String channelId = context.getString(R.string.ism_meeting_channel_id);
            Notification notification;

            Bitmap bitmap = null;
            if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {

                int density = (int) context.getResources().getDisplayMetrics().density;

                FutureTarget<Bitmap> futureTarget = Glide.with(context).asBitmap().load(initiatorImageUrl).transform(new CircleCrop()).submit(density * PlaceholderUtils.BITMAP_WIDTH, density * PlaceholderUtils.BITMAP_HEIGHT);
                try {
                    bitmap = futureTarget.get();
                } catch (Exception ignore) {

                }
            }
// Set the vibration pattern
            long[] vibrationPattern = {0, 1000, 500, 1000}; // Vibrate for 1 second, wait for 0.5 second, vibrate for 1 second

            // Set the ringtone
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

            if (bitmap == null) {

                Person person = new Person.Builder().setKey(initiatorId).setName(initiatorName).build();
                notification = new NotificationCompat.Builder(context, channelId).setSmallIcon(R.drawable.ism_notification_small_icon).setContentTitle(initiatorName).setContentText(message).setPriority(NotificationCompat.PRIORITY_MAX).setCategory(NotificationCompat.CATEGORY_CALL).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setContentIntent(pendingIntent).setAutoCancel(true).setTimeoutAfter(Constants.RINGING_DURATION_MILLISECONDS).setOngoing(true).addPerson(person).addAction(0, context.getString(R.string.ism_accept), acceptPendingIntent).addAction(0, context.getString(R.string.ism_reject), rejectPendingIntent).setFullScreenIntent(pendingIntent, true).setVibrate(vibrationPattern).setSound(ringtoneUri).build();

            } else {
                Person person = new Person.Builder().setKey(initiatorId).setName(initiatorName).setIcon(IconCompat.createWithBitmap(bitmap)).build();
                notification = new NotificationCompat.Builder(context, channelId).setSmallIcon(R.drawable.ism_notification_small_icon).setLargeIcon(bitmap).setContentTitle(initiatorName).setContentText(message).setPriority(NotificationCompat.PRIORITY_MAX).setCategory(NotificationCompat.CATEGORY_CALL).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setContentIntent(pendingIntent).setAutoCancel(true).setTimeoutAfter(Constants.RINGING_DURATION_MILLISECONDS).setOngoing(true).addPerson(person).addAction(0, context.getString(R.string.ism_accept), acceptPendingIntent).addAction(0, context.getString(R.string.ism_reject), rejectPendingIntent).setFullScreenIntent(pendingIntent, true).setVibrate(vibrationPattern).setSound(ringtoneUri).build();

            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, context.getString(R.string.ism_meeting_channel_name), NotificationManager.IMPORTANCE_HIGH);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);

            }

            notificationManager.notify(meetingId, NotificationUtil.getNotificationId(meetingId), notification);

        } catch (Exception r) {
            r.printStackTrace();
        }
    }
}
