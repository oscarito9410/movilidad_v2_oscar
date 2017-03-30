package com.gruposalinas.elektra.movilidadgs.alertar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.ApplicationBase;
import com.gruposalinas.elektra.movilidadgs.utils.SessionManager;

/**
 * Created by Adrian on 09/08/2016.
 */
public class Alertas extends AlertDialog
{

    public Alertas(Context context) {
        super(context);
    }

    // errores
    public void displayMensaje(String error,final Context context)
    {
        final SessionManager sessionManager = new SessionManager(context);
        final Dialog alert = new Dialog(context, R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        final View dialogo=inflater1.inflate(R.layout.alerta_error, null);
        TextView titulodialo=(TextView)dialogo.findViewById(R.id.tituloDialogo);
        LinearLayout confirmar=(LinearLayout)dialogo.findViewById(R.id.boton_confirmar);
        TextView error1=(TextView)dialogo.findViewById(R.id.mensajerrror);
        error1.setText(sessionManager.get(Constants.IS_ERROR_FECHA)==true? ApplicationBase.getAppContext().getString(R.string.revisa_fecha) :error);
        alert.setContentView(dialogo);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                if(sessionManager.get(Constants.IS_ERROR_FECHA)){
                    Activity activity=(Activity)context;
                    activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });
        alert.show();

    }

    //displayMensaje//confirmacion exito//

    public void Exito(final Context context, final Activity activity)
    {

        final Dialog alert = new Dialog(context,R.style.Theme_Dialog_Translucent);
        LayoutInflater inflater1=getLayoutInflater();
        final View dialogo=inflater1.inflate(R.layout.alerta_exito, null);
        final LinearLayout confirmar=(LinearLayout)dialogo.findViewById(R.id.boton_aceptar);

        alert.setContentView(dialogo);
        alert.setCancelable(false);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity == null) {
                    alert.dismiss();
                } else {
                    Intent intent = new Intent(context, activity.getClass());
                    context.startActivity(intent);
                    alert.dismiss();

                }

            }
        });

        alert.show();
    }



}
