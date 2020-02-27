package com.chuyendoidauso.chuyendoidanhba.notify;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.commons.AppConstant;
import com.chuyendoidauso.chuyendoidanhba.ui.splash.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResidentNotificationHelper {

    public static final String NOTICE_ID_KEY = "NOTICE_ID";
    public static final int NOTICE_ID_TYPE_0 = R.string.app_name;

    @TargetApi(16)
    public static void sendResidentNoticeType0(Context context, String title, String content, @DrawableRes int res) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_notification_type_0);
        remoteViews.setTextViewText(R.id.tag_tv, title);
        remoteViews.setTextViewText(R.id.content_tv, content);
        remoteViews.setTextViewText(R.id.time_tv, getTime());
        remoteViews.setImageViewResource(R.id.icon_iv, res);
        remoteViews.setInt(R.id.close_iv, "setColorFilter", getIconColor());
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(NOTICE_ID_KEY, NOTICE_ID_TYPE_0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice_view_type_0, pendingIntent);

        int requestCode1 = (int) SystemClock.uptimeMillis();
        Intent intent1 = new Intent(AppConstant.ACTION.ACTION_CLOSE_NOTICE);
        intent1.putExtra(NOTICE_ID_KEY, NOTICE_ID_TYPE_0);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, requestCode1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close_iv, pendingIntent1);
        builder.setSmallIcon(res);


        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if(android.os.Build.VERSION.SDK_INT >= 16) {

            notification = builder.build();
            notification.bigContentView = remoteViews;
        }
        notification.contentView = remoteViews;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTICE_ID_TYPE_0, notification);
    }


    public static void sendDefaultNotice(Context context, String title, String content, @DrawableRes int res) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        Notification notification = builder
                .setContentTitle("Campus")
                .setContentText("It's a default notification")
                .setSmallIcon(res)
                .setSmallIcon(res)
                .build();


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTICE_ID_TYPE_0, notification);
    }


    public static int getIconColor(){
        return Color.parseColor("#999999");

    }


    private static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE);
        return format.format(new Date());
    }


    public static void clearNotification(Context context, int noticeId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(noticeId);
    }

}