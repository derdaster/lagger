package com.android.lagger.logic.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kubaa on 2015-04-20.
 */
public class DrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    ArrayList<String> data;

    public DrawerListAdapter(Context inContext, ArrayList<String> d) {
        mContext = inContext;
        data = d;
        inflater = (LayoutInflater) inContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.listview_row_drawer, null);

        ImageView iv = (ImageView) vi.findViewById(R.id.ivIconDrawer);
        Drawable d = null;
        switch (position)
        {
            case 0:
                d = mContext.getResources().getDrawable( R.drawable.meetings );
                break;
            case 1:
                d = mContext.getResources().getDrawable(R.drawable.friends);
                break;
            case 2:
                d = mContext.getResources().getDrawable(R.drawable.settings);
                break;
            case 3:
                d = mContext.getResources().getDrawable(R.drawable.settings);
                break;
            case 4:
                d = mContext.getResources().getDrawable(R.drawable.settings);
                break;
        }
        iv.setImageDrawable(d);

        TextView name = (TextView) vi.findViewById(R.id.tvDrawerName);
        name.setText(data.get(position));

        return vi;
    }
}