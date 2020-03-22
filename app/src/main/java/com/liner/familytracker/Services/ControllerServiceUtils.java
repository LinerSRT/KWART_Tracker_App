package com.liner.familytracker.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.SplashActivity;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperListener;

public class ControllerServiceUtils {
    public static boolean isServiceRunning(Context context){
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (ControllerService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startForeground(Context context, ControllerService controllerService){
        String NOTIFICATION_CHANNEL_ID = context.getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = (controllerService.SEND_USER_LOCATION)?context.getString(R.string.bat_and_net_updating):context.getString(R.string.location_updating);
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(context.getColor(R.color.colorPrimary));
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Notification notification = new NotificationCompat.Builder( context.getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle((controllerService.SEND_USER_LOCATION)?context.getString(R.string.bat_and_net_updating):context.getString(R.string.location_updating))
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(false)
                    .setPriority(64)
                    .addAction(R.mipmap.ic_null, context.getString(R.string.open_app), PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setOngoing(true).build();
            controllerService.startForeground(2, notification);
        } else {
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Notification notification = new NotificationCompat.Builder( context.getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle((controllerService.SEND_USER_LOCATION)?context.getString(R.string.bat_and_net_updating):context.getString(R.string.location_updating))
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(false)
                    .setPriority(64)
                    .addAction(R.mipmap.ic_null, context.getString(R.string.open_app), PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setOngoing(true).build();


            controllerService.startForeground(1, notification);
        }
        controllerService.serviceForeground = true;
    }

    public static void updateDeviceStatus(Context context, Location location){
        final DeviceStatus deviceStatus = new DeviceStatus();
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra("status", -1);
        boolean isCharging = status == 2 || status == 5;
        int batteryLevel = batteryStatus.getIntExtra("level", -1);
        int batteryScale = batteryStatus.getIntExtra("scale", -1);
        int batteryTemp = batteryStatus.getIntExtra("temperature", -1);
        int batteryVoltage = batteryStatus.getIntExtra("voltage", -1);
        float batteryLevelPercent = (float)batteryLevel/(float)batteryScale;
        String networkStatus = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                networkStatus = "noconnect";
            } else if (activeNetworkInfo.getType() == 1) {
                networkStatus = "WiFi";
            } else if (activeNetworkInfo.getType() == 0) {
                switch (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        networkStatus = "2G";
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        networkStatus = "3G";
                        break;
                    case 13:
                        networkStatus = "4G";
                        break;
                }
            }
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            deviceStatus.setRingerMode(audioManager.getRingerMode());
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            deviceStatus.setScreenOn(powerManager.isInteractive());
        }
        deviceStatus.setBatteryCharging(isCharging);
        deviceStatus.setBatteryLevel(batteryLevel);
        deviceStatus.setBatteryLevelPercent(batteryLevelPercent);
        deviceStatus.setBatteryTemp(batteryTemp);
        deviceStatus.setBatteryVoltage(batteryVoltage);
        if(location != null) {
            Log.e("TAGTAG", "loc not bull: ");
            deviceStatus.setLocationAccuracy((int) location.getAccuracy());
            deviceStatus.setLocationAltitude((int) location.getAltitude());
            deviceStatus.setLocationDirection((int) location.getBearing());
            deviceStatus.setLocationLat((float) location.getLatitude());
            deviceStatus.setLocationLon((float) location.getLongitude());
            deviceStatus.setLocationSpeed((int) location.getSpeed());
            deviceStatus.setLocationTime(location.getTime());
        }
        deviceStatus.setNetworkType(networkStatus);
        Log.e("TAGTAG", "Update: "+deviceStatus.toString());
        Helper.getUserDatabase().child("deviceStatus").setValue(deviceStatus);
    }
    public static double distance(double d, double d2, double d3, double d4) {
        double radians = Math.toRadians(d3 - d);
        double radians2 = Math.toRadians(d4 - d2);
        double haversin = haversin(radians) + (Math.cos(Math.toRadians(d)) * Math.cos(Math.toRadians(d3)) * haversin(radians2));
        return Math.atan2(Math.sqrt(haversin), Math.sqrt(1.0d - haversin)) * 2.0d * 6371.0d;
    }

    public static double haversin(double d) {
        return Math.pow(Math.sin(d / 2.0d), 2.0d);
    }

}
