package com.yale.earthlive.service;

import java.io.IOException;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.yale.earthlive.AppPref;
import com.yale.earthlive.Constants;
import com.yale.earthlive.EarthLiveApplication;
import com.yale.earthlive.util.BitmapUtil;

/**
 * Created by niejunhong on 15/12/16.
 */
public class HimawariService extends Service {

  public static final String TAG = "EarthWallpaperService";

  private BroadcastReceiver timeTickReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      checkWallpaper();
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    registerTimeTick();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    checkWallpaper();
    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EarthLiveApplication.getContext().unregisterReceiver(timeTickReceiver);
  }

  private void registerTimeTick() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_TIME_TICK);
    filter.addAction(Intent.ACTION_TIME_CHANGED);
    filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
    EarthLiveApplication.getContext().registerReceiver(timeTickReceiver, filter);
  }


  private void checkWallpaper() {
    if (needUpdate()) {
      EarthLiveApplication.getNetworkManager().addToRequestQueue(getRequest());
    }
  }

  private boolean needUpdate() {
    long lastUpdateTime = EarthLiveApplication.getAppPref().getLong(AppPref.LAST_UPDATE_TIME, 0);
    if (System.currentTimeMillis() - lastUpdateTime > HimawariServiceClient.getInstance()
        .getConfig().interval) {
      return true;
    }
    return false;
  }

  public Request<String> getRequest() {
    StringRequest request =
        new StringRequest(Request.Method.GET, Constants.API_URL, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            Log.d(TAG, response);
            handleResponse(response);
          }
        }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
        });
    return request;
  }

  private void handleResponse(String response) {
    if (!TextUtils.isEmpty(response)) {
      String date = response.substring(9, 28);
      String date_formated = date.replace("-", "/").replace(" ", "/").replace(":", "");
      String imageUrl = Constants.IMAGE_PREFIX + date_formated + "_0_0.png";
      Log.d(TAG, "latestImageUrl:" + imageUrl);
      EarthLiveApplication.getNetworkManager().getImageLoader()
          .get(imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
              setAsWallpaper(response.getBitmap(), response.getRequestUrl());
              saveImage(response.getBitmap(), response.getRequestUrl());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
          });
    }
  }

  private void setAsWallpaper(Bitmap bitmap, String url) {
    if (bitmap != null) {
      WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
      try {
        myWallpaperManager.setBitmap(bitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  private void saveImage(final Bitmap bitmap, final String requestUrl) {
    if (bitmap != null && !TextUtils.isEmpty(requestUrl)) {
      new AsyncTask<Void, Void, Void>() {

        @Override
        protected Void doInBackground(Void... params) {
          BitmapUtil.saveBitmap(requestUrl, bitmap);
          EarthLiveApplication.getAppPref().setString(AppPref.LAST_SAVED_IMAGE_URL, requestUrl);
          EarthLiveApplication.getAppPref().setLong(AppPref.LAST_UPDATE_TIME,
              System.currentTimeMillis());
          return null;
        }
      }.execute();
    }
  }
}
