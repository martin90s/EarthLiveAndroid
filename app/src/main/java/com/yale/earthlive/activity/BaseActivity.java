package com.yale.earthlive.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.yale.earthlive.Constants;
import com.yale.earthlive.EarthLiveApplication;
import com.yale.earthlive.wallpaper.WallpaperDrawable;

/**
 * base activity
 * Created by niejunhong on 15/12/16.
 */
public class BaseActivity extends Activity {

  private View rootView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Constants.ACTION_WALLPAPER_CHANGE);
    registerReceiver(wallpaperChangeReceiver, intentFilter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(wallpaperChangeReceiver);
  }

  @Override
  public void setContentView(int layoutResID) {
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    setContentView(view);
  }

  @Override
  public void setContentView(View view) {
    rootView = view;
    super.setContentView(rootView);
  }

  private BroadcastReceiver wallpaperChangeReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      changeWallpaper();
    }
  };

  private void changeWallpaper() {
    if (EarthLiveApplication.getWallpaperManager().getWallpaper() != null && rootView != null) {
      rootView.setBackground(new WallpaperDrawable(getResources(), EarthLiveApplication
          .getWallpaperManager().getWallpaper()));
    }

  }
}
