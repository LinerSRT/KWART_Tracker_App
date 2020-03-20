package com.liner.familytracker.CoreUtils;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.SplashActivity;

import java.util.Objects;


public abstract class CoreFragment extends Fragment{
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public DatabaseReference usersDatabase, circlesDatabase;
    public UserModel userModel;
    public FirebaseUser firebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        circlesDatabase = firebaseDatabase.getReference().child("Circles");
        usersDatabase.keepSynced(true);
        circlesDatabase.keepSynced(true);
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(firebaseUser != null && !firebaseUser.isAnonymous()) {
                    userModel = dataSnapshot.child(Objects.requireNonNull(firebaseAuth.getUid())).getValue(UserModel.class);
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
                if(firebaseUser != null){
                    onUserSignedIn();
                } else {
                    onUserLoggedOut();
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

}
