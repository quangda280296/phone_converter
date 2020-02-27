package com.noname.quangcaoads.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class FileCacheUtil {

    public static String encode(String str) throws Exception {
        byte[] data = str.getBytes("UTF-8");
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    public static String decode(String base64) throws Exception {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String str = new String(data, "UTF-8");
        return str;
    }

    public static void writeToFile(Context context, String fileName, String body) {
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/.thumbnails/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File myFile = new File(dir, fileName);
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(myFile);
            fos.write(body.getBytes());
            fos.close();
        } catch (Exception e) {
        }

        try {
            SharedPreferencesCacheUtil.setValue(context, fileName, fileName, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context context, String fileName) {
        String strText = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/.thumbnails/");
            File file = new File(dir, fileName);
            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            strText = text.toString();
        } catch (Exception e) {
        }
        try {
            if (TextUtils.isEmpty(strText))
                strText = SharedPreferencesCacheUtil.getValue(context, fileName, fileName);
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(strText))
            strText = "";
        return strText;
    }

    public static void saveConfig(Context context, String strConfig) {
        try {
            writeToFile(context, ".fs34k9w5540opdmt", encode(strConfig));
        } catch (Exception e) {
        }
    }

    public static String loadConfig(Context context) {
        try {
            return decode(readFromFile(context, ".fs34k9w5540opdmt"));
        } catch (Exception e) {
            return "";
        }
    }

    public static void setTimeShowPopupAdsFullScreen(Context context) {
        try {
            long timeNow = System.currentTimeMillis();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("timeShow", timeNow);
            writeToFile(context, "TimeShowFull", jsonObject.toString());
        } catch (Exception e) {
        }
    }

    public static long getTimeShowPopupAdsFullScreen(Context context) {
        try {
            String str = readFromFile(context, "TimeShowFull");
            JSONObject jsonObject = new JSONObject(str);
            return jsonObject.getLong("timeShow");
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setTimeShowBannerAds(Context context) {
        try {
            long timeNow = System.currentTimeMillis();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("timeShow", timeNow);
            writeToFile(context, "TimeShowSmall", jsonObject.toString());
        } catch (Exception e) {
        }
    }

    public static long getTimeShowBannerAds(Context context) {
        try {
            String str = readFromFile(context, "TimeShowSmall");
            JSONObject jsonObject = new JSONObject(str);
            return jsonObject.getLong("timeShow");
        } catch (Exception e) {
            return 0;
        }
    }

}
