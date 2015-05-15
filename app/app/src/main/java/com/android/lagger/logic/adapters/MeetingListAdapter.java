package com.android.lagger.logic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.settings.Parser;

import java.util.Date;
import java.util.List;

/**
 * Created by Kubaa on 2015-04-01.
 */
public class MeetingListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Meeting> data;
    private static LayoutInflater inflater = null;

    public MeetingListAdapter(Context inContext, List<Meeting> d) {
        mContext = inContext;
        data = d;
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
        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.listview_row_meeting, null);

        TextView title = (TextView)vi.findViewById(R.id.tvTitleMeeting);
        TextView where = (TextView)vi.findViewById(R.id.tvWhere);
        TextView when = (TextView)vi.findViewById(R.id.tvWhen);
        TextView organizer = (TextView)vi.findViewById(R.id.tvOrganizer);

        title.setText(data.get(position).getName());
        where.setText(data.get(position).getLocationName());

        Date startDate = data.get(position).getStartTime();
        when.setText(Parser.parseDate(startDate));

        organizer.setText(data.get(position).getOrganizer().getLogin());

        return vi;
    }



}
