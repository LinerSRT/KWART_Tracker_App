package com.liner.familytracker.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.liner.familytracker.Services.ControllerService;
import com.liner.familytracker.Services.ControllerServiceReceiver;

public class AlarmUtils {
    public static void restartAlarms(Context context) {
        int intervalUpdate;
        cancelAlarms(context);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent(context, ControllerServiceReceiver.class), 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intervalUpdate = 1 * 60000;
        if (Build.VERSION.SDK_INT < 23) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((long) intervalUpdate), broadcast);
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((long) intervalUpdate), broadcast);
        }
        if (!ControllerService.isServiceRunning(context)) {
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(new Intent(context, ControllerService.class));
            } else {
                context.startService(new Intent(context, ControllerService.class));
            }
        }
    }

    public static void cancelAlarms(Context context) {
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent(context, ControllerServiceReceiver.class), 0);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(broadcast);
        broadcast.cancel();
    }

    public static void resetAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent(context, ControllerServiceReceiver.class), 0);
        boolean liveTracking = false;
        int intervalUpdate = 1 * 60000;
        if (Build.VERSION.SDK_INT < 23) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((long) (liveTracking ? 1 * 60000 : intervalUpdate * 60000)), broadcast);
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((long) (liveTracking ? 1 * 60000 : intervalUpdate * 60000)), broadcast);
        }
    }

}
