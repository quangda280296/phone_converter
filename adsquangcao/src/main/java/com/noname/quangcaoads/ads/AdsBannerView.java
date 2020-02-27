package com.noname.quangcaoads.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.noname.quangcaoads.R;
import com.noname.quangcaoads.model.ControlAds;
import com.noname.quangcaoads.util.FileCacheUtil;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.adsCommon.StartAppSDK;

public class AdsBannerView extends RelativeLayout {

    // Context
    private Context context;
    private View mView;
    private LinearLayout mLnrAdview;
    private ControlAds controlAds;
    private Gson gson;
    private boolean isLoadedAds = false;
    private boolean isShowInLayout = true;
    private int countLoad = 1;
    private CallbackBanner callbackBanner;

    // Ads of Admob
    private AdView mAdViewAbmob;
    private AdRequest mAdRequest;

    // Ads of Facebook
    private com.facebook.ads.AdView mAdViewFacebook;

    // Ads of StartApp
    private Banner mAdViewStartApp;

    public AdsBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public AdsBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AdsBannerView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        if (gson == null)
            gson = new Gson();
        mView = inflate(context, R.layout.ads_baner_view_xxxads, null);
        addView(mView);
        mLnrAdview = (LinearLayout) mView.findViewById(R.id.ads_banner_layout_xxxads);

        try {
            String strConfig = FileCacheUtil.loadConfig(context);
            controlAds = gson.fromJson(strConfig, ControlAds.class);
            if (controlAds.getActive_ads().getBanner().equals("admob")) {
                // Admob ads
                addAdmobAds();
            }
            if (controlAds.getActive_ads().getBanner().equals("facebook")) {
                // Facebook ads
                addFacebookAds();
            }
            if (controlAds.getActive_ads().getBanner().equals("start_apps")) {
                // Facebook ads
                addStartAppAds();
            }
            showBannerAds(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        if (mAdViewAbmob != null)
            mAdViewAbmob.destroy();
        if (mAdViewFacebook != null)
            mAdViewFacebook.destroy();
    }

    public void loadAds() {
        try {
            if (gson == null)
                gson = new Gson();
            if (controlAds == null) {
                String strConfig = FileCacheUtil.loadConfig(context);
                controlAds = gson.fromJson(strConfig, ControlAds.class);
            }
            if (controlAds.getActive_ads().getBanner().equals("admob")) {
                // Admob ads
                loadAdmob();
            }
            if (controlAds.getActive_ads().getBanner().equals("facebook")) {
                // Facebook ads
                loadFacebook();
            }
            if (controlAds.getActive_ads().getBanner().equals("start_apps")) {
                // Facebook ads
                loadStartApp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAdmobAds() {
        String keyAdsAdmob = controlAds.getList_key().getAdmob().getBanner();
        mAdViewAbmob = new AdView(getContext());
        mAdViewAbmob.setAdSize(AdSize.SMART_BANNER);
        mAdViewAbmob.setAdUnitId(keyAdsAdmob);
        mAdViewAbmob.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                isLoadedAds = false;
                showBannerAds(false);
                super.onAdFailedToLoad(errorCode);

                if (countLoad < 10) {
                    mAdViewAbmob.loadAd(mAdRequest);
                    countLoad++;
                } else {
                    try {
                        if (callbackBanner != null)
                            callbackBanner.onError();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdLoaded() {
                isLoadedAds = true;
                showBannerAds(true);
                super.onAdLoaded();
                try {
                    if (callbackBanner != null)
                        callbackBanner.onLoaded();
                } catch (Exception e) {
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                try {
                    if (callbackBanner != null)
                        callbackBanner.onError();
                } catch (Exception e) {
                }
            }
        });
        mLnrAdview.addView(mAdViewAbmob);
    }

    private void loadAdmob() {
        mAdRequest = new AdRequest.Builder().build();
        mAdViewAbmob.loadAd(mAdRequest);
    }

    private void addFacebookAds() {
        String keyAdsFacebook = controlAds.getList_key().getFacebook().getBanner();
        mAdViewFacebook = new com.facebook.ads.AdView(getContext(),
                keyAdsFacebook, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        mAdViewFacebook.setAdListener(new com.facebook.ads.AdListener() {

            @Override
            public void onError(Ad arg0, AdError arg1) {
                isLoadedAds = false;
                showBannerAds(false);
                if (countLoad < 10) {
                    mAdViewFacebook.loadAd();
                    countLoad++;
                } else {
                    try {
                        if (callbackBanner != null)
                            callbackBanner.onError();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad arg0) {
                isLoadedAds = true;
                showBannerAds(true);
                try {
                    if (callbackBanner != null)
                        callbackBanner.onLoaded();
                } catch (Exception e) {
                }
            }

            @Override
            public void onAdClicked(Ad arg0) {
                try {
                    if (callbackBanner != null)
                        callbackBanner.onError();
                } catch (Exception e) {
                }
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        mLnrAdview.addView(mAdViewFacebook);
    }

    private void loadFacebook() {
        mAdViewFacebook.loadAd();
    }

    private void addStartAppAds() {
        StartAppSDK.init(context, controlAds.getList_key().getStart_apps().getDevid(),
                controlAds.getList_key().getStart_apps().getAppid());
        mAdViewStartApp = new Banner(context);
        BannerListener bannerListener = new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                isLoadedAds = true;
                showBannerAds(true);
                try {
                    if (callbackBanner != null)
                        callbackBanner.onLoaded();
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                isLoadedAds = false;
                showBannerAds(false);
                if (countLoad < 10) {
                    mAdViewStartApp = new Banner(context);
                    mAdViewStartApp.setBannerListener(this);
                    countLoad++;
                } else {
                    try {
                        if (callbackBanner != null)
                            callbackBanner.onError();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onClick(View view) {
                try {
                    if (callbackBanner != null)
                        callbackBanner.onError();
                } catch (Exception e) {
                }
            }
        };
        mAdViewStartApp.setBannerListener(bannerListener);
    }

    private void loadStartApp() {
        LinearLayout.LayoutParams bannerParameters = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        mLnrAdview.addView(mAdViewStartApp, bannerParameters);
    }

    private void showBannerAds(boolean isShow) {
        try {
            if (isShow && isLoadedAds && isShowInLayout)
                AdsBannerView.this.setVisibility(VISIBLE);
            else
                AdsBannerView.this.setVisibility(GONE);
        } catch (Exception e) {
        }
    }

    public void showBannerAdsInLayout(boolean isShowInLayout) {
        this.isShowInLayout = isShowInLayout;
        showBannerAds(isShowInLayout);
    }

    public void setCallbackBanner(CallbackBanner callbackBanner) {
        this.callbackBanner = callbackBanner;
    }

    public interface CallbackBanner {
        void onError();

        void onLoaded();
    }
}
