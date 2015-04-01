package com.android.lagger.logic.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<Fragment>();
    }

    public PagerAdapter(FragmentManager fm, List<String> inTitles) {
        this(fm);
        titles = inTitles;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void addItem(Fragment inFragment) {
        fragments.add(inFragment);
    }

    @Override
    public int getCount() {
        return fragments.size();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


}

