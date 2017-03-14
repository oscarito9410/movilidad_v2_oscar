package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Multimedia;
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
 * Created by Adrian Guzman on 22/06/2016.
 */
public class AgregarEmpleado_ArchivoClipMultimediaWS
{
    public static String TAG = "Multimedia";
    private SecurityItems securityItems;
    private String token;
    private String idEmpleadoToken;
    private String line=null;
    private StringBuilder sb = new StringBuilder();
    private String resultado="";
    public Multimedia multimedia(Multimedia multimedia)
    {
        String URL = Constants.DOMAIN_URL + "/" + Constants.AgregarEmpleado_ArchivoClipMultimedia;

        // String URL ="http://10.51.118.170:8083/ServicioEmpleados.svc/"+Constants.AgregarEmpleado_ArchivoClipMultimedia;
        // String URL ="http://200.38.122.208/ServicioEmpleados.svc/"+Constants.AgregarEmpleado_ArchivoClipMultimedia;

         System.gc();

        JSONObject Json=new JSONObject();
        JSONArray jsonArray= new JSONArray();

        for (int i=0;i<multimedia.getMultimediaArrayList().size();i++)
        {
            JSONObject mu=new JSONObject();
            try {
                mu.put("nombreArchivo",multimedia.getMultimediaArrayList().get(i).getNombre_archivo());
                mu.put("archivoAdjunto",multimedia.getMultimediaArrayList().get(i).getArchivo());
                mu.put("tamanoArchivo",multimedia.getMultimediaArrayList().get(i).getTamano());
                mu.put("extension",multimedia.getMultimediaArrayList().get(i).getExtension());

                jsonArray.put(mu);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        try
        {
            this.securityItems=new SecurityItems(multimedia.getId_numero_empleado());
            this.token=securityItems.getTokenEncrypt();
            this.idEmpleadoToken=securityItems.getIdEmployEncrypt();
            Json.put("id_num_empleado", this.idEmpleadoToken);
            Json.put("telefonos",multimedia.getContactos());
            Json.put("coordenadas",multimedia.getCoordenadas());
            Json.put("archivos",jsonArray);
            Json.put("token",this.token);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        Log.d(TAG, "checando Json" + Json.toString());
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
            out.write(Json+"");
            out.close();
            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                    resultado=sb.toString();
                }
                br.close();

                SecurityItems securityItems = new SecurityItems(multimedia.getId_numero_empleado());
                Log.i("AFTER POST TOKEN",securityItems.getTokenEncrypt());

                System.out.println("json archivo"+sb.toString());

            }else{
                System.out.println(urlConnection.getResponseMessage());
                multimedia.setSuccess(false);
                multimedia.setMensajeError(urlConnection.getResponseMessage());
            }


            JSONObject obj;
            try {
                obj = new JSONObject(resultado);

                multimedia.setMensajeError(obj.getString("mensajeError"));

                if(obj.getString("error").equals("false"))
                {
                    multimedia.setSuccess(true);
                }
                else{

                    //TOKEN NO ES EL MISMO
                    if(!securityItems.getTokenEncrypt().equals(this.token) && !securityItems.getIdEmployEncrypt().equals(this.idEmpleadoToken)){
                            Log.i(TAG,"TOKEN NO ES EL MISMO");
                            this.multimedia(multimedia);
                    }
                    multimedia.setSuccess(false);
                    multimedia.setMensajeError("error");
                }


            }
            catch (JSONException e){
                e.printStackTrace();
                multimedia.setMensajeError(e.getMessage());
                multimedia.setSuccess(false);
            }
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            multimedia.setMensajeError(e.toString());
            multimedia.setSuccess(false);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
            multimedia.setMensajeError(e.toString());
            multimedia.setSuccess(false);
        }
        catch (IOException e){
            e.printStackTrace();
            multimedia.setMensajeError(e.toString());
            multimedia.setSuccess(false);
        }





        return multimedia;
    }
}
