package com.android.lagger.logic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.model.entities.Friend;

import java.util.List;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class FriendsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Friend> data;
    private static LayoutInflater inflater=null;

    public FriendsListAdapter(Context inContext, List<Friend> d) {
        mContext = inContext;
        data=d;
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
        if(convertView==null)
            vi = inflater.inflate(R.layout.listview_row_friends, null);

        TextView name = (TextView)vi.findViewById(R.id.tvName);
        name.setText(data.get(position).getFirstName() + " " + data.get(position).getLastName());

        return vi;
    }

}