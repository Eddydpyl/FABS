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

import com.eduardo.fabs.R;
import com.eduardo.fabs.SettingsActivity;
import com.eduardo.fabs.adapters.ExpandableListAdapter;
import com.eduardo.fabs.models.ExpandedMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscoverMoviesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "discoverMovies";

    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    Context context;

    private static int state;

    public static void setState(int i){
        state = i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovermovies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_discovermovies);
        setSupportActionBar(toolbar);

        context = this;

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_discovermovies);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_discovermovies);
        navigationView.setNavigationItemSelectedListener(this);
        expandableList = (ExpandableListView) findViewById(R.id.navigationMenu_discovermovies);

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
                        Intent intent = new Intent(context, MyMoviesActivity.class);
                        intent.putExtra(getString(R.string.intent_fragment), 1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    }
                    case "Plan to Watch":{
                        Intent intent = new Intent(context, MyMoviesActivity.class);
                        intent.putExtra(getString(R.string.intent_fragment), 2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    }
                    case "Popular":{
                        PopularMoviesFragment fragment = PopularMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    }
                    case "Top Rated":{
                        TopRatedMoviesFragment fragment = TopRatedMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    }
                    case "Upcoming":{
                        UpcomingMoviesFragment fragment = UpcomingMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    }
                    case "Now in Theaters":{
                        NowInTheatersMoviesFragment fragment = NowInTheatersMoviesFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
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
                        Intent intent = new Intent(context, MyMoviesFragment.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
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
                        Intent intent = new Intent();
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
        if(intent != null) {
            Integer i = intent.getIntExtra(getString(R.string.intent_fragment), 0);
            if (i != 0) {
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
                    fragment = PopularMoviesFragment.newInstance(1);
                    break;
                case 1:
                    fragment = TopRatedMoviesFragment.newInstance(1);
                    break;
                case 2:
                    fragment = UpcomingMoviesFragment.newInstance(1);
                    break;
                case 3:
                    fragment = NowInTheatersMoviesFragment.newInstance(1);
                    break;
                default:
                    fragment = PopularMoviesFragment.newInstance(1);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_discovermovies);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.discovermovies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_discovermovies) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_discovermovies);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}