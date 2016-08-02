package com.eduardo.fabs.sync;

/**
 * Created by Eduardo on 02/08/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FABSSyncService extends Service {

    private static FABSSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new FABSSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}