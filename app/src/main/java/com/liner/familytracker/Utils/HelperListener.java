package com.liner.familytracker.Utils;

import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.DatabaseModels.UserModel;

public interface HelperListener {
    void onFinish(UserModel userModel);
    void onFinish(DatabaseReference databaseReference);
}
