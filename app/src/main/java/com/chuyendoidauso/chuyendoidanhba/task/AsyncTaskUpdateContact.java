package com.chuyendoidauso.chuyendoidanhba.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.Config;
import com.chuyendoidauso.chuyendoidanhba.commons.DataUtils;
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

public class AsyncTaskUpdateContact extends AsyncTask<String, Void, Integer> implements Config {
    private List<Contact> contactList;
    private UpdateContactListener listener;
    private Context context;
    private int numberSuccess = 0;
    private int numberTotal = 0;

    public interface UpdateContactListener {
        void onComplete(int numberTotal, int numberSuccess);
    }

    public AsyncTaskUpdateContact(Context context, List<Contact> list, UpdateContactListener updateContactListener) {
        this.context = context;
        this.contactList = list;
        this.listener = updateContactListener;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Integer doInBackground(String... str) {
        ArrayList<Contact> list = new ArrayList();
        SimUtil simUtil = new SimUtil(context, context.getContentResolver());
        for (Contact contact : this.contactList) {
            if (contact.isChecked()) {
                this.numberTotal++;
                if (ContactHelper.updateContactList(this.context, contact.getName(), contact.getIndsOld(), contact.getNewIsdn())) {
                    this.numberSuccess++;
                    list.add(contact);
                    if (!TextUtils.isEmpty(contact.contactType) && contact.contactType.equals(simUtil.CONTACT_TYPE_SIM)) {
                        simUtil.deleteContactUpdate(contact);
                        simUtil.addContactUpdate(contact);
                    }
                }
            }
        }
        backupContact(list);
        return Integer.valueOf(this.numberSuccess);
    }

    private void backupContact(List<Contact> list) {
        String read = SharedReferenceUtils.read(this.context, Config.CONTACT_BACKUP);
        DataContact dataContact = new DataContact();

        if (!StringUtils.isNullOrEmpty(read)) {
            dataContact = new Gson().fromJson(read, DataContact.class);
        }
        dataContact.getContactList().addAll(list);
        ArrayList<Contact> originContactList = new ArrayList();
        if (AppConstant.originContactList != null) {
            for (Contact contact : AppConstant.originContactList) {
                for (Contact contact1 : dataContact.getContactList()) {
                    if (contact != null && contact1 != null && !TextUtils.isEmpty(contact.getName()) && !TextUtils.isEmpty(contact.getIsdn())) {
                        if (contact.getName().equals(contact1.getName())) {
                            originContactList.add(contact);
                        }
                    }
                }
            }
            //System.out.println("Jacky:" + originContactList.size());
            AppConstant.originContactList.removeAll(originContactList);
            AppConstant.COUNT_NUMBER = AppConstant.originContactList.size();

            DataRecovery dataRecovery = SharedReferenceUtils.get(this.context, DataRecovery.class.getName(), DataRecovery.class);
            if (dataRecovery == null) {
                dataRecovery  = new DataRecovery();
                dataRecovery.dataContactList = new ArrayList<>();
            }
            DataContact dataContactNew = new DataContact();
            dataContactNew.getContactList().addAll(list);
            dataContactNew.date = DataUtils.getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm:ss");
            if(dataRecovery.dataContactList!=null) dataRecovery.dataContactList.add(dataContactNew);

            SharedReferenceUtils.put(this.context, DataRecovery.class.getName(), dataRecovery);
            SharedReferenceUtils.save(this.context, Config.CONTACT_BACKUP, new Gson().toJson(dataContact));
        }
    }

    protected void onPostExecute(Integer num) {
        super.onPostExecute(num);
        if (isCancelled() || context == null || num == -1) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AsyncTaskUpdateContact.this.listener != null) {
                    AsyncTaskUpdateContact.this.listener.onComplete(AsyncTaskUpdateContact.this.numberTotal, AsyncTaskUpdateContact.this.numberSuccess);
                }
            }
        }, AppConstant.TIME_DELAY_TASK);
    }
}
