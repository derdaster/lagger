package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.lagger.R;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.User;
import com.android.lagger.model.entities.Friend;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.settings.State;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhoFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ViewPager parentPager;
    private Button btnAdd;
    private ListView mList;
    private FriendsListAdapter adapter;
    List<User> guestList;

    public MeetingWhoFragment(ViewPager inParentPager) {
        parentPager = inParentPager;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_who, container, false);
        mContext = getActivity().getApplicationContext();

        addButtonsAndListeners();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void addButtonsAndListeners() {

        mList = (ListView) parent.findViewById(R.id.guestList);

        guestList = new ArrayList<User>();

        adapter = new FriendsListAdapter(mContext, guestList);
        mList.setAdapter(adapter);

        /*
        guestList = (ListView) parent.findViewById(R.id.guestList);
        guestList.setAdapter(adapter);

        guestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition     = position;
                String  itemValue    = (String) guestList.getItemAtPosition(position);
                Toast.makeText(mContext,
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });*/
        btnAdd = (Button) parent.findViewById(R.id.btnAddGuest);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... urls) {



                        JsonObject newMeeting = new JsonObject();

                        //FIXME set userId only for tests
                        int userId = 1;
                        if (State.loggedUser != null) {
                            userId = State.loggedUser.getId();
                        }
//                        JsonElement elem = gson.
                        newMeeting.addProperty("idUser", userId);
                        newMeeting.addProperty("name", "testName");
                        newMeeting.addProperty("locationName", "testLocationName");
                        newMeeting.addProperty("latitude", 51.34243);
                        newMeeting.addProperty("longitude", 70.324234);
                        newMeeting.addProperty("startTime", new Date().getTime());
                        newMeeting.addProperty("endTime", new Date().getTime() + 60 * 60);
                        JSONArray userList =  new JSONArray();
                        userList.put(1);
                        userList.put(2);
                        userList.put(3);
                        userList.put(4);
                        newMeeting.addProperty("userList", userList.toString());

                        Gson gson = new GsonHelper().getGson();
                        String JSON = gson.toJson(newMeeting);

                        return HttpRequest.POST(URL.ADD_MEETING, );

                    }

                    // onPostExecute displays the results of the AsyncTask.
                    @Override
                    protected void onPostExecute(String result) {

                    }
                }.execute();
                */
            }
        });
    }

    private void getFriends(){

    }
/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        guestList.setAdapter(adapter);

        guestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition     = position;
                String  itemValue    = (String) guestList.getItemAtPosition(position);
                Toast.makeText(mContext,
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

        guestList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    guestList.scrollBy(0, 1);
                }
                return false;
            }
        });*/
    }

