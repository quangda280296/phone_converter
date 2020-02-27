package com.chuyendoidauso.chuyendoidanhba.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreApp {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("url_store")
    @Expose
    private String urlStore;
    @SerializedName("package")
    @Expose
    private String _package;
}
