package com.liner.familytracker.LoginRegister;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.ColorUtils;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class InviteCodeActivity extends CoreActivity {
    private ImageView qrCode;
    private Button actionButton;
    boolean phoneValid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        qrCode = findViewById(R.id.qrInviteCode);
        if(userModel.getInviteCode() == null) {
            currentUserDatabase.child("inviteCode").setValue( UserModel.generateInviteCode()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    QRGEncoder qrgEncoder = new QRGEncoder(userModel.getInviteCode(), null, QRGContents.Type.TEXT, 100);
                    qrgEncoder.setColorBlack(Color.BLACK);
                    qrgEncoder.setColorWhite(Color.WHITE);
                    qrCode.setImageBitmap(qrgEncoder.getBitmap());
                }
            });
        } else {
            QRGEncoder qrgEncoder = new QRGEncoder(userModel.getInviteCode(), null, QRGContents.Type.TEXT, 100);
            qrgEncoder.setColorBlack(Color.BLACK);
            qrgEncoder.setColorWhite(Color.WHITE);
            qrCode.setImageBitmap(qrgEncoder.getBitmap());
        }
        actionButton = findViewById(R.id.actionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}
