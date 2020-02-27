package com.chuyendoidauso.chuyendoidanhba.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.ui.splash.SplashActivity;

public class OpenNoticeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        Intent intent1 = new Intent(context, SplashActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent1);
    }
}
