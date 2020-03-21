package com.liner.familytracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.liner.familytracker.Utils.HelperActivity;

import java.util.ArrayList;

public class Application extends android.app.Application implements ApplicationInterface {
    static ArrayList<HelperActivity> activities;
    private FirebaseAuth firebaseAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        activities = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onActivityStopped(HelperActivity activity) {

    }

    @Override
    public void onActivityStarted(HelperActivity activity) {

    }

    @Override
    public void onActivityResumed(HelperActivity activity) {

    }

    @Override
    public void onActivityPaused(HelperActivity activity) {

    }

    @Override
    public void onActivityDestroyed(HelperActivity activity) {
        int idx = 0;
        for(AppCompatActivity item:activities){
            if(item.getClass() == activity.getClass()){
                activities.remove(idx);
                break;
            }
            idx++;
        }
    }

    @Override
    public void onActivityCreated(HelperActivity activity, Bundle savedInstanceState) {
        activities.add(activity);
    }

    @Override
    public void onFirebaseChanged() {
        if(firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isAnonymous()){
            for(HelperActivity item:activities){
                item.onFirebaseChanged();
            }
        }
    }
}
