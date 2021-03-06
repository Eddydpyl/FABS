package com.eduardo.fabs.view.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.eduardo.fabs.R;
import com.eduardo.fabs.model.data.FABSContract;
import com.eduardo.fabs.presenter.miscellany.SmoothActionBarDrawerToggle;
import com.eduardo.fabs.view.SettingsActivity;

import static com.eduardo.fabs.presenter.MovieActivityMethods.setUpNavHeader;

//TODO: Setup tablet display

public class MyMoviesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "myMovies";

    public static String sortOrder;
    private static MenuItem mPreviousMenuItem;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movies);
        setSupportActionBar(toolbar);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        final SmoothActionBarDrawerToggle toggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mymovies);
        navigationView.setNavigationItemSelectedListener(this);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                setUpNavBody(menuItem, drawer, toggle, prefs);
                return true;
            }
        });
        // Retrieve the desired fragment from Intents
        Intent intent = getIntent();
        String savedSortOrder = null;
        if(intent != null){
            savedSortOrder = intent.getStringExtra(getString(R.string.intent_sort_order));
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
            Integer state = prefs.getInt(getString(R.string.pref_my_movies_state),0);
            mPreviousMenuItem = navigationView.getMenu().getItem(state).setChecked(true).setCheckable(true);
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
        // Handle onClick events in nav_header
        setUpNavHeader(navigationView, this.getBaseContext());
    }

    protected void onResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mymovies);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Integer state = prefs.getInt(getString(R.string.pref_my_movies_state),0);
        mPreviousMenuItem = navigationView.getMenu().getItem(state);
        navigationView.setCheckedItem(mPreviousMenuItem.getItemId());
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // Only works as expected as long as there is only the movie collection, will need to be reworked later on
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_mymovies);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            if(prefs.getInt(getString(R.string.pref_my_movies_state),0)==0){
                finish();
            } else{
                final SharedPreferences.Editor edit = prefs.edit();
                edit.putInt(getString(R.string.pref_my_movies_state),0);
                edit.commit();
                sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                setTitle(getString(R.string.title_fragment_my_movies));
                reloadFragment();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mymovies);
                mPreviousMenuItem = navigationView.getMenu().getItem(0);
                mPreviousMenuItem.setChecked(true);
                mPreviousMenuItem.setCheckable(true);
            }
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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setUpNavBody(MenuItem menuItem, DrawerLayout drawer, SmoothActionBarDrawerToggle toggle, SharedPreferences prefs){
        final SharedPreferences.Editor edit = prefs.edit();
        if (mPreviousMenuItem != null) {
            mPreviousMenuItem.setChecked(false);
        }
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        mPreviousMenuItem = menuItem;

        //Closing drawer on item click
        drawer.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
            case(R.id.my_collection):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(getString(R.string.title_fragment_my_movies));
                        edit.putInt(getString(R.string.pref_my_movies_state), 0);
                        edit.commit();
                        MyMoviesFragment fragment = new MyMoviesFragment();
                        sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    }
                });
                break;
            }
            case(R.id.completed):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(getString(R.string.title_fragment_completed_movies));
                        edit.putInt(getString(R.string.pref_my_movies_state), 1);
                        edit.commit();
                        MyMoviesFragment fragment = new MyMoviesFragment();
                        sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING + " DESC";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    }
                });
                break;
            }
            case(R.id.plan_to_watch):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        setTitle(getString(R.string.title_fragment_plan_to_watch_movies));
                        edit.putInt(getString(R.string.pref_my_movies_state), 2);
                        edit.commit();
                        MyMoviesFragment fragment = new MyMoviesFragment();
                        sortOrder = FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    }
                });
                break;
            }
            case(R.id.discover):{
                break;
            }
            case(R.id.popular):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        edit.putInt(getString(R.string.pref_discover_movies_state), 0);
                        edit.commit();
                        startActivity(intent);
                    }
                });
                break;
            }
            case(R.id.top_rated):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        edit.putInt(getString(R.string.pref_discover_movies_state), 1);
                        edit.commit();
                        startActivity(intent);
                    }
                });
                break;
            }
            case(R.id.now_in_theaters):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        edit.putInt(getString(R.string.pref_discover_movies_state), 2);
                        edit.commit();
                        startActivity(intent);
                    }
                });
                break;
            }
            case(R.id.upcoming):{
                toggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyMoviesActivity.this, DiscoverMoviesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        edit.putInt(getString(R.string.pref_discover_movies_state), 3);
                        edit.commit();
                        startActivity(intent);
                    }
                });
                break;
            }
            case(R.id.settings):{
                Intent intent = new Intent(MyMoviesActivity.this, SettingsActivity.class);
                intent.putExtra(getString(R.string.intent_activity), TAG);
                intent.putExtra(getString(R.string.intent_sort_order), sortOrder);
                startActivity(intent);
                break;
            }
        }
    }
}
