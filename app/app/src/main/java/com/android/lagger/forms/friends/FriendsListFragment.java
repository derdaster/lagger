package com.android.lagger.forms.friends;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.lagger.R;
import com.android.lagger.logic.adapters.FriendsListAdapter;
import com.android.lagger.model.entities.Friend;

import java.util.ArrayList;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class FriendsListFragment extends Fragment {
    private View parent;
    private Context mContext;
    private ListView mList;
    private FriendsListAdapter adapter;
    private Button btnAdd;
    ArrayList<Friend> friendsList;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private String[] mHeaderNames = { "Zaproszenia", "Wszyscy" };
    private Integer[] mHeaderPositions = { 0, 2 };
    private ArrayList<SimpleSectionedListAdapter.Section> sections = new ArrayList<SimpleSectionedListAdapter.Section>();

    public FriendsListFragment(Context context) {
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_friends_list, container, false);

        mList = (ListView) parent.findViewById(R.id.friends_list);
        Friend f1 = new Friend(1, "Paweł", "Górniak", "abc@onet.pl");
        Friend f2 = new Friend(2, "Grzesiek", "Porwał", "abc@onet.pl");
        Friend f3 = new Friend(3, "Miłosz", "Mistrz", "abc@onet.pl");
        Friend f4 = new Friend(4, "Danuta", "Brzechwa", "abc@onet.pl");
        Friend f5 = new Friend(5, "Paweł", "Górniak", "abc@onet.pl");
        Friend f6 = new Friend(6, "Grzesiek", "Porwał", "abc@onet.pl");
        Friend f7 = new Friend(7, "Miłosz", "Mistrz", "abc@onet.pl");
        Friend f8 = new Friend(8, "Danuta", "Brzechwa", "abc@onet.pl");
        friendsList = new ArrayList<Friend>();
        friendsList.add(f1);
        friendsList.add(f2);
        friendsList.add(f3);
        friendsList.add(f4);
        friendsList.add(f5);
        friendsList.add(f6);
        friendsList.add(f7);
        friendsList.add(f8);
        adapter = new FriendsListAdapter(mContext, friendsList);
        for (int i = 0; i < mHeaderPositions.length; i++) {
            sections.add(new SimpleSectionedListAdapter.Section(mHeaderPositions[i], mHeaderNames[i]));
        }
        SimpleSectionedListAdapter simpleSectionedGridAdapter = new SimpleSectionedListAdapter(mContext, adapter,
                R.layout.listview_item_header, R.id.header);
        simpleSectionedGridAdapter.setSections(sections.toArray(new SimpleSectionedListAdapter.Section[0]));
        mList.setAdapter(simpleSectionedGridAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        fragmentManager = getFragmentManager();
        btnAdd = (Button) parent.findViewById(R.id.btnAddFriend);
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
    }
}