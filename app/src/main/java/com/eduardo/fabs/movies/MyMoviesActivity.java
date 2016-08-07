package com.eduardo.fabs.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.eduardo.fabs.DonateActivity;
import com.eduardo.fabs.R;
import com.eduardo.fabs.SettingsActivity;
import com.eduardo.fabs.adapters.ExpandableListAdapter;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.models.ExpandedMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMoviesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "myMovies";

    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    Context context;

    private static int state;

    public static void setState(int i){
        state = i;
    }

    public static final String[] MY_MOVIES_COLUMNS = {
        FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE._ID,
            FABSContract.MY_MOVIES_TABLE.COLUMN_POSTER_IMAGE,
            FABSContract.MY_MOVIES_TABLE.COLUMN_OVERVIEW,
            FABSContract.MY_MOVIES_TABLE.COLUMN_RELEASE_DATE,
            FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE,
            FABSContract.MY_MOVIES_TABLE.COLUMN_POPULARITY,
            FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE,
            FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY,
            FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING
    };

    static final int COL_MY_MOVIES_ID = 0;
    static final int COL_MY_MOVIES_POSTER_IMAGE = 1;
    static final int COL_MY_MOVIES_OVERVIEW = 2;
    static final int COL_MY_MOVIES_RELEASE_DATE = 3;
    static final int COL_MY_MOVIES_TITLE = 4;
    static final int COL_MY_MOVIES_POPULARITY = 5;
    static final int COL_MY_MOVIES_VOTE_AVERAGE = 6;
    static final int COL_MY_MOVIES_USER_CATEGORY = 7;
    static final int COL_MY_MOVIES_USER_RATING = 8;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymovies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mymovies);
        setSupportActionBar(toolbar);

        context = this;

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mymovies);
        navigationView.setNavigationItemSelectedListener(this);
        expandableList = (ExpandableListView) findViewById(R.id.navigationMenu_mymovies);

        prepareListData();

        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String childString = (String) mMenuAdapter.getChild(i, i1);
                drawer.closeDrawer(GravityCompat.START);
                switch (childString){
                    case "Completed":{
                        CompletedMoviesFragment fragment = CompletedMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    }
                    case "Plan to Watch":{
                        PlanToWatchMoviesFragment fragment = PlanToWatchMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    }
                    case "Popular":{
                        Intent intent = new Intent(context, DiscoverMoviesActivity.class);
                        intent.putExtra(getString(R.string.intent_fragment), 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    }
                    case "Top Rated":{
                        Intent intent = new Intent(context, DiscoverMoviesActivity.class);
                        intent.putExtra(getString(R.string.intent_fragment), 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    }
                    case "Upcoming":{
                        Intent intent = new Intent(context, DiscoverMoviesActivity.class);
                        intent.putExtra(getString(R.string.intent_fragment), 2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    }
                    case "Now in Theaters":{
                        Intent intent = new Intent(context, DiscoverMoviesActivity.class);
                        intent.putExtra(getString(R.string.intent_fragment), 3);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        // Click on "My Collection"
                        drawer.closeDrawer(GravityCompat.START);
                        MyMoviesFragment fragment = MyMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    }
                    case 1:{
                        // Click on "Discover"
                        break;
                    }
                    case 3:{
                        Intent intent = new Intent(context, SettingsActivity.class);
                        intent.putExtra(getString(R.string.intent_activity), TAG);
                        intent.putExtra(getString(R.string.intent_fragment), state);
                        startActivity(intent);
                        break;
                    }
                    case 4:{
                        // Click on "Donate"
                        Intent intent = new Intent(context, DonateActivity.class);
                        intent.putExtra(getString(R.string.intent_activity), TAG);
                        intent.putExtra(getString(R.string.intent_fragment), state);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
        // Both are expanded by default
        expandableList.expandGroup(0);
        expandableList.expandGroup(1);
        // Retrieve the desired fragment from Intents
        Intent intent = getIntent();
        if(intent != null){
            Integer i = intent.getIntExtra(getString(R.string.intent_fragment), 0);
            if(i != 0){
                setState(i);
            }
        }
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Fragment fragment;
            switch (state){
                case 0:
                    fragment = new MyMoviesFragment();
                    break;
                case 1:
                    fragment = new CompletedMoviesFragment();
                    break;
                case 2:
                    fragment = new PlanToWatchMoviesFragment();
                    break;
                default:
                    fragment = new MyMoviesFragment();
            }
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("My Collection");
        item1.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("Discover");
        item2.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item2);

        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("SEPARATOR");
        item3.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item3);

        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setIconName("Settings");
        item4.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item4);

        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("Donate");
        item5.setIconImg(android.R.drawable.ic_delete);
        listDataHeader.add(item5);

        List<String> heading1 = new ArrayList<String>();
        heading1.add("Completed");
        heading1.add("Plan to Watch");

        List<String> heading2 = new ArrayList<String>();
        heading2.add("Popular");
        heading2.add("Top Rated");
        heading2.add("Upcoming");
        heading2.add("Now in Theaters");
        listDataChild.put(listDataHeader.get(0), heading1);
        listDataChild.put(listDataHeader.get(1), heading2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mymovies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_mymovies) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}