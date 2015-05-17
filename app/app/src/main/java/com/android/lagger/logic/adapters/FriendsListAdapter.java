package com.android.lagger.logic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.model.entities.User;
import com.gc.materialdesign.views.CheckBox;

import java.util.List;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class FriendsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> data;
    private static LayoutInflater inflater=null;
    private Boolean isCheckboxNeeded;

    public FriendsListAdapter(Context inContext, List<User> d, Boolean inIsCheckboxNeeded) {
        mContext = inContext;
        data = d;
        isCheckboxNeeded = inIsCheckboxNeeded;
        inflater = (LayoutInflater)inContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.listview_row_friends, null);

        CheckBox cb = (CheckBox)vi.findViewById(R.id.checkBoxFriend);
        if(isCheckboxNeeded)
            cb.setVisibility(View.VISIBLE);

        TextView name = (TextView)vi.findViewById(R.id.tvName);
        name.setText(data.get(position).getLogin() + " (" + data.get(position).getEmail()+")");

        return vi;
    }

}