package com.chuyendoidauso.chuyendoidanhba.helper;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract.Data;
import android.util.Log;

import java.util.ArrayList;

public class ContactHelper {
    private static final String TAG = "ContactHelper";

    public static boolean updateContactList(Context context, String str, String str2, String str3) {
        if (context == null) return false;
        String str4 = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[name, oldPhoneNumber, newPhoneNumber] = [");
        stringBuilder.append(str);
        stringBuilder.append(", ");
        stringBuilder.append(str2);
        stringBuilder.append(", ");
        stringBuilder.append(str3);
        stringBuilder.append("]");
        Log.i(str4, stringBuilder.toString());

        try {
            ArrayList<ContentProviderOperation> list = new ArrayList<>();
            String[] strArr = new String[]{str2};
            list.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI).withSelection("data1 = ? ", strArr).withValue("data1", str3).build());
            try {
                context.getContentResolver().applyBatch("com.android.contacts", list);
            } catch (OperationApplicationException e) {
                e.printStackTrace();
                return false;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
