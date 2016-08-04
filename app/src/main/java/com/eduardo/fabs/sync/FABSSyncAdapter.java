package com.eduardo.fabs.sync;

/**
 * Created by Eduardo on 02/08/2016.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.eduardo.fabs.R;
import com.eduardo.fabs.fetch.FetchMovies;
import com.eduardo.fabs.utils.Constants;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */

public class FABSSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 60 * 60 * 24; // 24 hours

    ContentResolver mContentResolver;

    public FABSSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public FABSSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    private static Account getAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(context.getString(R.string.app_name), Constants.ACCOUNT_TYPE);

        if (accountManager.getPassword(newAccount) == null) {
            if (!accountManager.addAccountExplicitly(newAccount, null, null)) {
                return null;
            }
        }
        return newAccount;
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getAccount(context), Constants.CONTENT_AUTHORITY, bundle);
    }

    public static void periodicSync(Context context){
        ContentResolver.addPeriodicSync(getAccount(context), Constants.CONTENT_AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        FetchMovies.updateData(getContext());
    }
}