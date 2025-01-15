package io.isometrik.ui.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.meeting.R;
import io.isometrik.ui.utils.Constants;

/**
 * The helper class to format epoch timestamp to human readable datetime.
 */
public class TimeUtil {

  /**
   * Format timestamp to only date string.
   *
   * @param epochTimestamp the epoch timestamp
   * @return the string
   */
  public static String formatTimestampToOnlyDate(long epochTimestamp) {

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      LocalDateTime localDateTimeFromEpochTimestamp =
          LocalDateTime.ofEpochSecond(epochTimestamp / 1000, 0,
              OffsetDateTime.now(ZoneId.systemDefault()).getOffset());

      LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
      int hourValue = localDateTimeFromEpochTimestamp.getHour();
      int minuteValue = localDateTimeFromEpochTimestamp.getMinute();

      if (localDateTimeFromEpochTimestamp.toString()
          .substring(0, 10)
          .equals(currentDateTime.toString().substring(0, 10))) {
        String hour;
        String minute;
        if (hourValue < 10) {
          hour = "0" + hourValue;
        } else {
          hour = String.valueOf(hourValue);
        }
        if (minuteValue < 10) {
          minute = "0" + minuteValue;
        } else {
          minute = String.valueOf(minuteValue);
        }
        return hour + ":" + minute;
      } else if (localDateTimeFromEpochTimestamp.getYear() == currentDateTime.getYear()) {
        if (Math.abs(
            localDateTimeFromEpochTimestamp.getDayOfYear() - currentDateTime.getDayOfYear()) == 1) {
          return IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_yesterday);
        }
      }
      return localDateTimeFromEpochTimestamp.toString().substring(0, 10);
    } else {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
      dateFormat.setTimeZone(TimeZone.getDefault());

      String currentDateTimeString = dateFormat.format(new Date());
      String localDateTimeFromEpochTimestampString = dateFormat.format(new Date(epochTimestamp));

      if ((currentDateTimeString.substring(0, 8)).equals(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) {

        //return "Today";
        return localDateTimeFromEpochTimestampString.substring(8, 10)
            + ":"
            + localDateTimeFromEpochTimestampString.substring(10, 12);
      } else if ((Long.parseLong((currentDateTimeString.substring(0, 8))) - Long.parseLong(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) == 1) {

        return IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_yesterday);
      } else {
        return localDateTimeFromEpochTimestampString.substring(0, 4)
            + "-"
            + localDateTimeFromEpochTimestampString.substring(4, 6)
            + "-"
            + localDateTimeFromEpochTimestampString.substring(6, 8);
      }
    }
  }

  /**
   * Format timestamp to both date and time string.
   *
   * @param epochTimestamp the epoch timestamp
   * @return the string
   */
  public static String formatTimestampToBothDateAndTime(long epochTimestamp) {

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      LocalDateTime localDateTimeFromEpochTimestamp =
          LocalDateTime.ofEpochSecond(epochTimestamp / 1000, 0,
              OffsetDateTime.now(ZoneId.systemDefault()).getOffset());

      LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
      int hourValue = localDateTimeFromEpochTimestamp.getHour();
      int minuteValue = localDateTimeFromEpochTimestamp.getMinute();
      String hour;
      String minute;
      if (hourValue < 10) {
        hour = "0" + hourValue;
      } else {
        hour = String.valueOf(hourValue);
      }
      if (minuteValue < 10) {
        minute = "0" + minuteValue;
      } else {
        minute = String.valueOf(minuteValue);
      }

      if (localDateTimeFromEpochTimestamp.toString()
          .substring(0, 10)
          .equals(currentDateTime.toString().substring(0, 10))) {
        return IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_today)
            + IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_at)
            + hour
            + ":"
            + minute;
      } else if (localDateTimeFromEpochTimestamp.getYear() == currentDateTime.getYear()) {
        if (Math.abs(
            localDateTimeFromEpochTimestamp.getDayOfYear() - currentDateTime.getDayOfYear()) == 1) {
          return IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_yesterday)
              + IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_at)
              + hour
              + ":"
              + minute;
        }
      }
      return localDateTimeFromEpochTimestamp.toString().substring(0, 10)
          + IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_at)
          + hour
          + ":"
          + minute;
    } else {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
      dateFormat.setTimeZone(TimeZone.getDefault());

      String currentDateTimeString = dateFormat.format(new Date());
      String localDateTimeFromEpochTimestampString = dateFormat.format(new Date(epochTimestamp));

      if ((currentDateTimeString.substring(0, 8)).equals(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) {

        return IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_today)
            + IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_at)
            + localDateTimeFromEpochTimestampString.substring(8, 10)
            + ":"
            + localDateTimeFromEpochTimestampString.substring(10, 12);
      } else if ((Long.parseLong((currentDateTimeString.substring(0, 8))) - Long.parseLong(
          (localDateTimeFromEpochTimestampString.substring(0, 8)))) == 1) {

        return IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_yesterday)
            + IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_at)
            + localDateTimeFromEpochTimestampString.substring(8, 10)
            + ":"
            + localDateTimeFromEpochTimestampString.substring(10, 12);
      } else {
        return

            localDateTimeFromEpochTimestampString.substring(0, 4)
                + "-"
                + localDateTimeFromEpochTimestampString.substring(4, 6)
                + "-"
                + localDateTimeFromEpochTimestampString.substring(6, 8)
                + IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_at)
                + localDateTimeFromEpochTimestampString.substring(8, 10)
                + ":"
                + localDateTimeFromEpochTimestampString.substring(10, 12);
      }
    }
  }
  /**
   * Gets duration string.
   *
   * @param seconds the seconds
   * @return the duration string
   */
  public static String getDurationString(long seconds) {

    if (seconds < 0
            || seconds
            > 2000000)//there is an codec problem and duration is not set correctly,so display meaningfull string
    {
      seconds = 0;
    }
    long hours = seconds / 3600;
    long minutes = (seconds % 3600) / 60;
    seconds = seconds % 60;

    if (hours == 0) {
      return twoDigitString(minutes) + " : " + twoDigitString(seconds);
    } else {
      return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(
              seconds);
    }
  }

  private static String twoDigitString(long number) {

    if (number == 0) {
      return "00";
    }

    if (number / 10 == 0) {
      return "0" + number;
    }

    return String.valueOf(number);
  }

  /**
   * Gets duration.
   *
   * @param startTime the start time
   * @return the duration
   */
  public static long getDuration(long startTime) {

    long currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

    long duration = currentTime - startTime - Constants.TIME_CORRECTION;
    duration = duration / 1000;

    duration = (duration < 0) ? 0 : duration;
    return duration;
  }
}
