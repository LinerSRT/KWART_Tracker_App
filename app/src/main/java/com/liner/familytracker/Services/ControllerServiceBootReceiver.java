package com.liner.familytracker.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.liner.familytracker.Utils.AlarmUtils;

public class ControllerServiceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ContextCompat.startForegroundService(context, new Intent(context, ControllerService.class));
    }
}
