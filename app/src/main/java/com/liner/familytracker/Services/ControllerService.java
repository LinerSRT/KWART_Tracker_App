package com.liner.familytracker.Services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liner.familytracker.Utils.AlarmUtils;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class ControllerService extends Service {
    private LocationTracker locationTracker;
    private DeviceTracker deviceTracker;

    @Override
    public void onCreate() {
        ControllerServiceUtils.startForeground(this, this);
        deviceTracker = new DeviceTracker(this);
        super.onCreate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ControllerServiceUtils.startForeground(this, this);
        AlarmUtils.restartAlarms(getApplicationContext());
        locationTracker = new LocationTracker(this, new TrackerSettings()
                .setUseGPS(true)
                .setUseNetwork(true)
                .setUsePassive(true)
                .setTimeBetweenUpdates(30 * 60 * 1000)
                .setMetersBetweenUpdates(100)) {
            @Override
            public void onLocationFound(@NonNull Location location) {
                stopListening();
                deviceTracker.updateDeviceStatus(location, new DeviceTracker.TrackerInterface() {
                    @Override
                    public void onUpdateFinished() {
                        stopForeground(true);
                        stopSelf();
                    }
                });
            }

            @Override
            public void onTimeout() {
                stopForeground(true);
                stopSelf();
            }
        };
        locationTracker.startListening();
        return START_NOT_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationTracker != null){
            locationTracker.stopListening();
        }
    }

    public static boolean isServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (ControllerService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
