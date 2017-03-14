package com.gruposalinas.elektra.movilidadgs.webservices;

import android.net.ParseException;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RegisterEmployeeWS {
	public static String TAG = "CHECK_REGISTER_WS";
	String line=null;
	StringBuilder sb = new StringBuilder();
	String resultado="";

	public Empleado setEmployee(Empleado employee){
		employee.setSuccess(false);
		String urlService = Constants.DOMAIN_URL + "/" + Constants.InicioRegistroEmpleado;
		SecurityItems securityItems=new SecurityItems(employee.getIdEmployee(),employee.getRESPUESTA());
		JSONObject jsonObject= new JSONObject();
		try {
			jsonObject.put("id_num_empleado", securityItems.getIdEmployEncrypt());
			jsonObject.put("id_dispositivo",employee.getImei());
			jsonObject.put("va_numero_telefono",employee.getPhoneNumber());
			jsonObject.put("token",securityItems.getTokenEncrypt().trim());
			jsonObject.put("strPassword",securityItems.getPasswordEncrypt());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG,jsonObject.toString());
		HttpURLConnection urlConnection=null;
		try {

			URL url = new URL(urlService);
			urlConnection=Utils.checkIfHttps(url);
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
				employee.setSuccess(false);
				employee.setMensajeError(urlConnection.getResponseMessage());

			}

			Log.i(TAG, "EntityUtils =" + resultado);


			try {
				JSONObject empleadoJSON = new JSONObject(resultado);

				employee.setMensajeError(empleadoJSON.getString("mensajeError"));
				
				if(HttpResult == HttpURLConnection.HTTP_OK)
				{
					if(empleadoJSON.getString("error").trim().equals("false")){
						if(securityItems.getIdEmployEncrypt()==empleadoJSON.getString(Constants.ID_NUM_EMPLEADO)) {
							employee.setSuccess(true);
							empleadoJSON = new JSONObject(empleadoJSON.getString("empleado"));
							employee.setName(empleadoJSON.getString("va_nombre_completo"));
							employee.setEnterprise(empleadoJSON.getString("id_empresa"));
							employee.setPosition(empleadoJSON.getInt("id_puesto"));
							employee.setIdEmployee(employee.getIdEmployee());
							Log.i(TAG, "Nombre = " + employee.getName());
						   }
						else{
							Log.e(TAG,"ID_EMPLEADO IS NOT THE SAME");
						}
					}
				}
				
			} catch (ParseException e) {
				Log.i(TAG, "JSON fail!");
				e.printStackTrace();
				employee.setSuccess(false);
				employee.setMensajeError(e.toString());
			} catch (JSONException e) {
				Log.i(TAG, "JSON fail!");
				employee.setSuccess(false);
				employee.setMensajeError(e.getMessage());
			}
			
		} catch (MalformedURLException e) {
		e.printStackTrace();
			employee.setSuccess(false);
			employee.setMensajeError(e.getMessage());
			 
		} catch (IOException e) {
			e.printStackTrace();
			employee.setSuccess(false);
			employee.setMensajeError(e.getMessage());
		}	
         
        return employee;
	}
}


