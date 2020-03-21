package com.liner.familytracker.Main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.Application;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperActivity;
import com.liner.familytracker.Utils.HelperListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class InviteCodeActivity extends HelperActivity {
    private ImageView qrCode;
    private Button actionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        qrCode = findViewById(R.id.qrInviteCode);
        Helper.getUserModel(firebaseUser.getUid(), new HelperListener() {
            @Override
            public void onFinish(UserModel userModel) {
                QRGEncoder qrgEncoder = new QRGEncoder(userModel.getInviteCode(), null, QRGContents.Type.TEXT, 100);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);
                qrCode.setImageBitmap(qrgEncoder.getBitmap());
                actionButton = findViewById(R.id.actionButton);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }

            @Override
            public void onFinish(DatabaseReference databaseReference) {

            }
        });
    }

    @Override
    public void onFirebaseChanged() {

    }
}
