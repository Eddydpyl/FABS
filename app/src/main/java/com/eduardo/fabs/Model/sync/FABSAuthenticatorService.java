package com.eduardo.fabs.model.sync;

/**
 * Created by Eduardo on 02/08/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service which allows the sync adapter framework to access the authenticator.
 */

public class FABSAuthenticatorService extends Service{

    private FABSAuthenticator fabsAuthenticator;

    @Override
    public void onCreate() {
        fabsAuthenticator = new FABSAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return fabsAuthenticator.getIBinder();
    }
}
