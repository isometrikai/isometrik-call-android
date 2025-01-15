package io.isometrik.meeting.utils;

/**
 * The helper class to get size of the file in human readable format.
 */
public class FileUtils {
  /**
   * Gets size of file.
   *
   * @param size the size
   * @return the size of file
   */
  public static String getSizeOfFile(long size) {

    return org.apache.commons.io.FileUtils.byteCountToDisplaySize(size);
  }
}
