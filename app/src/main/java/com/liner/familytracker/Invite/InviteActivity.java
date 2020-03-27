package com.liner.familytracker.Invite;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.liner.familytracker.R;
import com.liner.familytracker.Services.AppTracker;
import com.liner.familytracker.SplashActivity;
import com.liner.familytracker.Utils.HelperActivity;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class InviteActivity extends AppCompatActivity {
    private ImageView permissionIcon;
    private TextView permissionTitle, permissionDesc, permissionDescSecond;
    private Button allowBtn;
    private boolean allGranted = true;
    private AppTracker appTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_layout);
        appTracker = new AppTracker(this);
        permissionIcon = findViewById(R.id.permissionIcon);
        permissionTitle = findViewById(R.id.permissionTitle);
        permissionDesc = findViewById(R.id.permissionDesc);
        permissionDescSecond = findViewById(R.id.permissionDescSecond);
        allowBtn = findViewById(R.id.permissionAllow);
        checkPermissions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 152){
            recreate();
            Log.e("TAGTAG", "Recreate!");
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void checkPermissions(){
        //allGranted = isUsageStatsGranted();
        String[] permissions = new String[]{
                "android.permission.ACCESS_COARSE_LOCATION"
                ,"android.permission.ACCESS_FINE_LOCATION"
                ,"android.permission.READ_PHONE_STATE"
                ,"android.permission.READ_CONTACTS"
                ,"android.permission.CAMERA"
        };
        for(String perm:permissions){
            if (ActivityCompat.checkSelfPermission( this, perm ) != PackageManager.PERMISSION_GRANTED ) {
                allGranted = false;
                switch (perm){
                    case "android.permission.READ_PHONE_STATE":
                    case "android.permission.READ_CONTACTS":
                        permissionIcon.setImageResource(R.drawable.ic_call_black_24dp);
                        permissionTitle.setText("Звонки");
                        permissionDesc.setText("Нам требуется доступ к звонкам*");
                        permissionDescSecond.setText("*Используется для геопозиционирования без доступа к сети Интернет. Нажмите, что бы прочитать подробности");
                        allowBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Permissions.check(InviteActivity.this, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_CONTACTS"}, null, null, new PermissionHandler() {
                                    @Override
                                    public void onGranted() {
                                        recreate();
                                    }

                                    @Override
                                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                                    }
                                });
                             }
                        });
                        break;
                    case "android.permission.ACCESS_FINE_LOCATION":
                    case "android.permission.ACCESS_COARSE_LOCATION":
                        permissionIcon.setImageResource(R.drawable.ic_location_on_black_24dp);
                        permissionTitle.setText("GPS");
                        permissionDesc.setText("Нам требуется доступ к GPS*");
                        permissionDescSecond.setText("*Используется для геопозиционирования");
                        allowBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Permissions.check(InviteActivity.this,  new String[] {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" }, null, null, new PermissionHandler() {
                                    @Override
                                    public void onGranted() {
                                        //recreate();
                                    }

                                    @Override
                                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                                    }
                                });
                            }
                        });
                        break;
                    case "android.permission.CAMERA":
                        permissionIcon.setImageResource(R.drawable.camera);
                        permissionTitle.setText("Камера");
                        permissionDesc.setText("Разрешите доступ к камере*");
                        permissionDescSecond.setText("*Используется для синхрониации пользователей по QR-коду");
                        allowBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Permissions.check(InviteActivity.this, new String[] {"android.permission.CAMERA"}, null, null, new PermissionHandler() {
                                    @Override
                                    public void onGranted() {
                                        recreate();
                                    }

                                    @Override
                                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {

                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }
        if(allGranted){
            startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } else {
            //permissionIcon.setImageResource(R.drawable.ic_settings_applications_black_24dp);
            //permissionTitle.setText("Статистика приложений");
            //permissionDesc.setText("Нам требуется доступ к статистике приложений*");
            //permissionDescSecond.setText("*Используется для получения информации о запущенных приложениях и частоте их использования");
            //allowBtn.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View view) {
            //        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            //        recreate();
            //    }
            //});
        }

    }

    private boolean isUsageStatsGranted(){
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }


}
