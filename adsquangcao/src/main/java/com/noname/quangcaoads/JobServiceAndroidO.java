package com.noname.quangcaoads;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.noname.quangcaoads.ads.AdmobUtils;
import com.noname.quangcaoads.ads.AdsFullScreen;
import com.noname.quangcaoads.model.ControlAds;
import com.noname.quangcaoads.util.Config;
import com.noname.quangcaoads.util.FileCacheUtil;
import com.noname.quangcaoads.util.SharedPreferencesGlobalUtil;

import java.util.Timer;
import java.util.TimerTask;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobServiceAndroidO extends JobService {

    private TimerTask timerTask;
    private Timer timer;
    private ControlAds controlAds;
    private AdsReceiver receiverAds;
    private boolean isShowAds;
    private boolean isFirsTime = true;
    private CountDownTimer countDownTimer;
    public long time_start_show_popup; // lấy config từ server, mỗi lần bật màn hình đều lấy giá trị mới từ server
    public long offset_time_show_popup; // lấy config từ server, mỗi lần bật màn hình đều lấy giá trị mới từ server
    public long time_user_start; // lúc user mở màn hình lưu lại thời gian tại thời điểm đó.
    public long currentTime; // thời gian hiện tại được cập nhật liên tục theo thời gian service chạy
    public long last_time_show_ads;

    private static final String TAG = JobServiceAndroidO.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d(TAG, "DebugLog onStartJob ");
//        Log.d(TAG, "DebugLog onStartJob " + jobParameters.getExtras().getString("test"));

        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (receiverAds != null)
                        unregisterReceiver(receiverAds);
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("XXX.AdsReceiver");
                    filter.addAction(Intent.ACTION_SCREEN_ON);
                    filter.addAction(Intent.ACTION_SCREEN_OFF);
                    receiverAds = new AdsReceiver();
                    registerReceiver(receiverAds, filter);
                    time_user_start = System.currentTimeMillis();
                    last_time_show_ads = SharedPreferencesGlobalUtil.getLong(getApplicationContext(), Config.TIME_LAST_TIME_SHOW_ADS);
                    if (!SharedPreferencesGlobalUtil.getBoolean(getApplicationContext(), Config.APP_LIVE_BACKGROUND)) {
                        QuangCaoSetup.initiate(getApplicationContext(), new QuangCaoSetup.QuangCaoSetupCallback() {
                            @Override
                            public void onSuccess() {
                                try {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Gson gson = new Gson();
                                            String strConfig = FileCacheUtil.loadConfig(getBaseContext());
                                            controlAds = gson.fromJson(strConfig, ControlAds.class);
                                            AdmobUtils.newInstance(getApplicationContext()).initiate(controlAds.getList_key().getAdmob().getPopup());
                                        }
                                    });
                                    timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            Handler aaa = new Handler(Looper.getMainLooper());
                                            aaa.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    System.out.println("AdsUtils: WorkService- time:::::::----1" );
                                                    time_start_show_popup = 1770 * 1000L;
                                                    offset_time_show_popup = 17775 * 1000L;
//                                                    time_start_show_popup = controlAds.getConfig_show().getTime_start_popup() * 1000L;
//                                                    offset_time_show_popup = controlAds.getConfig_show().getOffset_show_popup() * 1000L;
                                                    if (!AdmobUtils.newInstance(getApplicationContext()).getLoadAds()) {
                                                        AdmobUtils.newInstance(getApplicationContext()).loadAds();
                                                    }

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

                                                }
                                            });

                                        }
                                    };
                                    timer = new Timer(true);
                                    timer.schedule(timerTask, 1, 2);
                                } catch (Exception e) {
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                }
            }
        });

        return false;
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

    private void runStartAds(final Context context) {
        try {
            resetAll();
            Gson gson = new Gson();
            String strConfig = FileCacheUtil.loadConfig(getBaseContext());
            controlAds = gson.fromJson(strConfig, ControlAds.class);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdsFullScreen.initiate(context);
                    } catch (Exception e) {
                    }
                }
            });
            scheduleShowAds();
        } catch (Exception e) {
        }
    }

    private void scheduleShowAds() {
        timer = new Timer();
        if (timerTask != null)
            timerTask.cancel();
        AdmobUtils.newInstance(getApplicationContext()).initiate(controlAds.getList_key().getAdmob().getPopup());
        timerTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    if (isScreenOn(getApplicationContext())) {
                        try {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isScreenOn(getApplicationContext())) {
                                        Intent intent1 = new Intent(getApplicationContext(), AdsActivityFull.class);
                                        getApplicationContext().startActivity(intent1);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        resetAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resetAll();
                }
            }
        };

        if (controlAds != null) {
            timer.schedule(timerTask, 2 * 1000l, 5 * 1000l);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "DebugLog onStopJob ");
        if (receiverAds != null)
            unregisterReceiver(receiverAds);
        stopTimerTask();
        return false;
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public class AdsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
                if (isScreenOn(context)) {
                    QuangCaoSetup.initiate(context.getApplicationContext(), new QuangCaoSetup.QuangCaoSetupCallback() {
                        @Override
                        public void onSuccess() {
                            try {
                                Handler h = new Handler(Looper.getMainLooper());
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            runStartAds(context.getApplicationContext());
                                            System.out.println("AdsUtils:Data code ");
                                        } catch (Exception e) {
                                        }
                                    }
                                });

                            } catch (Exception e) {
                            }
                        }
                    });
                } else {
                    resetAll();
                    //JobServiceAndroidO.this.stopSelf();
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

    private void resetAll() {
        try {
            if (timerTask != null)
                timerTask.cancel();
            timerTask = null;
        } catch (Exception e) {
        }
        try {
            if (timer != null)
                timer.cancel();
            timer = null;
        } catch (Exception e) {
        }
        try {
            AdsFullScreen.destroy();
        } catch (Exception e) {
            new Handler().post(new Runnable() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "DebugLog onStartJob onDestroy");
        if (receiverAds != null)
            unregisterReceiver(receiverAds);
    }
}