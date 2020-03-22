package com.liner.familytracker.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionsChecker {
    public static boolean locationGranted(Context context) {
        return Build.VERSION.SDK_INT < 23 || (context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED 
                && context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED );
    }

    public static boolean cameraGranted(Context context) {
        return Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED ;
    }

    public static boolean phoneGranted(Context context) {
        return Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED ;
    }

    public static boolean smsGranted(Context context) {
        return Build.VERSION.SDK_INT < 23 || (context.checkSelfPermission("android.permission.SEND_SMS") == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission("android.permission.RECEIVE_SMS") == PackageManager.PERMISSION_GRANTED );
    }

    public static boolean storageGranted(Context context) {
        return Build.VERSION.SDK_INT < 23 || context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED ;
    }
}
