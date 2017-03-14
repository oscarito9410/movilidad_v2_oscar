package com.gruposalinas.elektra.movilidadgs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gruposalinas.elektra.movilidadgs.R;

import java.util.ArrayList;

/**
 * Created by adrian on 26/01/2017.
 */

public class AdapterTelefonos extends ArrayAdapter<String>
{
    Context context;
    ArrayList<String> telefonos;
    public AdapterTelefonos(Context context, int resource, ArrayList<String> telefonos) {
        super(context, resource,telefonos);
        this.context=context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTelefonos(ArrayList<String> telefonos) {
        this.telefonos = telefonos;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View vista = inflater.inflate(R.layout.vista_telefono, parent, false);

        TextView telefono=(TextView)vista.findViewById(R.id.telefonoscatalogo);
        telefono.setText(telefonos.get(position));

        return vista;

    }
}
