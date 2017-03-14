package com.gruposalinas.elektra.movilidadgs.webservices;

import android.util.Log;

import com.gruposalinas.elektra.movilidadgs.beans.Empleado;
import com.gruposalinas.elektra.movilidadgs.beans.Singleton;
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

public class CheckEmployeesWS {

	String line=null;
	StringBuilder sb = new StringBuilder();
	String resultado="";

	public static String TAG = "CHECK_EMPLOYEES_WS";
	public Empleado getEmployee(Empleado employee){
		employee.setSuccess(false);
		String url = Constants.DOMAIN_URL + "/" + Constants.CHECK_EMPLOYEE_WS;
		String numero_empleado=employee.getIdEmployee();
		SecurityItems securityItems=new SecurityItems(numero_empleado,employee.getRESPUESTA());

		JSONObject jsonObject= new JSONObject();
		try {
			jsonObject.put("id_dispositivo", Singleton.getInstance().getImei());
			jsonObject.put("id_empresa","EKT");
			jsonObject.put("id_num_empleado",securityItems.getIdEmployEncrypt());
			jsonObject.put("token",securityItems.getTokenEncrypt());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "inicia checa empleado"+jsonObject.toString());

		HttpURLConnection urlConnection=null;
			try {
				URL urla= new URL(url);
				urlConnection=Utils.checkIfHttps(urla);
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

					Log.i(TAG,"HTTP_OK");



				}else{
					System.out.println(urlConnection.getResponseMessage());
					employee.setSuccess(false);
					employee.setMensajeError(urlConnection.getResponseMessage());
					Log.e(TAG,urlConnection.getResponseMessage());
				}


				JSONObject obj;

				try {

					Log.i(TAG,"RESULTADO SERVER:"+resultado);

					obj = new JSONObject(resultado);	Log.i(TAG, "obj = " + obj.length());

					Log.i(TAG, "obj = " + obj.getString("mensajeError"));

					//JSONArray arr = obj.getJSONArray("empleados");
					JSONObject arr = new JSONObject(obj.getString("empleado"));

					Log.i(TAG, "TEST Array = " + arr.length());
					employee.setMensajeError(obj.getString("mensajeError"));

				boolean a=	obj.getBoolean("error");
					int i = 0;
					boolean endCycle = false;
					do{
						if(!obj.getBoolean("error")){
							if(!obj.getBoolean("error"))
							{
								endCycle = true;

								employee.setName(arr.getString("va_nombre_completo"));
								employee.setImei(arr.getString("id_dispositivo"));
								employee.setEnterprise(arr.getString("id_empresa"));
								employee.setPosition(Integer.valueOf(arr.getString("id_puesto")));
								employee.setIdEmployee(numero_empleado);
								employee.setPhoneNumber(arr.getString("va_numero_telefono"));
								employee.setSuccess(true);

							}
							else{
								employee.setName(Constants.ERROR);
								if(i+1 >= arr.length())
									endCycle = true;
							}
						}else{
							employee.setName(Constants.ERROR);
							if(i+1 >= arr.length())
								endCycle = true;
						}
						i++;
					}while(!endCycle);


				} catch (JSONException e) {
					e.printStackTrace();
					employee.setMensajeError(e.toString());
					employee.setSuccess(false);
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				employee.setMensajeError(e.toString());
				employee.setSuccess(false);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				employee.setMensajeError(e.toString());
				employee.setSuccess(false);
			} catch (IOException e) {
				e.printStackTrace();
				employee.setMensajeError(e.toString());
				employee.setSuccess(false);

			}
			finally{
				if(urlConnection!=null)
					urlConnection.disconnect();
			}
		employee.setRESPUESTA(TAG+" "+" otra  respuesta"+employee.getMensajeError());
        return employee;
	}


}