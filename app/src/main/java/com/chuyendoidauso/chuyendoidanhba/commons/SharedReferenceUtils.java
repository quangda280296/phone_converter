package com.chuyendoidauso.chuyendoidanhba.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedReferenceUtils {

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

    public static void save(Context context, String key, String value) {
        if (context == null) return;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String read(Context context, String key) {
        if (context == null) return "";
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    public static <T> void put(Context context, String key, T value) {
        if (value == null || context == null) {
            return;
        }
        Type type = new TypeToken<T>() {
        }.getType();
        String json = new Gson().toJson(value, type);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, json);
        editor.commit();
    }

    public static <T> T get(Context context, String key, Class<T> type) {
        if (context == null) return null;
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, type);
        } else {
            return null;
        }
    }
}
