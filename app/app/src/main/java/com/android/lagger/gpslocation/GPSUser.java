package com.android.lagger.gpslocation;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADERDA on 06-Apr-2015.
 */
public class GPSUser {
    private LatLng actualPositition;
    private List<LatLng> positionList;
    public GPSUser(double longitude, double latitude){
        positionList = new ArrayList<LatLng>();
        actualPositition = new LatLng(longitude, latitude);
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
}