package com.liner.familytracker.Utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.liner.familytracker.DatabaseModels.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static void getUserModel(String useruid, final HelperListener helperListener){
        final UserModel userModel = new UserModel();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference currentUserReference = firebaseDatabase.getReference().child("Users").child(useruid);
        currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                List<String> usersSynced = new ArrayList<>();
                if (dataSnapshot.hasChild("synchronizedUsers")) {
                    for (DataSnapshot item : dataSnapshot.child("synchronizedUsers").getChildren()) {
                        usersSynced.add(item.getValue().toString());
                    }
                }
                userModel.setSynchronizedUsers(usersSynced);
                helperListener.onFinish(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void setUserModel(String useruid, UserModel userModel, final HelperSetterListener helperListener){
        getUserDatabase(useruid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(helperListener != null) {
                    if (task.isSuccessful()) {
                        helperListener.onSuccess(task);
                    } else {
                        helperListener.onFailed();
                    }
                }
            }
        });
    }

    public static void setUserValue(String useruid, String key, Object object, final HelperSetterListener helperListener){
        getUserDatabase(useruid).child(key).setValue(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    helperListener.onSuccess(task);
                } else {
                    helperListener.onFailed();
                }
            }
        });
    }

    public static DatabaseReference getUserDatabase(String useruid){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        return firebaseDatabase.getReference().child("Users").child(useruid);
    }

}
