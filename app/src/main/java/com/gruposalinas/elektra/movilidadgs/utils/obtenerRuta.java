package com.gruposalinas.elektra.movilidadgs.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yvegav on 15/12/2016.
 */

public class obtenerRuta
{
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String path(Intent data, Context context){
        Uri selectedImage = data.getData();
        String wholeID = DocumentsContract.getDocumentId(selectedImage);
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[] { id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;

    }


    public Bitmap getImage(Uri uri,Context context) {
        BitmapFactory.Options generalOptions;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is, null, options);
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        generalOptions = options;
        return scaleImage(options, uri, 300,context);
    }

    public Bitmap scaleImage(BitmapFactory.Options options, Uri uri, int targetWidth,Context context) {
        BitmapFactory.Options generalOptions = null;
        if (options == null)
            options = generalOptions;
        Bitmap bitmap = null;
        double ratioWidth = ((float) targetWidth) / (float) options.outWidth;
        double ratioHeight = ((float) targetWidth) / (float) options.outHeight;
        double ratio = Math.min(ratioWidth, ratioHeight);
        int dstWidth = (int) Math.round(ratio * options.outWidth);
        int dstHeight = (int) Math.round(ratio * options.outHeight);
        ratio = Math.floor(1.0 / ratio);
        int sample =0;

        options.inJustDecodeBounds = false;
        if (sample <= 0) {
            sample = 1;
        }
        options.inSampleSize = (int) sample;
        options.inPurgeable = true;
        try {
            InputStream is;
            is = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (sample > 1)
                bitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, true);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }
}
