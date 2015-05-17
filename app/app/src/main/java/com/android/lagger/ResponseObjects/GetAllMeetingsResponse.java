package com.android.lagger.responseObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-17.
 */
public class GetAllMeetingsResponse extends ResponseObject{
    private GetMeetingsResponse getMeetingsResponse;
    private GetMeetingInvitationsResponse getMeetingInvitationsResponse;

    public GetAllMeetingsResponse(GetMeetingsResponse getMeetingsResponse,
                                  GetMeetingInvitationsResponse getMeetingInvitationsResponse) {
        this.getMeetingsResponse = getMeetingsResponse;
        this.getMeetingInvitationsResponse = getMeetingInvitationsResponse;
    }

    public GetMeetingsResponse getGetMeetingsResponse() {
        return getMeetingsResponse;
    }

    public GetMeetingInvitationsResponse getGetMeetingInvitationsResponse() {
        return getMeetingInvitationsResponse;
    }
}
