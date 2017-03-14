package com.gruposalinas.elektra.movilidadgs.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
* *
 *   ELEMENTOS NECESARIOS PARA LA COMUNICACIÓN ENTRE EL WEBSERVICE Y LA APP
 */
public class SecurityItems {

    public static final String TAG = "SEC_ITEMS";
    private String idEmploy;
    private String password;
    private String key=null, iv=null;

    public SecurityItems(String idEmploy){
        this.idEmploy=idEmploy;
    }

    public SecurityItems(String idEmploy,String password){
        this.idEmploy=idEmploy;
        this.password=password;
    }

   //SETTERS
    public void setPassword(String password){
        this.password=password;
    }

    public void setIdEmploy(String idEmploy){
        this.idEmploy=idEmploy;
    }


    /*
    * TOKEN ENCRYPTADO
    *
    * */
    public String getTokenEncrypt(){
        Log.i("TOKEN ENCRIPTADO",encryptAES(getToken(this.getIdEmployEncrypt())));
        return encryptAES(getToken(this.getIdEmployEncrypt()));
    }

    /*
    * ID EMPLEADO ENCRYPTADO
    * */

    public String getIdEmployEncrypt(){
       return encryptAES(this.idEmploy);
    }

    public String getPasswordEncrypt(){
        return  encryptAES(this.password);
    }




    /*
        Regresa Fecha en formato listo para encriptar
    * */
    private  String getFormatDate() {

        /*
         Calendar cal = Calendar.getInstance();
        int segundos=cal.get(Calendar.SECOND);
        Log.i(TAG,"SEGUNDOS:"+String.valueOf(segundos));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.MINUTE, segundos>=58?1:0);
        dateFormat.format(cal.getTime());
        return dateFormat.format(cal.getTime());*/

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i(TAG,"KEY UTC/A"+ dateFormat.format(cal.getTime()));
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmm");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i(TAG,"KEY UTC/N="+f.format(new Date()));
        return f.format(new Date());
    }



    /*
        Concatena la fecha obtenida con algún oto texto por ejemplo el número de empleado
    * */
    private String getToken(String idEmployEncrypt) {
        return idEmployEncrypt.concat(getFormatDate());
    }

    /*

        Regresa la llave de encription de cada uno de los métodos
    */
    private  String getKeyEncription() {
        return getFormatDate();
    }

    /*
    *   Regresa  IV  obligatorio para generar el encriptamiento
    * */
    private String getIV() {
        /*
         Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i(TAG, "IV=" + dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());*/

        Calendar cal = Calendar.getInstance();

         System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i(TAG, "IV CON UTC=" + dateFormat.format(cal.getTime()));
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHH");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i(TAG, "IV CON UTC NUEVO="+f.format(new Date()));
        return f.format(new Date());

    }

    private void  checkIfHaveChanges(){
        if(key==null || iv==null){
            this.key=getKeyEncription();
            this.iv=getIV();
        }
        else if(!key.equals(getKeyEncription())){
            this.key=getKeyEncription();
            Log.i(TAG,"HA CAMBIADO KEY");
        }
        else  if(!iv.equals(getIV())){
            this.iv=getIV();
            Log.i(TAG,"HA CAMBIADO EL IV");
        }
    }

    private String encryptAES(String textoEncriptar) {
        String encriptado = "";
        try {
            this.checkIfHaveChanges();
            CryptLib cryptLib = new CryptLib();
            String key = this.key;
            String iv = this.iv;
            encriptado = cryptLib.encrypt(textoEncriptar, key, iv);
            Log.i("AES ENCRIPTADO", encriptado);
            return encriptado.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encriptado.trim();
    }

    public  String decryptAES(String textoEncriptado) {
        String decrypt = "";
        try {
            this.checkIfHaveChanges();
            CryptLib cryptLib = new CryptLib();
            String key = this.key;
            String iv = this.iv;
            decrypt = cryptLib.decrypt(textoEncriptado.trim(), key, iv);
            Log.i(TAG,"DECRYPT:"+decrypt+" FROM:"+textoEncriptado);
            return decrypt.trim();
        } catch (Exception ex) {
            Log.e(TAG,"DECRYPT ERROR:"+ex.getMessage());
            ex.printStackTrace();
        }
        return decrypt;
    }


}
