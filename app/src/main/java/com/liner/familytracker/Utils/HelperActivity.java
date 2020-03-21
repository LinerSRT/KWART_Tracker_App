package com.liner.familytracker.Utils;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liner.familytracker.Application;
import com.liner.familytracker.DatabaseModels.UserModel;

public abstract class HelperActivity extends AppCompatActivity {
    protected Application application;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public DatabaseReference usersDatabase;
    public FirebaseUser firebaseUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        application = (Application) getApplication();
        application.onActivityCreated(this, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        DatabaseReference currentUserDatabase = Helper.getUserDatabase(firebaseUser.getUid());
        currentUserDatabase.keepSynced(true);
        currentUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(application != null){
                    if(firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isAnonymous()){
                        application.onFirebaseChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        usersDatabase.keepSynced(true);
        usersDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                Helper.getUserModel(firebaseUser.getUid(), new HelperListener() {
                    @Override
                    public void onFinish(UserModel userModel) {
                        if(userModel.getUID().equals(dataSnapshot.child("UID").getValue().toString())){
                            application.onFirebaseChanged();
                        } else {
                            for(String member:userModel.getSynchronizedUsers()){
                                if(member.equals(dataSnapshot.child("UID").getValue().toString())){
                                    application.onFirebaseChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFinish(DatabaseReference databaseReference) {

                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        application.onActivityPaused(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        application.onActivityResumed(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.onActivityDestroyed(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        application.onActivityStarted(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        application.onActivityStopped(this);
    }

    public abstract void onFirebaseChanged();
}
