package com.noname.quangcaoads.model;

public class ControlAds {

    private AdsKey list_key;
    private ActiveAds active_ads;
    private ConfigShow config_show;

    public AdsKey getList_key() {
        return list_key;
    }

    public void setList_key(AdsKey list_key) {
        this.list_key = list_key;
    }

    public ActiveAds getActive_ads() {
        return active_ads;
    }

    public void setActive_ads(ActiveAds active_ads) {
        this.active_ads = active_ads;
    }

    public ConfigShow getConfig_show() {
        return config_show;
    }

    public void setConfig_show(ConfigShow config_show) {
        this.config_show = config_show;
    }
}
