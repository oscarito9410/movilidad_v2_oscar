package com.gruposalinas.elektra.movilidadgs.async;

import android.os.AsyncTask;
import android.speech.tts.Voice;

import com.gruposalinas.elektra.movilidadgs.Activities.Contacto;
import com.gruposalinas.elektra.movilidadgs.Activities.Contactos;
import com.gruposalinas.elektra.movilidadgs.Activities.RegistroActivity;
import com.gruposalinas.elektra.movilidadgs.Activities.Settings_perfil;
import com.gruposalinas.elektra.movilidadgs.beans.Claves_telefonicas;
import com.gruposalinas.elektra.movilidadgs.webservices.ClavesTelefonicasWS;

/**
 * Created by yvegav on 21/12/2016.
 */

public class claves_TelefonicasAsync extends AsyncTask<Claves_telefonicas,Void,Claves_telefonicas> {


    Contactos contactos;
    RegistroActivity registroActivity;
    Settings_perfil settings_perfil;

    public claves_TelefonicasAsync(Contactos contactos1)
    {
        contactos=contactos1;

    }

    public claves_TelefonicasAsync(RegistroActivity registroActivity) {
        this.registroActivity = registroActivity;
    }

    public claves_TelefonicasAsync(Settings_perfil settings_perfil) {
        this.settings_perfil = settings_perfil;
    }

    @Override
    protected Claves_telefonicas doInBackground(Claves_telefonicas... params) {
        ClavesTelefonicasWS clavesTelefonicasWS= new ClavesTelefonicasWS();
        Claves_telefonicas claves_telefonicas=clavesTelefonicasWS.getclaves(params[0]);
        return claves_telefonicas;
    }

    @Override
    protected void onPostExecute(Claves_telefonicas claves_telefonicas) {
        super.onPostExecute(claves_telefonicas);

        if (contactos!=null)
        {
         contactos.ResultadoCatalago(claves_telefonicas);
        }

        if(registroActivity!=null)
        {
            registroActivity.cargarCatalogo_lista(claves_telefonicas);

        }

        if(settings_perfil!=null){
            settings_perfil.resultado(claves_telefonicas);
        }
    }
}
