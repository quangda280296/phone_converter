package com.noname.quangcaoads;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.noname.quangcaoads.util.LogUtils;

public class ComVsAppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String packageApp = intent.getStringExtra("package");
            if (!TextUtils.isEmpty(packageApp) &&
                    context.getApplicationContext().getPackageName().compareTo(packageApp) < 0) {
                if (isMyServiceRunning(context, AppCheckServices.class.getName())) {
                    context.stopService(new Intent(context.getApplicationContext(), AppCheckServices.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName()) &&
                    context.getApplicationContext().getPackageName().equals(service.service.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
