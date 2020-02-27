package com.noname.quangcaoads;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.noname.quangcaoads.ads.AdmobUtils;
import com.noname.quangcaoads.ads.AdsBannerView;

import net.mready.hover.HoverWindow;

public class FullAdsWindow extends HoverWindow {

    AdRequest mAdRequest;
    private boolean showAfterLoad = false;
    private int countLoad;

    @Override
    protected void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.window_overlay_fullads, params);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final InterstitialAd mInterstitialAd = new InterstitialAd(getApplicationContext());
                mInterstitialAd.setAdUnitId("ca-app-pub-9600258286690042/1508759734");
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        showAfterLoad = false;
                        mInterstitialAd.loadAd(mAdRequest);
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        showAfterLoad = false;
                        if (countLoad < 10 && mInterstitialAd != null) {
                            mInterstitialAd.loadAd(mAdRequest);
                            countLoad++;
                        }
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                        try {
                            showAfterLoad = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        System.out.println("DKM"+ "onAdLoaded");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mInterstitialAd.show();
                            }
                        });

                        showAfterLoad = false;
                        countLoad = 1;
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                        showAfterLoad = false;
                    }
                });

                mAdRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(mAdRequest);
            }
        },3000L);
    }

    @Override
    protected void onDestroy() {
        try {
            //close();
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}