package com.android.lagger.gpslocation;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.lagger.model.navigation.Position;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ADERDA on 06-Apr-2015.
 */
public class GPSUser implements Parcelable{
    private int idUser;
    private int arrivalTime;
    private List<Position> positionList=new ArrayList<>();

    public GPSUser(int idUser, int arrivalTime) {
        this.idUser = idUser;
        this.arrivalTime = arrivalTime;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
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

    public static final Creator<GPSUser> CREATOR = new Creator<GPSUser>() {
        public GPSUser createFromParcel(Parcel in) {
            return new GPSUser(in);
        }

        public GPSUser[] newArray(int size) {
            return new GPSUser[size];
        }
    };

    @Override
    public String toString() {
        return "Position{" +"idUser=" + idUser +
                "dateTime=" + arrivalTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(idUser);
        dest.writeSerializable(arrivalTime);
    }
    private GPSUser(Parcel in) {
        idUser = in.readInt();
        arrivalTime = in.readInt();
    }
}
