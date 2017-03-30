package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Permisos;
import com.gruposalinas.elektra.movilidadgs.utils.ApplicationBase;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.SecurityItems;
import com.gruposalinas.elektra.movilidadgs.utils.SessionManager;
import com.gruposalinas.elektra.movilidadgs.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Adrian Guzman on 20/09/2016.
 */
public class Permisos_empleadoWS
{

    public static String TAG = "Permisos";
    String line=null;
    StringBuilder sb = new StringBuilder();
    String resultado="";
    JSONArray jsonArray;




    public Permisos permisos (Permisos permisos)
    {
        permisos.setSuccess(false);

        String URL = Constants.DOMAIN_URL + "/" + Constants.ConsultarExclusionesEmpleados;
        SecurityItems securityItems = new SecurityItems(permisos.getId_numero_empleado());
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id_num_empleado",securityItems.getIdEmployEncrypt());
            jsonObject.put("estatus",Integer.valueOf(permisos.getEstatus()));
            jsonObject.put("token",securityItems.getTokenEncrypt());
            jsonObject.put("busqueda","");
            jsonObject.put("tipo",permisos.getTipo());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "checando permisos" + jsonObject.toString());

        HttpURLConnection urlConnection=null;
        try{
            java.net.URL url = new URL(URL);
            urlConnection = Utils.checkIfHttps(url);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            OutputStreamWriter out = new   OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonObject+"");
            out.close();

            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult == HttpsURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                    resultado=sb.toString();
                }
                br.close();

                System.out.println(""+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
                permisos.setSuccess(false);
                permisos.setMensajeError(urlConnection.getResponseMessage());
            }
            JSONObject obj;


            try{
                obj = new JSONObject(resultado);
                jsonArray= new JSONArray(obj.getString("exclusionesEmpleado"));
                permisos.setDatos(jsonArray);

                permisos.setMensajeError(obj.getString("mensajeError"));
                if(obj.getString("error").equals("false"))
                {

                    permisos.setSuccess(true);

                }
                else{

                    permisos.setSuccess(false);
                }
            }
            catch (JSONException e1){
                e1.printStackTrace();
                permisos.setMensajeError(e1.toString());
                permisos.setSuccess(false);

            }
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            permisos.setMensajeError(e.toString());
            permisos.setSuccess(false);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            permisos.setMensajeError(e.toString());
            permisos.setSuccess(false);
        } catch (IOException e) {
            permisos.setMensajeError(e.toString());
            e.printStackTrace();
            permisos.setSuccess(false);
        }

        return permisos;


    }


}
