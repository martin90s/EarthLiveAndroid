package com.yale.earthlive.service;

import android.text.format.DateUtils;

/**
 * Created by niejunhong on 15/12/16.
 */
public class EarthWallpaperConfig {

  // interval to fetch wallpaper
  public long interval;

  // the number to split the earth
  public int split;

  // the wallpaper image size
  public int size;

  private EarthWallpaperConfig(Builder builder) {
    this.interval = builder.interval;
    this.split = builder.split;
    this.size = builder.size;
  }

  public static class Builder {
    private long interval = DateUtils.MINUTE_IN_MILLIS * 10;

    private int split = 1;

    private int size = 550;

    public Builder setInterval(long interval) {
      this.interval = interval;
      return this;
    }

    public Builder setSize(int size) {
      this.size = size;
      return this;
    }

    public Builder setSplit(int split) {
      this.split = split;
      return this;
    }

    public EarthWallpaperConfig build() {
      return new EarthWallpaperConfig(this);
    }

  }

}
