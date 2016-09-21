package com.eduardo.fabs.presenter.miscellany;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;

/**
 * Created by Eduardo on 20/08/2016.
 */

public class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

    private Runnable runnable;
    private Activity activity;

    public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.activity = activity;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        invalidateOptionsMenu(activity);
    }
    @Override
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        invalidateOptionsMenu(activity);
    }
    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
        if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
            runnable.run();
            runnable = null;
        }
    }

    public void runWhenIdle(Runnable runnable) {
        this.runnable = runnable;
    }
}