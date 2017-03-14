package com.gruposalinas.elektra.movilidadgs.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.adapters.AdapterConfiguracion;
import com.gruposalinas.elektra.movilidadgs.beans.Configuracion;
import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.beans.Empleado;
import com.gruposalinas.elektra.movilidadgs.beans.ObtenerEstatusAlerta;
import com.gruposalinas.elektra.movilidadgs.beans.RangoMonitoreo;
import com.gruposalinas.elektra.movilidadgs.beans.RegistroGPS;
import com.gruposalinas.elektra.movilidadgs.beans.paginaweb;
import com.gruposalinas.elektra.movilidadgs.geolocation.EmployeeLocationService;
import com.gruposalinas.elektra.movilidadgs.geolocation.GPS_Service;
import com.gruposalinas.elektra.movilidadgs.geolocation.HoraExacta;
import com.gruposalinas.elektra.movilidadgs.geolocation.RecorderService;
import com.gruposalinas.elektra.movilidadgs.geolocation.ServicePanico;
import com.gruposalinas.elektra.movilidadgs.geolocation.Service_contact;
import com.gruposalinas.elektra.movilidadgs.geolocation.serviceEvento;
import com.gruposalinas.elektra.movilidadgs.ormlite.DBHelper;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConfiguracionApp extends Activity {

    private static final String TAG = "config";
    ListView listConfig;
    AdapterConfiguracion adapterConfiguracion;
    ArrayList<String> lista;
    ArrayList <Integer> imagenes;
    DBHelper mDBHelper;
    ImageView back;
    LinearLayout Panico1;
    ImageView paginaWEb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_app);

       init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void init()
    {
        lista= new ArrayList<>();
        imagenes= new ArrayList<>();
        back=(ImageView)findViewById(R.id.boton_regresar);
        paginaWEb=(ImageView)findViewById(R.id.paginaweb);

        Panico1=(LinearLayout)findViewById(R.id.panico);

        lista.add("Información Personal");
        lista.add("Información de contactos de emergencia");
      //  lista.add("Ayuda y Soporte");
      //  lista.add("Cerrar Sesión");
        imagenes.add(R.drawable.perfil_ekt);
        imagenes.add(R.drawable.icono_usuario_x2);
      //  imagenes.add(android.R.drawable.ic_menu_manage);
      //  imagenes.add(android.R.drawable.ic_menu_close_clear_cancel);


        listConfig=(ListView)findViewById(R.id.listaConfig);
        adapterConfiguracion= new AdapterConfiguracion(this,R.layout.item_horariosvista,lista);
        adapterConfiguracion.setLista(lista);
        adapterConfiguracion.setImagenes(imagenes);
        listConfig.setAdapter(adapterConfiguracion);

       listConfig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id)
           {


               if(position==0)
               {
                   Intent i= new Intent(ConfiguracionApp.this,Settings_perfil.class);
                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(i);
               }

               if(position==1)
               {
                   Intent i= new Intent(ConfiguracionApp.this,Contactos.class);
                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(i);

               }
               if(position==3)
               {
               //  borrar_sesion();
               }

           }
       });



        paginaWEb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paginaweb paginaweb = new paginaweb(getApplicationContext());


            }
        });
        Panico1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                validar();

            }
        });

    }

    public void borrar_sesion()
    {

        DBHelper helper = OpenHelperManager.getHelper(this, DBHelper.class);
        Dao dao = null;

        try {
            dao = helper.getDao(Empleado.class);

            DeleteBuilder<Empleado, Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.delete();



            dao = helper.getDao(RegistroGPS.class);

            DeleteBuilder<RegistroGPS, Integer> deleteBuilder1 = dao.deleteBuilder();
            deleteBuilder1.delete();

            dao = helper.getDao(RangoMonitoreo.class);

            DeleteBuilder<RangoMonitoreo, Integer> deleteBuilder2 = dao.deleteBuilder();
            deleteBuilder2.delete();


            dao = helper.getDao(ObtenerEstatusAlerta.class);

            DeleteBuilder<ObtenerEstatusAlerta, Integer> deleteBuilder3 = dao.deleteBuilder();
            deleteBuilder3.delete();

            SharedPreferences prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            edit.putString(Constants.SP_HASLOGGED, Constants.SP_NOTLOGGED);
            edit.apply();
            stopService(new Intent(this,EmployeeLocationService.class));
            stopService(new Intent(this,GPS_Service.class));
            stopService(new Intent(this,HoraExacta.class));
            stopService(new Intent(this,RecorderService.class));
            stopService(new Intent(this,serviceEvento.class));
            stopService(new Intent(this,ServicePanico.class));
            stopService(new Intent(this,Service_contact.class));

            Intent i = new Intent(ConfiguracionApp.this,RegistroActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);




        } catch (SQLException e)
        {
            Log.d(TAG,"Error");

            e.printStackTrace();

        }
    }

    public void validar()
    {
        DatosContacto datosContacto= new DatosContacto();

        if(!datosContacto.isguardar(this))
        {
            Intent intent= new Intent(this,Contactos.class);
            startActivity(intent);

        }
        else{

            Intent intent= new Intent(this,Panico.class);
            intent.putExtra("Main",false);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
