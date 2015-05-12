package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-12.
 */
public class RemoveMeetingRequest extends UserRequest {
    private Integer idMeeting;

    public RemoveMeetingRequest(Integer idUser, Integer idMeeting) {
        super(idUser);
        this.idMeeting = idMeeting;
    }
}
