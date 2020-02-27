package com.noname.quangcaoads.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

public class SharedPreferencesGlobalUtil {

    public static void saveLong(Context context, String key, long value) {
        if (context == null) return;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static long getLong(Context context, String key) {
        if (context == null) return 0;
        long value = 0;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getLong(key, 0);
        }
        return value;
    }

    public static void saveBoolean(Context context, String key, Boolean value) {
        if (context == null) return;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static boolean getBoolean(Context context, String key) {
        if (context == null) return false;
        boolean value = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getBoolean(key, false);
        }
        return value;
    }

    private static String encode(String str) throws Exception {
        byte[] data = str.getBytes("UTF-8");
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    private static String decode(String base64) throws Exception {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String str = new String(data, "UTF-8");
        return str;
    }

    public static String getValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("basesystem_sys", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, null);
        if (value != null) {
            try {
                value = decode(value);
            } catch (Exception e) {
                e.printStackTrace();
                value = null;
            }
        }
        return value;

    }

    public static void setValue(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("basesystem_sys", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        if (value != null) {
            try {
                value = encode(value);
                editor.putString(key, value);
            } catch (Exception e) {
                editor.remove(key);
            }
        } else {
            editor.remove(key);
        }
        editor.commit();
    }
}
