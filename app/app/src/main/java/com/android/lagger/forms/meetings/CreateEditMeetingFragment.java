package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.lagger.R;
import com.android.lagger.controls.custom.LinePageIndicator;
import com.android.lagger.logic.adapters.PagerAdapter;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Kubaa on 2015-03-20.
 */


public class CreateEditMeetingFragment extends Fragment {

    private View parent;
    private Context mContext;
    PagerAdapter pageAdapter;
    ViewPager pager;
    LinePageIndicator mIndicator;
    FloatingActionButton leftBtn;
    FloatingActionButton rightBtn;
    FloatingActionButton doneBtn;
    FrameLayout frameRight;
    FrameLayout frameDone;

    public CreateEditMeetingFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_pager, container, false);

        /*
        pager = (ViewPager)parent.findViewById(R.id.pager);
        pageAdapter = new PagerAdapter(getFragmentManager());
        pageAdapter.addItem(new MeetingWhenFragment(pager));
        pageAdapter.addItem(new MeetingWhereFragment(pager));
        pageAdapter.addItem(new MeetingWhoFragment(pager));


        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pageAdapter);
*/

        frameRight = (FrameLayout) parent.findViewById(R.id.frameRightPager);
        frameDone = (FrameLayout) parent.findViewById(R.id.frameDonePager);

        leftBtn = (FloatingActionButton) parent.findViewById(R.id.btnLeftPager);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);
            }
        });

        rightBtn = (FloatingActionButton) parent.findViewById(R.id.btnRightPager);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() + 1);
            }
        });

        doneBtn = (FloatingActionButton) parent.findViewById(R.id.btnDonePager);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (pager.getCurrentItem() == 0)
            leftBtn.setVisibility(View.INVISIBLE);

        mIndicator = (LinePageIndicator) parent.findViewById(R.id.indicator);
        //mIndicator.setViewPager(pager);

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
                if (i == 0)
                    leftBtn.setVisibility(View.INVISIBLE);
                else if (i == 2) {
                    frameRight.setVisibility(View.GONE);
                    frameDone.setVisibility(View.VISIBLE);
                } else {
                    leftBtn.setVisibility(View.VISIBLE);
                    frameRight.setVisibility(View.VISIBLE);
                    frameDone.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        return parent;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();
    }
}
