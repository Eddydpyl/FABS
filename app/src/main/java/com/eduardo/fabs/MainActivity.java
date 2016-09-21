package com.eduardo.fabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.eduardo.fabs.view.movies.MyMoviesActivity;
import com.eduardo.fabs.model.sync.FABSSyncAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final SharedPreferences.Editor edit = prefs.edit();
        final boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            FABSSyncAdapter.syncImmediately(this);
        }
        FABSSyncAdapter.periodicSync(this);
        edit.putInt(getString(R.string.pref_my_movies_state),0);
        edit.commit();
        Intent intent = new Intent(MainActivity.this, MyMoviesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        super.onCreate(savedInstanceState);
    }
}
