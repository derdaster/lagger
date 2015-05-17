package com.android.lagger.requestObjects;

/**
 * Created by Ewelina Klisowska on 2015-05-17.
 */
public class GetAllMeetingsRequest {
    private GetMeetingsRequest getMeetingsRequest;
    private GetMeetingInvitationsRequest getMeetingInvitationsRequest;

    public GetAllMeetingsRequest(GetMeetingsRequest getMeetingsRequest,
                                 GetMeetingInvitationsRequest getMeetingInvitationsRequest) {
        this.getMeetingsRequest = getMeetingsRequest;
        this.getMeetingInvitationsRequest = getMeetingInvitationsRequest;
    }

    public GetMeetingsRequest getGetMeetingsRequest() {
        return getMeetingsRequest;
    }

    public GetMeetingInvitationsRequest getGetMeetingInvitationsRequest() {
        return getMeetingInvitationsRequest;
    }
}
