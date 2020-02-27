package com.chuyendoidauso.chuyendoidanhba.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.widget.Toast;

import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.DataUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.ReplaceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SimUtil;
import com.chuyendoidauso.chuyendoidanhba.commons.StringUtils;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.ui.change.ChangeIsdnActivity;
import com.chuyendoidauso.chuyendoidanhba.ui.splash.SplashActivity;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AsyncTaskGetContact extends AsyncTask<String, Void, List<Contact>> {

    private GetContactListener listener;
    private ContentResolver contentResolver;
    private Context context;
    private boolean isSynch;

    public interface GetContactListener {
        void onGet(List<Contact> list);

        void onLoading();
    }

    public AsyncTaskGetContact(Context context, ContentResolver contentResolver, GetContactListener getContactListener) {
        this.context = context;
        this.contentResolver = contentResolver;
        this.listener = getContactListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) listener.onLoading();
    }

    protected List<Contact> doInBackground(String... strArr) {
        if (!AppConstant.isLoadData) {
            ArrayList<Contact> list = new ArrayList();
            ArrayList<String> listKey = new ArrayList();
            //
            InfoNumberChange responseBody = SharedReferenceUtils.get(context, InfoNumberChange.class.getName(), InfoNumberChange.class);

            ArrayList homeNetworkList = new ArrayList<>();
            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vietTel));
            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.mobilePhone));
            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vinaPhone));
            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vietnamobile));
            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.gmobile));
            //System.out.println("isChangeIsdn" );
            SimUtil simUtil = new SimUtil(context, context.getContentResolver());
            // simUtil.addContactStart(new Contact("A","0934544056"));
            //simUtil.getAllContact();

            // Case list contact null get list sim
            for (Contact contact : simUtil.getAll()) {
                String isChangeIsdn = isChangeIsdn(contact.getIsdn());
                if (!TextUtils.isEmpty(isChangeIsdn)) {
                    contact.setPrefixOld(ReplaceUtils.replaceAll(isChangeIsdn));
                    contact.setPrefixNew(ReplaceUtils.replaceAll(DataUtils.getValue(isChangeIsdn)));
                    contact.setChecked(isCheckDate(contact, homeNetworkList));
                    contact.isValidate = isCheckDate(contact, homeNetworkList);
                    contact.date = getDateValidate(contact, homeNetworkList);
                    contact.contactType = SimUtil.CONTACT_TYPE_SIM;

                    String key = contact.getIndsOld().trim() + contact.getName().trim();
                    synchronized (simUtil.getHashMapContact()) {
                        if (!simUtil.getHashMapContact().containsKey(key)) {
                            simUtil.getHashMapContact().put(key, contact);
                        }
                    }
                    //   if (!contact.isValidate) {
                    list.add(contact);
                    // } else {
                    //   list.add(0, contact);
                    //  }
                }
            }

            //String cache number phone

            String phoneCache = "";
            Contact contactCache = null;

            //get list contact
            Cursor query = this.contentResolver.query(Phone.CONTENT_URI, null, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    String name = query.getString(query.getColumnIndex("display_name"));
                    String phone = query.getString(query.getColumnIndex("data1"));

                    if (!TextUtils.isEmpty(phone)) {
                        //System.out.println("DKM:Phone:" + phone);
                        String fone = "";
                        if (phone.startsWith("+")) {
                            fone = "+" + phone.substring(1, phone.length()).replaceAll("[^0-9]", "");
                        } else {
                            fone = phone.replaceAll("[^0-9]", "");
                        }

                        String phoneFix = ReplaceUtils.replaceAll(fone.trim());

                        String isChangeIsdn = isChangeIsdn(phoneFix);

                        if (!TextUtils.isEmpty(isChangeIsdn) &&
                                !TextUtils.isEmpty(phone) &&
                                !TextUtils.isEmpty(phoneFix) &&
                                getNumberStartIsdn(phoneFix).length() == 10) {
                            Contact contact = getContact(name, phoneFix, phone, isChangeIsdn);
                            if (contact != null) {
                                String key;
                                if (!TextUtils.isEmpty(contact.getIndsOld()) && !TextUtils.isEmpty(contact.getName())) {
                                    key = contact.getIndsOld().trim() + contact.getName().trim();
                                } else {
                                    key = contact.getIndsOld() + contact.getName();
                                }

                                synchronized (simUtil.getHashMapContact()) {
                                    if (simUtil.getHashMapContact().containsKey(key)) {
                                        //  contact.isShow = false;
                                        // list.add(contact);
                                        //listKey.add(key);
                                        //lan 2
                                        //System.out.println("DKM::::" + phone.trim() + name.trim());
                                        if (!TextUtils.isEmpty(phoneCache)) {
                                            if (isSynch && phoneCache.equals(phone.trim() + name.trim())) {
                                                // may dong bo
                                                contact.contactType = "phone";
                                                list.add(contact);
                                                contactCache = null;
                                                isSynch = false;
                                                //System.out.println("DKM1:" + contact.getName());
                                            } else {
                                                //lan 1
                                                // may khong dong bo
                                                contactCache = contact;
                                                isSynch = true;
                                                //System.out.println("DKM::::" + contact.getIsdn());
                                            }
                                        } else {
                                            //lan 1
                                            // may khong dong bo
                                            contactCache = contact;
                                            isSynch = true;
                                            //System.out.println("DKM::::" + contact.getIsdn());
                                        }
                                    } else {

                                        list.add(contact);

                                        if (!TextUtils.isEmpty(phoneCache) && !phoneCache.equals(phone + name)
                                                && isSynch && contactCache != null) {
                                            // may ko dong bo
                                            list.add(contactCache);
                                            contactCache = null;

                                        }

                                    }
                                }
                                // list.add(getContact(name, phoneFix, phone, isChangeIsdn));
//                            if (!StringUtils.isNullOrEmpty(isChangeIsdn)) {
//                                 Contact contact = getContact(name, phoneFix, phone, isChangeIsdn);
//                                 String key = contact.getIndsOld().trim() + contact.getName().trim();
//                                 synchronized (simUtil.getHashMapContact()){
//                                     if(simUtil.getHashMapContact().containsKey(key)){
//                                         contact.contactType = SimUtil.CONTACT_TYPE_SIM;
//                                         list.add(contact);
//                                         listKey.add(key);
//                                     }else {
//                                         list.add(contact);
//                                     }
//                                 }
////                                //System.out.println("KIEUNN::" + listSim.size());
////                                if (listSim != null && listSim.size() > 0) {
////                                    for (Contact contactSIM : listSim) {
////                                        if (!TextUtils.isEmpty(contactSIM.getIndsOld()) &&
////                                                !TextUtils.isEmpty(phone) &&
////                                                contactSIM.getIndsOld().trim().equals(phone.trim()) &&
////                                                !TextUtils.isEmpty(contactSIM.getName()) &&
////                                                !TextUtils.isEmpty(name) &&
////                                                contactSIM.getName().toUpperCase().trim().equals(name.toUpperCase().trim())) {
////                                            Contact ct = getContact(name, phoneFix, phone, isChangeIsdn);
////                                            ct.contactType = SimUtil.CONTACT_TYPE_SIM;
////                                            isNumberSim = true;
////                                            list.add(ct);
////                                            //System.out.println("KIEUNN" + contactSIM);
////                                            break;
////                                        }
////                                    }
////                                    System.out.println("DKMM" + isNumberSim);
////                                    if (!isNumberSim) {
////                                        list.add(getContact(name, phoneFix, phone, isChangeIsdn));
////                                    }
////                                } else {
////                                    list.add(getContact(name, phoneFix, phone, isChangeIsdn));
////                                }
//                            }
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(name)) {
                        phoneCache = phone.trim() + name.trim();
                    } else {
                        phoneCache = phone + name;
                    }

                }
                query.close();
//                for(String key: listKey){
//                    simUtil.getHashMapContact().remove(key);
//                }

            }
            AppConstant.originContactList.clear();
            //check validate

            Collections.sort(list, new Comparator<Contact>() {
                @Override
                public int compare(Contact s1, Contact s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            });

            for (Contact contact : list) {
                contact.setChecked(isCheckDate(contact, homeNetworkList));
                contact.isValidate = isCheckDate(contact, homeNetworkList);
                contact.date = getDateValidate(contact, homeNetworkList);
                //if (!contact.isValidate) {
                AppConstant.originContactList.add(contact);
                // } else {
                //    AppConstant.originContactList.add(0, contact);
                // }
            }
            //Collections.sort(list, new ComparatorData());

            AppConstant.isLoadData = true;
            AppConstant.COUNT_NUMBER = AppConstant.originContactList.size();
        }
        return AppConstant.originContactList;
    }

    private Contact getContact(String name, String phoneFix, String phone, String isChangeIsdn) {
        Contact contact = new Contact(name, phoneFix, phone);
        contact.setPrefixOld(ReplaceUtils.replaceAll(isChangeIsdn));
        contact.setPrefixNew(ReplaceUtils.replaceAll(DataUtils.getValue(isChangeIsdn)));
        return contact;
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

    private String getNumberStartIsdn(String number) {
        if (!TextUtils.isEmpty(number) && number.length() > 5) {
            String data = "";
            if (number.startsWith("+0")) number = number.replace("+0", "");
            if (number.startsWith("84") || number.startsWith("+84")) {
                String start = number.substring(0, 4);
                String end = number.substring(4, number.length());
                data = start.replace("84", "").replace("+", "") + end;
            } else if (number.startsWith("0")) {
                data = number.substring(1, number.length()).trim();
            }
            return ReplaceUtils.replaceAll(data.trim());
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

    protected void onPostExecute(List<Contact> list) {
        super.onPostExecute(list);
        if (isCancelled() || list == null) return;
        if (this.listener != null) {
            this.listener.onGet(list);
        }

    }

    private String isChangeIsdn(String str) {
        str = StringUtils.formatStandard(str);
        for (String str2 : DataUtils.getPrefixList()) {
            System.out.println("DKMM:" + str + "x" + str2);
            if (str.startsWith(str2)) {
                return str2;
            }
        }
        return null;
    }
}
