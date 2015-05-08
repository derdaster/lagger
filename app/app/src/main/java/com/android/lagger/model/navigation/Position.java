package com.android.lagger.model.navigation;

import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class Position {
    private Date dateTime;
    private Double latitude;
    private Double longitude;

    public Position(Date dateTime, Double latitude, Double longitude) {
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position(Double latitude, Double longitude) {
        this.dateTime = new Date();
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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
}
