package com.android.lagger.model.entities;

import com.android.lagger.model.User;

import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-04-08.
 */
public class Meeting {

    private Integer id;
    private String name;
    private String locationName;
    private String startTime;
    private String endTime;
    private Double latitude;
    private Double longitude;
    private User organizer;

    public Meeting(String name, String locationName, String startTime, String endTime, Double latitude, Double longitude, User organizer) {
        this.name = name;
        this.locationName = locationName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.organizer = organizer;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", locationName='" + locationName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", organizerName=" + organizer.toString() +
                '}';
    }
}
