package com.eduardo.fabs.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.SettingsActivity;
import com.eduardo.fabs.adapters.SmoothActionBarDrawerToggle;
import com.eduardo.fabs.data.FABSContract;

public class DiscoverMoviesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = "discoverMovies";

    public static String sortOrder;
    public static SearchView searchView;
    public static Boolean searching;
    private static MenuItem mPreviousMenuItem;


    static final int COL_DISCOVER_MOVIES_ID = 0;
    static final int COL_DISCOVER_MOVIES_POSTER_IMAGE = 1;
    static final int COL_DISCOVER_MOVIES_OVERVIEW= 2;
    static final int COL_DISCOVER_MOVIES_RELEASE_DATE = 3;
    static final int COL_DISCOVER_MOVIES_TITLE = 4;
    static final int COL_DISCOVER_MOVIES_POPULARITY = 5;
    static final int COL_DISCOVER_MOVIES_VOTE_AVERAGE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovermovies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movies);
        setSupportActionBar(toolbar);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final SharedPreferences.Editor edit = prefs.edit();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_discovermovies);
        final SmoothActionBarDrawerToggle toggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        searching = false;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_discovermovies);
        navigationView.setNavigationItemSelectedListener(this);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
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
                                Intent intent = new Intent(DiscoverMoviesActivity.this, MyMoviesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                edit.putInt(getString(R.string.pref_my_movies_state), 0);
                                edit.commit();
                                startActivity(intent);
                            }
                        });
                        break;
                    }
                    case(R.id.completed):{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(DiscoverMoviesActivity.this, MyMoviesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                edit.putInt(getString(R.string.pref_my_movies_state), 1);
                                edit.commit();
                                startActivity(intent);
                            }
                        });
                        break;
                    }
                    case(R.id.plan_to_watch):{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(DiscoverMoviesActivity.this, MyMoviesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                edit.putInt(getString(R.string.pref_my_movies_state), 2);
                                edit.commit();
                                startActivity(intent);
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
                                edit.putInt(getString(R.string.pref_discover_movies_state), 0);
                                edit.commit();
                                PopularMoviesFragment fragment = new PopularMoviesFragment();
                                sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        break;
                    }
                    case(R.id.top_rated):{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                edit.putInt(getString(R.string.pref_discover_movies_state), 1);
                                edit.commit();
                                TopRatedMoviesFragment fragment = new TopRatedMoviesFragment();
                                sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        break;
                    }
                    case(R.id.now_in_theaters):{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                edit.putInt(getString(R.string.pref_discover_movies_state), 2);
                                edit.commit();
                                NowInTheatersMoviesFragment fragment = new NowInTheatersMoviesFragment();
                                sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        break;
                    }
                    case(R.id.upcoming):{
                        toggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {
                                edit.putInt(getString(R.string.pref_discover_movies_state), 3);
                                edit.commit();
                                UpcomingMoviesFragment fragment = new UpcomingMoviesFragment();
                                sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            }
                        });
                        break;
                    }
                    case(R.id.settings):{
                        Intent intent = new Intent(DiscoverMoviesActivity.this, SettingsActivity.class);
                        intent.putExtra(getString(R.string.intent_activity), TAG);
                        intent.putExtra(getString(R.string.intent_sort_order), sortOrder);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });

        // Retrieve the desired fragment from Intents
        Intent intent = getIntent();
        String savedSortOrder = null;
        if(intent != null) {
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
            Fragment fragment;
            Integer state = prefs.getInt(getString(R.string.pref_discover_movies_state),0);
            if(navigationView.getMenu().size()>0){
                mPreviousMenuItem = navigationView.getMenu().getItem(3).getSubMenu().getItem(state).setChecked(true).setCheckable(true);
            }
            switch (state){
                case 0:
                    fragment = new PopularMoviesFragment();
                    sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
                    break;
                case 1:
                    fragment = new TopRatedMoviesFragment();
                    sortOrder = FABSContract.TOP_RATED_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
                    break;
                case 2:
                    fragment = new NowInTheatersMoviesFragment();
                    sortOrder = FABSContract.NOW_IN_THEATERS_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
                    break;
                case 3:
                    fragment = new UpcomingMoviesFragment();
                    sortOrder = FABSContract.UPCOMING_MOVIES_TABLE.COLUMN_RELEASE_DATE;
                    break;
                default:
                    fragment = new PopularMoviesFragment();
                    sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
            }
            // Restore the sort order after clicking home in some other activity that returns here
            if(savedSortOrder!=null)
                sortOrder = savedSortOrder;
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        /// Handle onClick events in nav_header
        View headerView = navigationView.getHeaderView(0);
        final float scale = getResources().getDisplayMetrics().density;
        final int pixelsBig  = (int) (85 * scale + 0.5f);
        final int pixelsSmall  = (int) (55 * scale + 0.5f);
        final ImageView imageView_movies = (ImageView) headerView.findViewById(R.id.imageView_movies);
        final ImageView imageView_anime = (ImageView) headerView.findViewById(R.id.imageView_anime);
        final ImageView imageView_books = (ImageView) headerView.findViewById(R.id.imageView_books);
        final ImageView imageView_series= (ImageView) headerView.findViewById(R.id.imageView_series);
        imageView_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsBig;
                imageView_movies.getLayoutParams().width = pixelsBig;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsSmall;
                imageView_anime.getLayoutParams().width = pixelsSmall;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsSmall;
                imageView_books.getLayoutParams().width = pixelsSmall;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsSmall;
                imageView_series.getLayoutParams().width = pixelsSmall;
                imageView_series.requestLayout();
            }
        });
        imageView_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsSmall;
                imageView_movies.getLayoutParams().width = pixelsSmall;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsBig;
                imageView_anime.getLayoutParams().width = pixelsBig;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsSmall;
                imageView_books.getLayoutParams().width = pixelsSmall;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsSmall;
                imageView_series.getLayoutParams().width = pixelsSmall;
                imageView_series.requestLayout();
            }
        });
        imageView_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsSmall;
                imageView_movies.getLayoutParams().width = pixelsSmall;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsSmall;
                imageView_anime.getLayoutParams().width = pixelsSmall;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsBig;
                imageView_books.getLayoutParams().width = pixelsBig;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsSmall;
                imageView_series.getLayoutParams().width = pixelsSmall;
                imageView_series.requestLayout();
            }
        });
        imageView_series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsSmall;
                imageView_movies.getLayoutParams().width = pixelsSmall;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsSmall;
                imageView_anime.getLayoutParams().width = pixelsSmall;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsSmall;
                imageView_books.getLayoutParams().width = pixelsSmall;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsBig;
                imageView_series.getLayoutParams().width = pixelsBig;
                imageView_series.requestLayout();
            }
        });
        // Set Movies as selected
        imageView_movies.getLayoutParams().height = pixelsBig;
        imageView_movies.getLayoutParams().width = pixelsBig;
        imageView_movies.requestLayout();
        imageView_anime.getLayoutParams().height = pixelsSmall;
        imageView_anime.getLayoutParams().width = pixelsSmall;
        imageView_anime.requestLayout();
        imageView_books.getLayoutParams().height = pixelsSmall;
        imageView_books.getLayoutParams().width = pixelsSmall;
        imageView_books.requestLayout();
        imageView_series.getLayoutParams().height = pixelsSmall;
        imageView_series.getLayoutParams().width = pixelsSmall;
        imageView_series.requestLayout();
    }

    protected void onResume() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_discovermovies);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mPreviousMenuItem = navigationView.getMenu().getItem(3).getSubMenu().getItem(prefs.getInt(getString(R.string.pref_discover_movies_state),0));
        navigationView.setCheckedItem(mPreviousMenuItem.getItemId());
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // Only works as expected as long as there is only the movie collection, will need to be reworked later on
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_discovermovies);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(DiscoverMoviesActivity.this, MyMoviesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.discovermovies, menu);
        searchView = (SearchView) menu.findItem(R.id.search_discovermovies).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuSortDate_discovermovies) {
            sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_RELEASE_DATE;
            reloadFragment();
            return true;
        } else if(id == R.id.menuSortPopularity_discovermovies){
            sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_POPULARITY + " DESC";
            reloadFragment();
            return true;
        } else if(id == R.id.menuSortRating_discovermovies){
            sortOrder = FABSContract.POPULAR_MOVIES_TABLE.COLUMN_VOTE_AVERAGE + " DESC";
            reloadFragment();
            return true;
        } return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_discovermovies);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void reloadFragment(){
        Fragment fragment;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Integer state = prefs.getInt(getString(R.string.pref_discover_movies_state),0);
        switch (state){
            case 0:
                fragment = new PopularMoviesFragment();
                break;
            case 1:
                fragment = new TopRatedMoviesFragment();
                break;
            case 2:
                fragment = new NowInTheatersMoviesFragment();
                break;
            case 3:
                fragment = new UpcomingMoviesFragment();
                break;
            default:
                fragment = new PopularMoviesFragment();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
