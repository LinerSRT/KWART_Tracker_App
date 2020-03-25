package com.liner.familytracker.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.SplashActivity;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperListener;
import com.liner.familytracker.Utils.PrefHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
            String channelName = "TRACKER";
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
                    .setContentTitle("Сервис запущен")
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(false)
                    .setPriority(64)
                    .addAction(R.mipmap.ic_null, "Открыть приложение", PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setOngoing(true).build();
            controllerService.startForeground(2, notification);
        } else {
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Notification notification = new NotificationCompat.Builder( context.getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Сервис запущен")
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(false)
                    .setPriority(64)
                    .addAction(R.mipmap.ic_null, "Открыть приложение", PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setOngoing(true).build();
            controllerService.startForeground(1, notification);
        }
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

    public static List<Address> getGeoCodedAddress(Context context, float lat, float lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat,
                    lon, 1);
            return addresses;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAdressLine(Context context, float lat, float lon) {
        List<Address> addresses = getGeoCodedAddress(context, lat, lon);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        } else {
            return null;
        }
    }

    public static String getCity(Context context, float lat, float lon) {
        List<Address> addresses = getGeoCodedAddress(context, lat, lon);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            return address.getLocality();
        } else {
            return null;
        }
    }

    public static String getPlace(Context context, float lat, float lon) {
        List<Address> addresses = getGeoCodedAddress(context, lat, lon);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            StringBuilder returnString = new StringBuilder();
            returnString.append(address.getLocality());
            returnString.append(",");
            returnString.append(address.getThoroughfare());
            return returnString.toString();
        } else {
            return null;
        }
    }

    public static String getPostalCode(Context context, float lat, float lon) {
        List<Address> addresses = getGeoCodedAddress(context, lat, lon);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            return address.getPostalCode();
        } else {
            return null;
        }
    }

    public static String getCountryName(Context context, float lat, float lon) {
        List<Address> addresses = getGeoCodedAddress(context, lat, lon);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            return address.getCountryName();
        } else {
            return null;
        }
    }
}
