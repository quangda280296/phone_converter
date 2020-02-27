package com.chuyendoidauso.chuyendoidanhba.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.api.AppApi;
import com.chuyendoidauso.chuyendoidanhba.api.BaseObserver;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.Config;
import com.chuyendoidauso.chuyendoidanhba.commons.ShareApp;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.StringUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityMainBinding;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.models.PostDataResponse;
import com.chuyendoidauso.chuyendoidanhba.notify.NotifyUtils;
import com.chuyendoidauso.chuyendoidanhba.task.AsyncTaskGetContact;
import com.chuyendoidauso.chuyendoidanhba.ui.change.ChangeIsdnActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.contact.ContactActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.change.models.Data;
import com.chuyendoidauso.chuyendoidanhba.ui.info.InfoActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.recover.RecoveryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;

import static com.chuyendoidauso.chuyendoidanhba.commons.Config.PERMISSION_REQUEST_CONTACT;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener {

    private boolean doubleBackToExitPressedOnce = false;
    private boolean isCheckAds;
    private RequestOptions requestOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        binding.viewScanPhone.tvConvert.setOnClickListener(this);
        binding.viewScanPhone.tvRecover.setOnClickListener(this);
        //binding.viewDoneRecovery.btnBack.setOnClickListener(this);
        binding.viewScanPhone.tvConvertations.setOnClickListener(this);
        binding.viewDoneRecovery.btnShare.setOnClickListener(this);
        binding.viewDoneRecovery.btnRate.setOnClickListener(this);
        binding.imgShareApp.setOnClickListener(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                           // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        AppApi.getInstance().registerToken(token, new BaseObserver<ResponseBody>() {

                            @Override
                            protected void onResponse(final ResponseBody ads) {

                            }

                            @Override
                            protected void onFailure() {

                            }
                        });
                    }
                });
    }

    @Override
    public void initData() {
//        if(Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 1234);
//            }
//        }
        getInfoCalendar();
        AdsUtils.getInstance().getIdAds(new AdsUtils.OnCallBackData() {
            @Override
            public void onData(AdsModel adsModel) {
                if (adsModel != null) {
                    System.out.println("AdsUtils.getInstance");
                    binding.viewScanPhone.descriptionScanNumber.setText(adsModel.messageHome);
                    NotifyUtils.addNotify(MainActivity.this, adsModel.timeNotify);
                    AdsUtils.getInstance().initAds(MainActivity.this, new AdsUtils.OnCallBackLoading() {
                        @Override
                        public void onSucess(boolean isDone) {
                            AdsModel adsModel = SharedReferenceUtils.get(MainActivity.this, AdsModel.class.getName(), AdsModel.class);
                            //Toast.makeText(MainActivity.this, "" + String.valueOf(isDone), Toast.LENGTH_SHORT).show();
                            if (adsModel != null && adsModel.openShowAds == 1 && isDone && !isCheckAds) {
                                AdsUtils.getInstance().displayInterstitialStartApp();
                                isCheckAds = true;
                            }
                        }
                    });
                }
            }
        });
    }

    private void initDataCache() {
//        binding.adsBanner.setVisibility(View.GONE);
//        binding.bgAds.setVisibility(View.GONE);
//        final AdsModel adsModelCache = SharedReferenceUtils.get(MainActivity.this, AdsModel.class.getName(), AdsModel.class);
//        if (adsModelCache != null) {
//            binding.viewScanPhone.descriptionScanNumber.setText(adsModelCache.messageHome);
//            Random random = new Random();
//            int rd = random.nextInt(100);
//            if (rd <= adsModelCache.ratioShowBannerAdmob) {
//                binding.adsBanner.setVisibility(View.VISIBLE);
//            } else {
//                Glide.with(this)
//                        .applyDefaultRequestOptions(requestOptions)
//                        .load(adsModelCache.thumbai.img)
//                        .into(binding.bgAds);
//                binding.bgAds.setVisibility(View.VISIBLE);
//                binding.bgAds.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        ShareApp.rateIntentForUrl(MainActivity.this, adsModelCache.thumbai.url);
//                    }
//                });
//
//            }
//        }
    }

    private void getInfoCalendar() {
        AppApi.getInstance().getInfo(new BaseObserver<InfoNumberChange>() {

            @Override
            protected void onResponse(final InfoNumberChange responseBody) {
                SharedReferenceUtils.put(MainActivity.this, InfoNumberChange.class.getName(), responseBody);
            }

            @Override
            protected void onFailure() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.viewAnimator.getDisplayedChild() != 2) {
            showRecovery();
        }
        initDataCache();
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            getListContact();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") == 0) {
            getListContact();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_CONTACTS")) {
            //,"android.permission.WRITE_EXTERNAL_STORAGE"
            requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, PERMISSION_REQUEST_CONTACT);
        } else {
            //,"android.permission.WRITE_EXTERNAL_STORAGE"
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS"}, PERMISSION_REQUEST_CONTACT);
        }
    }

    private void getListContact() {
        new AsyncTaskGetContact(this, getContentResolver(), new AsyncTaskGetContact.GetContactListener() {
            @Override
            public void onGet(final List<Contact> list) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showContactList(list);
                    }
                }, 300);
                if (list != null && !SharedReferenceUtils.getBoolean(MainActivity.this, AppConstant.SP_POST_NUMBER_PHONE)) {
                    postCountNumberPhone(list.size());
                }
            }

            @Override
            public void onLoading() {
                showProcessing(getString(R.string.txt_process_system));
            }
        }).execute(new String[0]);
    }

    private void postCountNumberPhone(int size) {
        String read = SharedReferenceUtils.read(this, Config.CONTACT_BACKUP);
        int sizeConvert = 0;
        if (!StringUtils.isNullOrEmpty(read)) {
            DataContact dataContact = new Gson().fromJson(read, DataContact.class);
            if (!dataContact.getContactList().isEmpty()) {
                sizeConvert = dataContact.getContactList().size();
            }
        }
        final int phoneSize = size + sizeConvert;
        AppApi.getInstance().postCountNumberPhone(phoneSize, new BaseObserver<PostDataResponse>() {

            @Override
            protected void onResponse(final PostDataResponse postDataResponse) {
                SharedReferenceUtils.saveBoolean(MainActivity.this, AppConstant.SP_POST_NUMBER_PHONE, true);
                AdsModel adsModel = SharedReferenceUtils.get(MainActivity.this, AdsModel.class.getName(), AdsModel.class);
                adsModel.limitShow = postDataResponse.limit_show;
                SharedReferenceUtils.put(MainActivity.this, AdsModel.class.getName(), adsModel);
                System.out.print("postCountNumberPhone" + phoneSize + ":" + adsModel.limitShow);
            }

            @Override
            protected void onFailure() {

            }
        });
    }

    private void showContactList(List<Contact> list) {
        try {
            if (list != null && list.size() > 0) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("size = ");
                stringBuilder.append(list.size());
                Intent intent = new Intent(MainActivity.this, ChangeIsdnActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.BD_CONTACT_LIST, new Data(list));
                intent.putExtras(bundle);
                startActivity(intent);
                AdsUtils.getInstance().displayInterstitial();
            } else {
                binding.viewAnimator.setDisplayedChild(0);
                Toast.makeText(this, " Không tìm được thuê bao nào 11 số trong danh bạ của bạn.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProcessing(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                binding.viewScanning.tvScanning.setText(str);
                binding.viewAnimator.setDisplayedChild(1);
                binding.bgAds.setVisibility(View.GONE);
            }
        });
    }

    public void showRecovery(final int index, final boolean isActive) {
        runOnUiThread(new Runnable() {
            public void run() {
                Object[] objArr = new Object[1];
                int i = View.VISIBLE;
                objArr[0] = Integer.valueOf(index);
                binding.viewScanPhone.tvRecoveryContent.setText(getString(R.string.txt_recovery_content, objArr));
                LinearLayout linearLayout = binding.viewScanPhone.lnRecovery;
                if (!isActive) {
                    i = View.INVISIBLE;
                }
                linearLayout.setVisibility(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvConvert:
                askForContactPermission();
                break;
            case R.id.tvRecover:
                openActivity(RecoveryActivity.class);
                //recoveryOnClick();
                break;
            //case R.id.btnBack:
            //    backOnClick();
            //    break;
            case R.id.imgShareApp:
                ShareApp.shareApplication(this);
                break;
            case R.id.tvConvertations:
                openActivity(InfoActivity.class);
                AdsUtils.getInstance().displayInterstitial();
                //openActivity(ContactActivity.class);
                break;
            case R.id.btnRate:
                ShareApp.rateApplication(this);
                break;
            case R.id.btnShare:
                ShareApp.shareApplication(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getListContact();
                    }
                }, 300L);

            }
        }
    }

    private void showRecovery() {
        showRecoveryActive();
        binding.viewAnimator.setDisplayedChild(0);
    }

    private void showRecoveryActive() {
        String read = SharedReferenceUtils.read(this, Config.CONTACT_BACKUP);
        if (!StringUtils.isNullOrEmpty(read)) {
            DataContact dataContact = new Gson().fromJson(read, DataContact.class);
            if (!dataContact.getContactList().isEmpty()) {
                showRecovery(dataContact.getContactList().size(), true);
                return;
            }
        }
        showRecovery(0, false);
        binding.viewAnimator.setDisplayedChild(0);
    }

    private void backOnClick() {
        showRecovery();
        binding.viewAnimator.setDisplayedChild(0);
        AdsUtils.getInstance().displayInterstitial();
    }


    @Override
    public void onBackPressed() {
        if (binding.viewAnimator.getDisplayedChild() == 0) {
            if (doubleBackToExitPressedOnce) {
                AdsUtils.getInstance().displayInterstitialExit(this);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Ấn thêm lần nữa để thoát app!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1500);
        } else {
            binding.viewAnimator.setDisplayedChild(0);
            AdsUtils.getInstance().displayInterstitial();
        }
    }

    @Override
    protected void onDestroy() {
        AdsUtils.getInstance().destroy();
        super.onDestroy();
    }
}
