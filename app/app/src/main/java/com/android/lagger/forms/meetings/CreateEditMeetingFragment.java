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
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.lagger.R;
import com.android.lagger.controls.custom.LinePageIndicator;
import com.android.lagger.forms.main.MainActivity;
import com.android.lagger.logic.adapters.PagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class CreateEditMeetingFragment extends Fragment{

    private static final String TIME_PATTERN = "HH:mm";

    private View parent;
    private Context mContext;
    private TextView labelDate;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private ListView guestList;
    private Button btnDate;
    private Button btnTime;
    PagerAdapter pageAdapter;
    ViewPager pager;
    LinePageIndicator mIndicator;

    public CreateEditMeetingFragment(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_pager, container, false);

//        addButtonsAndListeners();
        pager = (ViewPager)parent.findViewById(R.id.pager);


        pageAdapter = new PagerAdapter(getFragmentManager());
        pageAdapter.addItem(new MeetingWhenFragment(pager, mContext));
        pageAdapter.addItem(new MeetingWhereFragment(pager, mContext));
        pageAdapter.addItem(new MeetingWhoFragment(pager, mContext));


        //pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pageAdapter);

        //mIndicator = (LinePageIndicator) parent.findViewById(R.id.indicator);
        //mIndicator.setViewPager(pager);
/*
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mIndicator.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

                mIndicator.setCurrentItem(i);
                mIndicator.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });*/

        return parent;
    }

    private void initControls() {

    }

    public void addButtonsAndListeners()
    {
        btnDate = (Button) parent.findViewById(R.id.btnDatePicker);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        update();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });
        btnTime = (Button) parent.findViewById(R.id.btnTimePicker);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        update();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        labelDate = (TextView) getView().findViewById(R.id.labelDate);
        guestList = (ListView) getView().findViewById(R.id.guestList);

//        createGuestList();
 //       update();
    }


    private void createGuestList(){
        String[] values = new String[] {
                "Karol Więcek",
                "Bożena Walczak",
                "Grzegorz Kaśków",
                "Marek Tomczak",
                "Paweł Nowak",
                "Ilona Kowalska",
                "Karol Więcek",
                "Bożena Walczak",
                "Grzegorz Kaśków",
                "Marek Tomczak",
                "Paweł Nowak",
                "Ilona Kowalska",
                "Piotrek Bilon"
        };

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
        });
    }

    private void update() {
        labelDate.setText(dateFormat.format(calendar.getTime()) + " " + timeFormat.format(calendar.getTime()));
    }


}
