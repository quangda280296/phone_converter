package com.chuyendoidauso.chuyendoidanhba.commons;

import android.text.TextUtils;

public class ReplaceUtils {

    public static String replaceAll(String content){
        if(TextUtils.isEmpty(content)) return "";
        return content.replaceAll(" ","")
                .replaceAll("-","")
                .replaceAll("_","");

    }
}
