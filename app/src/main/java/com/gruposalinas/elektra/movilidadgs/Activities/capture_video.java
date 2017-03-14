package com.gruposalinas.elektra.movilidadgs.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gruposalinas.elektra.movilidadgs.R;
import com.gruposalinas.elektra.movilidadgs.async.MultimediaAsync;
import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.beans.Multimedia;
import com.gruposalinas.elektra.movilidadgs.geolocation.EmployeeLocationService;
import com.gruposalinas.elektra.movilidadgs.geolocation.HoraExacta;
import com.gruposalinas.elektra.movilidadgs.geolocation.RecorderService;
import com.gruposalinas.elektra.movilidadgs.geolocation.ServicePanico;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class capture_video extends Activity implements SurfaceHolder.Callback {
    private static String mFileName = null;
   public static SurfaceHolder holder;
    boolean recording = false;
   public static SurfaceView cameraView;
    public static Camera mCamera;
    Handler handler;
    Runnable runnable;
    int anInt;
    ArrayList <Multimedia> lista;
    Multimedia multimedia6;
    DatosContacto datosContacto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);



        cameraView = (SurfaceView) findViewById(R.id.videocam);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        handler= new Handler();
        runnable= new Runnable() {
            @Override
            public void run()
            {
                anInt++;

                if (anInt==1)
                {
                     Intent intent = new Intent(capture_video.this, RecorderService.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startService(intent);
                    finish();


                }

                if (anInt==2)
                {

                    stopService(new Intent(capture_video.this, RecorderService.class));

                }

                handler.postDelayed(runnable,13000);

            }
        };
        handler.post(runnable);






    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {



    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }



    public void enviarMultimedia(Multimedia multimedia)
    {
        MultimediaAsync multimediaAsync= new MultimediaAsync(this);
        multimediaAsync.execute(multimedia);

    }
}
