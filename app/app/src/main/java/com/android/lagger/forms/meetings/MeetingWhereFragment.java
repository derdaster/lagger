package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.lagger.R;

import java.util.Calendar;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhereFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ViewPager parentPager;
    private Button btnLocation;

    public MeetingWhereFragment(ViewPager inParentPager,Context context) {
        parentPager = inParentPager;
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_where, container, false);
        addButtonsAndListeners();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    public void addButtonsAndListeners() {
        btnLocation = (Button) parent.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           }
        });
    }
}