package com.yale.earthlive.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.yale.earthlive.AppPref;
import com.yale.earthlive.Constants;
import com.yale.earthlive.EarthLiveApplication;
import com.yale.earthlive.util.BitmapUtil;

/**
 * Created by niejunhong on 15/12/16.
 */
public class WallpaperManager {
  private Context context;
  private Bitmap currentWallpaper;

  public WallpaperManager(Context context) {
    this.context = context;
  }


  public void start() {
    context.getApplicationContext().registerReceiver(systemWallpaperReceiver,
        new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
    loadWallpaper();
  }

  public void stop() {
    context.getApplicationContext().unregisterReceiver(systemWallpaperReceiver);
    currentWallpaper.recycle();
    currentWallpaper = null;
  }

  private final BroadcastReceiver systemWallpaperReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      loadWallpaper();
    }
  };

  private void loadWallpaper() {
    new AsyncTask<Void, Void, Bitmap>() {

      @Override
      protected Bitmap doInBackground(Void... params) {
        final String wallpaperPath =
            EarthLiveApplication.getAppPref().getString(AppPref.LAST_SAVED_IMAGE_URL, null);
        Bitmap result = null;
        if (!TextUtils.isEmpty(wallpaperPath)) {
          result = BitmapUtil.getBitmapFromSDCard(wallpaperPath, Constants.getScreenWidth(),
              Constants.getScreenHeight());
        }
        if (result == null) {
          result = BitmapUtil.getSystemWallpaper();
        }
        return result;
      }

      @Override
      protected void onPostExecute(Bitmap result) {
        if (result != null) {
          currentWallpaper = result;
          Intent intent = new Intent(Constants.ACTION_WALLPAPER_CHANGE);
          context.sendBroadcast(intent);
        }
      }
    }.execute();
  }

  public Bitmap getWallpaper() {
    return currentWallpaper;
  }

}
