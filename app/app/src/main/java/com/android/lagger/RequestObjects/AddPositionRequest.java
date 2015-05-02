package com.android.lagger.RequestObjects;

import java.util.Date;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class AddPositionRequest extends UserRequest {
    private Integer idMeering;
    private Double latitude;
    private Double longitude;
    private Date dateTime;

    public AddPositionRequest(Integer idUser, Integer idMeering, Double latitude,
                              Double longitude, Date dateTime) {
        super(idUser);
        this.idMeering = idMeering;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
    }
}
