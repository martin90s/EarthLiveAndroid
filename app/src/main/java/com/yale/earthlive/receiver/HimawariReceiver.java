package com.yale.earthlive.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.yale.earthlive.service.HimawariService;

/**
 * Created by niejunhong on 15/12/16.
 */
public class HimawariReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction() != null ? intent.getAction() : "";
    switch (action) {
      case ConnectivityManager.CONNECTIVITY_ACTION:
        Intent in = new Intent();
        in.setComponent(new ComponentName(context, HimawariService.class));
        context.startService(in);
        break;
      default:
        break;
    }
  }
}
