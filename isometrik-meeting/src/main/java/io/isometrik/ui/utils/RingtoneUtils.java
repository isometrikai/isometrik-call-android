package io.isometrik.ui.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class RingtoneUtils {
    private static Ringtone ringtone;
    private static Vibrator vibrator;

    public static void playRingtoneAndVibrate(Context context) {
        try {

            if (ringtone == null) {
                ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            }
            if (!ringtone.isPlaying()) {
                // Play ringtone
                ringtone.play();
            }

            if (vibrator == null) {

                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }

            // Start vibration
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 1000, 500, 1000}, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // For older versions without VibrationEffect
                vibrator.vibrate(new long[]{0, 1000, 500, 1000}, -1);
            }

        } catch (Exception ignore) {
        }
    }

    public static void cleanup() {
        try {
            // Stop ringtone if it's playing
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.stop();
                ringtone = null;
            }
        } catch (Exception ignore) {
        }
        try {
            // Stop vibration
            if (vibrator != null) {
                vibrator.cancel();
                vibrator = null;
            }
        } catch (Exception ignore) {
        }
    }
}
