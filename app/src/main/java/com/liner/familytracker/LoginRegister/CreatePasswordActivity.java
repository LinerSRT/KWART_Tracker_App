package com.liner.familytracker.LoginRegister;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.liner.familytracker.ColorUtils;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Main.MainActivity;
import com.liner.familytracker.R;

import java.util.regex.Pattern;

public class CreatePasswordActivity extends CoreActivity {
    private TextView welcomeBackText;
    private EditText passwordEditText;
    private Button nextStep;
    private boolean passwordValid = false;
    private boolean loginAction = false;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        welcomeBackText = findViewById(R.id.welcomeBackText);
        if(getIntent().getStringExtra("token") != null && !getIntent().getStringExtra("token").equals("")){
            welcomeBackText.setVisibility(View.VISIBLE);
            loginAction = true;
            usersDatabase.child(getIntent().getStringExtra("token")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    welcomeBackText.setText("С возвращением, "+userModel.getUserName()+"\nПодтвердите ваш пароль");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            welcomeBackText.setVisibility(View.GONE);
            loginAction = false;
        }
        passwordEditText = findViewById(R.id.passwordEditText);
        final ImageButton showPass = findViewById(R.id.showPassword);
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showPass.setColorFilter(getColor(R.color.text_color));
                    } else {
                        showPass.setColorFilter(getResources().getColor(R.color.text_color));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showPass.setColorFilter(getColor(R.color.accent_color));
                    } else {
                        showPass.setColorFilter(getResources().getColor(R.color.accent_color));
                    }
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                passwordEditText.setSelection(passwordEditText.length());
            }
        });
        nextStep = findViewById(R.id.nextStep);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().trim();
                if(validPassword(result)){
                    passwordValid = true;
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(CreatePasswordActivity.this, R.attr.colorPrimary));
                } else {
                    passwordValid = false;
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(CreatePasswordActivity.this, R.attr.backgroundColorDark));
                }
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordValid){
                    if(loginAction){
                        if(userModel != null){
                            if(passwordEditText.getText().toString().trim().equals(userModel.getUserPassword())){
                                firebaseAuth.signInWithEmailAndPassword(userModel.getUserEmail(), userModel.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        startActivity(new Intent(CreatePasswordActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                });
                            } else {
                                //final LDialogUtility dialogUtility = new LDialogUtility(CreatePasswordActivity.this);
                                //dialogUtility.createSimpleDialog(null, "Ошибка!", "Неверный пароль!", "Ок", new View.OnClickListener() {
                                //    @Override
                                //    public void onClick(View view) {
                                //        dialogUtility.close();
                                //    }
                                //}, null, null);
                                //dialogUtility.show();
                            }
                        }
                    } else {
                        currentUserDatabase.child("userPassword").setValue(passwordEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(CreatePasswordActivity.this, EnterEmailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra("password", passwordEditText.getText().toString().trim())
                                        .putExtra("phone_number", getIntent().getStringExtra("phone_number")));
                            }
                        });
                    }
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

    private boolean validPassword(String s) {
        return !TextUtils.isEmpty(s) && Pattern.compile("[a-zA-Z0-9]{6,24}").matcher(s).matches();
    }
    @Override
    public void onBackPressed() {
        sendToSplash();
        finish();
        super.onBackPressed();
    }

}
