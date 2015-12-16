package com.yale.earthlive.service;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;

/**
 * Created by niejunhong on 15/12/16.
 */
public class HimawariServiceClient {

  private EarthWallpaperConfig config;
  private boolean isRunning = false;

  private static HimawariServiceClient instance;

  public static HimawariServiceClient getInstance() {
    if (instance == null) {
      instance = new HimawariServiceClient();
    }
    return instance;
  }

  /**
   * called in Application's onCreate
   * 
   * @param context
   * @param config
   */
  public void start(Context context, EarthWallpaperConfig config) {
    checkUIThread();
    if (!isRunning) {
      this.config = config;
      this.isRunning = true;
      context.startService(new Intent(context, HimawariService.class));
    }
  }

  /**
   * @return current config
   */
  public EarthWallpaperConfig getConfig() {
    return config;
  }

  /**
   * @return had start wallpaper service
   */
  public boolean isRunning() {
    return isRunning;
  }

  private static void checkUIThread() {
    if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
      throw new AssertionError("WallpaperClient must run at the ui thread");
    }
  }

}
