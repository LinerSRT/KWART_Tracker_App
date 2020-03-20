package com.liner.familytracker.Main.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liner.familytracker.CoreUtils.CoreFragment;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;

public class MapViewFragment extends CoreFragment {

    @Override
    public View onInflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.map_view_fragment, container, false);
    }

    @Override
    public void onUserDataChanged(UserModel userModel) {

    }

    @Override
    public void onUserLoggedOut() {

    }

    @Override
    public void onUserSignedIn() {

    }
}