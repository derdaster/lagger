package com.android.lagger.gpslocation;

/**
 * Created by ADERDA on 13-Mrz-2015.
 */
public class GPSCoordinates {
    private double longtitude;
    private double latitude;

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public GPSCoordinates(double longtitude, double latitude) {
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

}
