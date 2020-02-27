package com.chuyendoidauso.chuyendoidanhba.models;

import android.text.TextUtils;
import android.widget.TextView;

import com.chuyendoidauso.chuyendoidanhba.commons.StringUtils;

import java.io.Serializable;

public class Contact implements Serializable {
    public boolean isShow = true;
    private boolean checked = true;
    private String isdn;
    private String isdnOld;
    private String name;
    private String prefixNew;
    private String prefixOld;
    public boolean isValidate;
    public String date;
    public String contactType = "phone";

    public Contact(String str, String str2, String isdnOld) {
        this.name = str;
        this.isdn = str2;
        this.isdnOld = isdnOld;
    }

    public Contact(String str, String str2) {
        this.name = str;
        this.isdn = str2;
    }

    public Contact() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getIsdn() {
        return this.isdn;
    }

    public String getIndsOld() {
        return this.isdnOld;
    }

    public void setIsdn(String str) {
        this.isdn = str;
    }

    public String getPrefixOld() {
        return this.prefixOld;
    }

    public void setPrefixOld(String str) {
        this.prefixOld = str;
    }

    public String getPrefixNew() {
        return this.prefixNew;
    }

    public void setPrefixNew(String str) {
        this.prefixNew = str;
    }

    public String getSuffix() {
        if (!TextUtils.isEmpty(getPrefixOld()) && getPrefixOld().length() > 1) {
            if (getIsdn().startsWith("+84")) {
                return "+84" + getPrefixOld().substring(1);
            }
            if (getIsdn().startsWith("84")) {
                return "84" + getPrefixOld().substring(1);
            }
        }
        return getPrefixOld();
    }

    public String getSuffixNumber() {
        return getIsdn().replace(getSuffix(), "");
    }

    public String getNetwork() {
        if (StringUtils.isViettel(this.isdn)) {
            return "Viettel";
        }
        if (StringUtils.isGMobile(this.isdn)) {
            return "GMobile";
        }
        if (StringUtils.isMobifone(this.isdn)) {
            return "Mobifone";
        }
        if (StringUtils.isVinaphone(this.isdn)) {
            return "VinaPhone";
        }

        return "VNMobile";
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean z) {
        this.checked = z;
    }

    public String getNewIsdn() {
        return getPrefixNew().trim() + getSuffixNumber().trim();
    }

    public Contact getNew(Contact contact) {
        Contact c = new Contact();
        c.isShow = contact.isShow;
        c.checked = contact.checked;
        c.isdn = contact.isdn;
        c.isdnOld = contact.isdnOld;
        c.name = contact.name;
        c.prefixNew = contact.prefixNew;
        c.prefixOld = contact.prefixOld;
        c.isValidate = contact.isValidate;
        c.date = contact.date;
        c.contactType = contact.contactType;
        return c;
    }
}
