package com.chuyendoidauso.chuyendoidanhba.ui.recover;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.ShareApp;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityRecoveryBinding;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListenerItem;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;
import com.chuyendoidauso.chuyendoidanhba.models.DataRecovery;
import com.chuyendoidauso.chuyendoidanhba.ui.contact.ContactActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.info.InfoActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.recover.detail.ListRecoveryActivity;

public class RecoveryActivity extends BaseActivity<ActivityRecoveryBinding> implements View.OnClickListener {

    private RecoveryAdapter recoveryAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recovery;
    }

    @Override
    public void initView() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.viewScanPhone.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recoveryAdapter = new RecoveryAdapter(this);
        recoveryAdapter.setOnRecyclerViewItemClickListener(new OnClickListenerItem<DataContact>() {
            @Override
            public void onClick(DataContact dataContact) {
                recoveryOnClick(dataContact);
            }
        });
        binding.viewScanPhone.recyclerView.setAdapter(this.recoveryAdapter);

        binding.viewDoneRecovery.btnBack2.setOnClickListener(this);
        binding.viewDoneRecovery.btnShare.setOnClickListener(this);
        binding.viewDoneRecovery.btnRate.setOnClickListener(this);
        binding.viewDoneRecovery.btnRate.setOnClickListener(this);
        binding.viewDoneRecovery.tvViewContact.setOnClickListener(this);

        AdsUtils.getInstance().initAds(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataRecovery dataRecovery = SharedReferenceUtils.get(this, DataRecovery.class.getName(), DataRecovery.class);
        if (dataRecovery != null && dataRecovery.dataContactList.size() > 0) {
            binding.viewScanPhone.recyclerView.setVisibility(View.VISIBLE);
            binding.viewScanPhone.tvNoData.setVisibility(View.GONE);
            this.recoveryAdapter.setDataList(dataRecovery.dataContactList);
        } else {
            binding.viewScanPhone.recyclerView.setVisibility(View.GONE);
            binding.viewScanPhone.tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void recoveryOnClick(final DataContact dataContact) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataContact.class.getName(), dataContact);
        openActivity(ListRecoveryActivity.class, bundle);
    }


    @Override
    public void onBackPressed() {
        if (binding.viewAnimator.getDisplayedChild() == 0) {
            finish();
        } else {
            binding.viewAnimator.showPrevious();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdsUtils.getInstance().displayInterstitial();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgShareApp:
                ShareApp.shareApplication(this);
                break;
            case R.id.tvConvertations:
                openActivity(InfoActivity.class);
                AdsUtils.getInstance().displayInterstitial();
                break;
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
            default:
                break;
        }
    }
}