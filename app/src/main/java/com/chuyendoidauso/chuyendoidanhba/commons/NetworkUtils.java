package com.chuyendoidauso.chuyendoidanhba.commons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jacky on 11/23/17.
 */

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCheckNetwork(Context mContext) {
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            return false;
        }
        return true;
    }

}
