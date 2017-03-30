package com.gruposalinas.elektra.movilidadgs.Activities;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.gruposalinas.elektra.movilidadgs.alertar.Alertas;
import com.gruposalinas.elektra.movilidadgs.beans.RangoMonitoreo;
import com.gruposalinas.elektra.movilidadgs.beans.RegistroGPS;
import com.gruposalinas.elektra.movilidadgs.ormlite.DBHelper;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.SessionManager;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity{
    public static final String TAG="BASE_ACTIVITY";

    @Override
    protected void onResume() {
        super.onResume();
        this.timerMovimientos();
    }

    private  void checkIfGps(){

    }


    /*
    * MUESTRA LOS MOVIMIENTOS QUE HA TENIDO EL USUARIO
    * CADA DOS SEGUNDOS
    * */

    private  void timerMovimientos(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showListMovimientos();
            }
        },2000);
    }

    protected  void displayAlertFecha(){
        SessionManager sessionManager = new SessionManager(BaseActivity.this);
        if(sessionManager.get(Constants.IS_ERROR_FECHA)) {
            Alertas alerta = new Alertas(BaseActivity.this);
            alerta.displayMensaje("Creo que la fecha no esta en correcto formato", BaseActivity.this);
        }
    }

    protected  void showHorarios(){

        Dao dao;
        DBHelper dbHelper = null;
        List<RangoMonitoreo> registros= null;
        try {
            dao = DBHelper.getHelper(getApplicationContext(), dbHelper).getRangosMonitoreoDao();
            registros = dao.queryForAll();
            if (registros.size() < 1) {
                Toast.makeText(this, "Ningún registro encontrado ", Toast.LENGTH_SHORT).show();
            }
            else{
                String result = "";
                for (int i = 0; i < registros.size(); i++) {
                    if (i == 0) {
                        result += "REG#" + i + " = " + registros.get(i).getFecha() + ": E:" + registros.get(i).getHoraInicial()
                                + "S" + registros.get(i).getHoraFinal();
                    } else {
                        result += "/n REG#" + i + " = " + registros.get(i).getFecha() + ": E:" + registros.get(i).getHoraInicial()
                                + "S" + registros.get(i).getHoraFinal();
                    }
                }

                Log.i( TAG,result + " numero de elementos." + registros.size());
            }

        }
        catch(Exception e){
            Log.e(TAG, "No se cargaron registros");
        }
        }


    protected   void showListMovimientos(){

        //Manda un log imprimiendo todos los datos en la BD
        Dao dao;
        DBHelper dbHelper = null;
        List<RegistroGPS> registros = null;
        try {
            dao = DBHelper.getHelper(getApplicationContext(), dbHelper).getGpsDAO();
            registros = dao.queryForAll();

            Log.i(TAG, "Size = " + registros.size());
            if (registros.size() < 1) {
                Toast.makeText(this, "Ningún registro encontrado ", Toast.LENGTH_SHORT).show();
            }
            else {
                String result = "";
                for (int i = 0; i < registros.size(); i++) {
                    if (i == 0) {
                        result += "REG#" + i + " = " + registros.get(i).getFecha() + ":" + registros.get(i).getHora()
                                + "/" + registros.get(i).getLatitud() + "||" + registros.get(i).getLongitud()
                                + "->" + registros.get(i).isSuccess();
                    } else {
                        result += "\nREG#" + i + " = " + registros.get(i).getFecha() + " " + registros.get(i).getHora()
                                + "/" + registros.get(i).getLatitud() + "||" + registros.get(i).getLongitud()
                                + "->" + registros.get(i).isSuccess();
                    }
                }

                Log.i( TAG,result + " numero de elementos." + registros.size());
                }
            }
        catch(Exception e){
            Log.e(TAG, "No se cargaron registros");
        }

  //      ArrayList<RegistroGPS> registrosList = (ArrayList) registros;
//        Log.d(TAG, "Registro 0 = " + registrosList.get(0).getId());

        // Sets the data behind this ListView
        Log.i(TAG, "Elementos = " + registros.size());
    }
}







