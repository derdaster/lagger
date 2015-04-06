package com.android.lagger.gpslocation;

/**
 * Created by ADERDA on 13-Mrz-2015.
 */
public class GPSCoordinates {
    private double longitude;
    private double latitude;

    public GPSCoordinates(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
