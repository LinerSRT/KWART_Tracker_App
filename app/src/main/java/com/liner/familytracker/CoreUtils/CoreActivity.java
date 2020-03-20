package com.liner.familytracker.CoreUtils;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.SplashActivity;


public abstract class CoreActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public DatabaseReference usersDatabase, circlesDatabase, currentUserDatabase;
    public UserModel userModel;
    public FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            firebaseAuth.signOut();
            firebaseAuth.signInAnonymously();
        }
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        currentUserDatabase = firebaseDatabase.getReference().child("Users").child(FirebaseInstanceId.getInstance().getToken());
        circlesDatabase = firebaseDatabase.getReference().child("Circles");
        usersDatabase.keepSynced(true);
        circlesDatabase.keepSynced(true);
        currentUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (firebaseUser != null && !firebaseUser.isAnonymous()) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    onUserDataChanged(userModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    if(firebaseUser.isAnonymous()){
                        //todo send to login
                    } else {
                        onUserSignedIn();
                    }
                } else {
                    onUserLoggedOut();
                    sendToSplash();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onCreate(savedInstanceState);
    }

    public abstract void onCircleDataChanged(CircleModel circleModel);
    public abstract void onUserDataChanged(UserModel userModel);
    public abstract void onUserLoggedOut();
    public abstract void onUserSignedIn();



    public void sendToSplash(){
        startActivity(new Intent(this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
