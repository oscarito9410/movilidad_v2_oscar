package com.gruposalinas.elektra.movilidadgs.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by yvegav on 13/12/2016.
 */

public class Datos_contactos_Panico
{

   public   String getName(Uri uri, Context context) {

        String name = null;

        ContentResolver contentResolver = context.getContentResolver();

        Cursor c = contentResolver.query(uri, new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }, null, null, null);


        if(c.moveToFirst()){
            name = c.getString(0);
        }

        /*
        Cerramos el cursor
         */
        c.close();

        return name;
    }


    public  ArrayList<String> getPhone(Uri uri,Context context) {
        String id = null;
        String phone = null;
        ArrayList <String> arrayList = null;
        arrayList=new ArrayList<>();



        Cursor contactCursor = context.getContentResolver().query(
                uri, new String[]{ContactsContract.Contacts._ID},
                null, null, null);


        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        String selectionArgs = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " + ContactsContract.CommonDataKinds.Phone.NUMBER;//+"= "+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        Cursor phoneCursor =context. getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, selectionArgs, new String[] { id }, null);
        if (phoneCursor.moveToFirst())
        {
            do
            {
                arrayList.add(phoneCursor.getString(0));

            }
            while (phoneCursor.moveToNext());
        }

        else
        {
            arrayList.add("");

        }

        phoneCursor.close();

        return arrayList;
    }




}
