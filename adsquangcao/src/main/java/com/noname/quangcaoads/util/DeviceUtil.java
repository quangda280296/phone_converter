package com.noname.quangcaoads.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;


public class DeviceUtil {

    public static int getHeightScreen(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();

        return height;
    }

    public static int getWidthScreen(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        return width;
    }

    public static void hideKeyboard(Activity activity, EditText myEditText) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static String getDeviceId(Context context) {
        try {
            String idDevice = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return idDevice;
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static String getIMEI(Context context) {
        try {
            TelephonyManager mngr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mngr.getDeviceId();
            if (imei == null)
                imei = "";
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAdvertisingId(Context context) {
        try {
            String advertisingId = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
            if (advertisingId == null)
                advertisingId = "";
            return advertisingId;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTelcoCode(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSimOperator();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTelcoName(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String name = telephonyManager.getSimOperatorName();
            if (name == null)
                name = "";
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String network = "";
        if (mWifi.isConnected()) {
            network = "wifi";
        } else {
            network = "3g";
        }
        return network;
    }

    public static String getVersionOS() {
        return Build.VERSION.RELEASE + " SDK: "
                + Build.VERSION.SDK_INT;
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String deviceName = "";
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            }
            if (manufacturer.equalsIgnoreCase("HTC")) {
                return "HTC " + model;
            }
            deviceName = capitalize(manufacturer) + " " + model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceName;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static String getVersionAppName(Context context) {
        String versionName = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e1) {
            versionName = "";
        }
        return versionName;
    }
}
