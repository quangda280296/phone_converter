package com.noname.quangcaoads;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.noname.quangcaoads.ads.AdsBannerView;

import net.mready.hover.HoverWindow;

public class OverlayWindow extends HoverWindow {

    private View btnClose;
    AdRequest mAdRequest;
    private boolean showAfterLoad = false;
    private int countLoad;
    private AdsBannerView adsBannerView;

    @Override
    protected void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        setContentView(R.layout.window_overlay_xxxads, params);

        btnClose = findViewById(R.id.btn_close_xxxads);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    close();
                } catch (Exception e) {
                }
            }
        });

        adsBannerView = findViewById(R.id.ads_banner_view_xxxads);
        adsBannerView.setCallbackBanner(new AdsBannerView.CallbackBanner() {
            @Override
            public void onError() {
                try {
                    close();
                } catch (Exception e) {
                }
            }

            @Override
            public void onLoaded() {
                try {
                    btnClose.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }
        });
        adsBannerView.loadAds();

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
            adsBannerView.destroy();
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}