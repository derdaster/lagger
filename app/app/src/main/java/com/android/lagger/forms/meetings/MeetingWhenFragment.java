package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.lagger.R;
import com.melnykov.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhenFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ViewPager parentPager;
    private Button btnDateStart;
    FloatingActionButton rightBtn;
    private Button btnTimeStart;
    private Button btnDateEnd;
    private Button btnTimeEnd;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final String TIME_PATTERN = "HH:mm";
    FragmentTransaction fragmentTransaction;

    public MeetingWhenFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_when, container, false);
        addButtonsAndListeners();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

    }

    public void addButtonsAndListeners()
    {

        btnDateStart = (Button) parent.findViewById(R.id.btnDatePickerStart);
        btnDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        btnDateStart.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });
        btnTimeStart = (Button) parent.findViewById(R.id.btnTimePickerStart);
        btnTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        btnTimeStart.setText(timeFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        btnDateEnd = (Button) parent.findViewById(R.id.btnDatePickerStart);
        btnDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        btnDateEnd.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });
        btnTimeEnd = (Button) parent.findViewById(R.id.btnTimePickerStart);
        btnTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        btnTimeEnd.setText(timeFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        rightBtn = (FloatingActionButton) parent.findViewById(R.id.btnRightPager);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container_body, new MeetingWhereFragment()).commit();
            }
        });
    }
}
