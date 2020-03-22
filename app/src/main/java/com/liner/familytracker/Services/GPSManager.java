package com.liner.familytracker.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

public class GPSManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, android.location.LocationListener {
    private Location lastKnowLocation, currentLocation;
    public GoogleApiClient googleApiClient;
    private Context context;
    private GPSManagerListener gpsManagerListener;

    public GPSManager(Context context, GPSManagerListener gpsManagerListener){
        this.context = context;
        this.gpsManagerListener = gpsManagerListener;
    }

    public void startLocationByGMS(){
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Permissions.check(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                googleApiClient.connect();
            }

        });
    }

    @SuppressLint("MissingPermission")
    public void startLocationByGPS(){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(isNETEnabled() && isGPSEnabled()){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    10000,
                    10,
                    this);
            lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if(isNETEnabled()){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    10000,
                    10,
                    this);
            lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            startLocationByGMS();
        }
    }

    @SuppressLint("MissingPermission")
    public void startLocationBySIM(){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager != null){
            if ((telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) || (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA)) {
                final CellLocation location = telephonyManager.getCellLocation();
                if (location != null) {
                    if (location instanceof GsmCellLocation) {
                        GsmCellLocation gsmLoc = (GsmCellLocation) location;
                        Log.i("TAGTAG", "Detected cell LAC: " + gsmLoc.getLac() + " CID: " + gsmLoc.getCid());
                        //cell = new CellLogEntry(gsmLoc.getCid(), gsmLoc.getLac());
                    } else if (location instanceof CdmaCellLocation) {
                        CdmaCellLocation cdmaLoc = (CdmaCellLocation) location;
                        Log.i("TAGTAG", "Detected cell NID: " + cdmaLoc.getNetworkId() + " BID: " + cdmaLoc.getBaseStationId());
                        //cell = new CellLogEntry(cdmaLoc.getBaseStationId(), cdmaLoc.getNetworkId());
                    }
                } else {
                    Log.i("TAGTAG", "Unknown phone type " + telephonyManager.getPhoneType());
                }
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = LocationRequest.create();
        request.setInterval(10000);
        request.setFastestInterval(1000);
        request.setSmallestDisplacement(10);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if(googleApiClient != null){
            if(googleApiClient.isConnected()){
                googleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(googleApiClient != null){
            if(googleApiClient.isConnected()){
                googleApiClient.disconnect();
            }
        }
        startLocationBySIM();
    }

    @Override
    public void onLocationChanged(Location location) {
        //ControllerServiceUtils.updateDeviceStatus(context, location);
        gpsManagerListener.onLocationReceived(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private boolean isGPSEnabled(){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private boolean isNETEnabled(){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public interface GPSManagerListener{
        void onLocationReceived(Location location);
    }

}
