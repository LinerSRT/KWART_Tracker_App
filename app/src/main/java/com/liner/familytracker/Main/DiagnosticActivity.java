package com.liner.familytracker.Main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.liner.familytracker.R;
import com.liner.familytracker.Views.StatusIconView;

public class DiagnosticActivity extends AppCompatActivity{
    private LinearLayout internetLayout, locationLayout, wifiLayout, androidLayout;
    private StatusIconView internetIcon, locationIcon, wifiIcon, androidIcon;
    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };
    private final Intent[] BATTERY_REQUEST_INTENTS = {

            new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
    };
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.diagnostic_activity);
        internetLayout = findViewById(R.id.internetLayout);
        internetIcon = findViewById(R.id.internetStatusIcon);
        locationLayout = findViewById(R.id.locationLayout);
        locationIcon = findViewById(R.id.locationStatusIcon);
        wifiLayout = findViewById(R.id.wifiLayout);
        wifiIcon = findViewById(R.id.wifiStatusIcon);
        androidLayout = findViewById(R.id.androidLayout);
        androidIcon = findViewById(R.id.androidStatusIcon);
        internetIcon.setEnabled(isNetworkAvailable());
        locationIcon.setEnabled(isLocationAvailable());
        wifiIcon.setEnabled(isWiFiAvailable());
        androidIcon.setEnabled(isBatOptimizeEnabled());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            androidLayout.setVisibility(View.GONE);
        }
        internetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!internetIcon.isIconEnabled()){
                    if (getPackageManager().hasSystemFeature("android.hardware.telephony")) {
                        Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                        intent.setClassName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locationIcon.isIconEnabled()){
                    startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                }
            }
        });
        wifiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wifiIcon.isIconEnabled()){
                    startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                }
            }
        });
        androidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!androidIcon.isIconEnabled()){
                    new MaterialDialog.Builder(DiagnosticActivity.this)
                            .title("Оптимизация")
                            .content("Пожалуйста добавьте наше приложение в исключения, для корректной работы в фоне")
                            .positiveText("Добавить")
                            .positiveColorRes(R.color.accent_color)
                            .negativeText("Нет")
                            .negativeColorRes(R.color.text_color)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Toast.makeText(DiagnosticActivity.this, "Теперь выберите Активные приложения -> Выьерите наше приложение и установите Флажок на \"Нет ограничений\"", Toast.LENGTH_LONG).show();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Intent intent;
                                        if(isPackageExist("com.miui.powerkeeper")){
                                            intent = new Intent().setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"));
                                        } else {
                                            intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                        }
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    }
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetIcon.setEnabled(isNetworkAvailable());
        locationIcon.setEnabled(isLocationAvailable());
        wifiIcon.setEnabled(isWiFiAvailable());
        androidIcon.setEnabled(isBatOptimizeEnabled());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            androidLayout.setVisibility(View.GONE);
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public boolean isLocationAvailable() {
        return ((LocationManager) getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isWiFiAvailable() {
        return ((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE)).isWifiEnabled();
    }

    public boolean isBatOptimizeEnabled() {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName = getPackageName();
        return powerManager != null && !powerManager.isIgnoringBatteryOptimizations(packageName);
    }

    public boolean isPackageExist(String packageName) {
        for (ApplicationInfo applicationInfo : getPackageManager().getInstalledApplications(0)) {
            if (applicationInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    public void onBackPressed() {
        finish();
    }
}
