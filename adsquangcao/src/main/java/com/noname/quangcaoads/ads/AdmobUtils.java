package com.noname.quangcaoads.ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdmobUtils {

    public static AdmobUtils admobUtils;
    public static AdmobUtils newInstance(Context context) {
        if (admobUtils == null) {
            admobUtils = new AdmobUtils();
            admobUtils.context = context;
        }
        return admobUtils;
    }

    private Context context;
    private InterstitialAd mInterstitialAd;
    private AdRequest mAdRequest;
    private AdmobUtilsListener mListener;
    private boolean showAfterLoad = false;
    private int countLoad;

    public void loadAds() {
        if (mInterstitialAd != null) {
            mAdRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(mAdRequest);
        } else {
            initiate(AdsFullScreen.controlAds.getList_key().getAdmob().getPopup());
        }
    }

    public interface AdmobUtilsListener {
        void onAdsClicked();

        void onAdsOpened();

        void onAdsClosed();
    }

    public void initiate(String adUnitId) {
        try {
            countLoad = 1;
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(adUnitId);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    showAfterLoad = false;
                    if (mListener != null) {
                        mListener.onAdsClosed();
                    }
                    countLoad = 0;
                    mAdRequest = new AdRequest.Builder().build();
                    mInterstitialAd.loadAd(mAdRequest);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    showAfterLoad = false;
                    System.out.println("AdsUtils:countLoad" + countLoad);
                    if (countLoad < 10 && mInterstitialAd != null) {
                        mAdRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(mAdRequest);
                        countLoad++;
                    }
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    try {
                        showAfterLoad = false;
                        if (mListener != null) {
                            mListener.onAdsClicked();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (showAfterLoad) {

                    }
                    showAfterLoad = false;
                    System.out.println("AdsUtils:onAdLoaded");
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    showAfterLoad = false;
                    if (mListener != null) {
                        mListener.onAdsOpened();
                    }
                }
            });

            mAdRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(mAdRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean showAds(AdmobUtilsListener listener) {
        try {
            mListener = listener;
            if (mInterstitialAd.isLoaded()) {
                try {
                    if (admobUtils.mInterstitialAd.isLoaded()) {
                        admobUtils.mInterstitialAd.show();
                    } else {
                        countLoad = 0;
                        mAdRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(mAdRequest);
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getLoadAds() {
        return admobUtils.mInterstitialAd.isLoaded();
    }

    public void loadADs(){
        try {
            mAdRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(mAdRequest);
        } catch (Exception e) {
        }
    }
}
