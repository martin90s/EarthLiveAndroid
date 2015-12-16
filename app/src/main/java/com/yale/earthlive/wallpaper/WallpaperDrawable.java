package com.yale.earthlive.wallpaper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import com.yale.earthlive.R;

/**
 * Created by niejunhong on 15/12/16.
 */
public class WallpaperDrawable extends BitmapDrawable {

  private int mask;

  public WallpaperDrawable(Resources res, Bitmap bitmap) {
    super(res, bitmap);
    mask = res.getColor(R.color.gray);
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    canvas.drawColor(mask);
  }

}
