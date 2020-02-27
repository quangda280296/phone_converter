package com.chuyendoidauso.chuyendoidanhba.models;

import java.io.Serializable;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "appinfo")
public class AppInfo implements Serializable {
    @Element(required = false)
    private String name;
    @Element(required = false)
    private String packageName;
    @Element(required = false)
    private String url;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }
}
