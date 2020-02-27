package com.noname.quangcaoads;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.noname.quangcaoads.ads.AdmobUtils;
import com.noname.quangcaoads.util.Config;
import com.noname.quangcaoads.util.SharedPreferencesGlobalUtil;

public class AdsActivityFull extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("AdsUtils:AdsActivityFull" + "show Ads");
                AdmobUtils.newInstance(AdsActivityFull.this).showAds(new AdmobUtils.AdmobUtilsListener() {
                    @Override
                    public void onAdsClicked() {

                    }

                    @Override
                    public void onAdsOpened() {

                    }

                    @Override
                    public void onAdsClosed() {

                    }
                });
                finish();
                SharedPreferencesGlobalUtil.saveBoolean(getApplicationContext(),Config.APP_LIVE_BACKGROUND,false) ;
            }
        }, 2000L);
    }
}
