package com.liner.familytracker.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.liner.familytracker.R;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperActivity;

public class MoreActivity extends HelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
    }

    @Override
    public void onFirebaseChanged() {

    }
}
