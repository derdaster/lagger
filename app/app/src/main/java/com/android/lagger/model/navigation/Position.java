package com.android.lagger.model.navigation;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class Position implements Parcelable {
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

    public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    @Override
    public String toString() {
        return "Position{" +
                "dateTime=" + dateTime.toString() + '\'' +
                ", latitude='" + latitude +
                ", longitude='" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(dateTime);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private Position(Parcel in) {
        dateTime = (java.util.Date) in.readSerializable();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }
}
