package net.mready.hover;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import static net.mready.hover.HoverService.ACTION_CLOSE;
import static net.mready.hover.HoverService.ACTION_SHOW;
import static net.mready.hover.HoverService.EXTRA_ARGUMENTS;
import static net.mready.hover.HoverService.EXTRA_WINDOW_CLASS;
import static net.mready.hover.HoverService.EXTRA_WINDOW_ID;

public final class Hover {
    private Hover() {
    }

    public static void showWindow(Context context, int id, Class<? extends HoverWindow> windowClass) {
        showWindow(context, id, windowClass, null);
    }

    public static void showWindow(Context context, int id, Class<? extends HoverWindow> windowClass, Bundle arguments) {
        context.startService(new Intent(context, HoverService.class)
                .setAction(ACTION_SHOW)
                .putExtra(EXTRA_WINDOW_ID, id)
                .putExtra(EXTRA_WINDOW_CLASS, windowClass)
                .putExtra(EXTRA_ARGUMENTS, arguments));
    }

    public static void closeWindow(Context context, int id) {
        context.startService(new Intent(context, HoverService.class)
                .setAction(ACTION_CLOSE)
                .putExtra(EXTRA_WINDOW_ID, id));
    }


    public static boolean hasOverlayPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }

//    public static boolean hasOverlayPermission(Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
//        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            try {
//                WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//                if (mgr == null) return false; //getSystemService might return null
//                View viewToAdd = new View(context);
//                WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0, android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
//                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
//                viewToAdd.setLayoutParams(params);
//                mgr.addView(viewToAdd, params);
//                mgr.removeView(viewToAdd);
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//        return false;
//    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestOverlayPermission(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.getPackageName()));

        activity.startActivityForResult(intent, requestCode);
    }
}
