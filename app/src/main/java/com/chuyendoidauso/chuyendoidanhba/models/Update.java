package com.chuyendoidauso.chuyendoidanhba.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Update {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("des")
    @Expose
    public String des;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("last_version")
    @Expose
    public String lastVersion;
    @SerializedName("max_show_of_version")
    @Expose
    public int maxShowOfVersion;
    @SerializedName("offset_show_update")
    @Expose
    public int offsetShowUpdate;
}
