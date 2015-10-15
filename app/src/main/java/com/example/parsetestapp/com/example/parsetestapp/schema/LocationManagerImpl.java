package com.example.parsetestapp.com.example.parsetestapp.schema;

import android.location.Location;
import android.util.Log;

/**
 * Created by AKhare on 28-Dec-14.
 */
public class LocationManagerImpl {
    private static LocationManagerImpl mInstance = new LocationManagerImpl();

    private Location mCurrentLocation;
    private String TAG = LocationManagerImpl.class.getSimpleName();

    private LocationManagerImpl(){

    }
    public static LocationManagerImpl getInstance(){
        return mInstance;
    }
    public void setCurrentLocation(Location location){
        this.mCurrentLocation = location;
    }

    public Location getCurrentLocation(){
        Log.d(TAG, "in getCurrentlocation,returning :"+mCurrentLocation);
        return this.mCurrentLocation;
    }
}
