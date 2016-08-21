package com.eduardo.fabs.movies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.SettingsActivity;
import com.eduardo.fabs.adapters.NavigationAdapter;
import com.eduardo.fabs.adapters.SmoothActionBarDrawerToggle;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.sync.FABSSyncAdapter;

//TODO: Setup tablet display

public class MyMoviesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "myMovies";

    public static String sortOrder;
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            FABSSyncAdapter.syncImmediately(this);
        }
        FABSSyncAdapter.periodicSync(this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        final SmoothActionBarDrawerToggle toggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mymovies);
        navigationView.setNavigationItemSelectedListener(this);
        final ListView listView = (ListView) findViewById(R.id.navigationMenu_mymovies);
        final String[] listItems = getResources().getStringArray(R.array.list_nav_movies);
        NavigationAdapter navigationAdapter = new NavigationAdapter(this, R.layout.nav_item, listItems);
        listView.setAdapter(navigationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                setTitle(getString(R.string.title_fragment_my_movies));
                                MyMoviesActivity.setState(0);
                                MyMoviesFragment fragment = new MyMoviesFragment();
                                Bundle bundle = new Bundle(1);
                                bundle.putInt(getString(R.string.intent_fragment), state);
                                fragment.setArguments(bundle);
                                sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 1: {
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                setTitle(getString(R.string.title_fragment_completed_movies));
                                MyMoviesActivity.setState(1);
                                MyMoviesFragment fragment = new MyMoviesFragment();
                                Bundle bundle = new Bundle(1);
                                bundle.putInt(getString(R.string.intent_fragment), state);
                                fragment.setArguments(bundle);
                                sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 2: {
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                setTitle(getString(R.string.title_fragment_plan_to_watch_movies));
                                MyMoviesActivity.setState(2);
                                MyMoviesFragment fragment = new MyMoviesFragment();
                                Bundle bundle = new Bundle(1);
                                bundle.putInt(getString(R.string.intent_fragment), state);
                                fragment.setArguments(bundle);
                                sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 3: {
                        break;
                    }
                    case 4: {
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                                intent.putExtra(getString(R.string.intent_fragment), 0);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 5:{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                                intent.putExtra(getString(R.string.intent_fragment), 1);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 6:{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                                intent.putExtra(getString(R.string.intent_fragment), 2);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 7:{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                                intent.putExtra(getString(R.string.intent_fragment), 3);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        });
                        listView.setItemChecked(i, true);
                        drawer.closeDrawers();
                        break;
                    }
                    case 8:{
                        break;
                    }
                    case 9:{
                        Intent intent = new Intent(MyMoviesActivity.this, SettingsActivity.class);
                        intent.putExtra(getString(R.string.intent_activity), TAG);
                        intent.putExtra(getString(R.string.intent_fragment), state);
                        intent.putExtra(getString(R.string.intent_sort_order), sortOrder);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        // Retrieve the desired fragment from Intents
        Intent intent = getIntent();
        String savedSortOrder = null;
        if(intent != null){
            Integer i = intent.getIntExtra(getString(R.string.intent_fragment), 0);
            savedSortOrder = intent.getStringExtra(getString(R.string.intent_sort_order));
            setState(i);
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
            MyMoviesFragment fragment = new MyMoviesFragment();
            Bundle bundle = new Bundle(1);
            bundle.putInt(getString(R.string.intent_fragment), state);
            fragment.setArguments(bundle);
            switch (state){
                case 0:
                    sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                    setTitle(getString(R.string.title_fragment_my_movies));
                    break;
                case 1:
                    sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                    setTitle(getString(R.string.title_fragment_completed_movies));
                    break;
                case 2:
                    sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
                    setTitle(getString(R.string.title_fragment_plan_to_watch_movies));
                    break;
                default:
                    sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                    setTitle(getString(R.string.title_fragment_my_movies));
            }
            // Restore the sort order after clicking home in some other activity that returns here
            if(savedSortOrder!=null){
                sortOrder = savedSortOrder;
            }
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
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
    public void startActivity(Intent intent) {
        //check if search intent
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra(getString(R.string.intent_activity), TAG);
            intent.putExtra(getString(R.string.intent_fragment), state);
            intent.putExtra(getString(R.string.intent_sort_order), sortOrder);
        }

        super.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymovies, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_mymovies).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuSortDate_mymovies) {
            sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_RELEASE_DATE;
            reloadFragment();
            return true;
        } else if(id == R.id.menuSortPopularity_mymovies){
            sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
            reloadFragment();
            return true;
        } else if(id == R.id.menuSortAverageRating_mymovies){
            sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
            reloadFragment();
            return true;
        } else if(id == R.id.menuSortPersonalRating_mymovies){
            sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
            reloadFragment();
            return true;
        } return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void reloadFragment(){
        MyMoviesFragment fragment = new MyMoviesFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(getString(R.string.intent_fragment), state);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
