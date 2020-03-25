package com.liner.familytracker.Main;

import android.os.Bundle;

import com.liner.familytracker.R;
import com.liner.familytracker.Utils.HelperActivity;

public class SettingsActivity extends HelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onFirebaseChanged() {

    }
}
