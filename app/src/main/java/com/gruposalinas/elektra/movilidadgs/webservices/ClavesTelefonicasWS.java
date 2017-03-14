package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Claves_telefonicas;
import com.gruposalinas.elektra.movilidadgs.beans.Empleado;
import com.gruposalinas.elektra.movilidadgs.beans.Singleton;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yvegav on 21/12/2016.
 */

public class ClavesTelefonicasWS
{
    String line=null;
    StringBuilder sb = new StringBuilder();
    String resultado="";

    public static String TAG = "ClavesTelefonicasWS";


    public Claves_telefonicas getclaves(Claves_telefonicas claves_telefonicas){
        claves_telefonicas.setSuccess(false);

        String url = Constants.DOMAIN_URL + "/" + Constants.ClavesTelefonicas;

        String numero_empleado=claves_telefonicas.getNumero_empleado();
        SecurityItems securityItems = new SecurityItems(numero_empleado);
        JSONObject jsonObject= new JSONObject();

        try {

            jsonObject.put("id_num_empleado",securityItems.getIdEmployEncrypt());
            jsonObject.put("token",securityItems.getTokenEncrypt());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "inicia claves"+jsonObject.toString());

        HttpURLConnection urlConnection=null;
        //HttpsURLConnection
        try {
            //java.net.URL urla = new URL(url);
            URL urla= new URL(url);
            urlConnection = Utils.checkIfHttps(urla);
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
                claves_telefonicas.setSuccess(false);
                claves_telefonicas.setMensajeError(urlConnection.getResponseMessage());
            }


            JSONObject obj;

            try {
                obj = new JSONObject(resultado);	Log.i(TAG, "obj = " + obj.length());

                Log.i(TAG, "obj = " + obj.getString("mensajeError"));
                JSONArray arr;
                ArrayList<Claves_telefonicas> tle= new ArrayList<>();

                if(!obj.getBoolean("error"))
                {
                    claves_telefonicas.setSuccess(true);
                   arr= new JSONArray(obj.getString("claves"));

                    for (int i=0;i<arr.length();i++)
                    {
                        JSONObject claves=arr.getJSONObject(i);
                        Claves_telefonicas guardar= new Claves_telefonicas();
                        guardar.setId(claves.getInt("id")+"");
                        guardar.setPais(claves.getString("pais"));
                        guardar.setCodigo(claves.getString("codigo"));
                        tle.add(guardar);
                        guardar=null;



                    }




                }

                if(tle.size()>=0){
                    claves_telefonicas.setSuccess(true);
                    claves_telefonicas.setClaves_telefonicasArrayList(tle);

                }
                else
                {
                    claves_telefonicas.setCodigo("");
                    claves_telefonicas.setPais("");
                    claves_telefonicas.setId("");
                    tle.add(claves_telefonicas);
                    claves_telefonicas.setClaves_telefonicasArrayList(tle);
                }





            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                claves_telefonicas.setMensajeError(e.toString());
                claves_telefonicas.setSuccess(false);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            claves_telefonicas.setMensajeError(e.toString());
            claves_telefonicas.setSuccess(false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            claves_telefonicas.setMensajeError(e.toString());
            claves_telefonicas.setSuccess(false);
        } catch (IOException e) {
            e.printStackTrace();
            claves_telefonicas.setMensajeError(e.toString());
            claves_telefonicas.setSuccess(false);

        }
        finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }

        return claves_telefonicas;
    }


}
