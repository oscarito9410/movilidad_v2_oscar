package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Empleado;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.SecurityItems;
import com.gruposalinas.elektra.movilidadgs.utils.Utils;

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
 * Created by Adrian Guzman on 15/01/2016.
 */
public class ChecarContrasenaWS {
    public static String TAG = "ChecKContrasena";
    String datos1;
    String line=null;
    StringBuilder sb = new StringBuilder();
    String resultado="";

public ChecarContrasenaWS(String datos){
    datos1=datos;

}
    public Empleado checar(Empleado empleado)
    {
        empleado.setSuccess(false);
        String URL = Constants.DOMAIN_URL + "/" + Constants.validarcontrasenia;

        String contrasena=(datos1);
        String numero_empleado=((empleado.getIdEmployee()));

        SecurityItems securityItems = new SecurityItems(numero_empleado);

        JSONObject jsonObject= new JSONObject();

        try {
            jsonObject.put("id_num_empleado",securityItems.getIdEmployEncrypt());
            //TODO REVISA SI ESTA PASSWORD DEVER DE IR ENCRIPTADA O NO! PENDENTE CON ROMEO
            jsonObject.put("strPassword",securityItems.getPasswordEncrypt());
            jsonObject.put("token",securityItems.getTokenEncrypt());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "inicia contraseniavalida"+jsonObject.toString());
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
            out.write(jsonObject.toString());
            out.close();
            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult == HttpsURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                    resultado=sb.toString();

                }
                br.close();

                System.out.println("" + sb.toString());



            }else{
                System.out.println(urlConnection.getResponseMessage());
                empleado.setSuccess(false);
                empleado.setMensajeError(urlConnection.getResponseMessage());
            }

            JSONObject obj;



            try{
                obj = new JSONObject(resultado);

                empleado.setMensajeError(obj.getString("mensajeError"));
                if(obj.getString("error").equals("false"))
                {
                    empleado.setSuccess(true);

                }
                else{

                    empleado.setSuccess(false);
                }
            }
            catch (JSONException e1){
                e1.printStackTrace();
                empleado.setMensajeError(e1.toString());
                empleado.setSuccess(false);

            }
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            empleado.setMensajeError(e.toString());
            empleado.setSuccess(false);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            empleado.setMensajeError(e.toString());
            empleado.setSuccess(false);
        } catch (IOException e) {
            empleado.setMensajeError(e.toString());
            e.printStackTrace();
            empleado.setSuccess(false);
        }
        finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        Log.d(TAG,"checando contrasena");

        empleado.setRESPUESTA(TAG+" "+ " otra respuesta "+empleado.getMensajeError());

     return empleado;
    }


}
