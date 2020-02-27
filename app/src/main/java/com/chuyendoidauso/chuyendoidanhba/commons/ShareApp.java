package com.chuyendoidauso.chuyendoidanhba.commons;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.api.AppApi;
import com.chuyendoidauso.chuyendoidanhba.api.BaseObserver;
import com.chuyendoidauso.chuyendoidanhba.models.AdsModel;

import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import okhttp3.ResponseBody;

public class ShareApp {

    public static void shareApplication(Activity act) {
        try {
            AdsModel adsModel = SharedReferenceUtils.get(act, AdsModel.class.getName(), AdsModel.class);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", act.getResources().getString(R.string.app_name));
            intent.putExtra("android.intent.extra.TEXT", String.format(act.getResources().getString(R.string.txt_share_app), adsModel == null ?
                    act.getPackageName() : adsModel.linkShare));
            act.startActivity(Intent.createChooser(intent, act.getResources().getString(R.string.txt_select_share_app)));
            postApi(act);
        } catch (Throwable e) {
            Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void postApi(Activity act) {
        if (act != null && act.isFinishing()) return;
        String idDevice = Settings.Secure.getString(act.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        AppApi.getInstance().postDeviceId(act.getPackageName(), idDevice, new BaseObserver<ResponseBody>() {

            @Override
            protected void onResponse(final ResponseBody responseBody) {
            }

            @Override
            protected void onFailure() {
            }
        });
    }

    public static void rateApplication(Activity act) {
        Uri uri = Uri.parse("market://details?id=" + act.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            act.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            act.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + act.getPackageName())));
        }
    }

    public static void rateIntentForUrl(Activity act,String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        act.startActivity(intent);
    }
}
