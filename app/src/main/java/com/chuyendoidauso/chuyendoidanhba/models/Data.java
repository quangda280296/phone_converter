package com.chuyendoidauso.chuyendoidanhba.models;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "root")
public class Data {
    @ElementList(inline = true, name = "appinfo", required = false)
    private List<AppInfo> appInfoList;
    @ElementList(inline = true, name = "network", required = false)
    private List<Network> networkList;

    public List<Network> getNetworkList() {
        if (this.networkList == null) {
            this.networkList = new ArrayList();
        }
        return this.networkList;
    }

    public void setNetworkList(List<Network> list) {
        this.networkList = list;
    }

    public List<AppInfo> getAppInfoList() {
        return this.appInfoList;
    }

    public void setAppInfoList(List<AppInfo> list) {
        this.appInfoList = list;
    }
}
