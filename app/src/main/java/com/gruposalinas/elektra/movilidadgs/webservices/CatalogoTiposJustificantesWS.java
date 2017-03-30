package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Incidencias;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.Encryption;
import com.gruposalinas.elektra.movilidadgs.utils.SecurityItems;
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
 * Created by Adrian Guzman on 09/03/2016.
 */
public class CatalogoTiposJustificantesWS
{
    public static String TAG = "Catalogojustificante";
    String line=null;
    StringBuilder sb = new StringBuilder();
    String resultado="";

    public Incidencias incidencias(Incidencias incidencias)
    {
       // String URL="http://10.112.51.5:8083/ServicioEmpleados.svc/CatalogoTiposJustificantes";// DESARROLLO
      // String URL="http://200.38.122.208:8081/ServicioEmpleados.svc/CatalogoTiposJustificantes";
        String URL = Constants.DOMAIN_URL + "/" + Constants.CatalogoTiposJustificantes;

        SecurityItems securityItems= new SecurityItems(incidencias.getNumero_empleado());

        String jsonCatalogo = "{"
                + "\"token\":\""+  Encryption.md5(incidencias.getNumero_empleado() + Encryption.getHour()) +"\","
                + "\"id_num_empleado\":\""+ incidencias.getNumero_empleado()+"\""
                + "}";



        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("token",securityItems.getTokenEncrypt());
            jsonObject.put("id_num_empleado",securityItems.getIdEmployEncrypt());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "inicia WS incidencia" + jsonObject.toString());

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

                System.out.println(""+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
                incidencias.setSuccess(false);
                incidencias.setMensajeError(urlConnection.getResponseMessage());
            }

            JSONObject obj;


            try{
                obj = new JSONObject(resultado);

                incidencias.setMensajeError(obj.getString("mensajeError"));
                if(obj.getString("error").equals("false"))
                {
                    JSONArray array=new JSONArray(obj.getString("lista"));
                    incidencias.setListaCatalogo(array);
                    incidencias.setSuccess(true);

                }
                else{
                    Utils.checkIfFecha(obj.getString(Constants.SERVER_UTC_TIME));
                    incidencias.setSuccess(false);
                }
            }
            catch (JSONException e1){
                e1.printStackTrace();
                incidencias.setMensajeError(e1.toString());
                incidencias.setSuccess(false);

            }
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            incidencias.setMensajeError(e.toString());
            incidencias.setSuccess(false);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            incidencias.setMensajeError(e.toString());
            incidencias.setSuccess(false);
        } catch (IOException e) {
            incidencias.setMensajeError(e.toString());
            e.printStackTrace();
            incidencias.setSuccess(false);
        }
        finally
        {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }



        return incidencias;
    }


}
