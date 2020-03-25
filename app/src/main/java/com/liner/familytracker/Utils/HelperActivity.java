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
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;

import java.util.ArrayList;
import java.util.List;

public abstract class HelperActivity extends AppCompatActivity {
    protected Application application;
    public PreferenceManager preferenceManager;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;
    public DatabaseReference usersDatabase;
    public FirebaseUser firebaseUser;
    public PrefHelper prefHelper;
    public UserModel currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        application = (Application) getApplication();
        application.onActivityCreated(this, savedInstanceState);
        preferenceManager = PreferenceManager.getInstance(this);
        prefHelper = new PrefHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        preferenceManager.saveString("user_uid", firebaseUser.getUid());
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = firebaseDatabase.getReference().child("Users");
        if(prefHelper.isUserExist(firebaseUser.getUid())){
            currentUser = prefHelper.getUser(preferenceManager.getString("user_uid", "null"));
        } else {
            Helper.getUserModel(new HelperListener() {
                @Override
                public void onFinish(UserModel userModel) {
                    currentUser = userModel;
                    prefHelper.saveUser(currentUser);
                }

                @Override
                public void onFinish(DatabaseReference databaseReference) {

                }
            });
        }
        Helper.getUserDatabase().child("uid").setValue(firebaseUser.getUid());
        usersDatabase.keepSynced(true);
        usersDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(firebaseUser.getUid())){
                    currentUser = getModel(dataSnapshot);
                    prefHelper.saveUser(currentUser);
                    onFirebaseChanged();
                } else {
                    for(String item:currentUser.getSynchronizedUsers()){
                        if(dataSnapshot.getKey().equals(item)){
                            prefHelper.saveUser(getModel(dataSnapshot));
                            onFirebaseChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(firebaseUser.getUid())){
                    currentUser = getModel(dataSnapshot);
                    prefHelper.saveUser(currentUser);
                    onFirebaseChanged();
                } else {
                    for(String item:currentUser.getSynchronizedUsers()){
                        if(dataSnapshot.getKey().equals(item)){
                            prefHelper.saveUser(getModel(dataSnapshot));
                            onFirebaseChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getKey().equals(firebaseUser.getUid())){
                    currentUser = getModel(dataSnapshot);
                    prefHelper.saveUser(currentUser);
                    onFirebaseChanged();
                } else {
                    for(String item:currentUser.getSynchronizedUsers()){
                        if(dataSnapshot.getKey().equals(item)){
                            prefHelper.saveUser(getModel(dataSnapshot));
                            onFirebaseChanged();
                        }
                    }
                }
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

    private UserModel getModel(DataSnapshot dataSnapshot){
        UserModel userModel = new UserModel();
        if (dataSnapshot.hasChild("uid")) {
            userModel.setUid(dataSnapshot.child("uid").getValue().toString());
        }
        if (dataSnapshot.hasChild("phoneNumber")) {
            userModel.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
        }
        if (dataSnapshot.hasChild("userName")) {
            userModel.setUserName(dataSnapshot.child("userName").getValue().toString());
        }
        if (dataSnapshot.hasChild("inviteCode")) {
            userModel.setInviteCode(dataSnapshot.child("inviteCode").getValue().toString());
        }
        if (dataSnapshot.hasChild("photoUrl")) {
            userModel.setPhotoUrl(dataSnapshot.child("photoUrl").getValue().toString());
        }
        List<String> usersSynced = new ArrayList<>();
        if (dataSnapshot.hasChild("synchronizedUsers")) {
            for (DataSnapshot item : dataSnapshot.child("synchronizedUsers").getChildren()) {
                usersSynced.add(item.getValue().toString());
            }
        }
        userModel.setSynchronizedUsers(usersSynced);
        DeviceStatus deviceStatus = new DeviceStatus();
        if(dataSnapshot.hasChild("deviceStatus")){
            deviceStatus = dataSnapshot.child("deviceStatus").getValue(DeviceStatus.class);
        }
        userModel.setDeviceStatus(deviceStatus);
        return userModel;
    }
}
