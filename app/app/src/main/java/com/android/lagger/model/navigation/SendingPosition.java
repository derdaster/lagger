package com.android.lagger.model.navigation;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class SendingPosition implements Parcelable {
    private Integer idUser;
    private Date dateTime;
    private Integer idMeeting;
    private Double latitude;
    private Double longitude;

    public SendingPosition(Date dateTime, Double latitude, Double longitude) {
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SendingPosition(Double latitude, Double longitude) {
        this.dateTime = new Date();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SendingPosition(Integer idUser, Date dateTime, Integer idMeeting, Double latitude, Double longitude) {
        this.idUser = idUser;
        this.dateTime = dateTime;
        this.idMeeting = idMeeting;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdMeeting() {
        return idMeeting;
    }

    public void setIdMeeting(Integer idMeeting) {
        this.idMeeting = idMeeting;
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

    public static final Creator<SendingPosition> CREATOR = new Creator<SendingPosition>() {
        public SendingPosition createFromParcel(Parcel in) {
            return new SendingPosition(in);
        }

        public SendingPosition[] newArray(int size) {
            return new SendingPosition[size];
        }
    };

    @Override
    public String toString() {
        return "Position{" + "idUser=" + idUser +
                "dateTime=" + dateTime.toString() + '\'' +
                "idMeeting=" + idMeeting +
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
        dest.writeInt(idUser);
        dest.writeSerializable(dateTime);
        dest.writeInt(idMeeting);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private SendingPosition(Parcel in) {
        idUser = in.readInt();
        dateTime = (Date) in.readSerializable();
        idMeeting = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }
}
