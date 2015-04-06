package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.lagger.R;
import com.android.lagger.controls.custom.LinePageIndicator;
import com.android.lagger.controls.custom.ZoomOutPageTransformer;
import com.android.lagger.logic.adapters.PagerAdapter;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class CreateEditMeetingFragment extends Fragment{

    private View parent;
    private Context mContext;
    PagerAdapter pageAdapter;
    ViewPager pager;
    LinePageIndicator mIndicator;

    public CreateEditMeetingFragment(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_pager, container, false);

        pager = (ViewPager)parent.findViewById(R.id.pager);
        pageAdapter = new PagerAdapter(getFragmentManager());
        pageAdapter.addItem(new MeetingWhenFragment(pager, mContext));
        pageAdapter.addItem(new MeetingWhereFragment(pager, mContext));
        pageAdapter.addItem(new MeetingWhoFragment(pager, mContext));


        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pageAdapter);

        mIndicator = (LinePageIndicator) parent.findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);

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
        });

        return parent;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


}
