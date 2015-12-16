package com.yale.earthlive.util;

import java.io.File;

import android.os.Environment;

import com.yale.earthlive.Constants;

/**
 * Created by niejunhong on 15/12/16.
 */
public class SystemHelper {

  /**
   * Get the external storage path of the device
   *
   * @return The external storage path of the device.
   */
  public static String getRootDirectory() {
    try {
      if (!Environment.getExternalStorageState().equals(
          Environment.MEDIA_MOUNTED)) {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    String rootDir = Environment.getExternalStorageDirectory()
        .getAbsolutePath() + "/" + Constants.ROOT_DIR + "/";
    File file = new File(rootDir);
    if (!file.exists()) {
      if (!file.mkdirs()) {
        return null;
      }
    }
    return rootDir;
  }
}
