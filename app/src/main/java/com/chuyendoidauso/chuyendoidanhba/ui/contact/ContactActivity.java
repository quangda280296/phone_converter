package com.chuyendoidauso.chuyendoidanhba.ui.contact;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.Config;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.StringUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityContactBinding;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;
import com.google.gson.Gson;

public class ContactActivity extends BaseActivity<ActivityContactBinding> {

    private ContactAdapter contactAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public void initView() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.contactAdapter = new ContactAdapter(this);
        binding.recyclerView.setAdapter(this.contactAdapter);
        AdsUtils.getInstance().initAds(this);
    }

    @Override
    public void initData() {
        String read = SharedReferenceUtils.read(this, Config.CONTACT_BACKUP);
        if (!StringUtils.isNullOrEmpty(read)) {
            DataContact dataContact = new Gson().fromJson(read, DataContact.class);
            if (!dataContact.getContactList().isEmpty()) {
                this.contactAdapter.setDataList(dataContact.getContactList());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdsUtils.getInstance().displayInterstitial();
    }
}
