package com.liner.familytracker.Utils;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liner.familytracker.Application;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    protected Application application;
    private static FirebaseHelper firebaseHelper;
    private Context context;
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public DatabaseReference usersDatabase;
    public UserModel userModel;
    public List<UserModel> synchronizedUsers;
    public FirebaseUser firebaseUser;

    public static FirebaseHelper getInstance(Context context, Application application) {
        if(firebaseHelper == null){
            return firebaseHelper = new FirebaseHelper(context, application);
        }
        return firebaseHelper;
    }

    private FirebaseHelper(Context context, Application application){
        this.context = context;
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        usersDatabase.keepSynced(true);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseUser != null) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

    }


    public void sendToSplash(){

    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public DatabaseReference getUsersDatabase() {
        return usersDatabase;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public List<UserModel> getSynchronizedUsers() {
        return synchronizedUsers;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }




}
