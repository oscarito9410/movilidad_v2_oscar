package com.gruposalinas.elektra.movilidadgs.Activities;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.alertar.Alertas;
import com.gruposalinas.elektra.movilidadgs.async.claves_TelefonicasAsync;
import com.gruposalinas.elektra.movilidadgs.beans.Claves_telefonicas;
import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.ImagenCircular;
import com.gruposalinas.elektra.movilidadgs.utils.RoundedImageView;
import com.gruposalinas.elektra.movilidadgs.utils.bloquear_pantalla;
import com.gruposalinas.elektra.movilidadgs.utils.obtenerRuta;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Settings_perfil extends BaseActivity {

    private Uri mImageUri;
    obtenerRuta Obtener;
    String ruta="";
    DatosContacto datosContacto;
    ImageView photoImage;
    TextView editar_foto,usuario,enterprise,numero_jefe,clave_tele;
    Button guardar_perfil;
    ImageButton back;
    Bitmap bitmap1;
    ArrayList<String> paises;
    ArrayList<Integer> codigos;
    ImageView clave_paises_;
    RelativeLayout progreso;
    bloquear_pantalla bloquearPantalla;
    EditText my_phone;
    int tempo_codigo;
    Alertas alertas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_perfil);
        init();
        editar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeria_camara();
            }
        });
        guardar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    guardar();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clave_tele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarlistado(paises,clave_tele);
            }
        });

        clave_paises_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarlistado(paises,clave_tele);
            }
        });

    }

    public void init(){
         bloquearPantalla= new bloquear_pantalla();
        alertas= new Alertas(this);
        photoImage=(ImageView)findViewById(R.id.main_photo_image);
        editar_foto=(TextView)findViewById(R.id.editar_foto);
        usuario=(TextView)findViewById(R.id.usuario);
        enterprise=(TextView)findViewById(R.id.compañia);
        numero_jefe=(TextView)findViewById(R.id.numero_jefe);
        guardar_perfil=(Button)findViewById(R.id.guardar);
        back=(ImageButton)findViewById(R.id.boton_regresar);
        clave_tele=(TextView)findViewById(R.id.lada1);
        clave_paises_=(ImageView)findViewById(R.id.flecha);
        progreso=(RelativeLayout)findViewById(R.id.progressbar);
        my_phone=(EditText)findViewById(R.id.tel2);
        paises= new ArrayList<>();
        codigos= new ArrayList<>();
        Obtener = new obtenerRuta();
        datosContacto= new DatosContacto();
        ruta=datosContacto.path(this);
        usuario.setText(datosContacto.usuario(this));
        enterprise.setText(datosContacto.enterprise(this));
        numero_jefe.setText(datosContacto.Numero_jefe(this));
        String a=datosContacto.get_MY_CLAVE(this);
        my_phone.setText(datosContacto.getMY_PHONE(this));
        clave_tele.setText(datosContacto.get_MY_CLAVE(this));
        tempo_codigo=Integer.valueOf(datosContacto.get_MY_CODIGO(this));

        Claves_telefonicas telefonicas= new Claves_telefonicas();
        telefonicas.setNumero_empleado(datosContacto.ID_EMPLEADO(this));

        claves_TelefonicasAsync claves_telefonicasAsync= new claves_TelefonicasAsync(this);

        claves_telefonicasAsync.execute(telefonicas);
        progreso.setVisibility(View.VISIBLE);
        bloquearPantalla.bloquear(this);


        if(!ruta.equals(""))
        {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/photo_perfil");

            if(file.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Bitmap bitmap4=    ImagenCircular.cropBitmapToSquare(bitmap);
                bitmap1=ImagenCircular.getRoundedCornerBitmap(bitmap4,120);
                bitmap.recycle();
                bitmap4.recycle();
                photoImage.setImageBitmap(bitmap1);


            }

        }

    }

    public void galeria_camara()
    {
        final Dialog alert = new Dialog(this,R.style.Theme_Dialog_Translucent);
        final LayoutInflater inflater1=getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.sub_menu, null);
        final ImageButton miHorario=(ImageButton)dialogo.findViewById(R.id.misHorarios);
        ImageButton plantillaHorario=(ImageButton)dialogo.findViewById(R.id.miplantillahorario);
       // TextView titulo=(TextView)dialogo.findViewById(R.id.tituloDialogo);
        TextView titulo1=(TextView)dialogo.findViewById(R.id.textotitulo);
        TextView plantilla=(TextView)dialogo.findViewById(R.id.plantilla);
        LinearLayout cerrar=(LinearLayout)dialogo.findViewById(R.id.cerrar);
        miHorario.setBackgroundResource(android.R.drawable.ic_menu_camera);
        titulo1.setText("Camara");
        plantilla.setText("Galeria");

        plantillaHorario.setBackgroundResource(android.R.drawable.ic_menu_gallery);


        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        miHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 1022);
                alert.dismiss();

            }
        });
        plantillaHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1020);
                alert.dismiss();


            }
        });

        alert.setContentView(dialogo);
        alert.show();

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == 1020 && resultCode == RESULT_OK)
        {
            SharedPreferences pat =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor pa= pat.edit();

            ruta=Environment.getExternalStorageDirectory().getAbsolutePath()+"/photo_perfil";

            pa.putString(Constants.path,ruta);
            pa.apply();

            mImageUri = data.getData();

            Bitmap bitmap=	Obtener.getImage(mImageUri,this);

            //Bitmap bitmap4= RoundedImageView.getCroppedBitmap(bitmap,120);
            Bitmap bitmap4= ImagenCircular.cropBitmapToSquare(bitmap);
             bitmap1= RoundedImageView.getCroppedBitmap(bitmap4,150);

            photoImage.setImageBitmap(bitmap1);


            if(!ruta.equals("")){


                ruta=Environment.getExternalStorageDirectory().getAbsolutePath()+"/photo_perfil";

                pa.putString(Constants.path,ruta);

                pa.apply();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();
                File ph= new File(ruta);

                try {
                    FileOutputStream fos = new FileOutputStream(ph);
                    fos.write(byteArray);
                    fos.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }


            }

        }
        if(requestCode==1022 && resultCode==RESULT_OK){

            SharedPreferences pat =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor pa= pat.edit();

            ruta=Environment.getExternalStorageDirectory().getAbsolutePath()+"/photo_perfil";

            pa.putString(Constants.path,ruta);
            pa.apply();

            Bitmap bitmap2=(Bitmap)data.getExtras().get("data");

            Bitmap bitmap4= ImagenCircular.cropBitmapToSquare(bitmap2);

           bitmap1=ImagenCircular.getRoundedCornerBitmap(bitmap4,120);
            bitmap4.recycle();
//            bitmap2.recycle();

            photoImage.setImageBitmap(bitmap1);


            if(!ruta.equals("")){


                ruta=Environment.getExternalStorageDirectory().getAbsolutePath()+"/photo_perfil";

                pa.putString(Constants.path,ruta);

                pa.apply();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();
                File ph= new File(ruta);

                try {
                    FileOutputStream fos = new FileOutputStream(ph);
                    fos.write(byteArray);
                    fos.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }


            }




        }
    }

    public void guardar()
    {
        if(my_phone.getText().toString().length()>=8)
        {
            SharedPreferences pat =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor pa= pat.edit();

             pa.putString(Constants.MY_PHONE,my_phone.getText().toString());
            pa.putString(Constants.MY_CLAVE,clave_tele.getText().toString());
            pa.putString(Constants.MY_CODIGO,tempo_codigo+"");
            pa.apply();


            alertas.Exito(this,null);

        }else {
            alertas.displayMensaje("Por favor revisa que el número de teléfono sea el correcto",this);
        }



    }


    public void resultado(Claves_telefonicas telefonicas)
    {
        progreso.setVisibility(View.GONE);
        bloquearPantalla.deslbloquear();
        if(telefonicas.isSuccess())
        {

            for (int i=0;i<telefonicas.getClaves_telefonicasArrayList().size();i++)
            {
                paises.add(telefonicas.getClaves_telefonicasArrayList().get(i).getPais());
                codigos.add(Integer.valueOf(telefonicas.getClaves_telefonicasArrayList().get(i).getId()));

            }


        }
        else {
            paises.add("Pais");

         //   displayMensaje.displayMensaje("No se pudo cargar el catalogo de claves de paises",this);
            clave_tele.setEnabled(false);
            clave_paises_.setEnabled(false);
        }


      //  clave_tele.setText(paises.get(0));

    }


    public  void mostrarlistado(ArrayList <String> lista1, final TextView textView){
        final Dialog alert = new Dialog(this,R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.lista_horas_pm, null);
        GridView lista=(GridView)dialogo.findViewById(R.id.listado);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.spinner_item,lista1);
        lista.setAdapter(arrayAdapter);
        alert.setContentView(dialogo);
        alert.show();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = parent.getItemAtPosition(position).toString();
                textView.setText(a);
               tempo_codigo=codigos.get(position);

                alert.dismiss();
            }
        });

    }


    }


