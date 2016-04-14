package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by dhaejong on 14.4.2016.
 */
public class GPSListener implements LocationListener {
    private final static String TAG = "GPSListener";
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSListener(Context context) {
        this.mContext = context;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(isGPSEnabled && isNetworkEnabled) {
                if (locationManager != null) {
                    try {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }catch(SecurityException e) {
                        Log.d(TAG, "security error");
                    }

                    if (location != null) {
                        return location;
                    }
                }
            }else{
                Log.e(TAG, "network error");
            }
                // if GPS Enabled get lat/long using GPS Services
            try {
                if (location == null && isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            return location;
                        }
                    }
                }
            }catch(SecurityException e){
                Log.d(TAG, "security error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "location provider disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "location provider enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                //textView.setText(textView.getText().toString() + "GPS available again\n");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                //textView.setText(textView.getText().toString() + "GPS out of service\n");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                //textView.setText(textView.getText().toString() + "GPS temporarily unavailable\n");
                break;
        }
    }
}
