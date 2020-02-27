package com.chuyendoidauso.chuyendoidanhba.commons;

import android.content.Context;
import android.content.res.Resources;

import com.chuyendoidauso.chuyendoidanhba.models.Network;
import com.chuyendoidauso.chuyendoidanhba.models.Rule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {
    private static final Map<String, String> mapPrefix = new HashMap();
    private static final List<String> prefixList = new ArrayList();

    public static Void initMapPrefix(Context context, Resources resources) {
        for (Network rules : XmlParserUtils.getRuleNetWork(context, resources)) {
            for (Rule rule : rules.getRules()) {
                mapPrefix.put(StringUtils.formatPrefix(rule.getSrc()), StringUtils.formatPrefix(rule.getDest()));
                prefixList.add(StringUtils.formatPrefix(rule.getSrc()));
            }
        }
        return null;
    }

    public static Map<String, String> getMapPrefix() {
        return mapPrefix;
    }

    public static String getValue(String str) {
        return mapPrefix.containsKey(str) ? mapPrefix.get(str) : str;
    }

    public static List<String> getPrefixList() {
        return prefixList;
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
