package com.noname.quangcaoads.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class SharedPreferencesCacheUtil {

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

    public static String getValue(Context context, String fileName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
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

    public static void setValue(Context context, String fileName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
