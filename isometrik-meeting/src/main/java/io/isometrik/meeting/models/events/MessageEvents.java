package io.isometrik.meeting.models.events;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.message.PublishMessageEvent;

/**
 * The helper class to parse and announce message events on respective registered realtime event
 * listeners for-
 * SendMessageEvent events.
 */
public class MessageEvents {

    /**
     * Handle message event.
     *
     * @param jsonObject        the json object
     * @param isometrikInstance the isometrik instance
     * @throws JSONException the json exception
     */
    public void handleMessageEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
            throws JSONException {

        String action = "";
        if (jsonObject.has("action")) action = jsonObject.getString("action");

        switch (action) {
            case "messagePublished":

                isometrikInstance.getRealtimeEventsListenerManager().getMessageListenerManager()
                        .announce(isometrikInstance.getGson()
                                .fromJson(jsonObject.toString(), PublishMessageEvent.class));
                break;
        }
    }
}
