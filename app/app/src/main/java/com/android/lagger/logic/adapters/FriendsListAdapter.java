package com.android.lagger.logic.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.model.entities.User;
import com.gc.materialdesign.views.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kubaa on 2015-04-03.
 */
public class FriendsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> dataUser;
    private static LayoutInflater inflater = null;
    private Boolean isCheckboxNeeded;
    private List<User> chosenUsers;

    public FriendsListAdapter(Context inContext, List<User> d, Boolean inIsCheckboxNeeded, List<User> chosenFriends) {
        mContext = inContext;
        dataUser = d;
        isCheckboxNeeded = inIsCheckboxNeeded;
        inflater = (LayoutInflater) inContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        chosenUsers = new ArrayList<User>();
        if (chosenFriends != null && chosenFriends.size() > 0) {
            chosenUsers = chosenFriends;
        }
    }

    public List<User> getChosenUsers() {
        return chosenUsers;
    }

    public int getCount() {
        return dataUser.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row_friends, null);
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxFriend);
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.image = (ImageView) convertView.findViewById(R.id.ivIconFriend);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(dataUser.get(position).getLogin() + " (" + dataUser.get(position).getEmail() + ")");

        if (isCheckboxNeeded) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                updateChosenUsers(b, position);
            }
        });

        if (chosenUsers.contains(dataUser.get(position))) {
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
        }
        return convertView;
    }

    private void updateChosenUsers(boolean b, final int position) {
        User chosenFriend = dataUser.get(position);
        if (b) {
            if (!chosenUsers.contains(chosenFriend)) {
                chosenUsers.add(chosenFriend);
            }
        } else {
            if (chosenUsers.contains(chosenFriend)) {
                chosenUsers.remove(chosenFriend);
            }
        }
    }

    private class ViewHolder{
        CheckBox checkBox;
        TextView name;
        ImageView image;
    }
}