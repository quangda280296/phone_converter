package com.chuyendoidauso.chuyendoidanhba.commons;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.chuyendoidauso.chuyendoidanhba.AppNumberChanger;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.api.AppApi;
import com.chuyendoidauso.chuyendoidanhba.api.BaseObserver;
import com.chuyendoidauso.chuyendoidanhba.models.Ads;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


/**
 * Created by jacky on 11/24/17.
 */

public class AdsUtils {

    private static AdsUtils adsUtils;
    private boolean isLoadFailedBanner = false;
    private int indexLoadPopup = 1;
    private boolean isLoadPopupAds = false;
    private AdsModel adsModel;
    private InterstitialAd adsFull;
    public boolean isShowAdsNormal = false;
    private CountDownTimer countDownTimer;
    private static final int INDEX_LOAD_ADS = 3;
    private boolean isShowAdsExitApp;
    private int timeCountDown;
    private boolean isShowAdsA;
    private Context context;


    public boolean isShowAdsA() {
        System.out.println("isShowAdsExitApp : isShowAdsA" + isShowAdsExitApp);
        return isShowAdsA;
    }

    public boolean isLoadFailedBanner() {
        return isLoadFailedBanner;
    }

    public static AdsUtils getInstance() {
        synchronized (AdsUtils.class) {
            if (adsUtils == null) {
                adsUtils = new AdsUtils();
                adsUtils.isLoadPopupAds = false;
                adsUtils.indexLoadPopup = 1;
            }
            return adsUtils;
        }
    }

    public void initCountDown(final AdsModel adsModel) {
        this.adsModel = adsModel;
        if (adsModel.offsetShowSds == 0) {
            isShowAdsNormal = true;
            return;
        }
        if (countDownTimer == null) {
            int timeStartShowSds = adsModel.timeStartShowSds;
            timeCountDown = timeStartShowSds;
            initCountDownAds(timeCountDown);
        }
    }

    public void initCountDownAds(int timeCountDown) {
        isShowAdsNormal = false;
        countDownTimer = new CountDownTimer(timeCountDown * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                //.out.println("countDownTimer: finish" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                isShowAdsNormal = true;
                //System.out.println("countDownTimer: finish");
            }
        };
        countDownTimer.start();
    }

    public void reStartCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        timeCountDown = adsModel.offsetShowSds;
        initCountDownAds(timeCountDown);
    }

    public void initBanner(final Activity activity, int idBanner) {
        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName(), Ads.class);
        if (ads == null || activity.findViewById(idBanner) == null) return;
        isLoadFailedBanner = false;
        if (activity.findViewById(idBanner) != null) {
            final RelativeLayout adContainer = activity.findViewById(idBanner);
            final AdView adView = new AdView(activity);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(ads.banner);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    if (!isLoadFailedBanner) {
                        // Create ad request.
                        AdRequest adRequest = new AdRequest.Builder().build();
                        // Begin loading your banner ads.
                        adView.loadAd(adRequest);
                        //System.out.println("AdsUtils.getInstance:faslse");
                    }
                }

                @Override
                public void onAdLeftApplication() {
                }

                @Override
                public void onAdOpened() {
                }

                @Override
                public void onAdLoaded() {
                    if (activity != null && activity.isFinishing()) return;
                    if (adContainer != null) {
                        isLoadFailedBanner = true;
                        adContainer.setVisibility(View.VISIBLE);
                        adView.setVisibility(View.VISIBLE);
                        if (adContainer != null) {
                            adContainer.removeAllViews();
                        }
                        adContainer.addView(adView);
                        //System.out.println("AdsUtils.getInstance:");
                        //mAdView.getLayoutParams().width = 2522;
                    }
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adContainer.setVisibility(View.VISIBLE);
        }
    }

    public void displayInterstitial() {
        indexLoadPopup = 0;
        if (!isShowAdsNormal || this.context == null) return;
        final AdsModel adsModel = SharedReferenceUtils.get(this.context, AdsModel.class.getName(), AdsModel.class);
        if (adsModel == null) return;
        //System.out.println("displayInterstitial: adsModel.adsFaceBook" + adsModel.adsNetwork);
        if (!AppConstant.FACE_BOOK_NETWORK.trim().toUpperCase().equals(adsModel.adsNetwork.trim().toUpperCase())) {
            displayInterstitialAds();
        } else {
            FBAdsUtils.getIntance().displayInterstitial();
        }
    }

    public void displayInterstitialAds() {
        //System.out.println("displayInterstitial: Admob");
        if (adsFull != null) {
            if (isLoadPopupAds) {
                if (adsFull.isLoaded()) {
                    adsFull.show();
                    isShowAdsA = true;
                }
            } else {
                AdRequest adRequestFull = new AdRequest.Builder().build();
                // Begin loading your interstitial.
                adsFull.loadAd(adRequestFull);
            }
        }
    }

    public void displayInterstitialExit(Activity activity) {
        //System.out.println("displayInterstitialExit" + adsFull.isLoaded());
        if ((AdsUtils.getInstance().adsModel != null && AdsUtils.getInstance().adsModel.closeShowAds == 0) || AdsUtils.getInstance().adsModel == null) {
            activity.finish();
            return;
        }
        if (adsFull != null) {
            if (adsFull.isLoaded()) {
                isShowAdsExitApp = true;
                adsFull.show();
            } else {
                activity.finish();
            }
        }
    }

    public void displayInterstitialStartApp() {
        if (adsFull != null && isLoadPopupAds) {
            if (adsFull.isLoaded()) {
                adsFull.show();
            }
        }
    }

    public void initAds(final Activity activity) {
        this.context = activity;
        final AdsModel adsModel = SharedReferenceUtils.get(activity, AdsModel.class.getName(), AdsModel.class);
        if (adsModel == null) return;
        if (!AppConstant.FACE_BOOK_NETWORK.equals(adsModel.adsNetwork)) {
            initBanner(activity, R.id.adsBanner);
        } else {
            FBAdsUtils.getIntance().initBanner(activity, R.id.adsBanner);
        }
        initAdsFull(activity);
    }

    public void initAds(final Activity activity, OnCallBackLoading onCallBackLoading) {
        initBanner(activity, R.id.adsBanner);
        initAdsFull(activity, onCallBackLoading);
        FBAdsUtils.getIntance().initInterstitial(activity);
    }

    private void initAdsFull(final Activity activity) {
        initAdsFull(activity, null);
        FBAdsUtils.getIntance().initInterstitial(activity);
    }

    private void initAdsFull(final Activity activity, final OnCallBackLoading onCallBackLoading) {
        if (onCallBackLoading != null) indexLoadPopup = 1;
        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName(), Ads.class);
        if (ads == null) return;
        if (adsFull == null) {
            adsFull = new InterstitialAd(activity);
            adsFull.setAdUnitId(ads.popup);
            adsFull.loadAd(new AdRequest.Builder().build());
        }

        adsFull.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //System.out.println("displayInterstitialStartApp:::" + adsFull.isLoaded() + isLoadPopupAds);
                isLoadPopupAds = true;
                //System.out.println("displayInterstitialStartApp" + isLoadPopupAds);
                if (onCallBackLoading != null) {
                    onCallBackLoading.onSucess(isLoadPopupAds);
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                // System.out.println("onAdFailedToLoad" + indexLoadPopup);
                if (adsFull == null) return;
                if (indexLoadPopup <= INDEX_LOAD_ADS) {
                    AdRequest adRequestFull = new AdRequest.Builder().build();
                    // Begin loading your interstitial.
                    adsFull.loadAd(adRequestFull);
                    indexLoadPopup++;
                } else if (isLoadPopupAds) {
                    if (adsFull.isLoaded()) {
                        adsFull.show();
                    }
                } else {
                    if (onCallBackLoading != null) onCallBackLoading.onSucess(isLoadPopupAds);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                isShowAdsA = true;

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                isShowAdsA = false;
            }

            @Override
            public void onAdClosed() {
                System.out.println("isShowAdsExitApp" + isShowAdsExitApp);
                if (isShowAdsExitApp && activity != null) {
                     System.exit(0);
                    //ActivityCompat.finishAffinity(activity);
                    isShowAdsExitApp = false;
                    return;
                }
                isLoadPopupAds = false;
                indexLoadPopup = 1;
                if (adsFull != null) {
                    AdRequest adRequestFull = new AdRequest.Builder().build();
                    adsFull.loadAd(adRequestFull);
                    if (isShowAdsNormal && activity != null) {
                        reStartCountDown();
                    }
                }

                isShowAdsA = false;
            }
        });
    }

    public static void getIdAds(final OnCallBackData onCallBackData) {
        if (AppNumberChanger.newInstance() == null) return;
        AppApi.getInstance().getAdsID(AppConstant.CODE_APP, new BaseObserver<AdsModel>() {

            @Override
            protected void onResponse(final AdsModel ads) {
                if (AppNumberChanger.newInstance() != null && ads != null && ads.admob != null) {
                    SharedReferenceUtils.put(AppNumberChanger.newInstance(), AdsModel.class.getName(), ads);
                    SharedReferenceUtils.put(AppNumberChanger.newInstance(), Ads.class.getName(), ads.admob);
                    SharedReferenceUtils.put(AppNumberChanger.newInstance(), Ads.class.getName() + AppConstant.FACE_BOOK, ads.adsFaceBook);
                    AdsUtils.getInstance().initCountDown(ads);
                    if (onCallBackData != null) onCallBackData.onData(ads);
                }
            }

            @Override
            protected void onFailure() {
                if (onCallBackData != null) onCallBackData.onData(null);
            }
        });
    }

    public void destroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        adsFull = null;
    }

    public interface OnCallBackData {
        void onData(AdsModel adsModel);
    }

    public interface OnCallBackLoading {
        void onSucess(boolean isDone);
    }

    public InterstitialAd getAdsFull() {
        return adsFull;
    }
}
