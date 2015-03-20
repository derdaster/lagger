package com.android.lagger.forms.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.lagger.R;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class LoginFragment extends Fragment {
    private Context mContext;

    public LoginFragment(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        return inflater.inflate(
                R.layout.fragment_login, container, false);
    }

}