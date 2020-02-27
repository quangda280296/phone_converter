package com.noname.quangcaoads.model;

public class AdsKey {

    private AdmobKey admob;
    private StartAppKey start_apps;
    private FacebookKey facebook;

    public AdmobKey getAdmob() {
        return admob;
    }

    public void setAdmob(AdmobKey admob) {
        this.admob = admob;
    }

    public StartAppKey getStart_apps() {
        return start_apps;
    }

    public void setStart_apps(StartAppKey start_apps) {
        this.start_apps = start_apps;
    }

    public FacebookKey getFacebook() {
        return facebook;
    }

    public void setFacebook(FacebookKey facebook) {
        this.facebook = facebook;
    }
}
