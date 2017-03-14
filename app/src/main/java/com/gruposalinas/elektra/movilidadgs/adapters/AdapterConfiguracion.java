package com.gruposalinas.elektra.movilidadgs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gruposalinas.elektra.movilidadgs.R;

import java.util.ArrayList;

/**
 * Created by adrian on 04/01/2017.
 */

public class AdapterConfiguracion extends ArrayAdapter<String>
{
    ArrayList<String> lista;
    ArrayList <Integer> imagenes;


    Context context;
    public void setLista(ArrayList<String> lista) {
        this.lista = lista;
    }

    public void setImagenes(ArrayList<Integer> imagenes) {
        this.imagenes = imagenes;
    }

    public AdapterConfiguracion(Context context, int resource , ArrayList<String> arrayList)
    {
        super(context, resource,arrayList);
        this.context=context;

    }



    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View vista = inflater.inflate(R.layout.item_configuracion, parent, false);

        TextView textView1=(TextView)vista.findViewById(R.id.tipo);
        ImageView linearLayout=(ImageView) vista.findViewById(R.id.justificacion);
        linearLayout.setBackgroundResource(imagenes.get(position));
        textView1.setText(lista.get(position));

        return vista;

    }
}
