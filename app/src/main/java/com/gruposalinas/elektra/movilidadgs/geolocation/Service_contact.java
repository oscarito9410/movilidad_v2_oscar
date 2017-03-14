package com.gruposalinas.elektra.movilidadgs.geolocation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.Activities.Contactos;
import com.gruposalinas.elektra.movilidadgs.async.InsertaTelefonoContactoAsync;
import com.gruposalinas.elektra.movilidadgs.beans.Multimedia;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Service_contact extends Service
{
    Timer timer;

    ArrayList<String> numeros;
    ArrayList<String>cods;

    public Service_contact() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate()
    {
        super.onCreate();


        Log.d("Se creo","Servicio Creado");

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        //wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");
        PowerManager.WakeLock wakeLock1=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DonotSlepp");
        wakeLock1.acquire();


        numeros= new ArrayList<>();
        cods= new ArrayList<>();
        String n1,n2,n3,c1,c2,c3;

        n1=  getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.tel1, "");

        n2=    getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.tel2, "");

        n3=  getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.tel3, "");

        c1=    getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.Codigo, "");

        c2=   getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.Codigo1, "");

        c3=    getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.Codigo2, "");



        if(!n1.equals(""))
        {
            numeros.add(n1);
            cods.add(c1);
        }
        if (!n2.equals(""))
        {
            numeros.add(n2);
            cods.add(c2);

        }
        if(!n3.equals(""))
        {
            numeros.add(n3);
            cods.add(c3);
        }

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              mandar();
            }
        }, 0, 60000L);


        wakeLock1.release();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }


    public void mandar(){

        Multimedia multimedia= new Multimedia();
        multimedia.setCodigo_internacional(cods);
        multimedia.setListacontacos(numeros);
        multimedia.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
        InsertaTelefonoContactoAsync insertaTelefonoContactoAsync= new InsertaTelefonoContactoAsync(this);
        insertaTelefonoContactoAsync.execute(multimedia);

    }

    public void res(Multimedia multimedia)
    {
        if(multimedia.isSuccess())
        {
            timer.cancel();
            stopService(new Intent(getBaseContext(), Service_contact.class));

        }

    }
}
