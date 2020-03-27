package com.liner.familytracker.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.Invite.InviteActivity;
import com.liner.familytracker.R;
import com.liner.familytracker.Services.ControllerService;
import com.liner.familytracker.SplashActivity;
import com.liner.familytracker.Utils.Adapters.SyncMemberAdapter;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperActivity;
import com.liner.familytracker.Utils.MapMarkerUtils;
import com.liner.familytracker.Views.LMapFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrackerActivity extends HelperActivity implements OnMapReadyCallback {
    //Map
    private GoogleMap googleMap;
    private ImageButton findMyLocationBtn, mapStyleBtn, refreshDataBtn;
    
    //Main
    private NestedScrollView mainScroll;
    private ImageButton settingsBtn;
    private RecyclerView peopleRecyclerView;
    
    //Profile
    private CircleImageView profilePhoto;
    private TextView profileName, profileEmail;
    private ImageButton addNewPeopleBtn, shareCodeBtn, profileMoreBtn, profileLogoutBtn;
   
   


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        if(!ControllerService.isServiceRunning(this)){
            ContextCompat.startForegroundService(this, new Intent(this, ControllerService.class));
        }
        findMyLocationBtn = findViewById(R.id.mapFindMyLocationBtn);
        mapStyleBtn = findViewById(R.id.mapStyleSwitch);
        refreshDataBtn = findViewById(R.id.refreshMapView);
        
        mainScroll = findViewById(R.id.nestedScroll);
        settingsBtn = findViewById(R.id.settingsButton);
        peopleRecyclerView = findViewById(R.id.memberRecycler);
        
        profilePhoto = findViewById(R.id.profilePhoto);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileLogoutBtn = findViewById(R.id.logout);
        addNewPeopleBtn = findViewById(R.id.addPeopleBtn);
        shareCodeBtn = findViewById(R.id.shareCodeBtn);
        profileMoreBtn = findViewById(R.id.profileMore);
        
        mainScroll.getParent().requestChildFocus(mainScroll, mainScroll);
        
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackerActivity.this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        
        refreshDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.onFirebaseChanged();
            }
        });
        
        shareCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackerActivity.this, ShareCodeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        
        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(TrackerActivity.this, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        
        addNewPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackerActivity.this, AddNewMemberActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        
        if (googleMap == null) {
            SupportMapFragment mapFragment = (LMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(this);
        }
        updateValue();
    }
    @Override
    public void onFirebaseChanged() {
        updateValue();

    }

    private void updateValue(){
        if(currentUser != null) {
            peopleRecyclerView.setAdapter(new SyncMemberAdapter(TrackerActivity.this, currentUser.getSynchronizedUsers()));
            if (currentUser.getUserEmail() != null)
                profileEmail.setText(currentUser.getUserEmail());
            if (currentUser.getPhotoUrl() != null)
                Picasso.with(this).load(currentUser.getPhotoUrl()).into(profilePhoto);
            else
                profilePhoto.setImageResource(R.drawable.user_profile_temp);
            if (currentUser.getUserName() != null)
                profileName.setText(currentUser.getUserName());
            if(googleMap != null){
                googleMap.clear();
                LatLng latLng = new LatLng(currentUser.getDeviceStatus().getLocationLat(), currentUser.getDeviceStatus().getLocationLon());
                createUserMarker(prefHelper.getUser(firebaseUser.getUid()));
                for(String item:currentUser.getSynchronizedUsers()){
                    if(prefHelper.isUserExist(item)){
                        createUserMarker(prefHelper.getUser(item));
                    }
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 1000, null);
            }
        }
    }


    private void createUserMarker(final UserModel model){
        if(model.getPhotoUrl() != null) {
            MapMarkerUtils.createUserMarker(this, model.getPhotoUrl(), new MapMarkerUtils.IListener() {
                @Override
                public void onMarkerReady(Bitmap bitmap) {
                    MarkerOptions options = new MarkerOptions().position(new LatLng(model.getDeviceStatus().getLocationLat(), model.getDeviceStatus().getLocationLon()));
                    options.title(model.getUserName());
                    options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    options.anchor(0.5f, 0.907f);
                    CircleOptions co = new CircleOptions();
                    co.center(new LatLng(model.getDeviceStatus().getLocationLat(), model.getDeviceStatus().getLocationLon()));
                    co.radius(model.getDeviceStatus().getLocationAccuracy());
                    co.fillColor(Color.parseColor("#602f90e5"));
                    co.strokeColor(Color.parseColor("#BBBBBB"));
                    co.strokeWidth(2.0f);
                    googleMap.addCircle(co);
                    googleMap.addMarker(options);
                }
            });
        } else {
            MapMarkerUtils.createUserMarker(this, BitmapFactory.decodeResource(getResources(), R.drawable.user_profile_temp), new MapMarkerUtils.IListener() {
                @Override
                public void onMarkerReady(Bitmap bitmap) {
                    MarkerOptions options = new MarkerOptions().position(new LatLng(model.getDeviceStatus().getLocationLat(), model.getDeviceStatus().getLocationLon()));
                    options.title(model.getUserName());
                    options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    options.anchor(0.5f, 0.907f);
                    CircleOptions co = new CircleOptions();
                    co.center(new LatLng(model.getDeviceStatus().getLocationLat(), model.getDeviceStatus().getLocationLon()));
                    co.radius(model.getDeviceStatus().getLocationAccuracy());
                    co.fillColor(Color.parseColor("#602f90e5"));
                    co.strokeColor(Color.parseColor("#BBBBBB"));
                    co.strokeWidth(2.0f);
                    googleMap.addCircle(co);
                    googleMap.addMarker(options);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMapToolbarEnabled(true);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        ((LMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView)).setListener(new LMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mainScroll.requestDisallowInterceptTouchEvent(true);
            }
        });
        this.googleMap.clear();
        LatLng latLng = new LatLng(currentUser.getDeviceStatus().getLocationLat(), currentUser.getDeviceStatus().getLocationLon());
        createUserMarker(prefHelper.getUser(firebaseUser.getUid()));
        for(String item:currentUser.getSynchronizedUsers()){
            if(prefHelper.isUserExist(item)){
                createUserMarker(prefHelper.getUser(item));
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
