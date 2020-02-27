package com.chuyendoidauso.chuyendoidanhba.notify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.chuyendoidauso.chuyendoidanhba.models.TimeNotify;

import java.util.ArrayList;
import java.util.Calendar;

public class NotifyUtils {

    public static void addNotify(Context context, ArrayList<TimeNotify> timeNotifies) {
        if (context != null) {
            addAlarmNotify(context, timeNotifies);
        }
    }

    private static void addAlarmNotify(Context context, ArrayList<TimeNotify> timeNotifies) {
        if (timeNotifies == null || (timeNotifies != null && timeNotifies.size() == 0)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SchedulingService.class);
        for (int i = 0; i < 4; i++) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.set(Calendar.MILLISECOND, 0);
            //System.out.println("addAlarmNotify" + timeNotifies.get(i).time);
            calendarTo.set(Calendar.HOUR_OF_DAY, 9);
            calendarTo.set(Calendar.MINUTE, 58);
            calendarTo.set(Calendar.SECOND, 00);

            Calendar calendarFrom = Calendar.getInstance();
            if (i != 0) {
                calendarFrom.set(Calendar.MILLISECOND, 0);
                // System.out.println("addAlarmNotify" + timeNotifies.get(i).time);
                calendarFrom.set(Calendar.HOUR_OF_DAY, 9);
                calendarFrom.set(Calendar.MINUTE, 52 + i);
                calendarFrom.set(Calendar.SECOND, 00);
            } else {
                calendarFrom = Calendar.getInstance();
            }

            intent.putExtra("calendarFrom", calendarFrom.getTimeInMillis());
            intent.putExtra("calendarTo", calendarTo.getTimeInMillis());
            intent.putExtra("notificationId", i + 1);
            intent.putExtra("isStart", i == 0);
            PendingIntent pendingIntent = PendingIntent.getService(context, i + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarFrom.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendarFrom.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
