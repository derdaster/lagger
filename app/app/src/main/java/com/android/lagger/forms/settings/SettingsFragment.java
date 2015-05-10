package com.android.lagger.forms.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.lagger.R;

/**
 * Created by Kubaa on 2015-05-10.
 */
public class SettingsFragment extends Fragment {

    private Context mContext;
    private View parent;

    public SettingsFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent =  inflater.inflate(R.layout.fragment_settings, container, false);
        addListenerOnLoginButton();
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
    }

    public void addListenerOnLoginButton() {
    }




}