package com.android.lagger.forms.meetings;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.controls.basic.SomeDialog;
import com.android.lagger.logic.adapters.MeetingListAdapter;
import com.android.lagger.model.entities.Meeting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;
import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter.Section;

/**
 * Created by Kubaa on 2015-04-01.
 */
public class MeetingListFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ListView mList;
    private MeetingListAdapter adapter;
    private Button btnAdd;
    ArrayList<Meeting> meetingsList;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private int indexOfInvited = 0;
    private int indexOfUpComing = 2;
    private String[] mHeaderNames = { "Zaproszenia", "Nadchodzące" };
    private Integer[] mHeaderPositions = { indexOfInvited, indexOfUpComing };
    private ArrayList<Section> sections = new ArrayList<Section>();

    public MeetingListFragment(Context context) {
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_list, container, false);

        mList = (ListView) parent.findViewById(R.id.meeting_list);
        Meeting m1 = new Meeting(1, "Urodziny", "Balonowa 44", "23.4.2015r.", "Jan");
        Meeting m2 = new Meeting(2, "Urodziny 2", "Balonowa 45", "24.6.2015r.", "Błażej");
        Meeting m3 = new Meeting(3, "Urodziny 3", "Balonowa 46", "25.7.2015r.", "Wojtek");
        Meeting m4 = new Meeting(4, "Urodziny 4", "Balonowa 44", "23.4.2015r.", "Jan");
        Meeting m5 = new Meeting(5, "Urodziny 5", "Balonowa 45", "24.6.2015r.", "Błażej");
        Meeting m6 = new Meeting(6, "Urodziny 6", "Balonowa 46", "25.7.2015r.", "Wojtek");
        meetingsList = new ArrayList<Meeting>();
        meetingsList.add(m1);
        meetingsList.add(m2);
        meetingsList.add(m3);
        meetingsList.add(m4);
        meetingsList.add(m5);
        meetingsList.add(m6);
        adapter = new MeetingListAdapter(mContext, meetingsList);
        for (int i = 0; i < mHeaderPositions.length; i++) {
            if(sections.size() < 2)
                sections.add(new Section(mHeaderPositions[i], mHeaderNames[i]));
        }
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                R.layout.listview_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
        mList.setAdapter(simpleSectionedGridAdapter);

        fragmentManager = getFragmentManager();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i <= indexOfUpComing)
                {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    SomeDialog newFragment = new SomeDialog (mContext, "Confirm", "Do you want to accept this meeting invitation?", true);
                    newFragment.show(fragmentTransaction, "dialog");

                }
                else
                {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content_frame, new ViewMeetingFragment(mContext)).commit();
                }
            }
        });


        btnAdd = (Button) parent.findViewById(R.id.btnAddMeeting);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, new CreateEditMeetingFragment(mContext)).commit();
            }
        });
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }
}
