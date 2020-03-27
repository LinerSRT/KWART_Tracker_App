package com.liner.familytracker.Services;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperListener;
import com.liner.familytracker.Utils.PrefHelper;

public class DeviceTracker {
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private PrefHelper prefHelper;
    private UserModel userModel;
    private DeviceStatus deviceStatus;

    public DeviceTracker(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        prefHelper = new PrefHelper(context);
        if(prefHelper.isUserExist(firebaseAuth.getCurrentUser().getUid())){
            userModel = prefHelper.getUser(firebaseAuth.getCurrentUser().getUid());
            deviceStatus = userModel.getDeviceStatus();
        } else {
            Helper.getUserModel(new HelperListener() {
                @Override
                public void onFinish(UserModel model) {
                    userModel = model;
                    deviceStatus = userModel.getDeviceStatus();
                }

                @Override
                public void onFinish(DatabaseReference databaseReference) {

                }
            });
        }
    }



    public void updateDeviceStatus(final TrackerInterface trackerInterface){
        Intent batteryCheckIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int batteryStatus = batteryCheckIntent.getIntExtra("status", -1);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String tempNetworkType = "Авиарежим";
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null)
        switch (networkInfo.getType()){
            case 0:
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                switch (telephonyManager.getNetworkType()){
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        tempNetworkType = "2G";
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
                        tempNetworkType = "3G";
                        break;
                    case 13:
                        tempNetworkType = "4G";
                        break;
                }
                break;
            case 1:
                tempNetworkType = "WiFi";
                break;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if(deviceStatus != null){
            deviceStatus.setScreenOn((powerManager == null) || powerManager.isInteractive());
            deviceStatus.setBatteryCharging(batteryStatus == 2 || batteryStatus == 5);
            deviceStatus.setBatteryLevel(batteryCheckIntent.getIntExtra("level", -1));
            deviceStatus.setRingerMode((audioManager != null)?audioManager.getRingerMode():2);
            deviceStatus.setNetworkType(tempNetworkType);
            userModel.setDeviceStatus(deviceStatus);
            userModel.setFinishedReg("true");
            prefHelper.saveUser(userModel);
            databaseReference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    trackerInterface.onUpdateFinished();
                }
            });
        }
    }


    public void updateDeviceStatus(Location location, final TrackerInterface trackerInterface){
        Intent batteryCheckIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int batteryStatus = batteryCheckIntent.getIntExtra("status", -1);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String tempNetworkType = "Авиарежим";
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null)
            switch (networkInfo.getType()){
                case 0:
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    switch (telephonyManager.getNetworkType()){
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            tempNetworkType = "2G";
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
                            tempNetworkType = "3G";
                            break;
                        case 13:
                            tempNetworkType = "4G";
                            break;
                    }
                    break;
                case 1:
                    tempNetworkType = "WiFi";
                    break;
            }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if(deviceStatus != null){
            deviceStatus.setScreenOn((powerManager == null) || powerManager.isInteractive());
            deviceStatus.setBatteryCharging(batteryStatus == 2 || batteryStatus == 5);
            deviceStatus.setBatteryLevel(batteryCheckIntent.getIntExtra("level", -1));
            deviceStatus.setRingerMode((audioManager != null)?audioManager.getRingerMode():2);
            deviceStatus.setNetworkType(tempNetworkType);
            if(location != null){
                if(location.getLatitude() != 0 && location.getLongitude() != 0) {
                    deviceStatus.setLocationAccuracy((int) location.getAccuracy());
                    deviceStatus.setLocationAltitude((int) location.getAltitude());
                    deviceStatus.setLocationDirection((int) location.getBearing());
                    deviceStatus.setLocationLat((float) location.getLatitude());
                    deviceStatus.setLocationLon((float) location.getLongitude());
                    deviceStatus.setLocationSpeed((int) location.getSpeed());
                    deviceStatus.setLocationTime(location.getTime());
                }
            }
            userModel.setFinishedReg("true");
            userModel.setDeviceStatus(deviceStatus);
            prefHelper.saveUser(userModel);
            databaseReference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    trackerInterface.onUpdateFinished();
                }
            });
        }
    }

    public interface TrackerInterface{
        void onUpdateFinished();
    }
}
