package com.android.lagger.gpslocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADERDA on 06-Apr-2015.
 */
public class GPSUser {
    private GPSCoordinates actualPositition;
    private List<GPSCoordinates> positionList;
    public GPSUser(double longitude, double latitude){
        positionList=new ArrayList<GPSCoordinates>();
        actualPositition=new GPSCoordinates(longitude,latitude);
    }

    public void setActualPositition(GPSCoordinates actualPositition) {
        this.actualPositition = actualPositition;
    }

    public GPSCoordinates getActualPositition() {
        return actualPositition;
    }

    public List<GPSCoordinates> getPositionList() {
        return positionList;
    }

    public void addGPSPosition(GPSCoordinates position){
        positionList.add(position);
    }
}