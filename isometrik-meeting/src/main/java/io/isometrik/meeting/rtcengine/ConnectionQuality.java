package io.isometrik.meeting.rtcengine;

public enum ConnectionQuality {
  EXCELLENT,
  GOOD,
  POOR,
  UNKNOWN;

  ConnectionQuality() {
  }

  public static ConnectionQuality fromInt(int code) {
    return values()[code];
  }
}
