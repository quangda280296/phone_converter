package com.chuyendoidauso.chuyendoidanhba.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AdsModel {
    @SerializedName("admob")
    public Ads admob;
    @SerializedName("facebook")
    public Ads adsFaceBook;
    @SerializedName("open_show_ads")
    public int openShowAds;
    @SerializedName("message_home")
    public String messageHome;
    @SerializedName("message_lich_chuyen_doi")
    public String messageLCD;
    @SerializedName("close_show_ads")
    public int closeShowAds;
    @SerializedName("time_start_show_ads")
    public int timeStartShowSds;
    @SerializedName("offset_show_ads")
    public int offsetShowSds;
    @SerializedName("time_notify")
    public ArrayList<TimeNotify> timeNotify;
    @SerializedName("link_share")
    public String linkShare;
    @SerializedName("limit_show")
    public int limitShow;
    @SerializedName("force_internet")
    public int forceInternet;
    @SerializedName("update")
    private Update update;
    @SerializedName("notify")
    private ArrayList<Notify> notify = null;
    @SerializedName("more_app")
    private ArrayList<MoreApp> moreApp = null;
    @SerializedName("link_lich_chuyen_doi")
    public String linkLichChuyenDoi;
    @SerializedName("ratio_show_banner_admob")
    public int ratioShowBannerAdmob;
    @SerializedName("thumbai")
    public Thumbai thumbai;
    @SerializedName("ads_network")
    public String adsNetwork;
}
