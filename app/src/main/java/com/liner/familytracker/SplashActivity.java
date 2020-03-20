package com.liner.familytracker;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.liner.familytracker.Main.MainActivity;

import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {
    private LinearLayout headerLayout, signInLayout, signUpLayout;
    private FirebaseAuth firebaseAuth;

    //login
    private EditText loginEmail, loginPassword;
    private ImageButton loginShowPassword;
    private Button loginButton;
    private TextView toRegisterLayout;
    boolean emailFieldCorrect = false;
    boolean passwordFieldCorrect = false;

    //Register
    private EditText registerEmail, registerPassword, registerPhone;
    private ImageButton registerShowPassword;
    private Button registerButton;
    private TextView toLoginLayout;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
        headerLayout = findViewById(R.id.headerLayout);
        signInLayout = findViewById(R.id.signInLayout);
        signUpLayout = findViewById(R.id.signUpLayout);
        loginEmail = findViewById(R.id.signEmailField);
        loginPassword = findViewById(R.id.signPasswordField);
        loginShowPassword = findViewById(R.id.signShowPassword);
        loginButton = findViewById(R.id.loginButton);
        toRegisterLayout = findViewById(R.id.toRegisterLayout);
        registerEmail = findViewById(R.id.signUpEmailField);
        registerPassword = findViewById(R.id.signUpPasswordField);
        registerPhone = findViewById(R.id.sighUpPhoneField);
        registerShowPassword = findViewById(R.id.signUpShowPassword);
        registerButton = findViewById(R.id.continueRegistrationButton);
        toLoginLayout = findViewById(R.id.toLoginLayout);
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                signInLayout.setAlpha(0);
                signInLayout.setVisibility(View.VISIBLE);
                signInLayout.animate().alpha(1f).setDuration(500);
            }
        }, 700);



        createEditTextValidation(loginEmail, "[a-zA-Z0-9]*@[a-zA-Z0-9]*.(com|ru|net)", new IEditTextListener() {
            @Override
            public void onValid() {
                emailFieldCorrect = true;
                if(passwordFieldCorrect) {
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
                if(emailFieldCorrect) {
                    loginButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.colorPrimary));
                }
            }

            @Override
            public void onNotValid() {
                passwordFieldCorrect = false;
                loginButton.setBackgroundColor(ColorUtils.getThemeColor(SplashActivity.this, R.attr.backgroundColorDark));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailFieldCorrect && passwordFieldCorrect){
                    firebaseAuth.signInWithEmailAndPassword(loginEmail.getText().toString().trim(), loginPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //todo login success

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

    public boolean validCellPhone(String number){
        return number.matches("^[+][0-9]{12}$");
    }





























    private void old(){
        if(firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isAnonymous()){
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        } else {
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //float measuredHeight = (float) findViewById(R.id.main_layout).getMeasuredHeight();
                    //float a = ((((float) 50) * getResources().getDisplayMetrics().density) + 0.5f);
                    //logoLayout.animate().setDuration(500).y(a).setListener(new Animator.AnimatorListener() {
                    //    @Override
                    //    public void onAnimationStart(Animator animator) {
//
                    //    }
//
                    //    @Override
                    //    public void onAnimationEnd(Animator animator) {
                    //        contentLayout.setAlpha(0.0f);
                    //        loginView.setAlpha(0f);
                    //        contentLayout.setVisibility(View.VISIBLE);
                    //        loginView.setVisibility(View.VISIBLE);
                    //        contentLayout.animate().alpha(1.0f).setDuration(500);
                    //        loginView.animate().alpha(1.0f).setDuration(500);
//
                    //    }
//
                    //    @Override
                    //    public void onAnimationCancel(Animator animator) {
//
                    //    }
//
                    //    @Override
                    //    public void onAnimationRepeat(Animator animator) {
//
                    //    }
                    //});
                    //contentLayout.getLayoutParams().height = (int) ((measuredHeight - ((float) contentLayout.getMeasuredHeight())) - a);
                    //startRegistrationView.setOnClickListener(new View.OnClickListener() {
                    //    @Override
                    //    public void onClick(View view) {
                    //        startActivity(new Intent(SplashActivity.this, EnterPhoneNumberActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    //    }
                    //});
                }
            });

        }
    }
    //@Override
    //public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
    //        return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
    //                insets.getSystemWindowInsetBottom()));
    //    } else {
    //        return insets;
    //    }
    //}
}
