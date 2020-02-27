package com.chuyendoidauso.chuyendoidanhba.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notify {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("url_web_view")
    @Expose
    public String urlWebView;
}
