package com.noname.quangcaoads.ads;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class FacebookAdsUtils {

	public static FacebookAdsUtils newInstance(Context context) {
		FacebookAdsUtils facebookAdsUtils = new FacebookAdsUtils();
		facebookAdsUtils.context = context;
		return facebookAdsUtils;
	}

	private Context context;
	private InterstitialAd mInterstitialAd;
    private boolean showAfterLoad = false;
    private boolean isShowAds = false;
	private FacebookAdsUtilsListener mListener;

	public interface FacebookAdsUtilsListener {
		void onAdsClicked();
		void onAdsOpened();
		void onAdsClosed();
	}
	
	public void destroy(){
		if(mInterstitialAd != null)
			mInterstitialAd.destroy();
	}

	public void initiate(String adUnitId) {
		mInterstitialAd = new InterstitialAd(context, adUnitId);
		mInterstitialAd.setAdListener(new InterstitialAdListener() {
			
			@Override
			public void onError(Ad arg0, AdError arg1) {
				showAfterLoad = false;
				Log.i("FacebookAdsUtils", arg1.getErrorMessage());
			}
			
			@Override
			public void onAdLoaded(Ad arg0) {
				if (showAfterLoad) {
					
				}
				showAfterLoad = false;
			}
			
			@Override
			public void onAdClicked(Ad arg0) {
				showAfterLoad = false;
				if (mListener != null) {
					mListener.onAdsClicked();
				}
			}

			@Override
			public void onLoggingImpression(Ad ad) {

			}

			@Override
			public void onInterstitialDisplayed(Ad arg0) {
                isShowAds = true;
				showAfterLoad = false;
				if (mListener != null) {
					mListener.onAdsOpened();
				}
			}
			
			@Override
			public void onInterstitialDismissed(Ad arg0) {
				showAfterLoad = false;
				if (mListener != null) {
					mListener.onAdsClosed();
				}
			}
		});

		mInterstitialAd.loadAd();
        isShowAds = false;
	}

	public boolean isLoaded(){
		try{
			return mInterstitialAd.isAdLoaded();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

    public boolean isShowAds() {
        return isShowAds;
    }

    public boolean showAds(FacebookAdsUtilsListener listener) {
		try{
			mListener = listener;
			if (mInterstitialAd.isAdLoaded()) {
				try{
					mInterstitialAd.show();
					return true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;

	}
}
