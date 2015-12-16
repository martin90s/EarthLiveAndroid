package com.yale.earthlive;

import android.util.DisplayMetrics;

/**
 * Created by niejunhong on 15/12/16.
 */
public class Constants {
  public static final String ROOT_DIR = "earthlivewallpaper";
  public static final String API_URL = "http://himawari8.nict.go.jp/img/D531106/latest.json";
  public static final String IMAGE_PREFIX =
      "http://himawari8-dl.nict.go.jp/himawari8/img/D531106/1d/550/";
  public static final String ACTION_WALLPAPER_CHANGE = "action_wallpaper_changed";

  private static int screen_width;
  private static int screen_height;

  public static int getScreenWidth() {
    if (screen_width == 0) {
      initScreenConfig();
    }
    return screen_width;
  }

  public static int getScreenHeight() {
    if (screen_height == 0) {
      initScreenConfig();
    }
    return screen_height;
  }

  private static void initScreenConfig() {
    DisplayMetrics temp = EarthLiveApplication.getContext().getResources().getDisplayMetrics();
    if (temp.heightPixels < temp.widthPixels) {
      screen_width = temp.heightPixels;
      screen_height = temp.widthPixels;
    } else {
      screen_width = temp.widthPixels;
      screen_height = temp.heightPixels;
    }

  }
}
