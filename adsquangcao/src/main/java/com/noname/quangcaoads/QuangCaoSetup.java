package com.noname.quangcaoads;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.noname.quangcaoads.util.Config;
import com.noname.quangcaoads.util.DeviceUtil;
import com.noname.quangcaoads.util.FileCacheUtil;
import com.noname.quangcaoads.util.SharedPreferencesGlobalUtil;

import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuangCaoSetup {

    private static QuangCaoSetup quangCaoSetup;
    private Context context;
    private OkHttpClient client;
    private QuangCaoSetupCallback callback;

    public static void setConfig(Context context, String code, String version) {
        SharedPreferencesGlobalUtil.setValue(context, "code", code);
        SharedPreferencesGlobalUtil.setValue(context, "version", version);
    }

    public static void setLinkServer(Context context, String link_server) {
        SharedPreferencesGlobalUtil.setValue(context, "link_server", link_server);
    }

    public static void initiate(Activity activity) {
        try {
            AppCheckServices.isShowAds = true;
            quangCaoSetup = new QuangCaoSetup();
            quangCaoSetup.setup(activity, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initiate(Context context, QuangCaoSetupCallback callback) {
        quangCaoSetup = new QuangCaoSetup();
        quangCaoSetup.setup(context, callback);
    }

    public static void setServiceEnable(Context context, boolean isEnable) {
        SharedPreferencesGlobalUtil.setValue(context, "isServiceEnable", String.valueOf(isEnable));
    }

    public void setup(final Context context, final QuangCaoSetupCallback callback) {
        if (SharedPreferencesGlobalUtil.getBoolean(context, Config.APP_LIVE_BACKGROUND)) return;
        String timeRegister = SharedPreferencesGlobalUtil.getValue(context, "time_register");
        if (TextUtils.isEmpty(timeRegister)) {
            SharedPreferencesGlobalUtil.setValue(context, "time_register",
                    String.valueOf(System.currentTimeMillis() / 1000));
        }

        this.context = context;
        this.callback = callback;
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        new Thread() {
            @Override
            public void run() {
                try {
                    getCountryOnline();
                    getConfig(context);
                    if (QuangCaoSetup.this.callback != null)
                        QuangCaoSetup.this.callback.onSuccess();
                } catch (Exception e) {

                }
            }
        }.start();
    }

    private void getCountryOnline() {
        String country = SharedPreferencesGlobalUtil.getValue(context, "country");
        if (TextUtils.isEmpty(country)) {
            try {
                Request request = new Request.Builder()
                        .url("https://ipinfo.io/json").get().build();
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject jsonObject = new JSONObject(result);
                country = jsonObject.getString("country").toUpperCase();
            } catch (Exception e) {
                country = Locale.getDefault().toString().toUpperCase();
            }
            SharedPreferencesGlobalUtil.setValue(context, "country", country);
        }
    }

    private String getCountryOffline() {
        String country = null;
        try {
            country = SharedPreferencesGlobalUtil.getValue(context, "country");
        } catch (Exception e) {

        }
        if (TextUtils.isEmpty(country)) {
            country = "VN";
        }
        return country;
    }

    private void getConfig(Context context) {
        try {
            String code = SharedPreferencesGlobalUtil.getValue(context, "code");
            if (TextUtils.isEmpty(code))
                code = "78974";
            String version = SharedPreferencesGlobalUtil.getValue(context, "version");
            if (TextUtils.isEmpty(version))
                version = "1.0";
            String timeRegister = SharedPreferencesGlobalUtil.getValue(context, "time_register");
            if (TextUtils.isEmpty(timeRegister))
                timeRegister = String.valueOf(System.currentTimeMillis() / 1000);
            String domain = SharedPreferencesGlobalUtil.getValue(context, "link_server");
            if (TextUtils.isEmpty(domain)) {
                domain = "http://sdk.gamemobjlee.com/android/apps/control_service.php";
            }
            String url = domain + "?code=_code_&version=_version_&deviceID=_deviceID_&country=_country_&timereg=_timereg_&timenow=_timenow_";
            url = url.replace("_code_", code);
            url = url.replace("_version_", version);
            url = url.replace("_timereg_", timeRegister);
            url = url.replace("_timenow_", String.valueOf(System.currentTimeMillis() / 1000));
            url = url.replace("_deviceID_", DeviceUtil.getDeviceId(context));
            url = url.replace("_country_", getCountryOffline());
            Request request = new Request.Builder().url(url).get().build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.length() > 0) {
                FileCacheUtil.saveConfig(context, result);
            }
        } catch (Exception e) {
        }
    }

    public interface QuangCaoSetupCallback {
        void onSuccess();
    }

    public static void onStartLockService(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ComponentName serviceComponent = new ComponentName(context, LockService.class);
            JobInfo.Builder builder = new JobInfo.Builder(2, serviceComponent);
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
        SharedPreferencesGlobalUtil.saveBoolean(context,Config.APP_LIVE_BACKGROUND,false);
    }
    public static void onStopLockService(Context context) {
        SharedPreferencesGlobalUtil.saveBoolean(context,Config.APP_LIVE_BACKGROUND,true);
    }

    private static boolean isMyServiceRunning(Context context, String serviceName) {
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
