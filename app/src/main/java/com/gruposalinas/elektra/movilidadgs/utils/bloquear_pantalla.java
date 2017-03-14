package com.gruposalinas.elektra.movilidadgs.utils;

import android.app.Dialog;
import android.content.Context;

import com.gruposalinas.elektra.movilidadgs.R;

/**
 * Created by yvegav on 21/12/2016.
 */

public class bloquear_pantalla
{

    Dialog alert;
    public  void bloquear(Context context)
    {
        if(context!=null) {
            alert = new Dialog(context, R.style.Theme_Dialog_Translucent);
            alert.setCancelable(false);
            alert.show();
        }


    }
    public void deslbloquear()
    {

        if(alert!=null)
        alert.dismiss();
    }
}
