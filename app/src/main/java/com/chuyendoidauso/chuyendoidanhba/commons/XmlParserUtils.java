package com.chuyendoidauso.chuyendoidanhba.commons;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.models.AppInfo;
import com.chuyendoidauso.chuyendoidanhba.models.Data;
import com.chuyendoidauso.chuyendoidanhba.models.Network;
import com.google.gson.Gson;

import java.util.List;

import org.simpleframework.xml.core.Persister;

public class XmlParserUtils implements Config {
    private static final String TAG = "XmlParserUtils";

    public static List<Network> getRuleNetWork(Context context, Resources resources) {
        //String read = SharedReferenceUtils.read(context, Config.RULE_DATA);
        //if (!StringUtils.isNullOrEmpty(read)) {
        //    return new Gson().fromJson(read, Data.class).getNetworkList();
       // }
        String xmlContent = getXmlContent(resources, R.xml.data);
        Log.i(TAG, xmlContent);
        try {
            Data obj = new Persister().read(Data.class, xmlContent);
            SharedReferenceUtils.save(context, Config.RULE_DATA, new Gson().toJson(obj));
            return obj.getNetworkList();
        } catch (Exception e) {
            Log.e(TAG, "Error" + e.getMessage());
            return null;
        }
    }

    public static List<Network> getNetWork(Context context, Resources resources) {
        String xmlContent = getXmlContent(resources, R.xml.network);
        Log.i(TAG, xmlContent);
        try {
            Data obj = new Persister().read(Data.class, xmlContent);
            SharedReferenceUtils.save(context, Config.NETWORK, new Gson().toJson(obj));
            return obj.getNetworkList();
        } catch (Exception e) {
            Log.e(TAG, "Error" + e.getMessage());
            return null;
        }
    }

    public static List<AppInfo> getAppInfo(Context context, Resources resources) {
        String xmlContent = getXmlContent(resources, R.xml.list_app);
        Log.i(TAG, xmlContent);
        try {
            Data data =  new Persister().read(Data.class, xmlContent);
            SharedReferenceUtils.save(context, Config.NETWORK, new Gson().toJson(data));
            return data.getAppInfoList();
        } catch (Exception e) {
            Log.e(TAG, "Error" + e.getMessage());
            return null;
        }
    }

    private static String getXmlContent(Resources resources, int i) {
        StringBuilder stringBuilder = new StringBuilder("");
        XmlResourceParser xrp = resources.getXml(i);
        try {
            xrp.next();
            for (i = xrp.getEventType(); i != 1; i = xrp.next()) {
                if (i == 2) {
                    stringBuilder.append("<");
                    stringBuilder.append(xrp.getName());
                    stringBuilder.append(">");
                } else if (i == 3) {
                    stringBuilder.append("</");
                    stringBuilder.append(xrp.getName());
                    stringBuilder.append(">");
                } else if (i == 4) {
                    stringBuilder.append(xrp.getText());
                }
            }
        } catch (Exception resources2) {
            Log.e(TAG, "Error", resources2);
        }
        return stringBuilder.toString();
    }
}
