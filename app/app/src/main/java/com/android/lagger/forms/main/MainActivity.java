package com.android.lagger.forms.main;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.controls.basic.SomeDialog;
import com.android.lagger.forms.friends.FriendsListFragment;
import com.android.lagger.forms.login.LoginFragment;
import com.android.lagger.forms.meetings.CreateEditMeetingFragment;
import com.android.lagger.forms.meetings.MeetingListFragment;
import com.android.lagger.gpslocation.GPSFragment;
import com.android.lagger.serverConnection.TestServerConnection;



public class MainActivity extends ActionBarActivity {

    private Context mContext;

    ListView mDrawerList;
    String responseGET;
    DrawerLayout mDrawerLayout;
    ArrayAdapter<String> mAdapter;
    ActionBarDrawerToggle mDrawerToggle;
    String mActivityTitle;
    String responsePOST;
    private Activity mActivity;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mActivity = this;

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        MainFragment fragment = new MainFragment(mContext);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }


    private void addDrawerItems() {
        String[] osArray = { "Meetings", "Friends", "Settings", "LoginTest", "GPSTest"  };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }
//
    private void selectItem(int position) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {

            case 0:
                fragmentTransaction.replace(R.id.content_frame, new MeetingListFragment(mContext)).commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.content_frame, new FriendsListFragment(mContext)).commit();
                break;
            case 2:
                break;
            case 3:
                fragmentTransaction.replace(R.id.content_frame, new LoginFragment(mContext)).commit();
                break;
            case 4:
                fragmentTransaction.replace(R.id.content_frame, new GPSFragment(mContext)).commit();
                break;
        }

        mDrawerList.setItemChecked(position, true);
        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
