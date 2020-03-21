package com.liner.familytracker;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Main.TrackerActivity;
import com.liner.familytracker.Register.CreateProfileActivity;
import com.liner.familytracker.Utils.ColorUtils;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperListener;

import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {
    private LinearLayout signInLayout, signUpLayout;
    private FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference usersDatabase, currentUserDatabase;

    //login
    private EditText loginEmail, loginPassword;
    private ImageButton loginShowPassword;
    private Button loginButton;
    boolean emailFieldCorrect = false;
    boolean passwordFieldCorrect = false;

    //Register
    private EditText registerEmail, registerPassword, registerPhone;
    private ImageButton registerShowPassword;
    private Button registerButton;
    boolean registerEmailFieldCorrect = false;
    boolean registerPasswordFieldCorrect = false;
    boolean registerPhoneFieldCorrect = false;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        signInLayout = findViewById(R.id.signInLayout);
        signUpLayout = findViewById(R.id.signUpLayout);
        loginEmail = findViewById(R.id.signEmailField);
        loginPassword = findViewById(R.id.signPasswordField);
        loginShowPassword = findViewById(R.id.signShowPassword);
        loginButton = findViewById(R.id.loginButton);
        TextView toRegisterLayout = findViewById(R.id.toRegisterLayout);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        registerEmail = findViewById(R.id.signUpEmailField);
        registerPassword = findViewById(R.id.signUpPasswordField);
        registerPhone = findViewById(R.id.sighUpPhoneField);
        registerShowPassword = findViewById(R.id.signUpShowPassword);
        registerButton = findViewById(R.id.continueRegistrationButton);
        TextView toLoginLayout = findViewById(R.id.toLoginLayout);
        toRegisterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInLayout.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        signInLayout.setVisibility(View.GONE);
                        signUpLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        });
        toLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpLayout.animate().alpha(0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        signUpLayout.setVisibility(View.GONE);
                        signInLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        });
        if(firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isAnonymous()){
            checkCurrentUser();
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    signInLayout.setAlpha(0);
                    signInLayout.setVisibility(View.VISIBLE);
                    signInLayout.animate().alpha(1f).setDuration(500);
                }
            }, 700);
            firebaseAuth.signOut();
        }

        {
            createEditTextValidation(loginEmail, "[a-zA-Z0-9]*@[a-zA-Z0-9]*.(com|ru|net)", new IEditTextListener() {
                @Override
                public void onValid() {
                    emailFieldCorrect = true;
                    if (passwordFieldCorrect) {
                        loginButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.colorPrimary));
                    }

                }

                @Override
                public void onNotValid() {
                    loginButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.backgroundColorDark));
                    emailFieldCorrect = false;
                }
            });
            createEditTextValidation(loginPassword, "[a-zA-Z0-9]{6,24}", new IEditTextListener() {
                @Override
                public void onValid() {
                    passwordFieldCorrect = true;
                    if (emailFieldCorrect) {
                        loginButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.colorPrimary));
                    }
                }

                @Override
                public void onNotValid() {
                    passwordFieldCorrect = false;
                    loginButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.backgroundColorDark));
                }
            });
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emailFieldCorrect) {
                        firebaseAuth.sendPasswordResetEmail(loginEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    new MaterialDialog.Builder(SplashActivity.this)
                                            .title("Восстановление пароля!")
                                            .content("На ваш эл. адрес была выслана инструкция по восстановлению пароля")
                                            .positiveText("Ок")
                                            .positiveColorRes(R.color.accent_color)
                                            .show();
                                } else {
                                    new MaterialDialog.Builder(SplashActivity.this)
                                            .title("Ошибка!")
                                            .content("Упс, что-то пошло не так. Побробуйте позже")
                                            .positiveText("Ок")
                                            .positiveColorRes(R.color.accent_color)
                                            .show();
                                }
                            }
                        });
                    } else {
                        new MaterialDialog.Builder(SplashActivity.this)
                                .title("Внимание!")
                                .content("Введите корректный адрес эл. почты!")
                                .positiveText("Ок")
                                .positiveColorRes(R.color.accent_color)
                                .show();
                    }
                }
            });
            loginShowPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (loginPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            loginShowPassword.setColorFilter(getColor(R.color.text_color));
                        } else {
                            loginShowPassword.setColorFilter(getResources().getColor(R.color.text_color));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            loginShowPassword.setColorFilter(getColor(R.color.accent_color));
                        } else {
                            loginShowPassword.setColorFilter(getResources().getColor(R.color.accent_color));
                        }
                        loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    loginPassword.setSelection(loginPassword.length());
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (emailFieldCorrect && passwordFieldCorrect) {
                        final MaterialDialog signInDialog = new MaterialDialog.Builder(SplashActivity.this)
                                .title("Вход")
                                .content("Выполняется вход, подождите")
                                .progressIndeterminateStyle(true)
                                .widgetColor(getResources().getColor(R.color.accent_color))
                                .progress(true, 0).build();
                        signInDialog.show();
                        firebaseAuth.signInWithEmailAndPassword(loginEmail.getText().toString().trim(), loginPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signInDialog.dismiss();
                                if (task.isSuccessful()) {
                                    //todo login success
                                    startActivity(new Intent(SplashActivity.this, TrackerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                } else {
                                    //todo wrong email or password
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new MaterialDialog.Builder(SplashActivity.this)
                                                    .title("Ошибка!")
                                                    .content("Неверный пароль или адрес эл. почты!")
                                                    .positiveText("Ок")
                                                    .positiveColorRes(R.color.accent_color)
                                                    .show();
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            });
        } // login section

        {
            createEditTextValidation(registerEmail, "[a-zA-Z0-9]*@[a-zA-Z0-9]*.(com|ru|net)", new IEditTextListener() {
                @Override
                public void onValid() {
                    registerEmailFieldCorrect = true;
                    if(registerPasswordFieldCorrect && registerPhoneFieldCorrect){
                        registerButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.colorPrimary));
                    }
                }

                @Override
                public void onNotValid() {
                    registerButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.backgroundColorDark));
                }
            });
            createEditTextValidation(registerPassword, "[a-zA-Z0-9]{6,24}", new IEditTextListener() {
                @Override
                public void onValid() {
                    registerPasswordFieldCorrect = true;
                    if(registerEmailFieldCorrect && registerPhoneFieldCorrect){
                        registerButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.colorPrimary));
                    }
                }

                @Override
                public void onNotValid() {
                    registerButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.backgroundColorDark));
                }
            });
            createEditTextValidation(registerPhone, "^[+][0-9]{12}$", new IEditTextListener() {
                @Override
                public void onValid() {
                    registerPhoneFieldCorrect = true;
                    if(registerEmailFieldCorrect && registerPasswordFieldCorrect){
                        registerButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.colorPrimary));
                    }
                }

                @Override
                public void onNotValid() {
                    registerButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.backgroundColorDark));
                }
            });
            registerShowPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (registerPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        registerPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            registerShowPassword.setColorFilter(getColor(R.color.text_color));
                        } else {
                            registerShowPassword.setColorFilter(getResources().getColor(R.color.text_color));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            registerShowPassword.setColorFilter(getColor(R.color.accent_color));
                        } else {
                            registerShowPassword.setColorFilter(getResources().getColor(R.color.accent_color));
                        }
                        registerPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    registerPassword.setSelection(registerPassword.length());
                }
            });
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(registerEmailFieldCorrect && registerPasswordFieldCorrect && registerPhoneFieldCorrect){
                        final MaterialDialog registerDialog = new MaterialDialog.Builder(SplashActivity.this)
                                .title("Регистрация")
                                .content("Выполняется регистрация, подождите")
                                .progressIndeterminateStyle(true)
                                .widgetColor(getResources().getColor(R.color.accent_color))
                                .progress(true, 0).build();
                        registerDialog.show();
                        Query query = usersDatabase.orderByChild("userEmail").equalTo(registerEmail.getText().toString().trim());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean founded = false;
                                for (DataSnapshot child: dataSnapshot.getChildren()) {
                                    String value = (String) child.child("userEmail").getValue();
                                    if(value.equals(registerEmail.getText().toString().trim()) && !value.equals("")){
                                        founded = true;
                                        break;
                                    }
                                }
                                if(founded){
                                    registerDialog.dismiss();
                                    new MaterialDialog.Builder(SplashActivity.this)
                                            .title("Ошибка!")
                                            .content("Данный адрес эл. почты уже используется")
                                            .positiveText("Ок")
                                            .positiveColorRes(R.color.accent_color)
                                            .show();
                                } else {
                                    firebaseAuth.createUserWithEmailAndPassword(registerEmail.getText().toString().trim(), registerPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                firebaseAuth.signOut();
                                                firebaseAuth.signInWithEmailAndPassword(registerEmail.getText().toString().trim(), registerPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()) {
                                                            currentUserDatabase = firebaseDatabase.getReference().child("Users").child(task.getResult().getUser().getUid());
                                                            usersDatabase.keepSynced(true);
                                                            currentUserDatabase.child("UID").setValue(task.getResult().getUser().getUid());
                                                            currentUserDatabase.child("userEmail").setValue(registerEmail.getText().toString().trim());
                                                            currentUserDatabase.child("userPassword").setValue(registerPassword.getText().toString().trim());
                                                            currentUserDatabase.child("phoneNumber").setValue(registerPhone.getText().toString().trim());
                                                            currentUserDatabase.child("inviteCode").setValue(UserModel.generateInviteCode());
                                                            registerDialog.dismiss();
                                                            startActivity(new Intent(SplashActivity.this, CreateProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                            finish();
                                                        } else {
                                                            //todo login failed
                                                            registerDialog.dismiss();
                                                            new MaterialDialog.Builder(SplashActivity.this)
                                                                    .title("Упс!")
                                                                    .content("Невозможно войти в аккаунт, попробуйте позже")
                                                                    .positiveText("Ок")
                                                                    .positiveColorRes(R.color.accent_color)
                                                                    .show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                //todo create user failed
                                                registerDialog.dismiss();
                                                new MaterialDialog.Builder(SplashActivity.this)
                                                        .title("Упс!")
                                                        .content("Невозможно войти в аккаунт, попробуйте позже")
                                                        .positiveText("Ок")
                                                        .positiveColorRes(R.color.accent_color)
                                                        .show();
                                            }
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
    }


    private void checkCurrentUser(){
        final MaterialDialog checkAccountDialog = new MaterialDialog.Builder(SplashActivity.this)
                .content("Подключение к серверам...")
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .widgetColor(getResources().getColor(R.color.accent_color))
                .progress(true, 0).build();
        checkAccountDialog.show();
        Query query = usersDatabase.orderByChild("UID").equalTo(firebaseAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot child: dataSnapshot.getChildren()) {
                    String value = (String) child.child("UID").getValue();
                    if(value.equals(firebaseAuth.getCurrentUser().getUid()) && !value.equals("")){
                        Helper.getUserModel(value, new HelperListener() {
                            @Override
                            public void onFinish(UserModel userModel) {
                                if(!userModel.isRegisterFinished().equals("true")){
                                    firebaseAuth.signInWithEmailAndPassword(child.child("userEmail").getValue().toString(), child.child("userPassword").getValue().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                checkAccountDialog.dismiss();
                                                startActivity(new Intent(SplashActivity.this, CreateProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                                finish();
                                            } else {
                                                checkAccountDialog.dismiss();
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        signInLayout.setAlpha(0);
                                                        signInLayout.setVisibility(View.VISIBLE);
                                                        signInLayout.animate().alpha(1f).setDuration(500);
                                                    }
                                                }, 700);
                                                firebaseAuth.signOut();
                                            }
                                        }
                                    });
                                } else {
                                    checkAccountDialog.dismiss();
                                    startActivity(new Intent(SplashActivity.this, TrackerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                }
                            }

                            @Override
                            public void onFinish(DatabaseReference databaseReference) {

                            }
                        });
                        return;
                    } else {
                        checkAccountDialog.dismiss();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                signInLayout.setAlpha(0);
                                signInLayout.setVisibility(View.VISIBLE);
                                signInLayout.animate().alpha(1f).setDuration(500);
                            }
                        }, 700);
                        firebaseAuth.signOut();
                        return;
                    }
                }
                checkAccountDialog.dismiss();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signInLayout.setAlpha(0);
                        signInLayout.setVisibility(View.VISIBLE);
                        signInLayout.animate().alpha(1f).setDuration(500);
                    }
                }, 700);
                firebaseAuth.signOut();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createEditTextValidation(EditText editText, final String regex, final IEditTextListener listener){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().trim();
                if(Pattern.compile(regex).matcher(result).matches()){
                    listener.onValid();
                } else {
                    listener.onNotValid();
                }
            }
        });
    }
    private interface IEditTextListener{
        void onValid();
        void onNotValid();
    }

}
