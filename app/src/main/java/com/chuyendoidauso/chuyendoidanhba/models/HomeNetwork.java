package com.chuyendoidauso.chuyendoidanhba.models;

import com.google.gson.annotations.SerializedName;

public class HomeNetwork {

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONTENT = 2;

    @SerializedName("dau_so_cu")
    public String phoneOld;
    @SerializedName("dau_so_moi")
    public String phoneNew;
    @SerializedName("date")
    public String date;
    public int TYPE = TYPE_CONTENT;
    public String title;
    public boolean isCheckNumber;

    public HomeNetwork(int TYPE, String title) {
        this.TYPE = TYPE;
        this.title = title;
    }
}
