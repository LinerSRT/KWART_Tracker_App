package com.liner.familytracker.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.liner.familytracker.ColorUtils;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;

import java.util.regex.Pattern;

public class EnterEmailActivity extends CoreActivity {
    private EditText emailEdit;
    private Button nextStep;
    boolean emailValid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);
        emailEdit = findViewById(R.id.emailEditText);
        nextStep = findViewById(R.id.nextStep);
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().trim();
                if(validEmail(result)){
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(EnterEmailActivity.this, R.attr.colorPrimary));
                    emailValid = true;
                } else {
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(EnterEmailActivity.this, R.attr.backgroundColorDark));
                    emailValid = false;
                }
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailValid){
                    Query query = usersDatabase.orderByChild("userEmail").equalTo(emailEdit.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean founded = false;
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                String value = (String) child.child("userEmail").getValue();
                                if(value.equals(emailEdit.getText().toString().trim()) && !value.equals("")){
                                    founded = true;
                                    break;
                                }
                            }
                            if(founded){
                                //final LDialogUtility dialogUtility = new LDialogUtility(EnterEmailActivity.this);
                                //dialogUtility.createSimpleDialog(null, "Внимание!", "Данный эл. адрес уже используется!", "Ок", new View.OnClickListener() {
                                //    @Override
                                //    public void onClick(View view) {
                                //        dialogUtility.close();
                                //    }
                                //}, null, null);
                                //dialogUtility.show();
                            } else {
                                currentUserDatabase.child("userEmail").setValue(emailEdit.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(EnterEmailActivity.this, CreateProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .putExtra("password", getIntent().getStringExtra("password"))
                                                .putExtra("phone_number", getIntent().getStringExtra("phone_number"))
                                                .putExtra("email", emailEdit.getText().toString().trim()));
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

    private boolean validEmail(String s) {
        return !TextUtils.isEmpty(s) && Pattern.compile("[a-zA-Z0-9]*@[a-zA-Z0-9]*.(com|ru|net)").matcher(s).matches();
    }
    @Override
    public void onBackPressed() {
        sendToSplash();
        finish();
        super.onBackPressed();
    }
}
