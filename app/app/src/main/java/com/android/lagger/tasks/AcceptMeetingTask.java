package com.android.lagger.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.requestObjects.AcceptMeetingRequest;
import com.android.lagger.responseObjects.ResponseObject;
import com.android.lagger.services.HttpClient;
import com.android.lagger.settings.State;

/**
 * Created by Ewelina Klisowska on 2015-05-05.
 */
public class AcceptMeetingTask extends AsyncTask<AcceptMeetingRequest, Void, ResponseObject> {
    private Context context;
    private HttpClient client;
    private Boolean isAccepted;

    public AcceptMeetingTask(Context context) {

        this.context = context;
        client = new HttpClient();
    }

    protected ResponseObject doInBackground(AcceptMeetingRequest... acceptMeetingReq) {
        isAccepted = acceptMeetingReq[0].getAccept();
        return client.acceptMeeting(acceptMeetingReq[0]);
    }

    protected void onPostExecute(ResponseObject loginResp) {
        showInfo(isAccepted);
    }

    private void showInfo(final boolean isAccepted) {
        String messageText;
        if (isAccepted) {
            messageText = context.getResources().getString(R.string.accept_meeting);
        } else {
            messageText = context.getResources().getString(R.string.refuse_meeting);
        }
        Toast.makeText(context, messageText, Toast.LENGTH_SHORT).show();
    }

    public static void acceptMeeting(Integer meetingId, Boolean accept, Context context){
        AcceptMeetingRequest acceptMeetingRequest = new AcceptMeetingRequest(State.getLoggedUserId(),
                meetingId, accept);

        AcceptMeetingTask acceptMeetingTask = new AcceptMeetingTask(context);
        acceptMeetingTask.execute(acceptMeetingRequest);
    }
}