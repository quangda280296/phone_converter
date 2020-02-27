package com.chuyendoidauso.chuyendoidanhba.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InfoNumberChange {

    @SerializedName("Viettel")
    public ArrayList<HomeNetwork> vietTel;
    @SerializedName("MobiFone")
    public ArrayList<HomeNetwork> mobilePhone;
    @SerializedName("VinaPhone")
    public ArrayList<HomeNetwork> vinaPhone;
    @SerializedName("Gmobile")
    public ArrayList<HomeNetwork> gmobile;
    @SerializedName("Vietnamobile")
    public ArrayList<HomeNetwork> vietnamobile;
}
