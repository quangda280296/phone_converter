package com.noname.quangcaoads.ads;

import android.content.Context;

import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;

public class StartAppUtils {

    public interface OnStartAppListener {
        void onStartAppClickListener();

        void onStartAppDisplayed();

        void onStartAppHidden();

        void onStartAppNotDisplayed();
    }

    public static StartAppUtils newInstance(Context context) {
        StartAppUtils startAppUtils = new StartAppUtils();
        startAppUtils.context = context;
        startAppUtils.mStartAppAd = new StartAppAd(context);
        return startAppUtils;
    }

    private Context context;
    private StartAppAd mStartAppAd;
    private OnStartAppListener mListener;
    private AdDisplayListener adDisplayListener;

    public void initiate(String devId, String appId) {
        StartAppSDK.init(context, devId, appId);
        adDisplayListener = new AdDisplayListener() {

            @Override
            public void adHidden(Ad arg0) {
                if (mListener != null) {
                    mListener.onStartAppHidden();
                }
            }

            @Override
            public void adDisplayed(Ad arg0) {
                if (mListener != null) {
                    mListener.onStartAppDisplayed();
                }
            }

            @Override
            public void adClicked(Ad arg0) {
                if (mListener != null) {
                    mListener.onStartAppClickListener();
                }
            }

            @Override
            public void adNotDisplayed(Ad arg0) {
                if (mListener != null) {
                    mListener.onStartAppNotDisplayed();
                }
            }
        };

        mStartAppAd.loadAd();
    }

    public void onResume() {
        try {
            mStartAppAd.onResume();
        } catch (Exception e) {
        }
    }

    public void onPause() {
        try {
            mStartAppAd.onPause();
        } catch (Exception e) {
        }
    }

    public boolean isLoaded() {
        try {
            return mStartAppAd.isReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean showAds(OnStartAppListener listener) {
        try {
            mListener = listener;
            if (mStartAppAd.isReady()) {
                try {
                    mStartAppAd.showAd(adDisplayListener);
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

}
