package com.liner.familytracker.Register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.Result;
import com.liner.familytracker.Application;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Main.TrackerActivity;
import com.liner.familytracker.R;
import com.liner.familytracker.Utils.HelperActivity;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;

public class CircleConfigActivity extends HelperActivity {
    private CodeScanner codeScanner;
    private Button createNewCircleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_circle);
        createNewCircleButton = findViewById(R.id.createCircle);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

            }
        });
        Permissions.check(this, Manifest.permission.CAMERA, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                codeScanner.startPreview();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);

            }
        });
        createNewCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //List<String> user = new ArrayList<>();
                //user.add(Application.firebaseHelper.getFirebaseUser().getUid());
                //Application.firebaseHelper.getCurrentUserDatabase().child("synchronizedUsers").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
               //     @Override
               //     public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(CircleConfigActivity.this, TrackerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
               //     }
                //});
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onFirebaseChanged() {

    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

}
