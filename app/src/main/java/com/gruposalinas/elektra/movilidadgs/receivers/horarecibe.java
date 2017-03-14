package com.gruposalinas.elektra.movilidadgs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gruposalinas.elektra.movilidadgs.geolocation.GPS_Service;


public class horarecibe extends BroadcastReceiver {
    public horarecibe() {
    }

    Context context;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: This method is called when the BroadcastReceiver is receiving
        this.context=context;
        Intent i =new Intent(this.context,GPS_Service.class);
        this.context.startService(i);


    }



}
