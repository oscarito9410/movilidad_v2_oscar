package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.BaseBean;
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
 * Created by oemy9 on 07/03/2017.
 */


public class BaseService {

    public  static BaseBean POST(String URL, JSONObject json, BaseBean bean){
        String line=null;
        StringBuilder sb = new StringBuilder();
        String resultado="";
        Log.d("ddd",json.toString());
        HttpURLConnection urlConnection=null;
        try{
            java.net.URL url = new URL(URL);
            urlConnection =null;
            if(url.getProtocol().toLowerCase().equals("https")){
              HttpsURLConnection urlHttpsConnection=(HttpsURLConnection)url.openConnection();
              urlHttpsConnection.setHostnameVerifier(Utils.hostnameVerifier());
              urlConnection=urlHttpsConnection;
            }
            else{
                urlConnection=(HttpsURLConnection)url.openConnection();
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();
            OutputStreamWriter out = new   OutputStreamWriter(urlConnection.getOutputStream());
            out.write(json+"");
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
                bean.setSuccess(false);
                bean.setMensajeError(urlConnection.getResponseMessage());
            }

            JSONObject obj;


            try{
                obj = new JSONObject(resultado);

                bean.setMensajeError(obj.getString("mensajeError"));
                if(obj.getString("error").equals("false"))
                {
                   bean.setSuccess(true);

                }
                else{
                    bean.setSuccess(false);
                }
            }
            catch (JSONException e1){
                e1.printStackTrace();
                bean.setMensajeError(e1.toString());
                bean.setSuccess(false);

            }
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            bean.setMensajeError(e.toString());
            bean.setSuccess(false);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            bean.setMensajeError(e.toString());
            bean.setSuccess(false);
        } catch (IOException e) {
            bean.setMensajeError(e.toString());
            e.printStackTrace();
            bean.setSuccess(false);
        }
        finally
        {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return bean;
    }


    }

