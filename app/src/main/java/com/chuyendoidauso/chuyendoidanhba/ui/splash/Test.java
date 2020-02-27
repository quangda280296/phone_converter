package com.chuyendoidauso.chuyendoidanhba.ui.splash;//import android.app.Activity;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import com.chuyendoidauso.chuyendoidanhba.AppNumberChanger;
//import com.chuyendoidauso.chuyendoidanhba.R;
//import com.chuyendoidauso.chuyendoidanhba.api.AppApi;
//import com.chuyendoidauso.chuyendoidanhba.api.BaseObserver;
//import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
//import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
//import com.chuyendoidauso.chuyendoidanhba.models.Ads;
//import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
//
//
///**
// * Created by jacky on 11/24/17.
// */
//
//public class AdsUtils {
//
//    private static com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils adsUtils;
//    private boolean isLoadFailedBanner = false;
//    private int indexLoadPopup[] = new int[2];
//    private boolean isLoadPopupAds[] = new boolean[2];
//    private AdsModel adsModel;
//    private InterstitialAd adsFull;
//    private InterstitialAd adsFullSave;
//    private static final int INDEX_LOAD_ADS = 3;
//
//    public static com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils getInstance() {
//        synchronized (com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils.class) {
//            if (adsUtils == null) {
//                adsUtils = new com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils();
//                for (int i = 0; i < 2; i++) {
//                    adsUtils.isLoadPopupAds[i] = false;
//                    adsUtils.indexLoadPopup[i] = 1;
//                }
//            }
//            return adsUtils;
//        }
//    }
//
//    public void initBanner(final Activity activity, int idBanner) {
//        if (activity == null) return;
//        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName(), Ads.class);
//        if (ads == null || activity.findViewById(idBanner) == null) return;
//
//        isLoadFailedBanner = false;
//        final RelativeLayout adContainer = activity.findViewById(idBanner);
//        final AdView mAdView = new AdView(activity);
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId(ads.banner);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                if (!isLoadFailedBanner) {
//                    // Create ad request.
//                    AdRequest adRequest = new AdRequest.Builder().build();
//                    // Begin loading your banner ads.
//                    mAdView.loadAd(adRequest);
//                }
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//            }
//
//            @Override
//            public void onAdOpened() {
//            }
//
//            @Override
//            public void onAdLoaded() {
//                if (activity != null && activity.isFinishing()) return;
//                if (adContainer != null) {
//                    isLoadFailedBanner = true;
//                    adContainer.setVisibility(View.VISIBLE);
//                    mAdView.setVisibility(View.VISIBLE);
//                    if (adContainer != null) {
//                        adContainer.removeAllViews();
//                    }
//                    adContainer.addView(mAdView);
//                }
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        adContainer.setVisibility(View.VISIBLE);
//    }
//
//    public void displayInterstitial() {
//        System.out.println("displayInterstitial");
//        if (adsFull != null && adsFullSave != null) {
//            if (!displayInterstitial(adsFull, 0)) {
//                displayInterstitial(adsFullSave, 1);
//            }
//        }
//    }
//
//    public boolean displayInterstitial(final InterstitialAd interstitialAd, final int index) {
//        if (isLoadPopupAds[index]) {
//            if (interstitialAd.isLoaded()) {
//                interstitialAd.show();
//                return true;
//            }
//        } else {
//            AdRequest adRequestFull = new AdRequest.Builder().build();
//            // Begin loading your interstitial.
//            interstitialAd.loadAd(adRequestFull);
//        }
//        return false;
//    }
//
//    public static void getIdAds() {
//        getIdAds(null);
//    }
//
//    public static void getIdAds(final com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils.OnCallBackData onCallBackData) {
//        if (AppNumberChanger.newInstance() == null) return;
//        AppApi.getInstance().getAdsID(AppConstant.CODE_APP, new BaseObserver<AdsModel>() {
//
//            @Override
//            protected void onResponse(final AdsModel ads) {
//                if (AppNumberChanger.newInstance() != null && ads != null && ads.admob != null) {
//                    SharedReferenceUtils.put(AppNumberChanger.newInstance(), Ads.class.getName(), ads.admob);
//                    com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils.getInstance().adsModel = ads;
//                    if (onCallBackData != null) onCallBackData.onData(ads);
//                }
//            }
//
//            @Override
//            protected void onFailure() {
//            }
//        });
//    }
//
//    public void initAds(final Activity activity) {
//        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName(), Ads.class);
//        if (ads == null) return;
//        if (adsFull == null && adsFullSave == null) {
//            adsFull = new InterstitialAd(activity);
//            adsFullSave = new InterstitialAd(activity);
//            adsFull.setAdUnitId(ads.popup);
//            adsFullSave.setAdUnitId(ads.popup);
//        }
//
//        initAdsFull(activity, adsFull, 0);
//        initAdsFull(activity, adsFullSave, 1);
//        initBanner(activity, R.id.adsBanner);
//    }
//
//    private void initAdsFull(Activity activity, final InterstitialAd interstitialAd, final int index) {
//        if (com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils.getInstance().adsModel != null && com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils.getInstance().adsModel.closeShowAds == 0)
//            return;
//        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName(), Ads.class);
//        if (ads == null) return;
//
//        interstitialAd.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                isLoadPopupAds[index] = true;
//                System.out.println("displayInterstitial" + isLoadPopupAds[index]  + index);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                if (indexLoadPopup[index] <= INDEX_LOAD_ADS) {
//                    AdRequest adRequestFull = new AdRequest.Builder().build();
//                    // Begin loading your interstitial.
//                    interstitialAd.loadAd(adRequestFull);
//                    indexLoadPopup[index]++;
//                    System.out.println("displayInterstitial" + indexLoadPopup);
//                } else if (isLoadPopupAds[index]) {
//                    if (interstitialAd.isLoaded()) {
//                        interstitialAd.show();
//                    }
//                }
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                isLoadPopupAds[index] = false;
//                indexLoadPopup[index] = 1;
//                AdRequest adRequestFull = new AdRequest.Builder().build();
//                interstitialAd.loadAd(adRequestFull);
//            }
//        });
//        interstitialAd.loadAd(new AdRequest.Builder().build());
//    }
//
//    public interface OnCallBackData {
//        void onData(AdsModel adsModel);
//    }
//}
