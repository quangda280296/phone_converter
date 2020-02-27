package com.chuyendoidauso.chuyendoidanhba.ui.recover;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.ShareApp;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityRecoveryDoneBinding;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.ui.contact.ContactActivity;

import java.util.Random;

public class RecoveryDoneActivity extends BaseActivity<ActivityRecoveryDoneBinding> implements View.OnClickListener {

    private RequestOptions requestOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL);
    
    @Override
    public int getLayoutId() {
        return R.layout.activity_recovery_done;
    }

    @Override
    public void initView() {
        //AdsUtils.getInstance().initAds(this);
        binding.btnBack2.setOnClickListener(this);
        binding.btnShare.setOnClickListener(this);
        binding.btnRate.setOnClickListener(this);
        binding.btnRate.setOnClickListener(this);
        binding.tvViewContact.setOnClickListener(this);
    }

    @Override
    public void initData() {
        binding.adsBannerDone.setVisibility(View.GONE);
        binding.bgAds.setVisibility(View.GONE);
        final AdsModel adsModelCache = SharedReferenceUtils.get(this, AdsModel.class.getName(), AdsModel.class);
        if (adsModelCache != null) {
            Random random = new Random();
            int rd = random.nextInt(100);
            if (rd <= adsModelCache.ratioShowBannerAdmob) {
                binding.adsBannerDone.setVisibility(View.VISIBLE);
            } else {
                Glide.with(this)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(adsModelCache.thumbai.img)
                        .into(binding.bgAds);
                binding.bgAds.setVisibility(View.VISIBLE);
                binding.bgAds.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareApp.rateIntentForUrl(RecoveryDoneActivity.this, adsModelCache.thumbai.url);
                    }
                });

            }
        }
        binding.tvDoneMessage.setText(getString(R.string.recovery_message_done, Integer.valueOf(3)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRate:
                ShareApp.rateApplication(this);
                break;
            case R.id.btnShare:
                ShareApp.shareApplication(this);
                break;
            case R.id.btnBack2:
                finish();
                break;
            case R.id.tvViewContact:
                openActivity(ContactActivity.class, true);
                break;
        }
    }
}
