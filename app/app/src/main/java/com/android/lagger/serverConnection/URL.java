package com.android.lagger.serverConnection;

/**
 * Created by Ewelina Klisowska on 2015-04-29.
 */
public class URL {
    /*
    MEETING
     */
    public static final String GET_MEETINGS = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/meetings/get";
    public static final String GET_MEETING_INVITATIONS = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/meetings/invitation";
    public static final String ACCEPT_MEETING_INVITATION = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/meetings/invitation/accept";
    public static final String ADD_MEETING = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/meetings/add";
    public static final String REMOVE_MEETING = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/meetings/remove";
    public static final String EDIT_MEETING = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/meetings/edit";

    /*
    FRIEND
     */
    public static final String GET_FRIENDS = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/friends/get";
    public static final String GET_INVITATION_FROM_FRIENDS = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/friends/invitation";
    public static final String FIND_FRIENDS = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/friends/find";
    public static final String ADD_FRIEND = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/friends/add";
    public static final String REMOVE_FRIEND = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/friends/remove";

    /*
    other
     */
    public static final String LOGIN = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/login";
    public static final String ADD_POSITION = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/positions/add";
    public static final String GET_POSITIONS = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/positions/get";

}
