package com.android.lagger.gpslocation;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADERDA on 06-Apr-2015.
 */
public class GPSUser {
    private String name;
    private LatLng actualPositition;
    private List<LatLng> positionList;
    private static int amount = 0;
    public GPSUser(double longitude, double latitude){
        positionList = new ArrayList<LatLng>();
        actualPositition = new LatLng(longitude, latitude);
        amount++;
        name = "User" + String.valueOf(amount);
    }

    public GPSUser(double longitude, double latitude, String name) {
        positionList = new ArrayList<LatLng>();
        actualPositition = new LatLng(longitude, latitude);
        amount++;
    }

    public void setActualPositition(LatLng actualPositition) {
        this.actualPositition = actualPositition;
    }

    public LatLng getActualPositition() {
        return actualPositition;
    }

    public List<LatLng> getPositionList() {
        return positionList;
    }

    public void addGPSPosition(LatLng position) {
        positionList.add(position);
    }

    public String getName() {
        return name;
    }
}
