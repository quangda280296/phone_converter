package com.noname.quangcaoads;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.text.TextUtils;

import com.noname.quangcaoads.util.SharedPreferencesGlobalUtil;

public class UserPresentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isServiceEnable = true;

        try {
            String str = SharedPreferencesGlobalUtil.getValue(context, "isServiceEnable");
            if (!TextUtils.isEmpty(str)) {
                isServiceEnable = Boolean.valueOf(str);
            }
        } catch (Exception e) {
        }

        if (!isServiceEnable) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isMyServiceRunning(context, JobServiceAndroidO.class.getName())) {
                ComponentName serviceComponent = new ComponentName(context, JobServiceAndroidO.class);
                JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
                builder.setMinimumLatency(1 * 1000); // wait at least
                builder.setOverrideDeadline(2 * 1000); // maximum delay
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
                builder.setRequiresDeviceIdle(true); // device should be idle
                builder.setRequiresCharging(false); // we don't care if the device is charging or not
                //PersistableBundle bundle = new PersistableBundle();
                //bundle.putString("test", "test");
                //builder.setExtras(bundle);
                JobScheduler jobScheduler =
                        (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.schedule(builder.build());
            }
        } else if (!isMyServiceRunning(context, AppCheckServices.class.getName())) {
            context.startService(new Intent(context, AppCheckServices.class));
            AppCheckServices.isShowAds = true;
        } else {
            Intent mIntent = new Intent("XXX.AdsReceiver");
            context.sendBroadcast(mIntent);
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