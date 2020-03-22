package com.liner.familytracker.Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

public class ControllerService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public int UPDATE_INTERVAL_MINUTES = 2;
    public boolean SEND_USER_LOCATION = true;
    public boolean USE_LBS = false;
    public Location currentLocation = null;
    public GoogleApiClient googleApiClient;


    public boolean serviceForeground = false;
    public Handler serviceHandler = new Handler();

    private int SPEED_LIMIT_KM = 20;
    private boolean SPEED_LIMIT_ENABLED = false;
    private float prevLat, prevLon, prevLocTime;

    private Runnable updateDevice = new Runnable() {
        @Override
        public void run() {
            ControllerServiceUtils.updateDeviceStatus(ControllerService.this, currentLocation);
            serviceHandler.postDelayed(updateDevice, (UPDATE_INTERVAL_MINUTES*1000)*1000);
        }
    };

    @Override
    public void onCreate() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentLocation = null;
        USE_LBS = false;
        SPEED_LIMIT_KM = 20;
        SEND_USER_LOCATION = true;
        UPDATE_INTERVAL_MINUTES = 1;
        ControllerServiceUtils.startForeground(this, this);
        ControllerServiceUtils.updateDeviceStatus(this, currentLocation);
        Permissions.check(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                googleApiClient.connect();
            }

        });
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = LocationRequest.create();
        request.setPriority(100);
        request.setInterval(10000);
        request.setFastestInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
        serviceHandler.postDelayed(updateDevice, 10000);
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopService();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopLocationUpdates();
        stopService();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            return;
        }
        if(SPEED_LIMIT_ENABLED){
            prevLat = (float) location.getLatitude();
            prevLon = (float) location.getLongitude();
            prevLocTime = location.getTime();
            float time = location.getTime() - prevLocTime;
            if (time == 0.0f || prevLat == 0d|| prevLon == 0d || time > ((float) (UPDATE_INTERVAL_MINUTES * 2 * 60000))) {
                ControllerServiceUtils.updateDeviceStatus(this, location);
                return;
            }
            double distance = ControllerServiceUtils.distance(prevLat, prevLon, location.getLatitude(), location.getLongitude());
            double d3 = (double) (time / 3600000.0f);
            if (distance / d3 < ((double) SPEED_LIMIT_KM)) {
                ControllerServiceUtils.updateDeviceStatus(this, location);
                return;
            }
            //todo warn about speed limit
            return;
        }
        ControllerServiceUtils.updateDeviceStatus(this, location);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        serviceHandler.removeCallbacks(updateDevice);
        googleApiClient.disconnect();

    }

    private void stopService(){
        if(serviceForeground){
            stopForeground(true);
        }
        stopSelf();
    }
    private void stopLocationUpdates(){
        if(googleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }
}
