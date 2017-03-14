/**
 * 
 */
package com.gruposalinas.elektra.movilidadgs.Activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.async.CheckEmployeesAsync;
import com.gruposalinas.elektra.movilidadgs.async.RegisterEmployeeAsync;
import com.gruposalinas.elektra.movilidadgs.async.claves_TelefonicasAsync;
import com.gruposalinas.elektra.movilidadgs.beans.Claves_telefonicas;
import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.beans.Empleado;
import com.gruposalinas.elektra.movilidadgs.geolocation.HoraExacta;
import com.gruposalinas.elektra.movilidadgs.ormlite.DBHelper;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.GuiTools;
import com.gruposalinas.elektra.movilidadgs.utils.bloquear_pantalla;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Eduardo Waring
 *
 */
public class RegistroActivity extends Activity {
	public static final String TAG = "REGISTRO_ACTIVITY";
	
	private EditText	empleadoEditText,contrasenia;
	private EditText 	telefonoEditText;
	private TextView 	registrarText,clave_pais;
	private DBHelper    mDBHelper;
	Empleado empleado1;
	private LinearLayout regresar;
	private RelativeLayout progressBar;
	bloquear_pantalla bloquearPantalla;
	private ImageButton back;
	String ruta="";
	ArrayList<String> pais;
	ArrayList<Integer>codigo_id;
	int temp_codigo;
	ImageView flec;
	DatosContacto datosContacto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		temp_codigo=2;
		bloquearPantalla= new bloquear_pantalla();
		
		empleadoEditText 	= (EditText)findViewById(R.id.registro_empleado_edittext);
		telefonoEditText 	= (EditText)findViewById(R.id.registro_telefono_edittext);
		registrarText 		= (TextView)findViewById(R.id.registro_registrar_button);
		progressBar 		= (RelativeLayout) findViewById(R.id.registro_progressbar);
		contrasenia         =(EditText)findViewById(R.id.contrasenia);
		regresar           = (LinearLayout)findViewById(R.id.regresar);
		clave_pais          =(TextView)findViewById(R.id.lada1);
		flec                =(ImageView)findViewById(R.id.flecha);
		back=                (ImageButton)findViewById(R.id.back);



		pais= new ArrayList<>();
		codigo_id= new ArrayList<>();
		//peticion_catalogo();

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		flec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mostrarlistado(pais,clave_pais);

			}
		});

		clave_pais.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mostrarlistado(pais,clave_pais);

			}
		});

		registrarText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int t=telefonoEditText.length();

				if(empleadoEditText.getText().toString().trim().equals("")){
					//Toast.makeText(getApplicationContext(), "¡Debe escribir su número de empleado!", Toast.LENGTH_LONG).show();
					alertaError("Por favor revisa que el número de empleado sea el correcto");
				}
				if(contrasenia.getText().toString().trim().equals("")){
					alertaError("Por favor revisa que la llave maestra o token sean correctos");
				}
				else if(telefonoEditText.getText().toString().trim().equals(""))
				{
					//Toast.makeText(getApplicationContext(), "¡Debe escribir su número telefónico!", Toast.LENGTH_LONG).show();
					alertaError("Por favor revisa que el número de teléfono sea el correcto");
				}


				else if(t<8)
				{
					alertaError("Por favor revisa que el número de teléfono sea el correcto");

				}




			/*	else if(clave_pais.getText().equals("Pais"))
				{

					alertaError("Debes seleccionar la clave de pais");

				}
				*/
				else{

					registerEmployee();
				}

			}
		});

		regresar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),MenuAplicaciones.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});


	}
	
	@SuppressLint("HardwareIds")
	private void registerEmployee(){
		progressBar.setVisibility(View.VISIBLE);
		bloquearPantalla.bloquear(this);
		Empleado empleado = new Empleado();
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		empleado.setIdEmployee((empleadoEditText.getText().toString()));
		empleado.setPhoneNumber(telefonoEditText.getText().toString());
		String a=contrasenia.getText().toString();
		empleado.setRESPUESTA(a);
		
		if (telephonyManager.getDeviceId() != null){
			empleado.setImei(telephonyManager.getDeviceId()); 
	    }else{
	    	empleado.setImei(Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID));
	    }
		// metodo que permite validar contraseña
		//checar(a, empleado);// ya no se usa


/*
		RegisterEmployeeAsync registerEmployeeAsync = new RegisterEmployeeAsync(this);
 registerEmployeeAsync.execute(empleado);*/

	llamada(empleado);

	}

	public void goToLoginActivity(Empleado result){
		progressBar.setVisibility(View.GONE);
		bloquearPantalla.deslbloquear();
		if(result.isSuccess())
		{
			archivo_foto(result);
			SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
			Editor edit = prefs.edit();
			edit.putString(Constants.SP_HASLOGGED, Constants.SP_LOGGED);
			edit.putString(Constants.SP_NAME, result.getName());
			edit.putString(Constants.SP_ENTERPRISE, result.getEnterprise());
			edit.putInt(Constants.SP_POSITION, result.getPosition());
			edit.putString(Constants.SP_HASLOGGED, Constants.SP_LOGGED);
			edit.putString(Constants.SP_ID, result.getIdEmployee());
			edit.putString(Constants.MY_PHONE,telefonoEditText.getText().toString());
			edit.putString(Constants.MY_CODIGO,temp_codigo+"");
			edit.putString(Constants.MY_CLAVE,clave_pais.getText().toString());

			edit.commit();

			//Después se procede a guardar en base de datos
			Dao dao;
			try {
			    dao = DBHelper.getHelper(getApplicationContext(), mDBHelper).getEmpleadoDAO();
			    dao.create(result);
			    Log.i(TAG, "Usuario creado exitosamente");
			} catch (SQLException e) {
			    Log.e(TAG, "Error creando usuario");
			    
			    try{
			    	dao = DBHelper.getHelper(getApplicationContext(), mDBHelper).getEmpleadoDAO();
			    	dao.update(result);
			    } catch(Exception ex){
			    	Log.e(TAG, "Error actualizando usuario");
			    }
			}
			
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			this.finish();
		}
		else{
			String mensajeError = "";
			if(result.getMensajeError() == null || result.getMensajeError().trim().equals(""))
				mensajeError = "Verifique su conexión";
			else
				mensajeError = result.getMensajeError();
			
		//	Toast.makeText(getApplicationContext(),  mensajeError, Toast.LENGTH_LONG).show();


			if("El usuario ya ha sido registrado anteriormente.".equals(result.getMensajeError()))
			{
				bloquearPantalla.bloquear(this);

				llamada(result);
			}
			else
			{
				alertaError(mensajeError);
			}

		}

		String a=result.getRESPUESTA();
		System.out.print(a);
	}
	
	protected void onDestroy() {
        super.onDestroy();
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
    }

	public void alertaError(final String error)
	{
		progressBar.setVisibility(View.GONE);

		final Dialog alert = new Dialog(RegistroActivity.this,R.style.Theme_Dialog_Translucent);
		LayoutInflater inflater1=getLayoutInflater();
		@SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.alerta_error, null);
		TextView titulodialo=(TextView)dialogo.findViewById(R.id.tituloDialogo);
		LinearLayout confirmar=(LinearLayout)dialogo.findViewById(R.id.boton_confirmar);
		TextView error1=(TextView)dialogo.findViewById(R.id.mensajerrror);
		error1.setText(error);
		alert.setContentView(dialogo);

		confirmar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{

				alert.dismiss();
			}
		});
		alert.show();

	}




	@Override
	public void onBackPressed()
	{
		finish();

	}

	public void llamada(Empleado empleado)
	{
		CheckEmployeesAsync checkEmployeesAsync = new CheckEmployeesAsync(this);
		checkEmployeesAsync.execute(empleado);



	}

	public  void session(Empleado empleado)
	{
		bloquearPantalla.deslbloquear();
		if(empleado.isSuccess())
		{

			saveInDatabase(empleado);
			archivo_foto(empleado);
			// enviar peticion de log
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			this.finish();

		}
		else {
			alertaError(empleado.getMensajeError());
		}

	}


	public void saveInDatabase(Empleado empleado){
		//Primero guardamos en Shared Preferences
		SharedPreferences prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
		Editor edit = prefs.edit();

		edit.putString(Constants.SP_NAME, empleado.getName());
		edit.putString(Constants.SP_ENTERPRISE, empleado.getEnterprise());
		edit.putInt(Constants.SP_POSITION, empleado.getPosition());
		edit.putString(Constants.SP_HASLOGGED, Constants.SP_LOGGED);
		edit.putString(Constants.SP_ID, empleado.getIdEmployee());
		edit.putString(Constants.MY_PHONE,telefonoEditText.getText().toString());
		edit.putString(Constants.MY_CODIGO,temp_codigo+"");
		edit.putString(Constants.MY_CLAVE,clave_pais.getText().toString());
		edit.commit();
		Log.i(TAG, "Empleado = " + empleado.getName());

		//Después se procede a guardar en base de datos
		Dao dao;
		try {
			dao = DBHelper.getHelper(getApplicationContext(), mDBHelper).getEmpleadoDAO();
			dao.create(empleado);
			Log.i(TAG, "Usuario creado exitosamente");
		} catch (SQLException e) {
			Log.e(TAG, "Error creando usuario");
			try{
				dao = DBHelper.getHelper(getApplicationContext(), mDBHelper).getEmpleadoDAO();
				dao.update(empleado);
			} catch(Exception ex){
				Log.e(TAG, "Error actualizando usuario");
			}
		}
	}

	public void archivo_foto(Empleado empleado3)
	{

		if(empleado3.getFoto()==null){
			return;
		}

		if(!empleado3.getFoto().equals("") && !empleado3.getFoto().equals(null))
		{
			byte[] decodedBytes = Base64.decode(empleado3.getFoto(), 0);

			//BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

			SharedPreferences prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
			Editor edit = prefs.edit();



			ruta= Environment.getExternalStorageDirectory().getAbsolutePath()+"/photo_perfil";
			edit.putString(Constants.path,ruta);
			edit.apply();


			File ph= new File(ruta);


			try {
				FileOutputStream fos = new FileOutputStream(ph);
				fos.write(decodedBytes);
				fos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}

	}



	public  void mostrarlistado(ArrayList<String> lista1, final TextView textView){
		final Dialog alert = new Dialog(this,R.style.Theme_Dialog_Translucent);
		LayoutInflater inflater1=getLayoutInflater();
		@SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.lista_horas_pm, null);
		GridView lista=(GridView)dialogo.findViewById(R.id.listado);
		ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.spinner_item,lista1);
		lista.setAdapter(arrayAdapter);
		alert.setContentView(dialogo);
		alert.show();

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("CommitPrefEdits")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String a = parent.getItemAtPosition(position).toString();
				textView.setText(a);
				temp_codigo=codigo_id.get(position);



				alert.dismiss();
			}
		});

	}

	public void peticion_catalogo()
	{
		bloquearPantalla.bloquear(this);
		progressBar.setVisibility(View.VISIBLE);
		Claves_telefonicas telefonicas= new Claves_telefonicas();
		telefonicas.setNumero_empleado("");

		claves_TelefonicasAsync claves_telefonicasAsync= new claves_TelefonicasAsync(this);

		claves_telefonicasAsync.execute(telefonicas);


	}

	public void cargarCatalogo_lista(Claves_telefonicas claves_telefonicas)
	{
		bloquearPantalla.deslbloquear();
		progressBar.setVisibility(View.GONE);


		if(claves_telefonicas.isSuccess())
		{
			if(claves_telefonicas.isSuccess())
			{

				for (int i=0;i<claves_telefonicas.getClaves_telefonicasArrayList().size();i++)
				{
					pais.add(claves_telefonicas.getClaves_telefonicasArrayList().get(i).getPais());
					codigo_id.add(Integer.valueOf(claves_telefonicas.getClaves_telefonicasArrayList().get(i).getId()));

				}

				//   codigo_paises.setCodigo_internacional(codigos);

			}
			else {

				pais.add("Pais");
				alertaError("No se cargo el catalogo de paises favor de intentar de registrarse mas tarde");
//          codigos.add(99999);

			}

		}
		else {
			alertaError("No se cargo el catalogo de paises favor de intentar de registrarse mas tarde");
		}

	}

}