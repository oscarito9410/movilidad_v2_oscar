package com.gruposalinas.elektra.movilidadgs.beans;

import android.content.Context;

import com.gruposalinas.elektra.movilidadgs.utils.Constants;

/**
 * Created by Adrian Guzman on 08/07/2016.
 */
public class DatosContacto
{
    public String gettel1(Context context)
    {
       return context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.tel1, "");


    }

    public String gettel2(Context context)
    {
       return context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.tel2, "");

    }
    public String gettel3(Context context)
    {
       return context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.tel3, "");

    }
    public String nombre1 (Context context){
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.nombre1,"");
    }

    public String nombre2(Context context){
        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.nombre2,"");
    }

    public String nombre3 (Context context){
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.nombre3,"");
    }

    public String getProvider(Context context){
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.provider,"network");
    }
    public boolean isguardar(Context context){

        return context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean("guardar", false);

    }
    public String path (Context context){
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.path,"");

    }


    public String lada (Context context){

        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.lada,"Pais");

    }

    public String lada1(Context context){
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.lada1,"Pais");

    }

    public String lada2 (Context context)
    {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.lada2,"Pais");

    }

    public String usuario (Context context)
    {

        return context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.SP_NAME,"");
    }
    public String enterprise(Context context){


        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.SP_ENTERPRISE,"");
    }

    public String Numero_jefe(Context context){


        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.NUMERO_JEFE,"");
    }

    public String ID_EMPLEADO(Context context){


        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.SP_ID,"");

    }

    public String getMY_PHONE(Context context)
    {
        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.MY_PHONE,"");

    }


    public String get_MY_CODIGO(Context context)
    {
        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.MY_CODIGO,"");

    }

    public String get_MY_CLAVE(Context context)
    {
        return  context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.MY_CLAVE,"");

    }

}
