package com.android.lagger.forms.friends;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.lagger.R;
import com.android.lagger.forms.meetings.CreateEditMeetingFragment;

/**
 * Created by Kubaa on 2015-04-28.
 */
public class FriendsAddFragment extends Fragment {
    private View parent;
    private Context mContext;
    private Button btnAdd;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FriendsAddFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_friends_add, container, false);

        fragmentManager = getFragmentManager();
        btnAdd = (Button) parent.findViewById(R.id.btnDone);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
            }
        });

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
    }
}