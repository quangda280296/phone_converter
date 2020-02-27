package com.chuyendoidauso.chuyendoidanhba.ui.change;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.base.BaseActivity;
import com.chuyendoidauso.chuyendoidanhba.commons.AdsUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.Config;
import com.chuyendoidauso.chuyendoidanhba.commons.DialogUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.ShareApp;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SimUtil;
import com.chuyendoidauso.chuyendoidanhba.commons.XmlParserUtils;
import com.chuyendoidauso.chuyendoidanhba.databinding.ActivityChangeIsdnBinding;
import com.chuyendoidauso.chuyendoidanhba.listener.OnClickListener;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.models.Network;
import com.chuyendoidauso.chuyendoidanhba.task.AsyncTaskUpdateContact;
import com.chuyendoidauso.chuyendoidanhba.ui.change.models.Data;
import com.chuyendoidauso.chuyendoidanhba.ui.change.popup.NetworkPopup;
import com.chuyendoidauso.chuyendoidanhba.ui.change.popup.number.NumberPhonePopup;
import com.chuyendoidauso.chuyendoidanhba.ui.contact.ContactActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeIsdnActivity extends BaseActivity<ActivityChangeIsdnBinding> implements View.OnClickListener {

    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private ArrayList homeNetworkList = new ArrayList<>();
    private List<Network> networkListSelect;
    private NetworkPopup networkPopup;
    private NumberPhonePopup numberPhonePopup;
    private boolean isCheckAll;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_isdn;
    }

    @Override
    public void initView() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(AppConstant.BD_CONTACT_LIST)) {
            contactList = ((Data) getIntent().getExtras().getSerializable(AppConstant.BD_CONTACT_LIST)).getContactList();

            binding.viewResultScanning.recyclerView.setLayoutManager(new LinearLayoutManager(this));

            numberPhonePopup = new NumberPhonePopup(new NumberPhonePopup.OnNumberPhoneListener() {
                @Override
                public void refreshFilter() {
                    refreshFilterDataPhone();
                }
            });

            if (contactList != null && contactList.size() > 0) {
                getDataAllWithDate();

                binding.viewResultScanning.checkboxAllF.setOnClickListener(this);
                binding.viewResultScanning.tvDso.setOnClickListener(this);
                binding.viewResultScanning.tvNetwork.setOnClickListener(this);

                binding.viewResultScanning.actionChangeIsdn.setOnClickListener(this);
                binding.viewDone.btnBack2.setOnClickListener(this);
                binding.viewDone.btnShare.setOnClickListener(this);
                binding.viewDone.btnRate.setOnClickListener(this);
                binding.viewDone.btnRate.setOnClickListener(this);
                binding.viewDone.tvViewContact.setOnClickListener(this);
                binding.viewResultScanning.toolbar.imgFilter.setOnClickListener(this);
                binding.viewResultScanning.toolbar.imgFilterNumber.setOnClickListener(this);
                binding.viewResultScanning.menu.setVisibility(View.GONE);
                binding.viewResultScanning.tvChooseNetwork.setOnClickListener(this);
                binding.viewResultScanning.tvChooseNumber.setOnClickListener(this);
            }
        }
        AdsUtils.getInstance().initAds(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionChangeIsdn:
                changeIsdn();
                // SimUtil simUtil = new SimUtil(this,getContentResolver());
                // simUtil.addContactStart(new Contact(null,SharedReferenceUtils.read(this,"CACHEXNAME"),SharedReferenceUtils.read(this,"CACHEXSDT") ));
                break;
            case R.id.checkboxAllF:
                checkBoxAllF();
                break;
            case R.id.tvChooseNetwork:
                binding.viewResultScanning.menu.setVisibility(View.GONE);
                if (networkPopup != null && networkListSelect != null) {
                    networkPopup.show(this, binding.viewResultScanning.tvNetwork);
                }
                break;
            case R.id.tvChooseNumber:
                binding.viewResultScanning.menu.setVisibility(View.GONE);
                if (numberPhonePopup != null) {
                    numberPhonePopup.show(this, networkPopup.getNetworkAdapter() != null
                                    ? networkPopup.getNetworkAdapter().getNetworkList() :
                                    networkListSelect,
                            networkPopup.isChangeNetwork,
                            binding.viewResultScanning.tvDso);
                    networkPopup.isChangeNetwork = false;
                }
                break;
            case R.id.btnBack2:
                backOnClick();
                break;
            case R.id.btnRate:
                ShareApp.rateApplication(this);
                break;
            case R.id.btnShare:
                ShareApp.shareApplication(this);
                break;
            case R.id.imgFilter:
                checkBoxAllF();
                break;
            case R.id.imgFilterNumber:
                binding.viewResultScanning.menu.setVisibility(View.VISIBLE);
                break;
            case R.id.tvViewContact:
                openActivity(ContactActivity.class, true);
                break;
            default:
                break;
        }
    }

    private void backOnClick() {
        finish();
    }

    private void changeIsdn() {
        if (requirePermissionWriteContact()) {
            doChangeIsdn();
        }
    }

    public boolean requirePermissionWriteContact() {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_CONTACTS") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_CONTACTS")) {
            requestPermissions(new String[]{"android.permission.WRITE_CONTACTS"}, Config.PERMISSION_REQUEST_WRITE_CONTACT);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_CONTACTS"}, Config.PERMISSION_REQUEST_WRITE_CONTACT);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1232) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                Toast.makeText(this, "No Permissions ", Toast.LENGTH_SHORT).show();
            } else {
                doChangeIsdn();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshFilterData() {
        new AsyncTask<ArrayList<HomeNetwork>, Void, List<Contact>>() {
            @Override
            protected List<Contact> doInBackground(ArrayList<HomeNetwork>... voids) {
                List<Contact> originContactList = contactAdapter.getOriginContactList();
                List<Contact> arrayList = new ArrayList();
                for (Contact contact : originContactList) {
                    if (isContactSelect(contact, networkPopup.getNetworkAdapter() != null ? networkPopup.getNetworkAdapter().getNetworkList() : networkListSelect)) {
                        contact.setChecked(isCheckDate(contact, homeNetworkList));
                        contact.isValidate = isCheckDate(contact, homeNetworkList);
                        arrayList.add(contact);
                    }
                }
                return arrayList;
            }

            @Override
            protected void onPostExecute(List<Contact> list) {
                super.onPostExecute(list);
                if (list != null && list.size() > 0) {
                    contactAdapter.filter(list);
                    isActiveCheckAllNumber(isSelectAll(list));
                } else {
                    contactAdapter.clearData();
                }
                showCountNumberChange();

            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void refreshFilterDataPhone() {
        new AsyncTask<ArrayList<HomeNetwork>, Void, List<Contact>>() {
            @Override
            protected List<Contact> doInBackground(ArrayList<HomeNetwork>... voids) {
                List<Contact> originContactList = contactAdapter.getOriginContactList();
                List<Contact> arrayList = new ArrayList();
                for (Contact contact : originContactList) {
                    if (numberPhonePopup.isContactPhoneSelect(contact) &&
                            isContactSelect(contact, networkPopup.getNetworkAdapter() != null ? networkPopup.getNetworkAdapter().getNetworkList() : networkListSelect)) {
                        contact.setChecked(isCheckDate(contact, homeNetworkList));
                        contact.isValidate = isCheckDate(contact, homeNetworkList);
                        arrayList.add(contact);
                    }
                }
                return arrayList;
            }

            @Override
            protected void onPostExecute(List<Contact> list) {
                super.onPostExecute(list);
                if (list != null && list.size() > 0) {
                    contactAdapter.filter(list);
                    binding.viewResultScanning.checkboxAllF.setChecked(isSelectAll(list));
                } else {
                    contactAdapter.clearData();
                }
                showCountNumberChange();

            }
        }.execute();
    }

    private void getDataAllWithDate() {
        getListDateWithPhoneNumber();
    }

    @SuppressLint("StaticFieldLeak")
    private void getListDateWithPhoneNumber() {
        new AsyncTask<ArrayList<HomeNetwork>, Void, List<Contact>>() {
            @Override
            protected List<Contact> doInBackground(ArrayList<HomeNetwork>... voids) {
                if (networkListSelect == null && networkPopup == null) {
                    networkListSelect = XmlParserUtils.getNetWork(ChangeIsdnActivity.this, getResources());
                    networkPopup = new NetworkPopup(networkListSelect, new NetworkPopup.OnNetworkPopupListener() {
                        @Override
                        public void refreshFilter() {
                            refreshFilterData();
                        }
                    });
                }
                InfoNumberChange responseBody = SharedReferenceUtils.get(ChangeIsdnActivity.this, InfoNumberChange.class.getName(), InfoNumberChange.class);
                homeNetworkList = new ArrayList<>();
                homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vietTel));
                homeNetworkList.addAll(getListDataHomeNetwork(responseBody.mobilePhone));
                homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vinaPhone));
                homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vietnamobile));
                homeNetworkList.addAll(getListDataHomeNetwork(responseBody.gmobile));
                AdsModel adsModel = SharedReferenceUtils.get(ChangeIsdnActivity.this, AdsModel.class.getName(), AdsModel.class);
                int limit;

                if (adsModel != null) {
                    limit = adsModel.limitShow != 0 ? adsModel.limitShow : AppConstant.INDEX_NUMBER_CONVERT;
                } else {
                    limit = AppConstant.INDEX_NUMBER_CONVERT;
                }

                List<Contact> originContactList = contactList.subList(0,
                        contactList.size() > limit
                                ? limit : contactList.size());

//                List arrayList = new ArrayList();
//                for (Contact contact : originContactList) {
//                    contact.setChecked(isCheckDate(contact, homeNetworkList));
//                    contact.isValidate = isCheckDate(contact, homeNetworkList);
//                    contact.date = getDateValidate(contact, homeNetworkList);
//                    if (!contact.isValidate) {
//                        arrayList.add(contact);
//                    } else {
//                        arrayList.add(0, contact);
//                    }
//                }

                return originContactList;
            }

            @Override
            protected void onPostExecute(List<Contact> list) {
                super.onPostExecute(list);
                if (list != null && list.size() > 0 && contactAdapter == null) {
                    contactAdapter = new ContactAdapter(ChangeIsdnActivity.this, list, new ContactAdapter.ContactItemListener() {
                        @Override
                        public void onClick(List<Contact> list) {
                            showCountNumberChange();
                            isActiveCheckAllNumber(isSelectAll(list));
                        }
                    });
                    binding.viewResultScanning.recyclerView.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();
                    showCountNumberChange();
                    isActiveCheckAllNumber(isSelectAll(contactAdapter.getContactList()));
                } else {

                }
            }
        }.execute();
    }

    private void isActiveCheckAllNumber(boolean isActive) {
        isCheckAll = isActive;
        binding.viewResultScanning.toolbar.imgFilter.setImageResource(isCheckAll ? R.drawable.ic_check_all : R.drawable.ic_check);
    }

    private void showCountNumberChange() {
        if (contactList != null && contactList.size() > 0 && contactAdapter != null) {
            binding.viewResultScanning.toolbar.tvTotalSub.setText(getString(R.string.title_number_need_change_isdn,
                    Integer.valueOf(getTotalContact()), contactAdapter.getItemCount()));
            binding.viewResultScanning.tvHeader.setText(getString(R.string.title_number_change_isdn,
                    Integer.valueOf(getTotalContact()), contactAdapter.getItemCount()));
        }
    }

    private boolean isCheckDate(Contact contact, ArrayList<HomeNetwork> list) {
        for (HomeNetwork homeNetwork : list) {
            if (contact != null && homeNetwork != null && !TextUtils.isEmpty(homeNetwork.phoneOld)) {
                //System.out.println("isCheckDate:=A:" + getNumberStartIsdn(contact.getIsdn()));
                //System.out.println("isCheckDate:=B:" + homeNetwork.phoneOld.substring(1, homeNetwork.phoneOld.length()));
                if (getNumberStartIsdn(contact.getIsdn()).startsWith(homeNetwork.phoneOld.substring(1, homeNetwork.phoneOld.length()).trim()) && a(homeNetwork.date)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getDateValidate(Contact contact, ArrayList<HomeNetwork> list) {
        for (HomeNetwork homeNetwork : list) {
            if (contact != null && homeNetwork != null && !TextUtils.isEmpty(homeNetwork.phoneOld)) {
                if (getNumberStartIsdn(contact.getIsdn()).startsWith(homeNetwork.phoneOld.substring(1, homeNetwork.phoneOld.length()))) {
                    return homeNetwork.date;
                }
            }
        }
        return "";
    }

    private String getNumberStartIsdn(String isdn) {
        if (!TextUtils.isEmpty(isdn) && isdn.length() > 5) {
            String data = "";
            if (isdn.startsWith("84") || isdn.startsWith("+84")) {
                data = isdn.replace("84", "").replace("+", "");
            } else if (isdn.startsWith("0")) {
                data = isdn.replace("0", "");
            }
            return data.trim();
        }
        return "";
    }

    private boolean a(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date strDate = sdf.parse(date);
            if (new Date().after(strDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private ArrayList<HomeNetwork> getListDataHomeNetwork(ArrayList<HomeNetwork> list) {
        ArrayList homeNetworkList = new ArrayList<>();
        for (HomeNetwork homeNetwork : list) {
            homeNetworkList.add(homeNetwork);
        }
        return homeNetworkList;
    }

    private boolean isContactSelect(Contact contact, List<Network> list) {
        for (Network network : list) {
            if (network.isChecked() && network.getCode().equals(contact.getNetwork())) {
                return true;
            }
        }
        return false;
    }

    private void checkBoxAllF() {
        isCheckAll = !isCheckAll;
        for (Contact checked : this.contactAdapter.getContactList()) {
            if (checked.isValidate) {
                checked.setChecked(isCheckAll);
            }
        }
        binding.viewResultScanning.toolbar.imgFilter.setImageResource(isCheckAll ? R.drawable.ic_check_all : R.drawable.ic_check);
        this.contactAdapter.notifyDataSetChanged();
        showCountNumberChange();
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

    private void doChangeIsdn() {
        if (getTotalContact() == 0) {
            Toast.makeText(this, "Bạn chưa chọn số thuê bao nào?", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showDialogConfirm(this, getString(R.string.title_confirm), getString(R.string.msg_confirm_change_isdn, Integer.valueOf(getTotalContact())),
                getString(R.string.btn_accept), new OnClickListener() {
                    @Override
                    public void onClick() {
                        changeIsdnActive(contactAdapter.getContactList());
                    }
                });
    }

    private void changeIsdnActive(List<Contact> list) {
        showProcessing(getString(R.string.txt_processing_change));
        new AsyncTaskUpdateContact(this, list, new AsyncTaskUpdateContact.UpdateContactListener() {
            @Override
            public void onComplete(int numberTotal, int numberSuccess) {
                binding.viewAnimator.showNext();
                binding.viewDone.tvDoneMessage.setText(getString(R.string.message_done,
                        Integer.valueOf(numberSuccess),Integer.valueOf(AppConstant.originContactList.size())));
                AdsUtils.getInstance().displayInterstitial();
            }
        }).execute();
    }

    public void showProcessing(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                binding.viewChanging.tvScanning2.setText(str);
                binding.viewAnimator.showNext();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.viewAnimator.getDisplayedChild() == 0 || binding.viewAnimator.getDisplayedChild() == 2) {
            finish();
        }
        AdsUtils.getInstance().displayInterstitial();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdsUtils.getInstance().displayInterstitial();
    }
}