package com.yale.earthlive.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by niejunhong on 15/12/16.
 */
public class NetworkManager {

  private static final int API_THREAD_POOL_SIZE = 4;
  private static final int API_CACHE_SIZE = 5 * 1024 * 1024;

  private RequestQueue requestQueue;
  private ImageLoader imageLoader;

  private Context context;

  public NetworkManager(Context context) {
    this.context = context;
    requestQueue = getRequestQueue();
    imageLoader = new ImageLoader(requestQueue,
        new ImageLoader.ImageCache() {
          private final LruCache<String, Bitmap>
          cache = new LruCache<String, Bitmap>(20);

          @Override
          public Bitmap getBitmap(String url) {
            return cache.get(url);
          }

          @Override
          public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
          }
        });
  }

  public <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }


  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    return requestQueue;
  }

  public ImageLoader getImageLoader() {
    return imageLoader;
  }
}
