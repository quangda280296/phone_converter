package com.chuyendoidauso.chuyendoidanhba.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.Config;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SimUtil;
import com.chuyendoidauso.chuyendoidanhba.commons.StringUtils;
import com.chuyendoidauso.chuyendoidanhba.helper.ContactHelper;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.DataContact;
import com.chuyendoidauso.chuyendoidanhba.models.DataRecovery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskRecoveryContact extends AsyncTask<String, Void, Integer> implements Config {

    private RecoveryContactListener listener;
    private Context context;
    private DataContact dataContact;

    public interface RecoveryContactListener {
        void onComplete(int i);
    }

    public AsyncTaskRecoveryContact(Context context, DataContact dataContact, RecoveryContactListener recoveryContactListener) {
        this.context = context;
        this.listener = recoveryContactListener;
        this.dataContact = dataContact;
    }

    protected Integer doInBackground(String... strArr) {
        int size =0;
        if (context == null || AppConstant.originContactList == null) return -1;
        String read = SharedReferenceUtils.read(this.context, Config.CONTACT_BACKUP);
        DataContact data = new DataContact();
        SimUtil simUtil = new SimUtil(context, context.getContentResolver());
        if (!StringUtils.isNullOrEmpty(read)) {
            data = new Gson().fromJson(read, DataContact.class);

            //System.out.println("dataContact" + new Gson().toJson(dataContact));
            if (!dataContact.getContactList().isEmpty()) {
                for (Contact contact : dataContact.getContactList()) {
                    //
                    if (contact.isChecked()) {
                        if (!TextUtils.isEmpty(contact.contactType) && contact.contactType.equals(simUtil.CONTACT_TYPE_SIM)) {
                            simUtil.deleteContactRecovery(contact);
                            simUtil.addContactRecovery(contact);
                        }
                        size = size +1;
                        ContactHelper.updateContactList(this.context, contact.getName(), contact.getNewIsdn(), contact.getIndsOld());
                    }
                }
            }
            int i = 0;

            List<Contact> listContact = new ArrayList<>();
            if (!data.getContactList().isEmpty()) {
                for (Contact contact : data.getContactList()) {
                    if (dataContact != null &&
                            i < dataContact.getContactList().size() &&
                            dataContact.getContactList().get(i) != null &&
                            dataContact.getContactList().get(i).getIsdn().equals(contact.getIsdn()) &&
                            dataContact.getContactList().get(i).getName().equals(contact.getName()) &&
                            dataContact.getContactList().get(i).contactType.equals(contact.contactType) &&
                            dataContact.getContactList().get(i).isChecked()) {
                        listContact.add(contact);
                    }
                    i = i + 1;
                }
            }
            data.getContactList().removeAll(listContact);
        }
//        //  for (Contact contact : AppConstant.originContactList) {
        // System.out.println("FUCK2: " + dataContact.getContactList());
        System.out.println("FUCK21: " + AppConstant.originContactList.size());
        for (Contact contact : dataContact.getContactList()) {
            //System.out.println("FUCK2: "+!AppConstant.originContactList.contains(contact));
            //if (!AppConstant.originContactList.contains(contact)) {
            if (contact.isChecked()) {
                AppConstant.originContactList.add(contact.getNew(contact));
            }
            // }
        }
        System.out.println("FUCK2D: " + AppConstant.originContactList.size());


        //Luu tru mang
        DataRecovery dataRecovery = SharedReferenceUtils.get(this.context, DataRecovery.class.getName(), DataRecovery.class);
        System.out.println("FUCK2D: " + new Gson().toJson(dataRecovery.dataContactList));
        if (dataRecovery != null && dataRecovery.dataContactList != null) {
            for (DataContact dataObject : dataRecovery.dataContactList) {
                System.out.println("FUCK2D: " +dataObject.date + ":" +dataContact.date);
                if (dataObject.date.equals(dataContact.date)) {
                    dataRecovery.dataContactList.remove(dataObject);
                    break;
                }
            }

            DataContact data1 = new DataContact();
            data1.date = dataContact.date;
            for (Contact contact : dataContact.getContactList()) {
                if (!contact.isChecked()) {
                    data1.getContactList().add(contact);
                }
            }
            if(data1.getContactList()!=null && !data1.getContactList().isEmpty()) dataRecovery.dataContactList.add(data1);
        }
        SharedReferenceUtils.put(this.context, DataRecovery.class.getName(), dataRecovery);

        AppConstant.COUNT_NUMBER = AppConstant.originContactList.size();
        SharedReferenceUtils.save(this.context, Config.CONTACT_BACKUP, new Gson().toJson(data));
        return size;
    }

    protected void onPostExecute(final Integer num) {
        super.onPostExecute(num);
        if (isCancelled() || context == null || num == -1) return;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (AsyncTaskRecoveryContact.this.listener != null) {
                    AsyncTaskRecoveryContact.this.listener.onComplete(num.intValue());
                }
            }
        }, AppConstant.TIME_DELAY_TASK);
    }
}
