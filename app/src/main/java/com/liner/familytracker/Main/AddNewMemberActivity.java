package com.liner.familytracker.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.liner.familytracker.Application;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperActivity;
import com.liner.familytracker.Utils.HelperListener;
import com.liner.familytracker.Utils.HelperSetterListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewMemberActivity extends HelperActivity {
    private CodeScanner codeScanner;
    private Button cancelBtn;
    private MaterialDialog addMemberDialog;
    List<String> syncUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        cancelBtn = findViewById(R.id.cancel);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        addMemberDialog = new MaterialDialog.Builder(AddNewMemberActivity.this)
                .content("Подождите...")
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .widgetColor(getResources().getColor(R.color.accent_color))
                .progress(true, 0).build();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                final String searchedValue = result.getText();
                final String currentUserUID = firebaseUser.getUid();
                Query query = usersDatabase.orderByChild("inviteCode").equalTo(result.getText());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (final DataSnapshot child: dataSnapshot.getChildren()) {
                            if(child.child("inviteCode").getValue().equals(searchedValue) && !child.child("inviteCode").getValue().equals("")){
                                //todo нашли у пользователя искомый код

                                final String foundedUserUID = child.child("UID").getValue().toString().trim();
                                Helper.getUserModel(foundedUserUID, new HelperListener() {
                                    @Override
                                    public void onFinish(UserModel userModel) {
                                        boolean currentUIDexist = false;
                                        for(String synced:userModel.getSynchronizedUsers()){
                                            if(synced.equals(currentUserUID)){
                                                currentUIDexist = true;
                                                break;
                                            }
                                        }
                                        //todo если нету нашего пользователя добавляем своего пользователя.
                                        if(!currentUIDexist){
                                            userModel.getSynchronizedUsers().add(currentUserUID);
                                            Helper.setUserValue(foundedUserUID, "synchronizedUsers", userModel.getSynchronizedUsers(), new HelperSetterListener() {
                                                @Override
                                                public void onSuccess(Task<Void> task) {
                                                    Helper.getUserModel(currentUserUID, new HelperListener() {
                                                        @Override
                                                        public void onFinish(UserModel currentModel) {
                                                            //todo проверяем у себя нету ли найденого пользователя в нашем списке
                                                            boolean foundedUIDexist = false;
                                                            for(String synced:currentModel.getSynchronizedUsers()){
                                                                if(synced.equals(currentUserUID)){
                                                                    foundedUIDexist = true;
                                                                    break;
                                                                }
                                                            }
                                                            //todo найденого ид нету, добавляем себе найденного пользователя
                                                            if(!foundedUIDexist){
                                                                currentModel.getSynchronizedUsers().add(foundedUserUID);
                                                                Helper.setUserValue(currentUserUID, "synchronizedUsers", currentModel.getSynchronizedUsers(), new HelperSetterListener() {
                                                                    @Override
                                                                    public void onSuccess(Task<Void> task) {
                                                                        finish();
                                                                    }

                                                                    @Override
                                                                    public void onFailed() {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFinish(DatabaseReference databaseReference) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailed() {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFinish(DatabaseReference databaseReference) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
