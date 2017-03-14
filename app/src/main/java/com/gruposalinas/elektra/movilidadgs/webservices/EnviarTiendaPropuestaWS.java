package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Tiendas;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.SecurityItems;
import com.gruposalinas.elektra.movilidadgs.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Adrian Guzman on 10/02/2016.
 */
public class EnviarTiendaPropuestaWS
{

    String URL = Constants.DOMAIN_URL + "/" + Constants.RegistrarPropuestaUbicacionCopia;

    String line=null;
    StringBuilder sb = new StringBuilder();
    String resultado="";


    public Tiendas tiendas(Tiendas tiendas)
{
    tiendas.setSuccess(false);

    JSONObject jsonObject= new JSONObject();
    JSONObject jsonObject1= new JSONObject();
    JSONArray jsonArray= new JSONArray();
    SecurityItems securityItems = new SecurityItems(tiendas.getId_empleado());

    try {
        jsonObject.put("va_numero_pos",tiendas.getPosicion());
        jsonArray.put(jsonObject);
        jsonObject1.put("id_num_empleado",securityItems.getIdEmployEncrypt());
        jsonObject1.put("token",securityItems.getTokenEncrypt());
        jsonObject1.put("posiciones",jsonArray);
        jsonObject1.put("tipo",tiendas.getTipo());

    } catch (JSONException e) {
        e.printStackTrace();
    }

    Log.d("EntraWSEnviarTienda","entrando"+jsonObject1.toString());

    HttpURLConnection urlConnection=null;
    try {
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
        out.write(jsonObject1+"");
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
            tiendas.setSuccess(false);
            tiendas.setMensajeError(urlConnection.getResponseMessage());
        }

        JSONObject obj;


        try{
            obj = new JSONObject(resultado);

            tiendas.setMensajeError(obj.getString("mensajeError"));
            if(obj.getString("error").equals("false"))
            {
                tiendas.setSuccess(true);

            }
            else{

                tiendas.setSuccess(false);

            }
        }
        catch (JSONException e1){
            e1.printStackTrace();
            tiendas.setMensajeError(e1.toString());

        }

    } catch (MalformedURLException e) {

        e.printStackTrace();
        tiendas.setSuccess(false);
        tiendas.setMensajeError(e.toString());
    }
    catch (IOException e) {
        tiendas.setSuccess(false);
        tiendas.setMensajeError(e.getMessage());
        e.printStackTrace();
    } finally{
        if(urlConnection!=null)
            urlConnection.disconnect();
    }
    Log.d("finWSEnviarTienda","fin");

    return  tiendas;
}
}
