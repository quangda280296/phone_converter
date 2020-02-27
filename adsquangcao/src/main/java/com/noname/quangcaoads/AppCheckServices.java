package com.noname.quangcaoads;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.noname.quangcaoads.ads.AdmobUtils;
import com.noname.quangcaoads.ads.AdsFullScreen;
import com.noname.quangcaoads.model.ControlAds;
import com.noname.quangcaoads.util.Config;
import com.noname.quangcaoads.util.FileCacheUtil;
import com.noname.quangcaoads.util.LogUtils;
import com.noname.quangcaoads.util.SharedPreferencesGlobalUtil;

import net.mready.hover.Hover;
import net.mready.hover.HoverWindow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class AppCheckServices extends Service {

    public static final String TAG = "AppCheckServices";
    private String myPackage;

    private Gson gson;
    private AdsReceiver receiverAds;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler handler;
    private ArrayList<String> listLauncher;
    private boolean hasPermissionBanner;
    private ControlAds controlAds;
    public static boolean isShowAds;
    private boolean isShowAdsA;
    private boolean isFirsTime = true;
    private CountDownTimer countDownTimer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isServiceEnable = true;
        try {
            String str = SharedPreferencesGlobalUtil.getValue(this, "isServiceEnable");
            if (!TextUtils.isEmpty(str)) {
                isServiceEnable = Boolean.valueOf(str);
            }
        } catch (Exception e) {
        }
        if (!isServiceEnable) {
            stopSelf();
            return START_NOT_STICKY;
        }

        LogUtils.logBlue(TAG, "     Service onStartCommand !!!");
        myPackage = getPackageName();
        controlAds = null;
        sendNotify();

        if (receiverAds != null)
            unregisterReceiver(receiverAds);
        IntentFilter filter = new IntentFilter();
        filter.addAction("XXX.AdsReceiver");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiverAds = new AdsReceiver();
        registerReceiver(receiverAds, filter);

        //////////////////
        listLauncher = new ArrayList<>();
        Intent intentLauncher = new Intent(Intent.ACTION_MAIN);
        intentLauncher.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> lst = getApplicationContext().getPackageManager().queryIntentActivities(intentLauncher, 0);
        if (!lst.isEmpty()) {
            for (ResolveInfo resolveInfo : lst) {
                listLauncher.add(resolveInfo.activityInfo.packageName);
            }
        }

        firstBanner = false;
        timeCreate = System.currentTimeMillis();

        if (Hover.hasOverlayPermission(this) && isPermissionToReadHistory()) {
            hasPermissionBanner = true;
        } else {
            hasPermissionBanner = false;
        }
        //////////////////

        handler = new Handler();

        if (!SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), Config.APP_LIVE_BACKGROUND)) {
            QuangCaoSetup.initiate(getApplicationContext(), new QuangCaoSetup.QuangCaoSetupCallback() {
                @Override
                public void onSuccess() {
                    try {
                        runStartAds(AppCheckServices.this.getApplicationContext());
                    } catch (Exception e) {
                    }
                }
            });
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        resetAll();
        if (receiverAds != null)
            unregisterReceiver(receiverAds);
        receiverAds = null;
        System.out.println("AdsUtils: WorkService- time:::::::----onDestroy");
        super.onDestroy();
    }

    private void sendNotify() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Random random = new Random();
                    int delay = random.nextInt(1900 - 500 + 1) + 500;
                    Thread.sleep(delay);
                    Intent intentSend = new Intent("com.nnvd.comvsapp");
                    intentSend.putExtra("package", AppCheckServices.this.getPackageName());
                    intentSend.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(intentSend);
                } catch (Exception e) {
                }
            }
        }.start();
    }

    private void runStartAds(Context context) {
        try {
            resetAll();
            if (gson == null)
                gson = new Gson();
            String strConfig = FileCacheUtil.loadConfig(context);
            controlAds = gson.fromJson(strConfig, ControlAds.class);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdmobUtils.newInstance(getApplicationContext()).initiate(controlAds.getList_key().getAdmob().getPopup());
                        AdsFullScreen.initiate(AppCheckServices.this);
                    } catch (Exception e) {
                    }
                }
            });
            System.out.println("AdsUtils: WorkService- time:::::::----1");
            scheduleShowAds(2, 1);
        } catch (Exception e) {
        }
    }

    private String getPakageCurrent() {
        if (Build.VERSION.SDK_INT < 21) {
            try {
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                if (taskInfo != null) {
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    if (componentInfo.getPackageName() != null)
                        return componentInfo.getPackageName();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT == 21) {
            try {
                final int PROCESS_STATE_TOP = 2;
                ActivityManager.RunningAppProcessInfo currentInfo = null;
                Field field = null;
                try {
                    field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
                } catch (Exception ignored) {
                }
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo app : appList) {
                    if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && app.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                        Integer state = null;
                        try {
                            state = field.getInt(app);
                        } catch (Exception e) {
                        }
                        if (state != null && state == PROCESS_STATE_TOP) {
                            currentInfo = app;
                            break;
                        }
                    }
                }
                if (currentInfo == null && appList.size() > 1)
                    currentInfo = appList.get(0);
                return currentInfo.processName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= 22) {
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String mpackageName = manager.getRunningAppProcesses().get(0).processName;
            if (!TextUtils.isEmpty(mpackageName) && !mpackageName.equals(myPackage))
                return mpackageName;

            UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            final long INTERVAL = 1800000;
            final long end = System.currentTimeMillis();
            final long begin = end - INTERVAL;
            UsageEvents usageEvents = usm.queryEvents(begin, end);
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, begin, end);
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                mpackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    String temp = event.getPackageName();
                    if (!TextUtils.isEmpty(temp) && mpackageName.equals(temp))
                        return mpackageName;
                }
            }

            if (!TextUtils.isEmpty(mpackageName))
                return mpackageName;
        }
        return "XXX";
    }


    public long time_start_show_popup; // lấy config từ server, mỗi lần bật màn hình đều lấy giá trị mới từ server
    public long offset_time_show_popup; // lấy config từ server, mỗi lần bật màn hình đều lấy giá trị mới từ server
    public long time_user_start; // lúc user mở màn hình lưu lại thời gian tại thời điểm đó.
    public long currentTime; // thời gian hiện tại được cập nhật liên tục theo thời gian service chạy
    public long last_time_show_ads;

    private void scheduleShowAds(int delay, int period) {
        mTimer = new Timer();
        if (mTimerTask != null)
            mTimerTask.cancel();
        time_user_start = System.currentTimeMillis();
        last_time_show_ads = SharedPreferencesGlobalUtil.getLong(getApplicationContext(), Config.TIME_LAST_TIME_SHOW_ADS);
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                AdmobUtils.newInstance(getApplicationContext()).initiate(controlAds.getList_key().getAdmob().getPopup());
//            }
//        });
        mTimerTask = new TimerTask() {

            @Override
            public void run() {

                try {
                    if (isScreenOn(AppCheckServices.this)) {


                        try {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isShowAds) {
//                                        LogUtils.logBlue("X_Time", "Full  " + AdsFullScreen.getTimeConLaiDeShowPopup(getApplicationContext()) +
//                                                "    ---      Banner  " + getTimeConLaiDeShowBanner());
//                                        if (Math.min(AdsFullScreen.getTimeConLaiDeShowPopup(getApplicationContext()), getTimeConLaiDeShowBanner()) > 3000) {
//                                            resetAll();
//                                            AppCheckServices.this.stopSelf();
//                                            return;
//                                        }
                                       // AdmobUtils.newInstance(getApplicationContext()).initiate(controlAds.getList_key().getAdmob().getPopup());
                                        if (hasPermissionBanner) {
                                            long time_Old = System.currentTimeMillis();
                                            String packageNow = getPakageCurrent();
                                            long time_New = System.currentTimeMillis();
                                            LogUtils.logRed("XXX", (time_New - time_Old) + "   " + packageNow);
                                            try {
                                                if (listLauncher.contains(packageNow) && isOkBanner()) {
                                                    openWindow(1235588, OverlayWindow.class);
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                        System.out.println("AdsUtils: WorkService- time:::::::----");
                                        //time_start_show_popup = 10 * 1000L;
                                        //offset_time_show_popup = 30 * 1000L;
                                        time_start_show_popup = 16665 * 1000L;
                                        offset_time_show_popup = 1666665 * 1000L;
                                        if (!AdmobUtils.newInstance(getApplicationContext()).getLoadAds()) {
                                            AdmobUtils.newInstance(getApplicationContext()).loadAds();
                                        }
                                        System.out.println("AdsUtils: WorkService- time:::::::----1");
                                        if ((System.currentTimeMillis() - time_user_start) >= time_start_show_popup) { // đủ thời gian time_start_show_ads sau khi bật màn hình
                                            if (last_time_show_ads == 0) { // user mới cài chưa show lần nào nên last_time_show_ads chưa có giá trị, dc phép show luôn
                                                showAds();
                                                System.out.println("AdsUtils: WorkService- time:::::::----1");
                                                last_time_show_ads = System.currentTimeMillis(); // cứ mỗi lần show ads thành công thì cập nhật biến này = thời gian tại thời điểm show
                                                SharedPreferencesGlobalUtil.saveLong(getApplicationContext(), Config.TIME_LAST_TIME_SHOW_ADS, last_time_show_ads);
                                            } else {
                                                if (System.currentTimeMillis() - last_time_show_ads >= offset_time_show_popup) {// đủ điều kiện offset
                                                    showAds();
                                                    System.out.println("AdsUtils: WorkService- time:::::::----2");
                                                    last_time_show_ads = System.currentTimeMillis(); // cứ mỗi lần show ads thành công thì cập nhật biến này = thời gian tại thời điểm show
                                                    SharedPreferencesGlobalUtil.saveLong(getApplicationContext(), Config.TIME_LAST_TIME_SHOW_ADS, last_time_show_ads);
                                                }

                                            }
                                        }
//                                        if (!isShowAdsA) {
//                                            System.out.println("AdsUtils: WorkService- time:::::::----");
//                                            isShowAdsA = true;
////                                            long time = isFirsTime ? controlAds.getConfig_show().getTime_start_popup() : controlAds.getConfig_show().getOffset_show_popup();
//
//                                            long time = isFirsTime ? 100 : 200;
//
//                                            isFirsTime = false;
//                                            //if(countDownTimer!=null) countDownTimer.cancel();
//
//                                            countDownTimer = new CountDownTimer(time * 1000, 1000) {
//
//                                                public void onTick(long millisUntilFinished) {
//                                                    //  System.out.println("AdsUtils: WorkService:" + AdmobUtils.newInstance(getApplicationContext()).getLoadAds());
//                                                    if (!AdmobUtils.newInstance(getApplicationContext()).getLoadAds()) {
//                                                        AdmobUtils.newInstance(getApplicationContext()).loadADs();
//                                                    }
//                                                }
//
//                                                public void onFinish() {
//                                                    isShowAdsA = false;
//                                                    if (!AdmobUtils.newInstance(getApplicationContext()).getLoadAds()) {
//                                                        AdmobUtils.newInstance(getApplicationContext()).loadAds();
//                                                    }
//                                                    System.out.println("AdsUtils: WorkService isScreenOn:" + isScreenOn(getApplicationContext()));
//                                                    //System.out.println("AdsUtils: WorkService" + SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), "LiveApp"));
//                                                    if (SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), Config.APP_LIVE_BACKGROUND)) return;
//                                                    try {
//                                                        //System.out.println("AdsUtils: WorkService" + AdmobUtils.newInstance(getApplicationContext()).getLoadAds());
//                                                        if (AdmobUtils.newInstance(getApplicationContext()).getLoadAds() && isScreenOn(getApplicationContext())) {
//                                                            Intent intent1 = new Intent(getApplicationContext(), AdsActivityFull.class);
//                                                            getApplicationContext().startActivity(intent1);
//                                                        } else {
//                                                            AdmobUtils.newInstance(getApplicationContext()).loadAds();
//                                                        }
//                                                    } catch (Exception e) {
//                                                    }
//                                                }
//                                            };
//                                            countDownTimer.start();
                                    }
                                }

//                            }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        resetAll();
                        AppCheckServices.this.stopSelf();
                    }
                } catch (
                        Exception e)

                {
                    e.printStackTrace();
                    resetAll();
                    AppCheckServices.this.stopSelf();
                }
            }
        }

        ;
        if (controlAds != null)

        {
            mTimer.schedule(mTimerTask, 1, 2);
        }
    }

    private void showAds() {
        System.out.println("AdsUtils: WorkService isScreenOn:" + isScreenOn(getApplicationContext()));
        //System.out.println("AdsUtils: WorkService" + SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), "LiveApp"));
        if (SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), Config.APP_LIVE_BACKGROUND))
            return;
        try {
            //System.out.println("AdsUtils: WorkService" + AdmobUtils.newInstance(getApplicationContext()).getLoadAds());
            if (AdmobUtils.newInstance(getApplicationContext()).getLoadAds() && isScreenOn(getApplicationContext())) {
                Intent intent1 = new Intent(getApplicationContext(), AdsActivityFull.class);
                getApplicationContext().startActivity(intent1);
            } else {
                AdmobUtils.newInstance(getApplicationContext()).loadAds();
            }
        } catch (Exception e) {
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

    private void resetAll() {
        try {
            if (mTimerTask != null)
                mTimerTask.cancel();
            mTimerTask = null;
        } catch (Exception e) {
        }
        try {
            if (mTimer != null)
                mTimer.cancel();
            mTimer = null;
        } catch (Exception e) {
        }
        try {
            AdsFullScreen.destroy();
        } catch (Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdsFullScreen.destroy();
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    private void openWindow(int id, Class<? extends HoverWindow> window) {
        if (Hover.hasOverlayPermission(this)) {
            firstBanner = true;
            timeShowOld = System.currentTimeMillis();
            FileCacheUtil.setTimeShowBannerAds(getApplicationContext());
            Hover.showWindow(this, id, window);
        }
    }

    private boolean firstBanner;
    private long timeShowOld = 0, timeCreate = 0;
    private int timeDelayAds = 0, firstDelayAds = 0;

    private boolean isOkBanner() {
        if (timeDelayAds == 0) {
            timeDelayAds = controlAds.getConfig_show().getOffset_show_banner();
        }
        if (firstDelayAds == 0) {
            firstDelayAds = controlAds.getConfig_show().getTime_start_banner();
        }
        long timeNow = System.currentTimeMillis();
        if (timeShowOld == 0)
            timeShowOld = FileCacheUtil.getTimeShowBannerAds(getApplicationContext());
        if (!firstBanner) {
            if ((timeNow - timeCreate) > (firstDelayAds * 1000)
                    && (timeNow - timeShowOld) > (timeDelayAds * 1000)) {
                return true;
            }
        } else {
            if ((timeNow - timeShowOld) > (timeDelayAds * 1000)) {
                return true;
            }
        }
        return false;
    }

    public int getTimeConLaiDeShowBanner() {
        if (!hasPermissionBanner) {
            return 9999999;
        } else {
            try {
                if (timeDelayAds == 0) {
                    timeDelayAds = controlAds.getConfig_show().getOffset_show_banner();
                }
                if (firstDelayAds == 0) {
                    firstDelayAds = controlAds.getConfig_show().getTime_start_banner();
                }
                try {
                    long timeNow = System.currentTimeMillis();
                    if (timeShowOld == 0)
                        timeShowOld = FileCacheUtil.getTimeShowBannerAds(getApplicationContext());
                    int denta;
                    if (!firstBanner) {
                        denta = (int) Math.max((timeCreate + firstDelayAds * 1000 - timeNow) / 1000,
                                (timeShowOld + timeDelayAds * 1000 - timeNow) / 1000);
                    } else {
                        denta = (int) ((timeShowOld + timeDelayAds * 1000 - timeNow) / 1000);
                    }
                    return denta;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private class AdsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
                if (isScreenOn(context)) {
                    if (!SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), Config.APP_LIVE_BACKGROUND)) {
                        QuangCaoSetup.initiate(context.getApplicationContext(), new QuangCaoSetup.QuangCaoSetupCallback() {
                            @Override
                            public void onSuccess() {
                                try {
                                    runStartAds(context.getApplicationContext());
                                } catch (Exception e) {
                                }
                            }
                        });
                    }
                } else {
                    resetAll();
                    AppCheckServices.this.stopSelf();
                }
            } catch (Exception e) {
            }
        }

    }

    private boolean isPermissionToReadHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }

}
