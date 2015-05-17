package com.android.lagger.responseObjects;

import com.android.lagger.model.entities.Meeting;

import java.util.List;

/**
 * Created by Ewelina Klisowska on 2015-05-17.
 */
public class GetMeetingInvitationsResponse extends ResponseObject {
    private List<Meeting> meetingInvitations;

    public GetMeetingInvitationsResponse(List<Meeting> meetingInvitations) {
        this.meetingInvitations = meetingInvitations;
    }

    public List<Meeting> getMeetingInvitations() {
        return meetingInvitations;
    }
}




