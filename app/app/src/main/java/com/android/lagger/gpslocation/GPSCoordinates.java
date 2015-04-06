package com.android.lagger.gpslocation;

/**
 * Created by ADERDA on 13-Mrz-2015.
 */
public class GPSCoordinates {
    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public GPSCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
