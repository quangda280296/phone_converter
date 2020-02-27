package com.chuyendoidauso.chuyendoidanhba.api;

import android.os.Build;
import android.provider.Settings;

import com.chuyendoidauso.chuyendoidanhba.AppNumberChanger;
import com.chuyendoidauso.chuyendoidanhba.BuildConfig;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.models.PostDataResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class AppApi {

    public static AppApi appApi;
    public String idDevice = Settings.Secure.getString(AppNumberChanger.newInstance().getContentResolver(),
            Settings.Secure.ANDROID_ID);
    public String deviceName = Build.MANUFACTURER + " " + Build.MODEL;
    public String osVersion = String.valueOf(Build.VERSION.RELEASE);

    public static AppApi getInstance() {
        if (appApi == null) {
            appApi = new AppApi();
        }
        appApi.onLoadingDataFromServer = null;
        return appApi;
    }

    public static AppApi getInstance(OnLoadingDataFromServer onLoading) {
        if (appApi == null) {
            appApi = new AppApi();
        }
        appApi.onLoadingDataFromServer = onLoading;
        return appApi;
    }

    private OnLoadingDataFromServer onLoadingDataFromServer;

    protected <M> void addObservable(Observable<M> observable, Observer<M> subscription) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        if (onLoadingDataFromServer != null) {
                            onLoadingDataFromServer.onDismissLoading(true);
                        }
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        if (onLoadingDataFromServer != null) {
                            onLoadingDataFromServer.onShowLoading(disposable);
                        }
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() {
                        if (onLoadingDataFromServer != null) {
                            onLoadingDataFromServer.onDismissLoading(false);
                        }
                    }
                })
                .subscribe(subscription);
    }

    public void getInfo(Observer<InfoNumberChange> subscriber) {
        addObservable(AppClient.getInstance().getApiService().getInfo(), subscriber);
    }

    public void postDeviceId(String packageName, String deviceId, Observer<ResponseBody> subscriber) {
        addObservable(AppClient.getInstance().getApiService().postDeviceId(packageName, deviceId,
                BuildConfig.VERSION_NAME,
                osVersion,
                deviceName), subscriber);
    }

    public void getAdsID(String code, Observer<AdsModel> subscriber) {
        addObservable(AppClient.getInstance().getApiService().getAdsID(code,
                idDevice,
                BuildConfig.VERSION_NAME,
                osVersion,
                deviceName
        ), subscriber);
    }

    public void registerToken(String token, Observer<ResponseBody> subscriber) {
        addObservable(AppClient.getInstance().getApiService().registerToken(
                BuildConfig.APPLICATION_ID,
                token,
                AppConstant.CODE_APP,
                idDevice,
                BuildConfig.VERSION_NAME,
                osVersion,
                deviceName
        ), subscriber);
    }

    public void postCountNumberPhone(int countNumberPhone, Observer<PostDataResponse> subscriber) {
        addObservable(AppClient.getInstance().getApiService().postCountNumberPhone(
                countNumberPhone,
                AppConstant.CODE_APP,
                idDevice
        ), subscriber);
    }

//    api này: http://gamemobileglobal.com/api/apps/update_time_push_notify.php
//    HTTP Post, các biến bắn lên gồm:
//    deviceID,code,package,version,os_version,phone_name,id_push
//    Cái type = statistical là để em thống kê lượng thiết bị còn giữ app đang là bao nhiêu chứ k phải để hiển thị notify.
    public void updateTimePushNotify(String idPush, Observer<ResponseBody> subscriber) {
        addObservable(AppClient.getInstance().getApiService().updateTimePushNotify(
                BuildConfig.APPLICATION_ID,
                idPush,
                AppConstant.CODE_APP,
                idDevice,
                BuildConfig.VERSION_NAME,
                osVersion,
                deviceName
        ), subscriber);
    }

}
