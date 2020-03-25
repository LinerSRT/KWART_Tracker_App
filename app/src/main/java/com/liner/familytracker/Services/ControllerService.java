package com.liner.familytracker.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ControllerService extends Service implements GPSManager.GPSManagerListener {
    private int UPDATE_INTERVAL_MINUTES = 1;
    private Location currentLocation;
    private GPSManager gpsManager;
    private Handler serviceHandler;
    private Runnable updateDevice = new Runnable() {
        @Override
        public void run() {
            ControllerServiceUtils.updateDeviceStatus(ControllerService.this, currentLocation);
            currentLocation = null;
            serviceHandler.postDelayed(updateDevice, UPDATE_INTERVAL_MINUTES*(60*1000));
        }
    };

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        Log.d(getClass().getSimpleName(), "Service started");
        ControllerServiceUtils.startForeground(this, this);
        serviceHandler = new Handler();
        gpsManager = new GPSManager(this, this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceHandler.postDelayed(updateDevice, 0);
        gpsManager.startLocationListen();
        ControllerServiceUtils.startForeground(this, this);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service died", Toast.LENGTH_SHORT).show();
        Log.d(getClass().getSimpleName(), "Service died");
        gpsManager.stopLocationListening();
        super.onDestroy();
    }

    @Override
    public void onLocationReceived(Location location) {
        currentLocation = location;
        ControllerServiceUtils.updateDeviceStatus(this, currentLocation);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 6000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }
}
