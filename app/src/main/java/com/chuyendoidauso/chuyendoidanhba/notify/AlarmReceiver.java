package com.chuyendoidauso.chuyendoidanhba.notify;//package com.chuyendoidauso.chuyendoidanhba.notify;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.chuyendoidauso.chuyendoidanhba.R;
//import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
//import com.chuyendoidauso.chuyendoidanhba.commons.SharedReferenceUtils;
//import com.chuyendoidauso.chuyendoidanhba.models.Contact;
//import com.chuyendoidauso.chuyendoidanhba.models.HomeNetwork;
//import com.chuyendoidauso.chuyendoidanhba.models.InfoNumberChange;
//import com.chuyendoidauso.chuyendoidanhba.task.AsyncTaskGetContact;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import static com.chuyendoidauso.chuyendoidanhba.notify.ResidentNotificationHelper.NOTICE_ID_TYPE_0;
//
//public class AlarmReceiver extends BroadcastReceiver {
//
//    public HashMap<String, Boolean> hashMap = new HashMap<>();
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
////        // show toast
////        long calendarFrom = intent.getLongExtra("calendarFrom", 1L);
////        long calendarTo = intent.getLongExtra("calendarTo", 1L);
////        String key = String.valueOf(calendarFrom) + String.valueOf(calendarTo);
////        synchronized (hashMap) {
////            if (!hashMap.containsKey(key)) {
////                hashMap.put(key, false);
////            }
////            Toast.makeText(context, compare(calendarFrom, calendarTo)+ "AlarmReceiver" + hashMap.containsKey(key)  +":"+ hashMap.get(key), Toast.LENGTH_SHORT).show();
////
////            if (compare(calendarFrom, calendarTo) && hashMap.containsKey(key) && !hashMap.get(key)) {
////                hashMap.remove(key);
////                hashMap.put(key, true);
////                getListDateWithPhoneNumber(context);
////            }
////        }
//    }
//
//    private boolean compare(long currentTimeFrom, long currentTimeTo) {
//        try {
//            Date currentDateForm = new Date(currentTimeFrom);
//            Date currentDateTo = new Date(currentTimeTo);
//            SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//            System.out.println("dateFormatWithZone" + dateFormatWithZone.format(currentDateForm));
//            if (new Date().after(currentDateForm) && new Date().before(currentDateTo)) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private void getListDateWithPhoneNumber(final Context context) {
//        new AsyncTaskGetContact(context.getContentResolver(), new AsyncTaskGetContact.GetContactListener() {
//            @Override
//            public void onGet(final List<Contact> list) {
//
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
//                                if (!contact.isValidate) {
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
//
//                        if (list != null) {
//                            ResidentNotificationHelper.sendResidentNoticeType0(context, context.getString(R.string.app_name), "Hôm nay bạn có " + String.valueOf(list.size()) + " thuê bao 11 số tới lịch cần chuyển", R.mipmap.ic_launcher);
//                        }
//                        ResidentNotificationHelper.clearNotification(context, NOTICE_ID_TYPE_0);
//                        //ResidentNotificationHelper.sendResidentNoticeType0(context, context.getString(R.string.app_name), "Hôm nay bạn có " + String.valueOf(2) + " thuê bao 11 số tới lịch cần chuyển", R.mipmap.ic_launcher);
//                    }
//                }.execute();
//
//            }
//        }).execute(new String[0]);
//    }
//
//    private ArrayList<HomeNetwork> getListDataHomeNetwork(ArrayList<HomeNetwork> list) {
//        ArrayList homeNetworkList = new ArrayList<>();
//        for (HomeNetwork homeNetwork : list) {
//            homeNetworkList.add(homeNetwork);
//        }
//        return homeNetworkList;
//    }
//
//    private boolean isCheckDate(Contact contact, ArrayList<HomeNetwork> list) {
//        for (HomeNetwork homeNetwork : list) {
//            if (contact != null && homeNetwork != null && !TextUtils.isEmpty(homeNetwork.phoneOld)) {
//                if (getNumberStartIsdn(contact.getIsdn()).startsWith(homeNetwork.phoneOld.substring(1, homeNetwork.phoneOld.length())) && a(homeNetwork.date)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private String getNumberStartIsdn(String isdn) {
//        if (!TextUtils.isEmpty(isdn) && isdn.length() > 5) {
//            String data = isdn.replace("84", "").replace("+84", "");
//            return data.substring(1, data.length());
//        }
//        return "";
//    }
//
//    private boolean a(String date) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            Date strDate = sdf.parse(date);
//            if (new Date().after(strDate)) {
//                return true;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//}