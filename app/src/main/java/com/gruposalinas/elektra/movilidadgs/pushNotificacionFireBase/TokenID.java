package com.gruposalinas.elektra.movilidadgs.pushNotificacionFireBase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by yvegav on 17/11/2016.
 */

public class TokenID extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ddd", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);/// pendiente cuando este terminado el servicio
    }
}
