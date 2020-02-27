package com.chuyendoidauso.chuyendoidanhba.commons;

import android.widget.EditText;

import java.util.regex.Pattern;

public class StringUtils {

//    private static final String REGEX_PREFIX_GMOBILE = "(099|0199)\\d{0,7}";
//    private static final String REGEX_PREFIX_MOBIFONE = "(0120|0121|0122|0126|0128|090|093|089)\\d{0,7}";
//    private static final String REGEX_PREFIX_VIETNAMMOBILE = "(092|0188|0186)\\d{0,7}";
//    private static final String REGEX_PREFIX_VIETTEL = "(0162|0163|0164|0165|0166|0167|0168|0169|096|097|098|0868)\\d{0,7}";
//    private static final String REGEX_PREFIX_VINAPHONE = "(0123|0124|0125|0127|0129|091|094|088)\\d{0,7}";

    public static final String EVENT_PARAM_VALUE_NO = "0";

    public static String REGEX_GMOBILE = "(099|0199)\\d{7}";
    public static String REGEX_MOBIFONE = "(0120|0121|0122|0126|0128|090|093|089)\\d{7}";
    public static String REGEX_VIETNAMMOBILE = "(01864|01863|01884|01885|01886|01887|01888|01889|01883|" +
            "01882|01865|01866|01867|01868|01869)\\d{7}";
    public static String REGEX_VIETTEL = "(0162|0163|0164|0165|0166|0167|0168|0169|096|097|098|0868)\\d{7}";
    public static String REGEX_VINAPHONE = "(0123|0124|0125|0127|0129|091|094|088)\\d{7}";

    public static boolean isViettel(String str) {
        return Pattern.compile(REGEX_VIETTEL).matcher(formatStandard(str)).matches();
    }

    public static boolean isMobifone(String str) {
        return Pattern.compile(REGEX_MOBIFONE).matcher(formatStandard(str)).matches();
    }

    public static boolean isVinaphone(String str) {
        return Pattern.compile(REGEX_VINAPHONE).matcher(formatStandard(str)).matches();
    }

    public static boolean isGMobile(String str) {
        return Pattern.compile(REGEX_GMOBILE).matcher(formatStandard(str)).matches();
    }

    public static boolean isVietnamMobile(String str) {
        return Pattern.compile(REGEX_VIETNAMMOBILE).matcher(formatStandard(str)).matches();
    }

    private static boolean isNetwork(String str, String str2) {
        return Pattern.compile(str2).matcher(formatStandard(str)).matches();
    }

//    public static String getPrefixNetwork(String str) {
//        if (isNetwork(str, REGEX_PREFIX_VIETTEL)) {
//            return "Viettel";
//        }
//        if (isNetwork(str, REGEX_PREFIX_GMOBILE)) {
//            return "GMobile";
//        }
//        if (isNetwork(str, REGEX_PREFIX_MOBIFONE)) {
//            return "MobiFone";
//        }
//        if (isNetwork(str, REGEX_PREFIX_VIETNAMMOBILE)) {
//            return "VietnamMobile";
//        }
//        return isNetwork(str, REGEX_PREFIX_VINAPHONE) ? "Vinaphone" : "Other";
//    }

    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().isEmpty();
        }
        return obj instanceof EditText ? isNullOrEmpty(((EditText) obj).getText().toString()) : null;
    }

    public static String formatIsdn(String str) {
        if (str != null) {
            if (!str.trim().isEmpty()) {
                str = str.trim().replaceAll(" ", "").replaceAll("\\D+", "");
                if (str.startsWith(EVENT_PARAM_VALUE_NO)) {
                    return str.substring(1);
                }
                if (str.startsWith("84")) {
                    return str.substring(2);
                }
                if (str.startsWith("840")) {
                    return str.substring(3);
                }
                if (str.startsWith("+84")) {
                    return str.substring(3);
                }
                return str.startsWith("+840") ? str.substring(4) : str;
            }
        }
        return "";
    }

    public static String formatMsisdn(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("84");
        stringBuilder.append(formatIsdn(str));
        return stringBuilder.toString();
    }

    public static String formatStandard(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EVENT_PARAM_VALUE_NO);
        stringBuilder.append(formatIsdn(str));
        return stringBuilder.toString();
    }

    public static String formatPrefix(String str) {
        if (str.startsWith(EVENT_PARAM_VALUE_NO)) {
            return str.trim();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EVENT_PARAM_VALUE_NO);
        stringBuilder.append(str.trim());
        return stringBuilder.toString();
    }
}
