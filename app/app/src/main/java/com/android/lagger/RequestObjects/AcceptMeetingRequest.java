package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class AcceptMeetingRequest extends UserRequest {
    private Integer idMeeting;
    private Boolean accept;

    public AcceptMeetingRequest(Integer idUser, Integer idMeeting, Boolean accept) {
        super(idUser);
        this.idMeeting = idMeeting;
        this.accept = accept;
    }

    public Boolean getAccept() {
        return accept;
    }
}
