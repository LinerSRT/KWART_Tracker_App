package com.liner.familytracker.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.Services.ControllerService;
import com.liner.familytracker.SplashActivity;
import com.liner.familytracker.Utils.Adapters.SyncMemberAdapter;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperActivity;
import com.liner.familytracker.Utils.HelperListener;

import java.util.ArrayList;
import java.util.List;

public class TrackerActivity extends HelperActivity implements OnMapReadyCallback {
    private RecyclerView memberRecyclerView;
    private SyncMemberAdapter syncMemberAdapter;
    private ImageButton addNewMemberBtn, moreBtn, test;
    private ArrayList<UserModel> userModels;

    private GoogleMap mMap;

    private LatLng latLng = new LatLng(0, 0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        startService(new Intent(this, ControllerService.class));
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
            }
        });
        addNewMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.onFirebaseChanged();
                startActivity(new Intent(TrackerActivity.this, AddNewMemberActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });



        MapFragment map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView));
        map.getMapAsync(this);


        setupMemberRecycler();










    }

    private int size = 0;
    @Override
    public void onFirebaseChanged() {
        setupMemberRecycler();
        if(mMap != null){
            latLng = new LatLng(currentUser.getDeviceStatus().getLocationLat(), currentUser.getDeviceStatus().getLocationLon());
            //MarkerOptions marker = new MarkerOptions().position(latLng).title("Anuradha Rajashekar");
            ////marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_black_24dp));
            //mMap.addMarker(marker);
            // mMap.addMarker(new MarkerOptions().position(sydney).title("Anuradha's location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

      /*  LatLng location2 = new LatLng(37.402276,-121.942161);
        mMap.addMarker(new MarkerOptions().position(location2).title("Anuradha's new location"));
       mMap.moveCamera(CameraUpdateFactory.newLatLng(location2));*/

            //zoom into a particular position
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
            mMap.moveCamera(zoom);
            mMap.animateCamera(zoom);
        }
    }

    private void setupMemberRecycler(){
        SyncMemberAdapter syncMemberAdapter = new SyncMemberAdapter(TrackerActivity.this, currentUser.getSynchronizedUsers());
        memberRecyclerView.setAdapter(syncMemberAdapter);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(TrackerActivity.this, LinearLayoutManager.VERTICAL, false));
        memberRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onBackPressed() {

    }
}
