package com.chuyendoidauso.chuyendoidanhba.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseCoreActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.AssetUtil;
import com.chuyendoidauso.chuyendoidanhba.commons.DataUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.DialogUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.NetworkUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListenerDialog;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.notify.NotifyUtils;
import com.chuyendoidauso.chuyendoidanhba.notify.ResidentNotificationHelper;
import com.chuyendoidauso.chuyendoidanhba.ui.home.MainActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SplashActivity extends BaseCoreActivity {

    private AdsModel adsModel;
    private SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppConstant.originContactList = new ArrayList<>();
        AppConstant.isLoadData = false;
        ResidentNotificationHelper.clearNotification(this, R.string.app_name);
        setContentView(R.layout.activity_splash);
        spinKitView = findViewById(R.id.loading);

        requestApiConfig();
    }

    private void requestApiConfig() {
        spinKitView.setVisibility(View.VISIBLE);
        adsModel = SharedReferenceUtils.get(SplashActivity.this, AdsModel.class.getName(), AdsModel.class);
        if (adsModel != null) {
            AdsUtils.getInstance().initAds(SplashActivity.this, new AdsUtils.OnCallBackLoading() {
                @Override
                public void onSucess(boolean isDone) {

                }
            });
        }
        AdsUtils.getInstance().getIdAds(new AdsUtils.OnCallBackData() {
            @Override
            public void onData(AdsModel adsModel) {
                if (adsModel != null) {
                    NotifyUtils.addNotify(SplashActivity.this, adsModel.timeNotify);
                    SharedReferenceUtils.put(SplashActivity.this, AdsModel.class.getName(), adsModel);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void run() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (adsModel == null) {
                            Gson gson = new Gson();
                            InfoNumberChange infoNumberChange = gson.fromJson(AssetUtil.loadJSONFromAsset(SplashActivity.this, AppConstant.FILE_DS),
                                    InfoNumberChange.class);
                            SharedReferenceUtils.put(SplashActivity.this, InfoNumberChange.class.getName(), infoNumberChange);
                            AdsModel adsModel = gson.fromJson(AssetUtil.loadJSONFromAsset(SplashActivity.this, AppConstant.FILE_CONTROL),
                                    AdsModel.class);
                            SharedReferenceUtils.put(SplashActivity.this, AdsModel.class.getName(), adsModel);
                        }
                        return initMapPrefix();
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        adsModel = SharedReferenceUtils.get(SplashActivity.this, AdsModel.class.getName(), AdsModel.class);
                        if (adsModel != null) {
                            if (adsModel.forceInternet == 0) {
                                openActivity(MainActivity.class, true);
                            } else if (adsModel.forceInternet == 1) {
                                spinKitView.setVisibility(View.GONE);
                                if (!NetworkUtils.isNetworkAvailable(SplashActivity.this)) {
                                    showDialogActiveInternet();
                                } else {
                                    openActivity(MainActivity.class, true);
                                }
                            }
                        }
                    }
                }.execute();
            }
        }, AppConstant.TIME_DELAY_SPLASH);
    }

    private void showDialogActiveInternet() {
        DialogUtils.showDialogConfirm(this,
                getString(R.string.title_info),
                getString(R.string.msg_confirm_connect_internet),
                getString(R.string.btn_try),
                getString(R.string.btn_cancel),
                new OnClickListenerDialog() {

                    @Override
                    public void onClickPositiveButton() {
                        requestApiConfig();
                    }

                    @Override
                    public void onClickNegativeButton() {
                        finish();
                    }
                });
    }

    public Void initMapPrefix() {
        return DataUtils.initMapPrefix(this, getResources());
    }


}
