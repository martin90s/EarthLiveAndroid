package com.yale.earthlive;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

/**
 * Created by niejunhong on 15/12/16.
 */
public class AppPref {

  public static final String PREF_NAME = "com.yale.earthlive.pref";

  @Retention(RetentionPolicy.CLASS)
  @StringDef({LAST_UPDATE_TIME, LAST_SAVED_IMAGE_URL})
  public @interface PrefItemName {}

  public static final String LAST_UPDATE_TIME = "last_update_time";

  public static final String LAST_SAVED_IMAGE_URL = "last_saved_image_url";

  private final SharedPreferences pref;

  public AppPref(Context context) {
    pref = context.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
  }

  public long getLong(@PrefItemName String itemName, long defaultVal) {
    return pref.getLong(itemName, defaultVal);
  }

  public void setLong(@PrefItemName String itemName, long value) {
    pref.edit().putLong(itemName, value).apply();
  }

  public int getInt(@PrefItemName String itemName, int defaultVal) {
    return pref.getInt(itemName, defaultVal);
  }

  public void setInt(@PrefItemName String itemName, int value) {
    pref.edit().putInt(itemName, value).apply();
  }

  public String getString(@PrefItemName String itemName, String defaultVal) {
    return pref.getString(itemName, defaultVal);
  }

  public void setString(@PrefItemName String itemName, String value) {
    pref.edit().putString(itemName, value).apply();
  }
}
