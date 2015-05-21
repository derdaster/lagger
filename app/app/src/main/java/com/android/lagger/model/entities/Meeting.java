package com.android.lagger.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-04-08.
 */
public class Meeting implements Parcelable {

    private Integer id;
    private String name;
    private String locationName;
    private Date startTime;
    private Date endTime;
    private Double latitude;
    private Double longitude;
    private User organizer;

    private List<Integer> userList;

    public Meeting(){
        name = "";
        startTime = new Date();
        endTime = new Date();
        locationName = "";
        latitude = Double.MIN_VALUE;
        longitude = Double.MIN_VALUE;
        organizer = new User();
    }

    public Meeting(String name, String locationName, Date startTime, Date endTime,
                   Double latitude, Double longitude, User organizer) {
        this.name = name;
        this.locationName = locationName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.organizer = organizer;
    }

    public Meeting(String name, String locationName, Date startTime, Date endTime,
                   Double latitude, Double longitude, User organizer, List<Integer> userList) {
        this(name, locationName, startTime, endTime, latitude, longitude, organizer);
        this.userList = userList;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<Integer> getUserList() {
        return userList;
    }

    public void setUserList(List<Integer> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", locationName='" + locationName + '\'' +
                ", startTime='" + startTime.toString() + '\'' +
                ", endTime='" + endTime.toString() + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", organizer=" + organizer +
                ", userList=" + userList +
                '}';
    }


    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(locationName);
        dest.writeSerializable(startTime);
        dest.writeSerializable(endTime);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeParcelable(organizer, 0);
    }

    private Meeting(Parcel in) {
        id = in.readInt();
        name = in.readString();
        locationName = in.readString();
        ;
        startTime = (java.util.Date) in.readSerializable();
        endTime = (java.util.Date) in.readSerializable();
        latitude = in.readDouble();
        longitude = in.readDouble();
        organizer = in.readParcelable(User.class.getClassLoader());
    }
}
