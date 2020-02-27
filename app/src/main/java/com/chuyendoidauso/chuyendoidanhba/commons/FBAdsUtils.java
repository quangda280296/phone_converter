package com.chuyendoidauso.chuyendoidanhba.commons;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.chuyendoidauso.chuyendoidanhba.models.Ads;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.io.IOException;


/**
 * Created by jacky on 11/24/17.
 */

public class FBAdsUtils {
    private static FBAdsUtils fbAdsUtils;
    private InterstitialAd mInterstitial;
    private AdView mAdView;

    private boolean canAccessDisplayAds = true;
    private boolean isLoadFailedBanner = false;
    private boolean isFinishLoadPopup = false;
    private int indexLoadBanner = 1;
    private int indexLoadPopup = 1;

    public static FBAdsUtils getIntance() {
        synchronized (FBAdsUtils.class) {
            if (fbAdsUtils == null) {
                fbAdsUtils = new FBAdsUtils();
            }
            return fbAdsUtils;
        }
    }

    public void initBanner(final Activity activity, final int idBanner) {
        indexLoadBanner = 0;
        final String TAG = "initBannerFB";
        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName() + AppConstant.FACE_BOOK, Ads.class);
        if (ads == null) return;
        //String key = ads.banner;
        String key = "453084138430184_453085285096736";
        Log.i(TAG, "keyBanner = " + key);
        final RelativeLayout adContainer = activity.findViewById(idBanner);
        mAdView = new AdView(activity, key, AdSize.BANNER_HEIGHT_50);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.i(TAG, "onError(): " + adError.getErrorMessage());
                indexLoadBanner++;
                Log.i(TAG, "indexLoadBanner = " + indexLoadBanner);
                if (indexLoadBanner <= 3) {
                    // Begin loading your banner ads.
                    mAdView.loadAd();
                } else {
                    isLoadFailedBanner = true;
                    AdsUtils.getInstance().initBanner(activity, idBanner);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.i(TAG, "onAdLoaded()");
                indexLoadBanner = 1;

                if (adContainer.getVisibility() == View.GONE) {
                    adContainer.setVisibility(View.VISIBLE);
                    mAdView.setVisibility(View.VISIBLE);
                    adContainer.addView(mAdView);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.i(TAG, "onAdClicked()");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.i(TAG, "onLoggingImpression()");
            }
        });
        AdSettings.addTestDevice("HASHED ID");
        mAdView.loadAd();
        mAdView.setVisibility(View.GONE);
        adContainer.setVisibility(View.GONE);
    }

    public void initInterstitial(final Activity activity) {
        final String TAG = "initInterstitialFB";
        Ads ads = SharedReferenceUtils.get(activity, Ads.class.getName() + AppConstant.FACE_BOOK, Ads.class);
        if (ads == null) return;
        //String key = ads.popup;
        String key = "453084138430184_453084415096823";
        Log.i(TAG, "keyPopup = " + key);
        mInterstitial = new InterstitialAd(activity, key);
        mInterstitial.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.i(TAG, "onInterstitialDisplayed()");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                indexLoadPopup = 1;
                // Begin loading your interstitial.
                mInterstitial.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.i(TAG, "onError(): " + adError.getErrorMessage());
                try {
                    if (mInterstitial == null) return;
                    indexLoadPopup++;
                    Log.i(TAG, "indexLoadPopup = " + indexLoadPopup);
                    if (indexLoadPopup <= 3 && mInterstitial != null) {
                        // Begin loading your interstitial.
                        mInterstitial.loadAd();
                    } else {
                        isFinishLoadPopup = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.i(TAG, "onAdLoaded()");
                isFinishLoadPopup = true;
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.i(TAG, "onAdClicked()");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.i(TAG, "onLoggingImpression()");
            }
        });
        mInterstitial.loadAd();
    }

    public void displayInterstitial() {
        System.out.println("displayInterstitial: Facebook");
        String TAG = "displayFB";

        if (mInterstitial != null) {
            Log.i(TAG, "displayInterstitial() = true" + mInterstitial.isAdLoaded());
            if (mInterstitial.isAdLoaded()) {
                if (!canAccessDisplayAds)
                    return;

                Log.i(TAG, "mInterstitialAd.loaded()");
                canAccessDisplayAds = false;
                mInterstitial.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canAccessDisplayAds = true;
                    }
                }, 500);

            } else {
                Log.i(TAG, "mInterstitialAd.loadFailed()");
                if (AdsUtils.getInstance().getAdsFull() == null) {
                    Log.i(TAG, "Admob Interstitial == null");
                    return;
                }

                if (AdsUtils.getInstance().getAdsFull().isLoaded())
                    AdsUtils.getInstance().displayInterstitialAds();
                else
                    Log.i(TAG, "Admob Interstitial load failed");
            }
        } else {
            Log.i(TAG, "mInterstitialAd == null");
        }
    }

    public void setVisibility(boolean isShow) {
        if (mAdView != null) {
            mAdView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public InterstitialAd getmInterstitial() {
        return mInterstitial;
    }

    public boolean isLoadFailedBanner() {
        return isLoadFailedBanner;
    }

    public boolean isFinishLoadPopup() {
        return isFinishLoadPopup;
    }
}
