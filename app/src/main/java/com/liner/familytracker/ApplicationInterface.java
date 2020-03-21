package com.liner.familytracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Utils.HelperActivity;

public interface ApplicationInterface {
    void onActivityStopped(HelperActivity activity);
    void onActivityStarted(HelperActivity activity);
    void onActivityResumed(HelperActivity activity);
    void onActivityPaused(HelperActivity activity);
    void onActivityDestroyed(HelperActivity activity);
    void onActivityCreated(HelperActivity activity, Bundle savedInstanceState);
    void onFirebaseChanged();
}
