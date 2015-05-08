package com.android.lagger.gpslocation;

import com.android.lagger.model.navigation.Position;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ADERDA on 06-Apr-2015.
 */
public class GPSUser{
    private int idUser;
    private Date arrivalTime;
    private List<Position> positionList;

    public GPSUser(int idUser, Date arrivalTime) {
        this.idUser = idUser;
        this.arrivalTime = arrivalTime;
    }

    public int getIdUser() {
        return idUser;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    private static int amount = 0;

    public GPSUser(double longitude, double latitude){
        positionList = new ArrayList();
        positionList.add(new Position(latitude,longitude));
        amount++;
        //name = "User" + String.valueOf(id);
    }

    public GPSUser(double longitude, double latitude, String name) {
        positionList = new ArrayList();
        positionList.add(new Position(latitude,longitude));
        amount++;
    }


    public LatLng getActualPositition() {
        return new LatLng(positionList.get(positionList.size() - 1).getLatitude(),positionList.get(positionList.size() - 1).getLongitude());
    }

    public List<Position> getPositionList() {
        return positionList;
    }


    public void addGPSPosition(LatLng position) {
        positionList.add(new Position(position.latitude,position.longitude));
    }
    public void addGPSPosition(LatLng position,Date date) {
        positionList.add(new Position(date,position.latitude,position.longitude));
    }


}
