package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.content.Context;
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
import com.android.lagger.model.User;
import com.android.lagger.model.entities.Friend;

import java.util.ArrayList;
import java.util.Calendar;
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

    public MeetingWhoFragment(ViewPager inParentPager,Context context) {
        mContext = context;
        parentPager = inParentPager;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_who, container, false);


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

