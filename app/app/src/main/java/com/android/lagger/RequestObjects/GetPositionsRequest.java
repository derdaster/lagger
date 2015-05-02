package com.android.lagger.RequestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class GetPositionsRequest extends UserRequest{
    private Integer idMeeting;

    public GetPositionsRequest(Integer idUser, Integer idMeeting) {
        super(idUser);
        this.idMeeting = idMeeting;
    }
}
