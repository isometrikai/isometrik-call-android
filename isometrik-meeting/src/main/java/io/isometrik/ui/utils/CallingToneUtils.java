package io.isometrik.ui.utils;

import android.content.Context;
import android.media.MediaPlayer;

import io.isometrik.meeting.R;

public class CallingToneUtils {
    private static MediaPlayer mediaPlayer;

    public static void playCallingTone(Context context) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.calling);
            }

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        } catch (Exception ignore) {
        }
    }

    public static void cleanup() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception ignore) {
        }
    }


}
