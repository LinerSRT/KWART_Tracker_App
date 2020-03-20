package com.liner.familytracker.LoginRegister;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.Result;
import com.liner.familytracker.ColorUtils;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserJoinedCircles;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Main.MainActivity;
import com.liner.familytracker.Models.User;
import com.liner.familytracker.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;

public class CircleConfigActivity extends CoreActivity {
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
                //todo create new circle and add this user to them
                //todo add new created circle to user joined circles
                List<UserModel> userModels = new ArrayList<>();
                List<UserJoinedCircles> joinedCircles = new ArrayList<>();
                userModels.add(userModel);
                CircleModel circleModel = new CircleModel();
                circleModel.setCircleName("Семья");
                circleModel.setCricleUID(CircleModel.generateCircleUID());
                circleModel.setJoinedUsers(userModels);
                CircleModel circleModel2 = new CircleModel();
                circleModel2.setCircleName("Семья 2");
                circleModel2.setCricleUID(CircleModel.generateCircleUID());
                circleModel.setJoinedUsers(userModels);
                UserJoinedCircles userJoinedCircles = new UserJoinedCircles();
                userJoinedCircles.setCircleName(circleModel.getCircleName());
                userJoinedCircles.setCircleUID(circleModel.getCricleUID());
                UserJoinedCircles userJoinedCircles2 = new UserJoinedCircles();
                userJoinedCircles2.setCircleName(circleModel2.getCircleName());
                userJoinedCircles2.setCircleUID(circleModel2.getCricleUID());
                joinedCircles.add(userJoinedCircles);
                joinedCircles.add(userJoinedCircles2);
                currentUserDatabase.child("joinedCircles").setValue(userJoinedCircles);
                circlesDatabase.child(circleModel.getCricleUID()).setValue(circleModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(CircleConfigActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
            }
        });
    }

    @Override
    public void onCircleDataChanged(CircleModel circleModel) {

    }

    @Override
    public void onUserDataChanged(UserModel userModel) {

    }

    @Override
    public void onUserLoggedOut() {

    }

    @Override
    public void onUserSignedIn() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

}
