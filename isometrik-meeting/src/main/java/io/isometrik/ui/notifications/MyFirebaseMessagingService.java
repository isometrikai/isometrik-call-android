package io.isometrik.ui.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;
import java.util.Map;

import io.isometrik.meeting.builder.meetings.FetchMeetingsQuery;
import io.isometrik.ui.IsometrikCallSdk;
import io.isometrik.meeting.R;
import io.isometrik.ui.meetings.list.MeetingsActivity;
import com.bumptech.glide.Glide;
import io.isometrik.ui.utils.MeetingsNotifiedToUsers;
import io.isometrik.ui.utils.NotificationUtil;
import io.isometrik.ui.utils.PlaceholderUtils;

/**
 * The service to receive the firebase message, parse the content and generate
 * notification for realtime message.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        Log.d("log1", "1" + data);

        if (data.get("action") != null) {
            String userId = IsometrikCallSdk.getInstance().getUserSession().getUserId();
            if (userId != null) {

                switch (data.get("action")) {
                    case "meetingCreated": {
                        //Meeting Created
                        String initiatorId = data.get("createdBy");

                        if (!userId.equals(initiatorId)) {

                            String meetingId = data.get("meetingId");
                            String initiatorName = data.get("initiatorName");
                            String initiatorImageUrl = data.get("initiatorImageUrl");
                            String customType = data.get("customType");
                            boolean audioOnly = Boolean.parseBoolean(data.get("audioOnly"));
                            boolean hdMeeting = Boolean.parseBoolean(data.get("hdMeeting"));
                            long meetingCreationTime = Long.parseLong(data.get("creationTime"));
                            Intent intent = MeetingsNotifiedToUsers.getInstance().addMeetingNotified(this, meetingId, audioOnly, hdMeeting, initiatorName, initiatorImageUrl, meetingCreationTime, true);
                            if (intent != null) {
                                IsometrikCallSdk.getInstance().getIsometrik().getRemoteUseCases().getMeetingUseCases().fetchMeetings(new FetchMeetingsQuery.Builder().setMeetingIds(Collections.singletonList(meetingId)).setUserToken(IsometrikCallSdk.getInstance().getUserSession().getUserToken()).build(), (var1, var2) -> {
                                    if (var1 != null) {
                                        NotificationGenerator.generateIncomingCallNotification(customType, initiatorId, meetingId, initiatorName, initiatorImageUrl, meetingCreationTime, hdMeeting, audioOnly, intent, this);

                                    }
                                });
                            }

                        }
                        break;
                    }
                    case "meetingEndedDueToNoUserPublishing":
                    case "meetingEndedDueToRejectionByAll":
                    case "meetingEndedByHost": {

                        String meetingId = data.get("meetingId");
//                                    NotificationManager notificationManager =
//                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.cancel(meetingId, NotificationUtil.getNotificationId(meetingId));

                        JSONArray members;
                        try {
                            members = new JSONArray(data.get("missedByMembers"));

                            int size = members.length();

                            for (int i = 0; i < size; i++) {
                                if (members.get(i).equals(userId)) {

                                    String initiatorName = data.get("initiatorName");
                                    String initiatorImageUrl = data.get("initiatorImageUrl");
                                    String initiatorId = data.get("initiatorId");

                                    Bitmap bitmap = null;
                                    if (PlaceholderUtils.isValidImageUrl(initiatorImageUrl)) {

                                        int density = (int) getResources().getDisplayMetrics().density;

                                        FutureTarget<Bitmap> futureTarget = Glide.with(this).asBitmap().load(initiatorImageUrl).transform(new CircleCrop()).submit(density * PlaceholderUtils.BITMAP_WIDTH, density * PlaceholderUtils.BITMAP_HEIGHT);
                                        try {
                                            bitmap = futureTarget.get();
                                        } catch (Exception ignore) {

                                        }
                                    }

                                    Notification notification;
                                    String channelId = getString(R.string.ism_meeting_channel_id);
                                    PendingIntent pendingIntent;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), new Intent(this, MeetingsActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                                    } else {
                                        pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), new Intent(this, MeetingsActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                                    }

                                    if (bitmap == null) {
                                        Person person = new Person.Builder().setKey(initiatorId).setName(initiatorName).build();
                                        notification = new NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.ism_notification_small_icon).setContentTitle(initiatorName).setContentText(getString(R.string.ism_missed_call)).setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_MISSED_CALL).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setAutoCancel(true)
                                                .addPerson(person)
                                                .setContentIntent(pendingIntent).build();

                                    } else {
                                        Person person = new Person.Builder().setKey(initiatorId).setName(initiatorName).setIcon(IconCompat.createWithBitmap(bitmap)).build();
                                        notification = new NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.ism_notification_small_icon).setLargeIcon(bitmap).setContentTitle(initiatorName).setContentText(getString(R.string.ism_missed_call)).setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_MISSED_CALL).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setAutoCancel(true)
                                                .addPerson(person)
                                                .setContentIntent(pendingIntent).build();

                                    }

                                    // Since android Oreo notification channel is needed.
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.ism_meeting_channel_name), NotificationManager.IMPORTANCE_HIGH);
                                        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                                        notificationManager.createNotificationChannel(channel);
                                    }

                                    notificationManager.notify(meetingId, NotificationUtil.getNotificationId(meetingId), notification);
                                    break;
                                }
                            }
                        } catch (JSONException ignore) {
                        }
                    }

                }
            }
        }

    }


    @Override
    public void onNewToken(@NonNull @NotNull String token) {
        super.onNewToken(token);
        //TODO Nothing
    }


}