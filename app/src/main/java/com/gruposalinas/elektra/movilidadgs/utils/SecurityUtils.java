package com.gruposalinas.elektra.movilidadgs.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by oemy9 on 08/03/2017.
 */

public class SecurityUtils {

    public static final String TAG = "UTILS";

    /*
        Regresa Fecha en formato listo para encriptar
    * */
    public static String getFormatDate() {
        Calendar cal = Calendar.getInstance();
        int segundos=cal.get(Calendar.SECOND);
        Log.i(TAG,"SEGUNDOS:"+String.valueOf(segundos));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.MINUTE, segundos>=58?1:0);
        dateFormat.format(cal.getTime());
        return dateFormat.format(cal.getTime());
    }



    /*
        Concatena la fecha obtenida con algún oto texto por ejemplo el número de empleado
    * */
    public static String getToken(String idEmployEncrypt) {
        return idEmployEncrypt.concat(getFormatDate());
    }

    /*

        Regresa la llave de encription de cada uno de los métodos
    */
    public static String getKeyEncription() {
        return getFormatDate();
    }

    /*
    *   Regresa  IV  obligatorio para generar el encriptamiento
    * */
    public static String getIV() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i(TAG, "IV=" + dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }


    public static String encryptAES(String textoEncriptar) {
        String encriptado = "";
        try {
            CryptLib cryptLib = new CryptLib();
            String key = getKeyEncription();
            String iv = getIV();
            encriptado = cryptLib.encrypt(textoEncriptar, key, iv);
            Log.i("AES ENCRIPTADO", encriptado);
            return encriptado.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encriptado.trim();
    }

    public static String decryptAES(String textoEncriptado) {
        String decrypt = "";
        try {
            CryptLib cryptLib = new CryptLib();
            String key = getKeyEncription();
            String iv = getIV();
            decrypt = cryptLib.decrypt(textoEncriptado.trim(), key, iv);
            Log.i(TAG,"DECRYPT:"+decrypt+" FROM:"+textoEncriptado);
            return decrypt.trim();
        } catch (Exception ex) {
            Log.e(TAG,"DECRYPT ERROR:"+ex.getMessage());
            ex.printStackTrace();
        }
        return decrypt;
    }

    /*
    public static  boolean tokenHasError(String errorToken){
        return  errorToken.equals(Constants.ERROR_LLAVE);
    }*/


}
