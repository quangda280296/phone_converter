package com.chuyendoidauso.chuyendoidanhba.ui.recover.detail;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.DialogUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.ShareApp;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityListRecoveryBinding;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListener;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;
import com.chuyendoidauso.chuyendoidanhba.task.AsyncTaskRecoveryContact;
import com.chuyendoidauso.chuyendoidanhba.ui.change.ContactAdapter;
import com.chuyendoidauso.chuyendoidanhba.ui.contact.ContactActivity;

import java.util.List;

public class ListRecoveryActivity extends BaseActivity<ActivityListRecoveryBinding> implements View.OnClickListener {

    private DataContact dataContact;
    private ContactAdapter contactAdapter;
    private boolean isCheckAll;
    private RequestOptions requestOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    public int getLayoutId() {
        return R.layout.activity_list_recovery;
    }

    @Override
    public void initView() {
        AdsUtils.getInstance().initAds(this);
        //   AdsUtils.getInstance().initBanner(this, R.id.adsBannerDone);
        binding.viewScanPhone.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        if (getIntent().getExtras() != null) {
            dataContact = (DataContact) getIntent().getExtras().getSerializable(DataContact.class.getName());
        }
        contactAdapter = new ContactAdapter(this, dataContact.contactList, new ContactAdapter.ContactItemListener() {
            @Override
            public void onClick(List<Contact> list) {
                showCountNumberChange();
                isActiveCheckAllNumber(isSelectAll(list));
            }
        }, true);

        binding.viewScanPhone.recyclerView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
        showCountNumberChange();
        isActiveCheckAllNumber(isSelectAll(dataContact.contactList));

        binding.actionChangeIsdn.setOnClickListener(this);

        binding.imgFilter.setOnClickListener(this);
        binding.imgFilterNumber.setOnClickListener(this);
        binding.imgFilterNumber.setImageResource(R.drawable.ic_arrow_back_white_24dp);

        binding.viewDoneRecovery.btnBack2.setOnClickListener(this);
        binding.viewDoneRecovery.btnShare.setOnClickListener(this);
        binding.viewDoneRecovery.btnRate.setOnClickListener(this);
        binding.viewDoneRecovery.btnRate.setOnClickListener(this);
        binding.viewDoneRecovery.tvViewContact.setOnClickListener(this);
    }

    private void showCountNumberChange() {
        if (contactAdapter != null && contactAdapter.getContactList().size() > 0) {
            binding.tvTotalSub.setText(getString(R.string.title_number_need_change_isdn,
                    Integer.valueOf(getTotalContact()), contactAdapter.getItemCount()));
        }
    }

    private void isActiveCheckAllNumber(boolean isActive) {
        isCheckAll = isActive;
        binding.imgFilter.setImageResource(isActive ? R.drawable.ic_check_all : R.drawable.ic_check);
    }

    private boolean isSelectAll(List<Contact> list) {
        for (Contact isChecked : list) {
            if (!isChecked.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private int getTotalContact() {
        int i = 0;
        if (this.contactAdapter == null || (contactAdapter != null && contactAdapter.getContactList() == null))
            return i;
        for (Contact isChecked : this.contactAdapter.getContactList()) {
            if (isChecked.isChecked()) {
                i++;
            }
        }
        return i;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionChangeIsdn:
                changeIsdn();
                // SimUtil simUtil = new SimUtil(this,getContentResolver());
                // simUtil.addContactStart(new Contact(null,SharedReferenceUtils.read(this,"CACHEXNAME"),SharedReferenceUtils.read(this,"CACHEXSDT") ));
                break;
            case R.id.imgFilterNumber:
                finish();
                break;
            case R.id.imgFilter:
                checkBoxAllF();
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
        }
    }

    private void checkBoxAllF() {
        isCheckAll = !isCheckAll;
        for (Contact checked : this.contactAdapter.getContactList()) {
            if (checked.isValidate) {
                checked.setChecked(isCheckAll);
            }
        }
        binding.imgFilter.setImageResource(isCheckAll ? R.drawable.ic_check_all : R.drawable.ic_check);
        this.contactAdapter.notifyDataSetChanged();
        showCountNumberChange();
    }

    private void changeIsdn() {
        doChangeIsdn();
    }

    private void doChangeIsdn() {
        if (getTotalContact() == 0) {
            Toast.makeText(this, "Bạn chưa chọn số thuê bao nào?", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showDialogConfirm(this, getString(R.string.title_confirm), getString(R.string.msg_confirm_change_isdn, Integer.valueOf(getTotalContact())),
                getString(R.string.btn_accept), new OnClickListener() {
                    @Override
                    public void onClick() {
                        DataContact data = new DataContact();
                        data.setContactList(contactAdapter.getContactList());
                        data.date = dataContact.date;
                        doRecovery(dataContact);
                    }
                });
    }

    private void doRecovery(DataContact dataContact) {
        showProcessing(getString(R.string.txt_processing_recovery));
        new AsyncTaskRecoveryContact(this, dataContact, new AsyncTaskRecoveryContact.RecoveryContactListener() {
            @Override
            public void onComplete(int i) {
                showDoneRecovery(i);
                AdsUtils.getInstance().displayInterstitial();
            }
        }).execute(new String[0]);
    }

    private void showDoneRecovery(int i) {
//        binding.viewDoneRecovery.adsBannerDone.setVisibility(View.GONE);
//        binding.viewDoneRecovery.bgAds.setVisibility(View.GONE);
//        final AdsModel adsModelCache = SharedReferenceUtils.get(this, AdsModel.class.getName(), AdsModel.class);
//        if (adsModelCache != null) {
//            Random random = new Random();
//            int rd = random.nextInt(100);
//            if (rd <= adsModelCache.ratioShowBannerAdmob) {
//                binding.viewDoneRecovery.adsBannerDone.setVisibility(View.VISIBLE);
//            } else {
//                Glide.with(this)
//                        .applyDefaultRequestOptions(requestOptions)
//                        .load(adsModelCache.thumbai.img)
//                        .into(binding.viewDoneRecovery.bgAds);
//                binding.viewDoneRecovery.bgAds.setVisibility(View.VISIBLE);
//                binding.viewDoneRecovery.bgAds.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ShareApp.rateIntentForUrl(ListRecoveryActivity.this, adsModelCache.thumbai.url);
//                    }
//                });
//
//            }
//        }
        binding.imgFilter.setVisibility(View.INVISIBLE);
        binding.imgFilterNumber.setVisibility(View.INVISIBLE);
        binding.actionChangeIsdn.setVisibility(View.GONE);
        binding.tvTotalSub.setText("Phục hồi");
        binding.viewAnimator.setDisplayedChild(2);
        binding.viewDoneRecovery.tvDoneMessage.setText(getString(R.string.recovery_message_done, Integer.valueOf(i)));
        AdsUtils.getInstance().displayInterstitial();
    }


    public void showProcessing(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                binding.viewScanning.tvScanning.setText(str);
                binding.viewAnimator.setDisplayedChild(1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdsUtils.getInstance().displayInterstitial();
    }
}
