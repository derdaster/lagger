package com.android.lagger.responseObjects;

import com.android.lagger.model.entities.Meeting;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-02.
 */
public class GetMeetingsResponse extends ResponseObject {
    private List<Meeting> meetings;

    public GetMeetingsResponse(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }
}
