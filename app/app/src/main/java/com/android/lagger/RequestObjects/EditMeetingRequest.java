package com.android.lagger.requestObjects;

import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-23.
 */
public class EditMeetingRequest extends UserRequest {
    private String name;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private Date startTime;
    private Date endTime;
    private List<Integer> usersList;

    public EditMeetingRequest(Integer idUser, Meeting meeting){
        super(idUser);
        this.name = meeting.getName();
        this.locationName = meeting.getLocationName();
        this.latitude = meeting.getLatitude();
        this.longitude = meeting.getLongitude();
        this.startTime = meeting.getStartTime();
        this.endTime = meeting.getEndTime();

        usersList = new ArrayList<Integer>();
        for(User u: meeting.getUserList()) {
            usersList.add(u.getId());
        }
    }

}
