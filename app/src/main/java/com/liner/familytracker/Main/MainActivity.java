package com.liner.familytracker.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class MainActivity extends CoreActivity {
    private RecyclerView circleUsersRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleUsersRecycler = findViewById(R.id.circleUsersRecycler);





        Button bv = findViewById(R.id.safagasgasg);
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Hi There! Click this card and see what happens.")
                .setTarget(bv)
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .show();
//
        bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
            }
        });
    }

    @Override
    public void onCircleDataChanged(CircleModel circleModel) {

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
