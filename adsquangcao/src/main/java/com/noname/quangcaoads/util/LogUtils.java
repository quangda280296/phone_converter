package com.noname.quangcaoads.util;

import android.util.Log;

public class LogUtils {

    private static final boolean CAN_LOG = true;
    private static final String TAG = "XXX";

    public static void log(String message) {
        if (CAN_LOG && message != null) {
            Log.i(TAG, message);
        }
    }

    public static void log(boolean b) {
        if (CAN_LOG) {
            Log.i(TAG, Boolean.toString(b));
        }
    }

    public static void logBlue(String tag, String message) {
        if (CAN_LOG && tag != null && message != null) {
            Log.i(tag, message);
        }
    }

    public static void logRed(String tag, String message) {
        if (CAN_LOG && tag != null && message != null) {
            Log.e(tag, message);
        }
    }

}
