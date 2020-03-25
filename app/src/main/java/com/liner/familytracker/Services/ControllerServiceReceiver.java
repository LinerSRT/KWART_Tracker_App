package com.liner.familytracker.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.liner.familytracker.Utils.AlarmUtils;

public class ControllerServiceReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(new Intent(context, ControllerService.class));
        } else {
            context.startService(new Intent(context, ControllerService.class));
        }
        AlarmUtils.resetAlarms(context);
    }
}
