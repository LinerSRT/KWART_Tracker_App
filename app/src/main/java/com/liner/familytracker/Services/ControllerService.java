package com.liner.familytracker.Services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Utils.AlarmUtils;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperListener;
import com.liner.familytracker.Utils.PrefHelper;

public class ControllerService extends Service implements GPSManager.GPSManagerListener {
    private int UPDATE_INTERVAL_MINUTES = 1;
    private Location currentLocation;
    private GPSManager gpsManager;
    private Handler serviceHandler;
    private String currUserUID;
    private Runnable updateDevice = new Runnable() {
        @Override
        public void run() {
            if(currentLocation != null) {
                updateDeviceStatus();
                stopForeground(true);
                stopSelf();
            } else {
                updateDeviceStatus();
                serviceHandler.postDelayed(updateDevice, 5000);
            }

        }
    };
    private PrefHelper prefHelper;

    @Override
    public void onCreate() {
        currUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        Log.d(getClass().getSimpleName(), "Service started");
        ControllerServiceUtils.startForeground(this, this);
        prefHelper = new PrefHelper(this);
        serviceHandler = new Handler();
        gpsManager = new GPSManager(this, this);
        currentLocation = null;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceHandler.postDelayed(updateDevice, 0);
        gpsManager.startLocationListen();
        ControllerServiceUtils.startForeground(this, this);
        AlarmUtils.restartAlarms(getApplicationContext());
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
        gpsManager.stopLocationListening();
    }

    @Override
    public void onLocationReceived(Location location) {
        currentLocation = location;
        updateDeviceStatus();
    }

    //@Override
    //public void onTaskRemoved(Intent rootIntent) {
    //    Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
    //    restartServiceIntent.setPackage(getPackageName());
    //    PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
    //    AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    //    alarmService.set(
    //            AlarmManager.ELAPSED_REALTIME,
    //            SystemClock.elapsedRealtime() + 6000,
    //            restartServicePendingIntent);
    //    super.onTaskRemoved(rootIntent);
    //}


    private int status;
    private boolean isCharging;
    private int batteryLevel;
    private int batteryScale;
    private int batteryTemp;
    private int batteryVoltage;
    private int ringerMode;
    private boolean screenOn;
    private float batteryLevelPercent;
    private String networkStatus = "Авиарежим";


    private void updateDeviceStatus(){
        UserModel currentUser;
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        status = batteryStatus.getIntExtra("status", -1);
        isCharging = status == 2 || status == 5;
        batteryLevel = batteryStatus.getIntExtra("level", -1);
        batteryScale = batteryStatus.getIntExtra("scale", -1);
        batteryTemp = batteryStatus.getIntExtra("temperature", -1);
        batteryVoltage = batteryStatus.getIntExtra("voltage", -1);
        ringerMode = 2;
        batteryLevelPercent = (float)batteryLevel/(float)batteryScale;
        networkStatus = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                networkStatus = "Авиарежим";
            } else if (activeNetworkInfo.getType() == 1) {
                networkStatus = "WiFi";
            } else if (activeNetworkInfo.getType() == 0) {
                switch (((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) {
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
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            ringerMode = audioManager.getRingerMode();
        }
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            screenOn = powerManager.isInteractive();
        }
        if(prefHelper.isUserExist(currUserUID)){
            currentUser = prefHelper.getUser(currUserUID);
            currentUser.getDeviceStatus().setBatteryCharging(isCharging);
            currentUser.getDeviceStatus().setBatteryLevel(batteryLevel);
            currentUser.getDeviceStatus().setBatteryLevelPercent(batteryLevelPercent);
            currentUser.getDeviceStatus().setBatteryTemp(batteryTemp);
            currentUser.getDeviceStatus().setBatteryVoltage(batteryVoltage);
            if(currentLocation != null) {
                if(currentLocation.getLatitude() != 0 && currentLocation.getLongitude() != 0) {
                    currentUser.getDeviceStatus().setLocationAccuracy((int) currentLocation.getAccuracy());
                    currentUser.getDeviceStatus().setLocationAltitude((int) currentLocation.getAltitude());
                    currentUser.getDeviceStatus().setLocationDirection((int) currentLocation.getBearing());
                    currentUser.getDeviceStatus().setLocationLat((float) currentLocation.getLatitude());
                    currentUser.getDeviceStatus().setLocationLon((float) currentLocation.getLongitude());
                    currentUser.getDeviceStatus().setLocationSpeed((int) currentLocation.getSpeed());
                    currentUser.getDeviceStatus().setLocationTime(currentLocation.getTime());
                }
            }
            currentUser.getDeviceStatus().setNetworkType(networkStatus);
            prefHelper.saveUser(currentUser);
            Helper.getUserDatabase().child("deviceStatus").setValue(currentUser.getDeviceStatus());
        } else {
            Helper.getUserModel(new HelperListener() {
                @Override
                public void onFinish(UserModel userModel) {
                    userModel = prefHelper.getUser(currUserUID);
                    userModel.getDeviceStatus().setBatteryCharging(isCharging);
                    userModel.getDeviceStatus().setBatteryLevel(batteryLevel);
                    userModel.getDeviceStatus().setBatteryLevelPercent(batteryLevelPercent);
                    userModel.getDeviceStatus().setBatteryTemp(batteryTemp);
                    userModel.getDeviceStatus().setBatteryVoltage(batteryVoltage);
                    if(currentLocation != null) {
                        if(currentLocation.getLatitude() != 0 && currentLocation.getLongitude() != 0) {
                            userModel.getDeviceStatus().setLocationAccuracy((int) currentLocation.getAccuracy());
                            userModel.getDeviceStatus().setLocationAltitude((int) currentLocation.getAltitude());
                            userModel.getDeviceStatus().setLocationDirection((int) currentLocation.getBearing());
                            userModel.getDeviceStatus().setLocationLat((float) currentLocation.getLatitude());
                            userModel.getDeviceStatus().setLocationLon((float) currentLocation.getLongitude());
                            userModel.getDeviceStatus().setLocationSpeed((int) currentLocation.getSpeed());
                            userModel.getDeviceStatus().setLocationTime(currentLocation.getTime());
                        }
                    }
                    userModel.getDeviceStatus().setNetworkType(networkStatus);
                    prefHelper.saveUser(userModel);
                    Helper.getUserDatabase().child("deviceStatus").setValue(userModel.getDeviceStatus());
                }

                @Override
                public void onFinish(DatabaseReference databaseReference) {

                }
            });
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
