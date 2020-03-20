package com.liner.familytracker.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.liner.familytracker.ColorUtils;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;

public class EnterPhoneNumberActivity extends CoreActivity {
    private EditText phoneNumberEdit;
    private Button nextStep;
    boolean phoneValid = false;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);
        phoneNumberEdit = findViewById(R.id.phoneNumberEditText);
        nextStep = findViewById(R.id.nextStep);
        TextView licenceView = findViewById(R.id.licence);
        licenceView.setText(new SpannableStringBuilder(Html.fromHtml(getResources().getString(R.string.tos_split_screen_account_create))));
        phoneNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().trim();
                if(validCellPhone(result)){
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(EnterPhoneNumberActivity.this, R.attr.colorPrimary));
                    phoneValid = true;
                } else {
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(EnterPhoneNumberActivity.this, R.attr.backgroundColorDark));
                    phoneValid = false;
                }
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneValid){
                    Query query = usersDatabase.orderByChild("phoneNumber").equalTo(phoneNumberEdit.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean founded = false;
                            String token = "null";
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                String value = (String) child.child("phoneNumber").getValue();
                                if(value.equals(phoneNumberEdit.getText().toString().trim()) && !value.equals("")){
                                    founded = true;
                                    token = String.valueOf(child.child("token").getValue());
                                    userModel = child.getValue(UserModel.class);
                                    break;
                                }
                            }
                            if(founded){
                                if(userModel != null)
                                if(userModel.isRegisterFinished() == null){
                                    currentUserDatabase.child("phoneNumber").setValue(phoneNumberEdit.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            currentUserDatabase.child("token").setValue(FirebaseInstanceId.getInstance().getToken()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    startActivity(new Intent(EnterPhoneNumberActivity.this, CreatePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("phone_number", phoneNumberEdit.getText().toString().trim()));
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    startActivity(new Intent(EnterPhoneNumberActivity.this, CreatePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra("phone_number", phoneNumberEdit.getText().toString().trim())
                                            .putExtra("token", token));
                                }
                            } else {
                                currentUserDatabase.child("phoneNumber").setValue(phoneNumberEdit.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        currentUserDatabase.child("token").setValue(FirebaseInstanceId.getInstance().getToken()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                startActivity(new Intent(EnterPhoneNumberActivity.this, CreatePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("phone_number", phoneNumberEdit.getText().toString().trim()));
                                            }
                                        });
                                    }
                                });
                            }
                         }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
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

    public boolean validCellPhone(String number){
        return number.matches("^[+][0-9]{12}$");
    }

    @Override
    public void onBackPressed() {
        sendToSplash();
        finish();
        super.onBackPressed();
    }
}
