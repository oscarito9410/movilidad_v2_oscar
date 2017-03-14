package com.gruposalinas.elektra.movilidadgs.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.adapters.AdapterTelefonos;
import com.gruposalinas.elektra.movilidadgs.async.claves_TelefonicasAsync;
import com.gruposalinas.elektra.movilidadgs.beans.Claves_telefonicas;
import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.beans.Multimedia;
import com.gruposalinas.elektra.movilidadgs.beans.paginaweb;
import com.gruposalinas.elektra.movilidadgs.geolocation.Service_contact;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.Datos_contactos_Panico;
import com.gruposalinas.elektra.movilidadgs.utils.bloquear_pantalla;

import java.util.ArrayList;
import java.util.Timer;

public class Contactos extends Activity {

    EditText tel1,tel2,tel3;
    Button guardar;
    String t1,t2,t3,n1,n2,n3,l1,l2,l3;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    ImageButton back;
    ArrayList <String> lista;
    ImageView importarc1,importarc2,importarc3,flecha1,flecha2,flecha3,pa1,pa2,pa3;
    Uri contacs;
     int contacto1=4,contacto2=6,contacto3=8;
    Datos_contactos_Panico datos_c;
    EditText nombre1,nombre2,nombre3;
    TextView lada,lada1,lada2;
    ArrayList<String>telefonos;
    RelativeLayout progreso;
   ArrayList <String> paises;
    bloquear_pantalla bloquearPantalla;
    boolean v1=false,v2=false,v3=false;
    Multimedia codigo_paises;
    ArrayList<Integer>  codigos;
    String co,co2,co3;
    ArrayList <String> cods;
    boolean valid1=false,valid2=false,valid3=false,valid4=false;
    Timer enviar;
    String pn,pn1,pn2;
    ImageView paginaWEb;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        bloquearPantalla= new bloquear_pantalla();
        codigo_paises= new Multimedia();
        codigos= new ArrayList<>();
        cods= new ArrayList<>();
        init();
        datos_c=new Datos_contactos_Panico();
        telefonos= new ArrayList<>();
        captutrar();

        paginaWEb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paginaweb paginaweb = new paginaweb(getApplicationContext());


            }
        });


        clicks();

    }

    private void init()
    {
        tel1=(EditText)findViewById(R.id.tel1);
        tel2=(EditText)findViewById(R.id.tel2);
        tel3=(EditText)findViewById(R.id.tel3);
        guardar=(Button)findViewById(R.id.guardar);
        back=(ImageButton)findViewById(R.id.boton_regresar);
        importarc1=(ImageView)findViewById(R.id.importarContactos);
        importarc2=(ImageView)findViewById(R.id.importarContactos1);
        importarc3=(ImageView)findViewById(R.id.importarContactos2);
        nombre1=(EditText)findViewById(R.id.nombre1Contacto);
        nombre2=(EditText)findViewById(R.id.nombre2Contacto);
        nombre3=(EditText)findViewById(R.id.nombre3Contacto);
        flecha1=(ImageView)findViewById(R.id.flecha);
        flecha2=(ImageView)findViewById(R.id.flecha1);
        flecha3=(ImageView)findViewById(R.id.flecha2);
        lada=(TextView)findViewById(R.id.lada1);
        lada1=(TextView)findViewById(R.id.lada2);
        lada2=(TextView)findViewById(R.id.lada3);
        pa1=(ImageView)findViewById(R.id.catalogo_paises);
        pa2=(ImageView)findViewById(R.id.catalogo_paises1);
        pa3=(ImageView)findViewById(R.id.catalogo_paises2);
        progreso= (RelativeLayout)findViewById(R.id.horario_progressbar);
        paginaWEb=(ImageView)findViewById(R.id.paginaweb5);
        lista= new ArrayList<>();
        SharedPreferences prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String id_empleado=prefs.getString(Constants.SP_ID, "");

        Claves_telefonicas telefonicas= new Claves_telefonicas();
        telefonicas.setNumero_empleado(id_empleado);

        claves_TelefonicasAsync claves_telefonicasAsync= new claves_TelefonicasAsync(this);

        claves_telefonicasAsync.execute(telefonicas);
        progreso.setVisibility(View.VISIBLE);
        bloquearPantalla.bloquear(this);

    }

    private void mostrarContactos()
    {
        DatosContacto datosContacto=new DatosContacto();
        tel1.setText(datosContacto.gettel1(this));
        tel2.setText(datosContacto.gettel2(this));
        tel3.setText(datosContacto.gettel3(this));
        nombre1.setText(datosContacto.nombre1(this));
        nombre2.setText(datosContacto.nombre2(this));
        nombre3.setText(datosContacto.nombre3(this));
        lada.setText(datosContacto.lada(this));
        lada1.setText(datosContacto.lada1(this));
        lada2.setText(datosContacto.lada2(this));
    }

    private void captutrar()
    {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1 = tel1.getText().toString();
                t2 = tel2.getText().toString();
                t3 = tel3.getText().toString();
                n1=nombre1.getText().toString();
                n2=nombre2.getText().toString();
                n3=nombre3.getText().toString();
                l1=lada.getText().toString();
                l2=lada1.getText().toString();
                l3=lada2.getText().toString();
                v1=false;
                v2=false;
                v3=false;
                valid1=false;
                valid2= false;
                valid3= false;
                valid4= false;

                if (t1.equals("") && t2.equals("") && t3.equals(""))
                {
                    alerta("Debes ingresar al menos un contacto para en caso de emergencia.");

                }
                else
                {
                  if (( !t1.equals("") && !lada.getText().toString().equals("Pais") )  && t1.length()>=8 )
                {

                    v1=true;
                }


                else if(( t1.equals("") )  && t1.length()==0)
                  {
                      v1=true;
                      n1="";
                      l1="Pais";


                  }


                    if (( !t2.equals("") && !lada1.getText().toString().equals("Pais") )  && t2.length()>=8 )
                    {

                        v2=true;

                    }
                      else if (( t2.equals("") )  && t2.length()==0 )
                    {

                        v2=true;
                        n2="";
                        l2="Pais";

                    }



                    if (( !t3.equals("") && !lada2.getText().toString().equals("Pais") )  && t3.length()>=8 )
                    {
                        v3=true;

                    }

                   else if (( t3.equals("")  )  && t3.length()==0 )
                    {
                        v3=true;
                       n3="";
                        l3="Pais";
                    }


                    if(v1 && v2 && v3)
                    {

                        if((t1.equals(t2) && t1.equals(t3)) && !t1.equals(""))
                        {
                            alerta("Los contactos 1, 2 y  3 tienen el mismo numero");

                        }
                        else
                        {
                            if((t1.equals(t2) && !t1.equals("")))
                            {
                                alerta("El contacto 1 y 2 tienen el mismo numero");

                            }
                            else {
                                valid1=true;// esta bien
                            }

                            if((t2.equals(t3)  && !t2.equals("")) )
                            {

                                alerta("El contacto 2 y 3 tienen el mismo numero");
                            }

                            else
                            {
                                valid2= true;
                            }

                            if( (t3.equals(t1)  && !t3.equals("")))
                            {
                                alerta("El contacto  1 y 3 tienen el mismo numero");
                            }
                            else
                            {
                                valid3= true;
                            }

                            valid4= true;
                        }

                        if(valid1 && valid2 && valid3 && valid4)
                        {
                            GuardarDatos(t1, t2, t3,n1,n2,n3);

                           // alerta("todo bien validado");


                        }

                    }


                    else
                    {
                        if(!v1)
                        {
                            alerta("Al Contacto 1 le falta la clave del país o revisar el número teléfonico");

                        }
                        if (!v2)
                        {

                            alerta("Al Contacto 2 le falta la clave del país o revisar el número teléfonico");

                        }
                        if(!v3)
                        {
                            alerta("Al Contacto 3  le falta la clave del pais o revisar el número teléfonico");

                        }
                    }
                }
            }
        });


        importarc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacto1=1;
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i,100);
            }
        });
        importarc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacto2=2;
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i,100);
            }
        });

        importarc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacto3=3;
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i,100);
            }
        });

        lada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mostrarlistado(paises,lada,1);

            }
        });
        lada2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarlistado(paises,lada2,3);
            }
        });
        lada1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarlistado(paises,lada1,2);
            }
        });
        flecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarlistado(paises,lada,1);

            }
        });
        flecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarlistado(paises,lada1,2);

            }
        });
        flecha3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarlistado(paises,lada2,3);

            }
        });

    }

    @SuppressLint("CommitPrefEdits")
    private void GuardarDatos(String t1, String t2, String t3, String n1, String n2, String n3)
    {

        prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        edit= prefs.edit();

        edit.putString(Constants.tel1, t1);
        edit.putString(Constants.nombre1,n1);
        edit.putString(Constants.lada,l1);



        edit.putString(Constants.tel2, t2);

        edit.putString(Constants.nombre2,n2);

        edit.putString(Constants.lada1,l2);




        edit.putString(Constants.tel3,t3);
        edit.putString(Constants.nombre3,n3);
        edit.putString(Constants.lada2,l3);


        if(!t1.equals(""))
        {
            lista.add(t1);
            cods.add(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.Codigo, ""));


        }

        if(!t2.equals(""))
        {
            lista.add(t2);
            cods.add(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.Codigo1, ""));



        }

        if(!t3.equals(""))
        {
  lista.add(t3);

            cods.add(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.Codigo2, ""));


        }


        edit.putBoolean("guardar", true);
        edit.apply();



        tarea();


        System.out.println(co+"  "+co2+"   "+co3);

        finish();


    }

    @SuppressLint("SetTextI18n")
    public void alerta(String mensaje){

        final Dialog alert = new Dialog(this,R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.alerta_error, null);
        TextView titulodialo=(TextView)dialogo.findViewById(R.id.tituloDialogo);
        titulodialo.setText("  Aviso");
        LinearLayout confirmar=(LinearLayout)dialogo.findViewById(R.id.boton_confirmar);
        TextView error1=(TextView)dialogo.findViewById(R.id.mensajerrror);
        error1.setText(mensaje);
        alert.setContentView(dialogo);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
         finish();
    }

    public void tarea()
    {

        startService(new Intent(Contactos.this,Service_contact.class));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


       if(requestCode==100)
       {
           if(resultCode==RESULT_OK)
           {
               contacs=data.getData();

               if(contacto1==1)
               {
                   contacto1=9;
                   telefonos=datos_c.getPhone(contacs,getApplicationContext());
                 //  nombre1.setText(datos_c.getName(contacs,getApplicationContext()));
                   pn=datos_c.getName(contacs,getApplicationContext());

                   if(telefonos.size()>=0)
                   {
                      // mostrarlistado(telefonos,tel1);
                       va(pn.toString(),"Selecciona el teléfono",1);

                   }

                //  tel1.setText(datos_c.getPhone(contacs,getApplicationContext()));


               }
               if(contacto2==2)
               {
                  // tel2.setText(datos_c.getPhone(contacs,getApplicationContext()).trim());

                   telefonos=datos_c.getPhone(contacs,getApplicationContext());
                  // nombre2.setText(datos_c.getName(contacs,getApplicationContext()));
                   pn1=datos_c.getName(contacs,getApplicationContext());

                   if(telefonos.size()>=0)
                   {
                     //  mostrarlistado(telefonos,tel2);
                       va(pn1,"Selecciona el teléfono",2);

                   }



                   contacto2=6;

               }
               if(contacto3==3)
               {
                  // nombre3.setText(datos_c.getName(contacs,getApplicationContext()));

                   pn2=datos_c.getName(contacs,getApplicationContext());

                   telefonos=datos_c.getPhone(contacs,getApplicationContext());

                   if(telefonos.size()>=0)
                   {
                     //  mostrarlistado(telefonos,tel3);
                       va(pn2,"Selecciona el teléfono",3);

                   }

                   //  tel3.setText(datos_c.getPhone(contacs,getApplicationContext()).trim());
                   contacto3=7;
               }


           }
       }
    }


    public  void mostrarlistado(ArrayList <String> lista1, final TextView textView, final int identificador){
        final Dialog alert = new Dialog(Contactos.this,R.style.Theme_Dialog_Translucent);
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

                prefs =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                edit= prefs.edit();

                if(identificador==1)
                {
                    edit.putString(Constants.Codigo, codigos.get(position)+"");
                    co=codigos.get(position)+"";
                 //  cods.add(co);

                }
                if (identificador==2)
                {
                    co2=codigos.get(position)+"";

                    edit.putString(Constants.Codigo1,codigos.get(position)+"");
                  //  cods.add(co2);

                }
                if (identificador==3)
                {
                    co3=codigos.get(position)+"";

                  //  cods.add(co3);
                    edit.putString(Constants.Codigo2,codigos.get(position)+"");

                }
                edit.apply();

                alert.dismiss();
            }
        });

    }
    public  void mostrarlistado(ArrayList<String> arrayList, final EditText textView){
        final Dialog alert = new Dialog(Contactos.this,R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.lista_horas_pm, null);
        GridView lista=(GridView)dialogo.findViewById(R.id.listado);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.spinner_item,arrayList);
        lista.setAdapter(arrayAdapter);
        alert.setContentView(dialogo);
        alert.show();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = parent.getItemAtPosition(position).toString();
                textView.setText(a);
                alert.dismiss();
            }
        });

    }

    public void ResultadoCatalago(Claves_telefonicas claves_telefonicas)
    {
        bloquearPantalla.deslbloquear();
        progreso.setVisibility(View.GONE);
        mostrarContactos();
        paises= new ArrayList<>();


        if(claves_telefonicas.isSuccess())
        {

            for (int i=0;i<claves_telefonicas.getClaves_telefonicasArrayList().size();i++)
            {
                paises.add(claves_telefonicas.getClaves_telefonicasArrayList().get(i).getPais());
                codigos.add(Integer.valueOf(claves_telefonicas.getClaves_telefonicasArrayList().get(i).getId()));

            }

         //   codigo_paises.setCodigo_internacional(codigos);

        }
        else {

            paises.add("Pais");
            lada.setEnabled(false);
            lada1.setEnabled(false);
            lada2.setEnabled(false);
            flecha1.setEnabled(false);
            flecha2.setEnabled(false);
            flecha3.setEnabled(false);
//          codigos.add(99999);

        }

    }


    public void va (String titulo, String titulose, final int identificador){
        final Dialog alert = new Dialog(Contactos.this,R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogo=inflater1.inflate(R.layout.catalogo_pais, null);
        ImageView cerrar=(ImageView)dialogo.findViewById(R.id.Cerrar_catalogo);
        TextView nombrep=(TextView)dialogo.findViewById(R.id.Nombre_principal);
        TextView nombresc=(TextView)dialogo.findViewById(R.id.Nombresecundario);
        nombrep.setText(titulo);
        nombresc.setText(titulose);
        ListView listado_paises=(ListView) dialogo.findViewById(R.id.paises_catalogo);
        AdapterTelefonos adapterTelefonos= new AdapterTelefonos(this,R.layout.spinner_item,telefonos);
        adapterTelefonos.setTelefonos(telefonos);
        adapterTelefonos.setContext(this);
        listado_paises.setAdapter(adapterTelefonos);
        alert.setContentView(dialogo);
        alert.show();

        listado_paises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //telefonos.get(position);
                if (identificador==1)
                {
                    tel1.setText(telefonos.get(position));
                    nombre1.setText(datos_c.getName(contacs,getApplicationContext()));
                }
                if(identificador==2){
                    tel2.setText(telefonos.get(position));
                    nombre2.setText(datos_c.getName(contacs,getApplicationContext()));
                }
                if(identificador==3){
                    tel3.setText(telefonos.get(position));
                    nombre3.setText(datos_c.getName(contacs,getApplicationContext()));
                }

                alert.dismiss();

            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    private void clicks()
    {
        pa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                va("Catálogo de paises","Selecciona el país",1);
            }
        });

        pa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                va("Catálogo de paises","Selecciona el país",2);
            }
        });

        pa3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //va("Catálogo de paises","Selecciona el país");
            }
        });


    }




}
