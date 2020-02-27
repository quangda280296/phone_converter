package com.chuyendoidauso.chuyendoidanhba.commons;

import android.widget.Toast;

import com.chuyendoidauso.chuyendoidanhba.AppNumberChanger;

public class ToastUtils {

    private static Toast toast;

    public static void showToastShort(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AppNumberChanger.newInstance(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showToastShort(int resId) {
        showToastShort(AppNumberChanger.newInstance().getString(resId));
    }
}
