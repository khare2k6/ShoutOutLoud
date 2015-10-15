package com.example.parsetestapp.com.example.parsetestapp.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AKhare on 24-Dec-14.
 */
public class TripManager {
    private final List<Trip> mTripsList;
    private final int ZERO_INDEX = 0;
    private static TripManager mInstance = new TripManager();

    private TripManager(){
        mTripsList = new ArrayList<Trip>();
    }
    public static TripManager getInstance(){
        return mInstance;
    }

    public void addTrip(Trip trip){
        if(!mTripsList.contains(trip)) {
            mTripsList.add(trip);
        }
    }

    /**
     * Gets trip at 0th index of the trip list
     * @return removal succesful or not
     */
    public Trip getTrip(){
        Trip trip = null;
        if(!mTripsList.isEmpty()) {
            trip = mTripsList.get(ZERO_INDEX);
        }
        return trip;
    }

    public boolean removeTrip(Trip trip){
        return mTripsList.remove(trip);
    }
    public boolean isTripListEmpty(){
        return mTripsList.isEmpty();
    }

    /**
     * Removes trip at the zeroth index
     * @return remove succesful or not
     */
    public boolean removeTrip(){
        return (mTripsList.remove(ZERO_INDEX) != null);
    }
}
