package com.eduardo.fabs.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eduardo.fabs.R;

public class MovieDetailsActivity extends AppCompatActivity {

    private static String TAG;
    private static Integer STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_moviedetails);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        TAG = intent.getStringExtra(getString(R.string.intent_activity));
        STATE = intent.getIntExtra(getString(R.string.intent_fragment),0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (TAG.equals(DiscoverMoviesActivity.TAG)) {
                    switch (STATE) {
                        case 0: {
                            Intent intent = new Intent(this, DiscoverMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 0);
                            startActivity(intent);
                            break;
                        }
                        case 1: {
                            Intent intent = new Intent(this, DiscoverMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 1);
                            startActivity(intent);
                            break;
                        }
                        case 2: {
                            Intent intent = new Intent(this, DiscoverMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 2);
                            startActivity(intent);
                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(this, DiscoverMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 3);
                            startActivity(intent);
                            break;
                        }
                    }
                } else {
                    switch (STATE) {
                        case 0: {
                            Intent intent = new Intent(this, MyMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 0);
                            startActivity(intent);
                            break;
                        }
                        case 1: {
                            Intent intent = new Intent(this, MyMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 1);
                            startActivity(intent);
                            break;
                        }
                        case 2: {
                            Intent intent = new Intent(this, MyMoviesActivity.class);
                            intent.putExtra(getString(R.string.intent_fragment), 2);
                            startActivity(intent);
                            break;
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
