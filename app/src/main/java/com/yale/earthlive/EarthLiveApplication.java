package com.yale.earthlive;

import android.app.Application;
import android.content.Context;

import com.yale.earthlive.network.NetworkManager;
import com.yale.earthlive.service.EarthWallpaperConfig;
import com.yale.earthlive.service.HimawariServiceClient;
import com.yale.earthlive.wallpaper.WallpaperManager;

/**
 * Created by niejunhong on 15/12/16.
 */
public class EarthLiveApplication extends Application {


  private static Context context;

  private static AppPref appPref;

  private static NetworkManager networkManager;

  private static WallpaperManager wallpaperManager;

  @Override
  public void onCreate() {
    super.onCreate();
    appPref = new AppPref(this);
    context = this;
    networkManager = new NetworkManager(this);
    wallpaperManager = new WallpaperManager(this);
    wallpaperManager.start();
    HimawariServiceClient.getInstance().start(this, new EarthWallpaperConfig.Builder().build());
  }

  public static Context getContext() {
    return context;
  }

  public static AppPref getAppPref() {
    return appPref;
  }

  public static NetworkManager getNetworkManager() {
    return networkManager;
  }

  public static WallpaperManager getWallpaperManager() {
    return wallpaperManager;
  }
}
