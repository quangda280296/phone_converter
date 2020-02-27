package com.chuyendoidauso.chuyendoidanhba.commons;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class DeviceUtil {
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
