package com.android.lagger.forms.meetings;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.lagger.R;
import com.android.lagger.model.entities.Meeting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Kubaa on 2015-04-08.
 */
public class ViewMeetingFragment extends Fragment {
    private View parent;
    private Context mContext;
    private Button btnAccept;
    private Button btnRefuse;
    private TextView labelWhen;
    private TextView labelWhere;
    private TextView labelOrganizer;
//    private int idMeeting;

    public ViewMeetingFragment(Context context) {
        mContext = context;
//        this.idMeeting = idMeeting;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_view_meeting, container, false);
        addButtonsAndListeners();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        labelWhen = (TextView) getView().findViewById(R.id.labelWhen);
        labelWhere = (TextView) getView().findViewById(R.id.labelWhere);
        labelOrganizer = (TextView) getView().findViewById(R.id.labelOrganizer);
    }

    public void addButtonsAndListeners()
    {
        btnAccept = (Button) parent.findViewById(R.id.btnAcceptMeeting);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRefuse = (Button) parent.findViewById(R.id.btnRefuseMeeting);
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


}
