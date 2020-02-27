package com.chuyendoidauso.chuyendoidanhba.notify;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Toast;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.commons.DataUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.ReplaceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
import com.chuyendoidauso.chuyendoidanhba.commons.StringUtils;
import com.chuyendoidauso.chuyendoidanhba.models.Contact;
import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
import com.chuyendoidauso.chuyendoidanhba.task.AsyncTaskGetContact;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.chuyendoidauso.chuyendoidanhba.notify.ResidentNotificationHelper.NOTICE_ID_TYPE_0;

public class SchedulingService extends IntentService {
    private Context mContext;

    public SchedulingService() {
        super(SchedulingService.class.getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        //Toast.makeText(mContext,  "SchedulingService" , Toast.LENGTH_SHORT).show();
//        long calendarFrom = intent.getLongExtra("calendarFrom", 1L);
//        long calendarTo = intent.getLongExtra("calendarTo", 1L);
//        int notificationId = intent.getIntExtra("notificationId", -1);
//        boolean isStart = intent.getBooleanExtra("isStart", false);
//        if (notificationId != -1) {
//            String key = SchedulingService.class.getName() + String.valueOf(notificationId);
//            boolean isNotify = SharedReferenceUtils.getBoolean(mContext, key);
//          //  Toast.makeText(mContext, compare(calendarFrom, calendarTo) + "SchedulingService" + isNotify, Toast.LENGTH_SHORT).show();
//
//          //  if (compare(calendarFrom, calendarTo) && !isNotify) {
//            //    getListDateWithPhoneNumber(mContext, key, isStart);
//           // }
//        }
    }

    private boolean compare(long currentTimeFrom, long currentTimeTo) {
        try {
            Date currentDateForm = new Date(currentTimeFrom);
            Date currentDateTo = new Date(currentTimeTo);
            SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            //System.out.println("dateFormatWithZone" + dateFormatWithZone.format(currentDateForm));
            if (new Date().after(currentDateForm) && new Date().before(currentDateTo)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private void getListDateWithPhoneNumber(final Context context, final String key, final boolean isStart) {
        //System.out.println("StaticFieldLeak" + "getListDateWithPhoneNumber");
        // Gson gson = new Gson();
        // InfoNumberChange infoNumberChange = gson.fromJson(loadJSONFromAsset(), InfoNumberChange.class);

        // System.out.println("StaticFieldLeak" + infoNumberChange);
//        ExecutorService es = Executors.newFixedThreadPool(5);
//        es.execute(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<Contact> list = new ArrayList();
//                Cursor query = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//                if (query != null) {
//                    while (query.moveToNext()) {
//                        String name = query.getString(query.getColumnIndex("display_name"));
//                        String phone = query.getString(query.getColumnIndex("data1"));
//                        String phoneFix = ReplaceUtils.replaceAll(phone);
//                        String isChangeIsdn = isChangeIsdn(phoneFix);
//                        if (!TextUtils.isEmpty(phoneFix) && phoneFix.length() > 9) {
//                            if (!StringUtils.isNullOrEmpty(isChangeIsdn)) {
//                                Contact contact = new Contact(name, phoneFix);
//                                contact.setPrefixOld(ReplaceUtils.replaceAll(isChangeIsdn));
//                                contact.setPrefixNew(ReplaceUtils.replaceAll(DataUtils.getValue(isChangeIsdn)));
//
//                                //System.out.println("Data:setPrefixOld" +contact.getPrefixOld());
//                                list.add(contact);
//                            }
//                        }
//                    }
//                    query.close();
//                    //Collections.sort(list, new ComparatorData());
//                }
//                System.out.println("StaticFieldLeak" + list);
//            }
//        });

//        new AsyncTaskGetContact(context.getContentResolver(), new AsyncTaskGetContact.GetContactListener() {
//            @Override
//            public void onGet(final List<Contact> list) {
////                System.out.println("StaticFieldLeak:" + list);
//                new AsyncTask<ArrayList<HomeNetwork>, Void, List<Contact>>() {
//                    @Override
//                    protected List<Contact> doInBackground(ArrayList<HomeNetwork>... voids) {
//                        InfoNumberChange responseBody = SharedReferenceUtils.get(context, InfoNumberChange.class.getName(), InfoNumberChange.class);
//                        if (responseBody != null) {
//                            ArrayList homeNetworkList = new ArrayList<>();
//                            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vietTel));
//                            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.mobilePhone));
//                            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vinaPhone));
//                            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.vietnamobile));
//                            homeNetworkList.addAll(getListDataHomeNetwork(responseBody.gmobile));
//                            List<Contact> originContactList = list.subList(0,
//                                    list.size() > AppConstant.INDEX_NUMBER_CONVERT
//                                            ? AppConstant.INDEX_NUMBER_CONVERT : list.size());
//
//                            List arrayList = new ArrayList();
//                            for (Contact contact : originContactList) {
//                                contact.isValidate = isCheckDate(contact, homeNetworkList);
//                                if (contact.isValidate) {
//                                    arrayList.add(contact);
//                                }
//                            }
//                            return arrayList;
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(List<Contact> list) {
//                        super.onPostExecute(list);
//                        System.out.println("StaticFieldLeak" + list);
//                        ResidentNotificationHelper.clearNotification(context, NOTICE_ID_TYPE_0);
//                        if (list != null && list.size() > 0) {
//                            SharedReferenceUtils.saveBoolean(mContext, key, true);
        //  ResidentNotificationHelper.sendResidentNoticeType0(context, context.getString(R.string.app_name), "Hôm nay bạn có " + String.valueOf(1) + " thuê bao 11 số tới lịch cần chuyển", R.mipmap.ic_launcher);
//                            if (isStart) {
//                                // ResidentNotificationHelper.clearNotification(context, NOTICE_ID_TYPE_0);
//                            }
//                        } else {
////                            SharedReferenceUtils.saveBoolean(mContext, key, true);
//                            getListDateWithPhoneNumber(context, key, isStart);
//
//                            new Handler().postDelayed(new Runnable() {
//                                @SuppressLint("StaticFieldLeak")
//                                @Override
//                                public void run() {
//                                    new AsyncTask<Void, Void, InfoNumberChange>() {
//                                        @Override
//                                        protected InfoNumberChange doInBackground(Void... voids) {
//                                            Gson gson = new Gson();
//                                            InfoNumberChange infoNumberChange = gson.fromJson(loadJSONFromAsset(), InfoNumberChange.class);
//                                            return infoNumberChange;
//                                        }
//
//                                        @Override
//                                        protected void onPostExecute(InfoNumberChange infoNumberChange) {
//                                            super.onPostExecute(infoNumberChange);
//                                            System.out.println("StaticFieldLeak" + infoNumberChange);
//                                        }
//                                    }.execute();
//                                }
//                            }, AppConstant.TIME_DELAY_SPLASH);
//                        }
//                    }
//                }.execute();
//
//            }
//        }).execute(new String[0]);
    }

    private String isChangeIsdn(String str) {
        str = StringUtils.formatStandard(str);
        for (String str2 : DataUtils.getPrefixList()) {
            //System.out.println("DKMM:" + str + "x" + str2);
            if (str.startsWith(str2)) {
                return str2;
            }
        }
        return null;
    }

//    public String loadJSONFromAsset() {
//        String json;
//        try {
//            InputStream is = getAssets().open("list_dau_so.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

    private ArrayList<HomeNetwork> getListDataHomeNetwork(ArrayList<HomeNetwork> list) {
        ArrayList homeNetworkList = new ArrayList<>();
        for (HomeNetwork homeNetwork : list) {
            homeNetworkList.add(homeNetwork);
        }
        return homeNetworkList;
    }

    private boolean isCheckDate(Contact contact, ArrayList<HomeNetwork> list) {
        for (HomeNetwork homeNetwork : list) {
            if (contact != null && homeNetwork != null && !TextUtils.isEmpty(homeNetwork.phoneOld)) {
                if (getNumberStartIsdn(contact.getIsdn()).startsWith(homeNetwork.phoneOld.substring(1, homeNetwork.phoneOld.length()).trim()) && a(homeNetwork.date)) {
                    return true;
                }
            }
        }
        return false;
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
}