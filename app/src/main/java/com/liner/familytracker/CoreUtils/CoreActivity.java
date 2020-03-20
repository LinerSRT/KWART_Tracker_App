package com.liner.familytracker.CoreUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserJoinedCircles;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.SplashActivity;

import java.util.ArrayList;
import java.util.List;


public abstract class CoreActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public DatabaseReference usersDatabase, circlesDatabase, currentUserDatabase;
    public UserModel userModel;
    public List<UserJoinedCircles> userJoinedCircles;
    public List<CircleModel> userCircles;
    public List<UserModel> circleUsers;

    public FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            sendToSplash();
        }
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        currentUserDatabase = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid());
        circlesDatabase = firebaseDatabase.getReference().child("Circles");
        usersDatabase.keepSynced(true);
        circlesDatabase.keepSynced(true);
        userJoinedCircles = new ArrayList<>();
        userCircles = new ArrayList<>();
        circleUsers = new ArrayList<>();
        currentUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (firebaseUser != null && !firebaseUser.isAnonymous()) {
                    if(userModel == null){
                        userModel = new UserModel();
                    }
                    if (dataSnapshot.hasChild("UID")) {
                        userModel.setUID(dataSnapshot.child("UID").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("phoneNumber")) {
                        userModel.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("userName")) {
                        userModel.setUserName(dataSnapshot.child("userName").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("userPassword")) {
                        userModel.setUserPassword(dataSnapshot.child("userPassword").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("userEmail")) {
                        userModel.setUserEmail(dataSnapshot.child("userEmail").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("longtitude")) {
                        userModel.setLongtitude(dataSnapshot.child("longtitude").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("latitude")) {
                        userModel.setLatitude(dataSnapshot.child("latitude").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("inviteCode")) {
                        userModel.setInviteCode(dataSnapshot.child("inviteCode").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("photoUrl")) {
                        userModel.setPhotoUrl(dataSnapshot.child("photoUrl").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("deviceToken")) {
                        userModel.setDeviceToken(dataSnapshot.child("deviceToken").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("registerFinished")) {
                        userModel.setRegisterFinished(dataSnapshot.child("registerFinished").getValue().toString());
                    }
                    if (dataSnapshot.hasChild("joinedCircles")) {
                        for (DataSnapshot circle: dataSnapshot.child("joinedCircles").getChildren()) {
                            Log.e("TAGTAG", "Joined: "+circle.getValue().toString());
                            //userJoinedCircles.add(circle.getValue(UserJoinedCircles.class));
                        }
                    }
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
                        sendToSplash();
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
