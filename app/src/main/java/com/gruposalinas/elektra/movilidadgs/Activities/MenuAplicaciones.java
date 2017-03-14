package com.gruposalinas.elektra.movilidadgs.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adrian.vistaweb.vista_web_descarga;
import com.example.adrian.vistaweb.vistawebview;
import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.alertar.Alertas;
import com.gruposalinas.elektra.movilidadgs.async.ObtenerConfiguracionesAplicacion;
import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.beans.Incidencias;
import com.gruposalinas.elektra.movilidadgs.geolocation.HoraExacta;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.Encryption;
import com.gruposalinas.elektra.movilidadgs.utils.Utils;
import com.gruposalinas.elektra.movilidadgs.utils.bloquear_pantalla;

import java.util.StringTokenizer;

public class MenuAplicaciones extends Activity {

    ImageButton Movilidad_GS, FamiliaSocio;
    ImageView panico, Actuliza;
    TextView actulizar,bienvenida, textoActualizar,Version;
    String url,version;
    boolean bandera;
    RelativeLayout progreso;
    bloquear_pantalla bloquearPantalla;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    Alertas alertas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bloquearPantalla= new bloquear_pantalla();
        setContentView(R.layout.activity_menu_aplicaciones);
        init();
        startService(new Intent(getBaseContext(), HoraExacta.class));
        Movilidad_GS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAplicaciones.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });
        FamiliaSocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuAplicaciones.this, vistawebview.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pie_pagina", false);
                startActivity(intent);


            }
        });


        Actuliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progreso.setVisibility(View.VISIBLE);
                tarea();
                bandera = true;

            }
        });

        panico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(prefs.getString(Constants.SP_HASLOGGED, Constants.SP_NOTLOGGED).equals(Constants.SP_NOTLOGGED))
                {validarsesion();

                }else
                {
                    validar();
                }
            }
        });

    }

    public void init()
    {
        Movilidad_GS=(ImageButton)findViewById(R.id.imageButton);
        FamiliaSocio=(ImageButton)findViewById(R.id.imageButton2);
        actulizar=(TextView)findViewById(R.id.actualizar);
        bienvenida=(TextView)findViewById(R.id.bienvenido);
        panico=(ImageView)findViewById(R.id.imageButton3);
        progreso=(RelativeLayout)findViewById(R.id.main_progressbar);
        Actuliza=(ImageView)findViewById(R.id.imageButton4);
        textoActualizar=(TextView)findViewById(R.id.textoactualizar);
        Version=(TextView)findViewById(R.id.version);
        Version.setText(Version.getText().toString()+" 2.2.9");
        alertas= new Alertas(this);

        prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        edit = prefs.edit();
        edit.putInt(Constants.hora, Encryption.getHora());
        edit.putInt(Constants.minuto, Encryption.getminuto());
        edit.apply();
        tarea();




    }


    public void validarsesion(){
        alertas.displayMensaje("Debes iniciar sesión para activar botón de pánico",this);

    }


    public void validar()
    {
        DatosContacto datosContacto= new DatosContacto();

        if(!datosContacto.isguardar(this))
        {
            Intent intent= new Intent(MenuAplicaciones.this,Contactos.class);
            startActivity(intent);

        }
        else{

            Intent intent= new Intent(MenuAplicaciones.this,Panico.class);
                intent.putExtra("Main",false);
                startActivity(intent);
        }

    }
    public void tarea()
    {
        bloquearPantalla.bloquear(this);
        progreso.setVisibility(View.VISIBLE);

        String versionS_O= Build.VERSION.RELEASE;
        String modelo_celular=Build.MANUFACTURER +" "+Build.MODEL;
        @SuppressLint("HardwareIds") String id_IMEI= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Incidencias incidencias= new Incidencias();
        String numero_Empleado=getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.SP_ID, "");
        incidencias.setNumero_empleado(numero_Empleado);
        incidencias.setVersion(getString(R.string.version_name));
        incidencias.setSistema("ANDROID");
        incidencias.setVersion_S_O(versionS_O);
        incidencias.setModelo_Celular(modelo_celular);
        incidencias.setID_Dispositivo(id_IMEI);
        ObtenerConfiguracionesAplicacion obtenerConfiguracionesAplicacion= new ObtenerConfiguracionesAplicacion(this);
        obtenerConfiguracionesAplicacion.execute(incidencias);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(progreso.isShown()){
                ObtenerConfiguracionesAplicacion obtenerConfiguracionesAplicacion= new ObtenerConfiguracionesAplicacion(this);
                obtenerConfiguracionesAplicacion.cancel(true);
                finish();
            }
            else{
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public  void respuesta(Incidencias incidencias)
    {
        progreso.setVisibility(View.INVISIBLE);
        bloquearPantalla.deslbloquear();
        if(incidencias.isSuccess())
        {
            String fechapura;
            StringTokenizer tokenizer = new StringTokenizer(incidencias.getFECHA_LIMITE());
            int contador1 = 0;
            String[] datos = new String[2];
            while (tokenizer.hasMoreTokens())
            {
                datos[contador1] = tokenizer.nextToken();
                contador1++;
            }
            fechapura=datos[0];
            int contador=0;
            String datos1[]= new String[3];
            StringTokenizer tokenizer1 = new StringTokenizer(fechapura,"-");

            while (tokenizer1.hasMoreTokens())
            {
                datos1[contador]=tokenizer1.nextToken();
                contador++;
            }

            String finalfecha=datos1[0]+"-"+datos1[1]+"-"+datos1[2];



            if(Utils.fecha_actual().compareTo(finalfecha)>0)
            {

                if( getString(R.string.version_name).compareTo(incidencias.getVersion())<0)
                {
                    Movilidad_GS.setVisibility(View.INVISIBLE);
                    FamiliaSocio.setVisibility(View.INVISIBLE);

                    textoActualizar.setGravity(Gravity.START);
                    bienvenida.setText("No tienes acceso a la aplicación debido a que no la tienes actualizada,aunque sí sigue reportando ubicaciones.\nDesgraciadamente la fecha de actualización ha terminado.");

                }

            }

            url=incidencias.getURL();
            version=incidencias.getVersion();
            if(version.equals(getString(R.string.version_name))&& bandera)
            {
               // alerta de que son igual
                String mensaje=getString(R.string.mensajeactualizado);
                alerta(mensaje);
            }
            if(bandera && getString(R.string.version_name).compareTo(version)<0)
            {
                Intent intent= new Intent(MenuAplicaciones.this,vista_web_descarga.class);
                intent.putExtra("URL",url);
                startActivity(intent);

            }
            if(version.compareTo(getString(R.string.version_name))>0){
                String mensaje="Actualmente Existe una nueva versión de la aplicación Socio MAS. \n presiona el icono de actualizar App para descargar la nueva versión";
                alerta(mensaje);
            }



        }
    }

    public void alerta(String mensaje){

        final Dialog alert = new Dialog(MenuAplicaciones.this,R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        final View dialogo=inflater1.inflate(R.layout.alerta_error, null);
        TextView titulodialo=(TextView)dialogo.findViewById(R.id.tituloDialogo);
        LinearLayout confirmar=(LinearLayout)dialogo.findViewById(R.id.boton_confirmar);

        titulodialo.setGravity(Gravity.CENTER_HORIZONTAL);
        if(mensaje.equals("Actualmente Existe una nueva versión de la aplicación Socio MAS. \n" +
                " presiona el icono de actualizar App para descargar la nueva versión"))
        {
            titulodialo.setText(" Tu aplicación no está actualizada");

        }
        else
        {
            titulodialo.setText(" Tu aplicación está actualizada.");


        }
        TextView error1=(TextView)dialogo.findViewById(R.id.mensajerrror);
        error1.setText(mensaje);
        alert.setContentView(dialogo);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }





}
