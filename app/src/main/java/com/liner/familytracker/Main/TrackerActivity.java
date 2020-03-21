package com.liner.familytracker.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.SplashActivity;
import com.liner.familytracker.Utils.Adapters.SyncMemberAdapter;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperActivity;
import com.liner.familytracker.Utils.HelperListener;

import java.util.ArrayList;
import java.util.List;

public class TrackerActivity extends HelperActivity {
    private RecyclerView memberRecyclerView;
    private SyncMemberAdapter syncMemberAdapter;
    private ImageButton addNewMemberBtn, moreBtn, test;
    private List<UserModel> userModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        memberRecyclerView = findViewById(R.id.memberRecycler);
        addNewMemberBtn = findViewById(R.id.addNewMember);
        test = findViewById(R.id.refreshData);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackerActivity.this, InviteCodeActivity.class));
            }
        });
        moreBtn = findViewById(R.id.moreSettings);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(TrackerActivity.this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        addNewMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.onFirebaseChanged();
                startActivity(new Intent(TrackerActivity.this, AddNewMemberActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public void onFirebaseChanged() {
        setupMemberRecycler();
    }

    private void setupMemberRecycler(){
        userModels = new ArrayList<>();
        syncMemberAdapter = new SyncMemberAdapter(TrackerActivity.this, userModels);
        Helper.getUserModel(firebaseUser.getUid(), new HelperListener() {
            @Override
            public void onFinish(UserModel userModel) {
                userModels.add(userModel);
                for(String item:userModel.getSynchronizedUsers()){
                    Helper.getUserModel(item, new HelperListener() {
                        @Override
                        public void onFinish(UserModel userModel) {
                            userModels.add(userModel);
                            syncMemberAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFinish(DatabaseReference databaseReference) {

                        }
                    });
                }
                syncMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFinish(DatabaseReference databaseReference) {

            }
        });
        memberRecyclerView.setAdapter(syncMemberAdapter);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(TrackerActivity.this, LinearLayoutManager.HORIZONTAL, false));
        memberRecyclerView.smoothScrollToPosition(0);
    }
}
