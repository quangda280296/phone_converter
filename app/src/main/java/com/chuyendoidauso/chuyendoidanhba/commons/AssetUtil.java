package com.chuyendoidauso.chuyendoidanhba.commons;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetUtil {

    public static String loadJSONFromAsset(Context context,String fileNameJson) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileNameJson);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
