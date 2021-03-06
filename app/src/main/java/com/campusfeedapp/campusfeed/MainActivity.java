package com.campusfeedapp.campusfeed;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.campusfeedapp.campusfeed.Adapters.DrawerAdapter;
import com.campusfeedapp.campusfeed.AsyncTasks.HTTPPostAsyncTask;
import com.campusfeedapp.campusfeed.Fragments.DiscoverChannelsFragment;
import com.campusfeedapp.campusfeed.Fragments.HomeFragment;
import com.campusfeedapp.campusfeed.Fragments.MyChannelsFragment;
import com.campusfeedapp.campusfeed.Interfaces.OnHTTPCompleteListener;
import com.campusfeedapp.campusfeed.Utils.Constants;
import com.campusfeedapp.campusfeed.Utils.GCMRegistrationTask;
import com.campusfeedapp.campusfeed.Utils.GoogleServicesUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private List<String> mDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        if (GoogleServicesUtil.checkPlayServices(MainActivity.this)) {
            new GCMRegistrationTask(MainActivity.this,
                    getApplicationContext()).execute();
        }

        initializeDrawerItemList();
        mTitle = mDrawerTitle = mDrawerItems.get(0);
        getActionBar().setTitle(mTitle);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_view);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,  GravityCompat.START);

        // Add items to the ListView
        mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems));
        // Set the OnItemClickListener so something happens when a
        // user clicks on an item.
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enable ActionBar app icon to behave as action to toggle the NavigationDrawer
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set the default content area to item 0
        // when the app opens for the first time
        if(savedInstanceState == null) {
            navigateTo(0);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleServicesUtil.checkPlayServices(MainActivity.this);
    }

    private void initializeDrawerItemList(){
        mDrawerItems = new ArrayList<String>();
        mDrawerItems.add("Home");
        mDrawerItems.add("Channels I own");
        mDrawerItems.add("Discover Channels");
        mDrawerItems.add("Add a post");
        mDrawerItems.add("Logout");
    }


    /*
     * If you do not have any menus, you still need this function
     * in order to open or close the NavigationDrawer when the user
     * clicking the ActionBar app icon.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * When using the ActionBarDrawerToggle, you must call it during onPostCreate()
     * and onConfigurationChanged()
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            getActionBar().setTitle(mDrawerItems.get(position));
            navigateTo(position);
        }
    }

    private void navigateTo(int position) {

        switch(position) {
            case 0:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, HomeFragment.newInstance(), HomeFragment.TAG).commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, MyChannelsFragment.newInstance(), MyChannelsFragment.TAG).commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, DiscoverChannelsFragment.newInstance(), DiscoverChannelsFragment.TAG).commit();
                break;
            case 3:
                MyChannelsFragment myChannelsFragment = MyChannelsFragment.newInstance();
                myChannelsFragment.setAddPost();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,myChannelsFragment,MyChannelsFragment.TAG).commit();
                break;
            case 4:
                HTTPPostAsyncTask httpPostAsyncTask = new HTTPPostAsyncTask(this,true);
                httpPostAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.URL_LOGOUT,new JSONObject().toString());
                httpPostAsyncTask.setHTTPCompleteListener(new OnHTTPCompleteListener() {
                    @Override
                    public void onHTTPDataReceived(String result, String url) {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                });

        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}