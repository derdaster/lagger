package com.android.lagger.requestObjects;

import com.android.lagger.model.entities.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class AddMeetingRequest extends UserRequest {

    private String name;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private Date startTime;
    private Date endTime;
    private List<User> userList;

    public AddMeetingRequest(Integer idUser, String name, String locationName, Double latitude,
                             Double longitude, Date startTime, Date endTime, List<User> userList) {
        super(idUser);
        this.name = name;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userList = userList;
    }
}
