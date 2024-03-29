package com.android.lagger.forms.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.lagger.R;
import com.android.lagger.forms.drawer.FragmentDrawer;
import com.android.lagger.forms.friends.FriendsListFragment;
import com.android.lagger.forms.home.HomeFragment;
import com.android.lagger.forms.login.LoginActivity;
import com.android.lagger.forms.meetings.MeetingListFragment;
import com.android.lagger.forms.settings.SettingsFragment;
import com.android.lagger.gpslocation.MapFragment;
import com.android.lagger.logic.forms.SaveSharedPreference;
import com.android.lagger.model.entities.Meeting;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private Context mContext;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    static String mActivityTitle;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
        {
            initialize();
            displayView(0);
        }
    }

    private void initialize() {
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        mActivityTitle = getTitle().toString();
        mActivity = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new MeetingListFragment(mContext);
                title = getString(R.string.title_meetings);
                break;
            case 2:
                fragment = new FriendsListFragment(mContext);
                title = getString(R.string.title_friends);
                break;
            case 3:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;
            case 4:
                SaveSharedPreference.setUserName(mContext, "");
                title = getString(R.string.title_logOut);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fm = getFragmentManager();
            int count = fm.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }


}
