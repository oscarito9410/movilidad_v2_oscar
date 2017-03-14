package com.gruposalinas.elektra.movilidadgs.pushNotificacionFireBase;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by yvegav on 17/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      Log.e("FIreBase",remoteMessage.getNotification().getBody());

    }


}
