package com.noname.quangcaoads;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LockService extends JobService {

    private LockReceiver receiverAds;
    private boolean isLock;

    private static final String TAG = LockService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "DebugLog onStartJob ");
        Log.d(TAG, "DebugLog onStartJob " + jobParameters.getExtras().getString("abc"));

        UserPresentReceiver userPresentReceiver = new UserPresentReceiver();
        IntentFilter i=new IntentFilter();
        i.addAction(Intent.ACTION_USER_PRESENT);
        getApplicationContext().registerReceiver(userPresentReceiver ,i);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public void onDestroy() {
        if (receiverAds != null)
            unregisterReceiver(receiverAds);
        receiverAds = null;
        super.onDestroy();
    }

    public class LockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
                Log.d(TAG, "DebugLog onStartJob " + intent);
                if (isScreenOn(context)) {

                } else {
                    LockService.this.stopSelf();
                }
            } catch (Exception e) {
            }
        }
    }

    private boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isScreenOn();
        } else {
            isScreenOn = pm.isInteractive();
        }
        return isScreenOn;
    }

}