package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Incidencias;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Adrian Guzman on 14/03/2016.
 */
public class ObtenerConfiguracionesAplicacionWS
{
    public static String TAG = "Check Versión";
    String datos1;
    String line=null;
    StringBuilder sb = new StringBuilder();
    String resultado="";

    public Incidencias incidencias(Incidencias incidencias)
    {
        //String URL="http://10.112.51.5/ServicioEmpleados.svc/ObtenerConfiguracionesAplicacion";// DESARROLLO
      // String URL="https://200.38.122.208/ServicioEmpleados.svc/ObtenerConfiguracionesAplicacion";
        String URL = Constants.DOMAIN_URL + "/" + Constants.ObtenerConfiguracionesAplicacion;

        SecurityItems securityItems=new SecurityItems(incidencias.getNumero_empleado());

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("token",securityItems.getTokenEncrypt());
            jsonObject.put("id_num_empleado",securityItems.getIdEmployEncrypt());
            jsonObject.put("sistema",incidencias.getSistema());
            jsonObject.put("version_actual",incidencias.getVersion());
            jsonObject.put("id_dispositivo",incidencias.getID_Dispositivo());
            jsonObject.put("modelo_celular",incidencias.getModelo_Celular());
            jsonObject.put("version_sistema_operativo",incidencias.getVersion_S_O());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "inicia WS version App" + jsonObject.toString());

       // HttpURLConnection urlConnection=null;
        HttpURLConnection urlConnection=null;

        try{

            ///fin de salta validacion///
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
            int HttpsResult =urlConnection.getResponseCode();
            if(HttpsResult == HttpsURLConnection.HTTP_OK)
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
                    incidencias.setFECHA_LIMITE(obj.getString("FECHA"));
                    incidencias.setURL(obj.getString("URL"));
                    incidencias.setVersion(obj.getString("VERSION"));
                    incidencias.setNumero_jefe(obj.getString("NUM_JEFE"));

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
        } finally
        {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return incidencias;
    }


}
