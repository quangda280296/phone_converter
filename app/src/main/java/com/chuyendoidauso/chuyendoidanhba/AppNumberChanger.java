package com.chuyendoidauso.chuyendoidanhba;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.DeviceUtil;
import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AdSettings;
import com.noname.quangcaoads.QuangCaoSetup;

import io.fabric.sdk.android.Fabric;

import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;

public class AppNumberChanger extends MultiDexApplication implements LifeCycleDelegate {

    public static AppNumberChanger instance;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        this.instance = this;
        Fabric.with(this, new Crashlytics());
        QuangCaoSetup.setConfig(this, AppConstant.CODE_APP, DeviceUtil.getVersionAppName(this));
        QuangCaoSetup.setLinkServer(this, AppConstant.URL_LINK_SERVER);
        AppLifecycleHandler lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);
        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
    }

    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }


    public static Context newInstance() {
        return instance;
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }

    @Override
    public void onAppBackgrounded() {
        QuangCaoSetup.onStartLockService(this);
    }

    @Override
    public void onAppForegrounded() {
        QuangCaoSetup.onStopLockService(this);
    }
}
