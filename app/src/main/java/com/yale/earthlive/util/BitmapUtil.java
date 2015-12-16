package com.yale.earthlive.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yale.earthlive.Constants;
import com.yale.earthlive.EarthLiveApplication;

/**
 * Created by niejunhong on 15/12/16.
 */
public class BitmapUtil {

  private static final String TAG = "BitmapUtil";
  // limit 30M cache
  public static final int CACHE_SIZE_LIMIT = 30 * 1024 * 1024;

  private static String SAVE_PATH = SystemHelper.getRootDirectory() + "images/";

  /**
   * @param url
   * @param width
   * @param height
   * @return
   */
  private static BitmapFactory.Options getScaleBitmapOptions(String url,
      int width, int height) {
    InputStream inputStream = getBitmapStream(url);
    if (inputStream == null) {
      return null;
    }
    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
    bmpFactoryOptions.inJustDecodeBounds = true;
    try {
      BitmapFactory.decodeStream(inputStream, null, bmpFactoryOptions);

      int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
          / height);
      int widthRatio = (int) Math
          .ceil(bmpFactoryOptions.outWidth / width);

      /*
       * If both of the ratios are greater than 1, one of the sides of the
       * image is greater than the screen
       */
      if (heightRatio > 1 && widthRatio > 1) {
        if (heightRatio > widthRatio) {
          // Height ratio is larger, scale according to it
          bmpFactoryOptions.inSampleSize = heightRatio;
        } else {
          // Width ratio is larger, scale according to it
          bmpFactoryOptions.inSampleSize = widthRatio;
        }
      }

      // Decode it for real
      bmpFactoryOptions.inJustDecodeBounds = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    // 关闭java 层的stream
    closeInputStream(inputStream);

    return bmpFactoryOptions;
  }

  /**
   * save bitmap to file
   * 
   * @param url
   * @param mBitmap
   */
  public static void saveBitmap(String url, Bitmap mBitmap) {
    if (mBitmap == null) {
      return;
    }
    int bitmapSize = mBitmap.getRowBytes() * mBitmap.getHeight();
    checkStorageAvailable(bitmapSize);
    try {
      BufferedOutputStream outputStream = new BufferedOutputStream(
          new FileOutputStream(getFileName(url)));
      int quality = 100;
      if (bitmapSize > CACHE_SIZE_LIMIT) {
        quality = 80;
      }
      mBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void closeInputStream(InputStream inputStream) {
    try {
      if (inputStream != null) {
        inputStream.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * delete old file if overflow
   * 
   * @param bitmapSize
   */
  private static void checkStorageAvailable(long bitmapSize) {
    long dirSize = getDirSize(new File(SAVE_PATH));
    if (dirSize + bitmapSize > CACHE_SIZE_LIMIT) {
      Map<Long, String> map = getFilePathAndModyTime(new File(SAVE_PATH));
      long deleteSize = 0;
      long size = (int) (dirSize * 0.3);
      File file = null;
      for (Long time : map.keySet()) {
        file = new File(map.get(time));
        deleteSize += file.length();
        if (deleteSize < size) {
          file.delete();
        } else {
          break;
        }
      }
    }
  }

  private static long getDirSize(File file) {
    long total = 0;
    if (file.exists()) {
      if (file.isFile()) {
        return file.length();
      } else if (file.isDirectory()) {
        for (File f : file.listFiles()) {
          total += getDirSize(f);
        }
      }
    }
    return total;
  }


  private static Map<Long, String> getFilePathAndModyTime(File file) {
    Map<Long, String> map = new HashMap<Long, String>();
    if (file.isFile()) {
      map.put(file.lastModified(), file.getAbsolutePath());
    } else if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        map.putAll(getFilePathAndModyTime(f));
      }
    }
    return map;
  }

  /**
   * get file stream
   * 
   * @param url
   * @return
   */
  public static InputStream getBitmapStream(String url) {
    InputStream is = null;
    try {
      try {
        is = new FileInputStream(new File(getFileName(url)));
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (is == null || is.available() <= 0) {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return is;
  }

  /**
   * generate file path
   * 
   * @param url
   * @return
   */
  public static String getFileName(String url) {
    String filePath = url;
    if (url.startsWith("http://") || url.startsWith("https://")) {
      filePath = SAVE_PATH + url.hashCode();
    }
    Log.d(TAG, "SAVE_PATH" + filePath);
    return filePath;
  }


  public static Bitmap getBitmapFromSDCard(String url, int width, int height) {
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(new File(getFileName(url)));
      if (inputStream != null && inputStream.available() > 0) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
            getScaleBitmapOptions(url, width, height));
        return bitmap;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public static Bitmap getSystemWallpaper() {
    Context context = EarthLiveApplication.getContext();
    Resources resources = context.getResources();
    if (resources == null) {
      return null;
    }
    android.app.WallpaperManager wm = android.app.WallpaperManager.getInstance(context);
    Drawable wallpaper = null;
    try {
      wallpaper = wm.getDrawable();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Bitmap result = null;
    if (wallpaper instanceof BitmapDrawable) {
      BitmapDrawable drawable = (BitmapDrawable) wallpaper;
      result =
          scaleBitmap(drawable.getBitmap(), Constants.getScreenWidth(),
              Constants.getScreenHeight());
      wm.forgetLoadedWallpaper();
    }
    return result;
  }

  private static Bitmap scaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
    int srcWidth = src.getWidth();
    int srcHeight = src.getHeight();
    float srcRatio = (float) srcWidth / (float) srcHeight;
    float dstRatio = (float) dstWidth / (float) dstHeight;

    int clipWidth;
    int clipHeight;
    int leftMargin;
    int topMargin;

    if (srcRatio > dstRatio) {
      clipWidth = (int) (srcHeight * dstRatio);
      clipHeight = srcHeight;
      leftMargin = (srcWidth - clipWidth) / 2;
      topMargin = 0;
    } else {
      clipWidth = srcWidth;
      clipHeight = (int) (srcWidth / dstRatio);
      leftMargin = 0;
      topMargin = (srcHeight - clipHeight) / 2;
    }

    Rect clipRect = new Rect(leftMargin, topMargin, leftMargin + clipWidth, topMargin + clipHeight);
    RectF dstRect = new RectF(0, 0, dstWidth, dstHeight);

    Bitmap result;
    try {
      result = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
    } catch (OutOfMemoryError e) {
      try {
        result = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.RGB_565);
      } catch (OutOfMemoryError e2) {
        result = null;
      }
    }
    if (result != null) {
      Canvas canvas = new Canvas(result);
      canvas.drawBitmap(src, clipRect, dstRect, new Paint());
    }
    return result;
  }
}
