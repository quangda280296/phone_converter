package com.noname.quangcaoads.ads;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.noname.quangcaoads.model.ControlAds;
import com.noname.quangcaoads.util.FileCacheUtil;

public class AdsFullScreen {

    public static ControlAds controlAds;
    private static String ads_network = "";
    private static AdmobUtils admobUtils;
    private static FacebookAdsUtils facebookAdsUtils;
    private static StartAppUtils startAppUtils;
    private static Gson gson;

    private static long timeShowOld = 0, timeCreate = 0;
    private static int timeDelayAds = 0, firstDelayAds = 0;
    private static boolean firstShow = false;

    private static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }

    public static void initiate(Context context) {
        timeShowOld = 0;
        timeDelayAds = 0;
        firstDelayAds = 0;
        firstShow = false;
        timeCreate = System.currentTimeMillis();
        try {
            getGson();
            String strConfig = FileCacheUtil.loadConfig(context);
            controlAds = gson.fromJson(strConfig, ControlAds.class);
            ads_network = controlAds.getActive_ads().getPopup();
            if (ads_network == null)
                ads_network = "";
        } catch (Exception e) {
            ads_network = "";
        }

        // Load admob ads
        try {
            if (ads_network.equals("admob")) {
                admobUtils = AdmobUtils.newInstance(context.getApplicationContext());
                admobUtils.initiate(controlAds.getList_key().getAdmob().getPopup());
            }
        } catch (Exception e) {
        }

        // Load facebook ads
        try {
            if (ads_network.equals("facebook")) {
                facebookAdsUtils = FacebookAdsUtils.newInstance(context.getApplicationContext());
                facebookAdsUtils.initiate(controlAds.getList_key().getFacebook().getPopup());
            }
        } catch (Exception e) {
        }

        // Load facebook ads
        try {
            if (ads_network.equals("start_apps")) {
                startAppUtils = StartAppUtils.newInstance(context.getApplicationContext());
                startAppUtils.initiate(controlAds.getList_key().getStart_apps().getDevid(),
                        controlAds.getList_key().getStart_apps().getAppid());
            }
        } catch (Exception e) {
        }
    }

    public static void showAds(final Context context) {
        showAds(context, null);
    }

    public static void showAds(final Context context, CallbackAdsHDV callbackAdsHDV) {
        runAds(context, callbackAdsHDV);
//        try {
//            if (controlAds == null) {
//                try {
//                    getGson();
//                    String strConfig = FileCacheUtil.loadConfig(context);
//                    controlAds = gson.fromJson(strConfig, ControlAds.class);
//                    ads_network = controlAds.getActive_ads().getPopup();
//                    if (TextUtils.isEmpty(ads_network))
//                        return;
//                } catch (Exception e) {
//                    return;
//                }
//            }
//            if (timeDelayAds == 0) {
//                timeDelayAds = controlAds.getConfig_show().getOffset_show_popup();
//            }
//            if (firstDelayAds == 0) {
//                firstDelayAds = controlAds.getConfig_show().getTime_start_popup();
//            }
//            try {
//                if (controlAds != null) {
//                    long timeNow = System.currentTimeMillis();
//                    if (timeShowOld == 0)
//                        timeShowOld = FileCacheUtil.getTimeShowPopupAdsFullScreen(context);
//                    if (!firstShow) {
//                        if ((timeNow - timeCreate) > (firstDelayAds * 1000)
//                                && (timeNow - timeShowOld) > (timeDelayAds * 1000)) {
//
//                            firstShow = true;
//                        }
//                    } else {
//                        if ((timeNow - timeShowOld) > (timeDelayAds * 1000)) {
//                            runAds(context, callbackAdsHDV);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static void runAds(final Context context, final CallbackAdsHDV callbackAdsHDV) {
        if (!isScreenOn(context)) {
            destroy();
            return;
        }

        saveTimeShow(context);
        if (ads_network.equals("admob")) {
            admobUtils.showAds(new AdmobUtils.AdmobUtilsListener() {
                boolean flagClickAds = false;

                @Override
                public void onAdsOpened() {
                }

                @Override
                public void onAdsClicked() {
                    flagClickAds = true;
                }

                @Override
                public void onAdsClosed() {
                    try {
                        if (!flagClickAds) {
                            if (callbackAdsHDV != null) {
                                callbackAdsHDV.onNotClick();
                            }
                        } else {
                            if (callbackAdsHDV != null) {
                                callbackAdsHDV.onAdsClicked();
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }

        if (ads_network.equals("facebook")) {
            facebookAdsUtils.showAds(new FacebookAdsUtils.FacebookAdsUtilsListener() {
                boolean flagClickAds = false;

                @Override
                public void onAdsOpened() {
                }

                @Override
                public void onAdsClicked() {
                    flagClickAds = true;
                }

                @Override
                public void onAdsClosed() {
                    try {
                        if (!flagClickAds) {
                            if (callbackAdsHDV != null) {
                                callbackAdsHDV.onNotClick();
                            }
                        } else {
                            if (callbackAdsHDV != null) {
                                callbackAdsHDV.onAdsClicked();
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }

        if (ads_network.equals("start_apps")) {
            startAppUtils.showAds(new StartAppUtils.OnStartAppListener() {
                boolean flagClickAds = false;

                @Override
                public void onStartAppClickListener() {
                    flagClickAds = true;
                }

                @Override
                public void onStartAppDisplayed() {
                }

                @Override
                public void onStartAppHidden() {
                }

                @Override
                public void onStartAppNotDisplayed() {
                }
            });
        }
    }

    public static void loadAdsFacebook(Context context) {
        try {
            if (!isLoadedAdsFacebook() && ads_network.equals("facebook")) {
                if (!facebookAdsUtils.isShowAds())
                    return;
                try {
                    facebookAdsUtils.destroy();
                } catch (Exception e) {
                }
                try {
                    if (controlAds == null) {
                        return;
                    }
                    facebookAdsUtils = FacebookAdsUtils.newInstance(context);
                    facebookAdsUtils.initiate(controlAds.getList_key().getFacebook().getPopup());
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }

    private static boolean isLoadedAdsFacebook() {
        try {
            if (ads_network.equals("facebook")) {
                return facebookAdsUtils.isLoaded();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveTimeShow(Context context) {
        try {
            timeDelayAds = controlAds.getConfig_show().getOffset_show_popup();
        } catch (Exception e) {
        }
        try {
            timeShowOld = System.currentTimeMillis();
            FileCacheUtil.setTimeShowPopupAdsFullScreen(context);
        } catch (Exception e) {
        }
    }

    public static void destroy() {
        try {
            try {
                if (facebookAdsUtils != null) {
                    facebookAdsUtils.destroy();
                }
            } catch (Exception e) {
            }
            controlAds = null;
            ads_network = null;
            admobUtils = null;
            facebookAdsUtils = null;
            gson = null;
            timeDelayAds = 0;
        } catch (Exception e) {
        }
    }

    private static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = pm.isScreenOn();
        } else {
            isScreenOn = pm.isInteractive();
        }
        return isScreenOn;
    }

    public static int getTimeConLaiDeShowPopup(Context context) {
        try {
            if (controlAds == null) {
                try {
                    getGson();
                    String strConfig = FileCacheUtil.loadConfig(context);
                    controlAds = gson.fromJson(strConfig, ControlAds.class);
                    ads_network = controlAds.getActive_ads().getPopup();
                    if (TextUtils.isEmpty(ads_network))
                        return 0;
                } catch (Exception e) {
                    return 0;
                }
            }
            if (timeDelayAds == 0) {
                timeDelayAds = controlAds.getConfig_show().getOffset_show_popup();
            }
            if (firstDelayAds == 0) {
                firstDelayAds = controlAds.getConfig_show().getTime_start_popup();
            }
            try {
                if (controlAds != null) {
                    long timeNow = System.currentTimeMillis();
                    if (timeShowOld == 0)
                        timeShowOld = FileCacheUtil.getTimeShowPopupAdsFullScreen(context);
                    int denta;
                    if (!firstShow) {
                        denta = (int) Math.max((timeCreate + firstDelayAds * 1000 - timeNow) / 1000,
                                (timeShowOld + timeDelayAds * 1000 - timeNow) / 1000);
                    } else {
                        denta = (int) ((timeShowOld + timeDelayAds * 1000 - timeNow) / 1000);
                    }
                    return denta;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
