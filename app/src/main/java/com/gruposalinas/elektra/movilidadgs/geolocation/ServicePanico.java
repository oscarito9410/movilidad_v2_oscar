package com.gruposalinas.elektra.movilidadgs.geolocation;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import android.media.MediaRecorder;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.gruposalinas.elektra.movilidadgs.async.MultimediaAsync;

import com.gruposalinas.elektra.movilidadgs.beans.DatosContacto;
import com.gruposalinas.elektra.movilidadgs.beans.Multimedia;

import com.gruposalinas.elektra.movilidadgs.beans.RegistroGPS;
import com.gruposalinas.elektra.movilidadgs.utils.Constants;
import com.gruposalinas.elektra.movilidadgs.utils.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


public class ServicePanico extends Service
{
    boolean revisar=false;
    Handler handler1;
     Runnable runnable1;
    private static String mFileName,mFileName1,mFileName2 = null;
    private MediaRecorder mRecorder = null, recorder2=null,recorder3=null;
    String graba;
    int valor=0;
    String graba1;
   Multimedia multimedia,multimedia1,multimedia2,multimedia3,multimedia4,multimedia6;
    DatosContacto datosContacto;

    ArrayList <Multimedia> multimedias;
    String nobre;
    String filevideo;
    Timer timer;


    @Override
    public void onCreate() {
        super.onCreate();

        takePhoto();
        multimedias= new ArrayList<>();

      datosContacto=  new DatosContacto();
        multimedia= new Multimedia();
        multimedia1= new Multimedia();
        multimedia2= new Multimedia();
        multimedia3= new Multimedia();
        multimedia4=new Multimedia();
        multimedia6=new Multimedia();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock1=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DonotSlepp");
        wakeLock1.acquire();

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/grabando1.wav";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setMaxDuration(10000);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);



         graba = Environment.getExternalStorageDirectory().getAbsolutePath();
        graba += "/grabando2.wav";

        recorder2 = new MediaRecorder();
        recorder2.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder2.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder2.setMaxDuration(10000);
        recorder2.setOutputFile(graba);
        recorder2.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            recorder2.prepare();
        } catch (IOException e)
        {
            System.out.print("no se pudo preparar el microfono");

        }

         graba1= Environment.getExternalStorageDirectory().getAbsolutePath();
        graba1 += "/grabando3.wav";

        recorder3 = new MediaRecorder();
        recorder3.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder3.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder3.setMaxDuration(10000);
        recorder3.setOutputFile(graba1);
        recorder3.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            recorder3.prepare();
        } catch (IOException e)
        {
            System.out.print("no se pudo preparar el microfono");


        }



        filevideo= Environment.getExternalStorageDirectory().getAbsolutePath();
        filevideo += "/video.mp4";

        String a= null;
        try {
            File file= new File(filevideo);


            a = Base64.encodeToString(Utils.loadFile(file), 0);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, "") + date;

            multimedia6.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
            multimedia6.setArchivo(a);
            multimedia6.setNombre_archivo(photoFile);
            multimedia6.setExtension("mp4");
            multimedia6.setContactos(datosContacto.gettel1(getApplicationContext())+","+datosContacto.gettel2(getApplicationContext())+","+datosContacto.gettel3(getApplicationContext()));

            multimedia6.setTamano(file.getTotalSpace() + "");
            multimedias.add(multimedia6);
            multimedia6.setMultimediaArrayList(multimedias);

        } catch (IOException e) {
            e.printStackTrace();
        }

        handler1= new Handler();
        final String finalGraba = graba1;
        runnable1= new Runnable() {
            @Override
            public void run() {

                valor++;



                if(valor==1)
                {


                    try {
                        mRecorder.prepare();
                    } catch (IOException e)
                    {
                        System.out.print("no se pudo preparar el microfono");


                    }

                  mRecorder.start();
                    Log.d("inicia", "inicia grabacion1");

                }
                if (valor==2)
                {
                    GPSTracker gps = new GPSTracker(getApplicationContext());
                    SMS(gps.getLatitud() + "", gps.getLongitud() + "");
                    multimedia.setCoordenadas(gps.getLatitud()+"@"+gps.getLongitud());

                    Log.d("se para", "parar grabacion1");
                  mRecorder.stop();
                    File file= new File(mFileName);
                    try {

                         String a=  Base64.encodeToString(Utils.loadFile(file),0);

//                        System.out.println(a);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                        String date = dateFormat.format(new Date());
                        String photoFile =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, "") + date;


                        multimedia.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
                        multimedia.setArchivo(a);
                        multimedia.setNombre_archivo(photoFile);
                        multimedia.setExtension("wav");
                        multimedia.setContactos(datosContacto.gettel1(getApplicationContext())+","+datosContacto.gettel2(getApplicationContext())+","+datosContacto.gettel3(getApplicationContext()));
                        multimedia.setTamano(file.length() + "");

                        Long t=file.getTotalSpace();

                      //  multimedias.add(multimedia);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("inicia", "inicia grabacion2");
                  recorder2.start();

                    // inicia la grabacion 2
                }


                if(valor==3)
                {


                    Log.d("se para", "inicia grabacion2");


                    // metodo para parar grabacion 2
                  recorder2.stop();
                    File file= new File(graba);
                    try {

                        String a=  Base64.encodeToString(Utils.loadFile(file),0);

                      //   System.out.println(a);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                        String date = dateFormat.format(new Date());
                        String photoFile =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, "") + date;


                        multimedia1.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
                        multimedia1.setArchivo(a);
                        multimedia1.setNombre_archivo(photoFile);
                        multimedia1.setExtension("wav");
                        multimedia1.setContactos(datosContacto.gettel1(getApplicationContext())+","+datosContacto.gettel2(getApplicationContext())+","+datosContacto.gettel3(getApplicationContext()));
                        multimedia1.setTamano(file.getTotalSpace() + "");
                        Long t=file.getTotalSpace();
                       // multimedias.add(multimedia1);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("inicia", "grbacion3");
                  recorder3.start();
                }

                if (valor==4){
                    Log.d("se para", "inicia grabacion3");

                  recorder3.stop();


                    File file= new File(graba1);
                    try {

                        String a=  Base64.encodeToString(Utils.loadFile(file),0);

                    //     System.out.println(a);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                        String date = dateFormat.format(new Date());
                        String photoFile =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, "") + date;

                        multimedia2.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
                        multimedia2.setArchivo(a);
                        multimedia2.setNombre_archivo(photoFile);
                        multimedia2.setExtension("wav");
                        multimedia2.setContactos(datosContacto.gettel1(getApplicationContext())+","+datosContacto.gettel2(getApplicationContext())+","+datosContacto.gettel3(getApplicationContext())+","+datosContacto.Numero_jefe(getApplicationContext()));
                        multimedia2.setTamano(file.getTotalSpace() + "");
                        Long t=file.getTotalSpace();

                        multimedias.add(multimedia2);

                 //     enviarMultimedia(multimedia);




                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    multimedia.setMultimediaArrayList(multimedias);

                    enviarMultimedia(multimedia);



                  /*  timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {



                        }
                    }, 0, 120000L);

                    */



                    // prueba//

                    handler1.removeCallbacks(runnable1);
                    ///video parte del video


                  /*  Intent intent1= new Intent(getApplicationContext(),capture_video.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    */



                ///video parte del video


                    //
                }

                handler1.postDelayed(runnable1,10000);
            }
        };
        handler1.post(runnable1);



        Log.d("se crea", "servicio panico");
        wakeLock1.release();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        takePhoto();
        alertapanico();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public  void alertapanico()
    {

        if(revisar){
            Log.d("Entra TIMER", true +"");
            GPSTracker gps = new GPSTracker(this);
            muestraPosicionpanico(gps);

        }else{
            GPSTracker gps = new GPSTracker(this);
            muestraPosicionpanico(gps);
        }

    }
    public void muestraPosicionpanico(GPSTracker _location){

      String l=_location.getLatitud()+"";
        String L=_location.getLongitud()+"";
      Date  date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DAY_FORMAT);
        dateFormatter.setLenient(false);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter = new SimpleDateFormat(Constants.HOUR_FORMAT);
        timeFormatter.setLenient(false);


       String dateString = dateFormatter.format(date);
       String hourString = timeFormatter.format(date);


        RegistroGPS registroGPS= new RegistroGPS();
        registroGPS.setLatitud(_location.getLatitud());
        registroGPS.setLongitud(_location.getLongitud());
        registroGPS.setJsonDate(generateJsonDate(dateString, hourString));
        registroGPS.setNumEmpleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));

        System.out.println(registroGPS+"");

       // EnvioAlertaAsync envioAlertaAsync= new EnvioAlertaAsync(this);
       //envioAlertaAsync.execute(registroGPS);


    }

    private String generateJsonDate(String fecha ,String hora){
        //Setting jsonDate
        String jsonDate = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date date = new Date();
        try {
            date = formatter.parse(fecha + " " + hora);
        } catch (java.text.ParseException e) {
            Log.e("error", "Error al parsear fecha");
        }
        jsonDate = Utils.getJsonDate(date);

        return jsonDate;
    }

    @SuppressLint("UnlocalizedSms")
    public void SMS(String l,String L)
    {
        String numeroJefe = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.NUMERO_JEFE, "");
        DatosContacto datosContacto= new DatosContacto();
        String nombrepersona=getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.SP_NAME,"");

        ArrayList<String> d= new ArrayList<>();
        StringTokenizer separar= new StringTokenizer(nombrepersona);

        while (separar.hasMoreTokens())
        {
           d.add(separar.nextToken());

        }
        System.out.print(d.size());
        String temp="";
        for(int t=0;t<d.size();t++)
        {
            if(t==0)
            {
                temp=d.get(t);
            }
            else{
                temp=temp+d.get(t).substring(0,1);
            }

        }

        String mensaje=temp +" Necesita ayuda http://maps.google.com/maps?f=q&q=("+l+","+L+")";

        if(!numeroJefe.equals(""))
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(numeroJefe, null, mensaje , null, null);


                Toast.makeText(getApplicationContext(), "Enviando SMS!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
        if(!datosContacto.gettel1(this).equals(""))
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(datosContacto.gettel1(this), null, mensaje , null, null);


                Toast.makeText(getApplicationContext(), "Enviando SMS!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

        if(!datosContacto.gettel2(this).equals(""))
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(datosContacto.gettel2(this), null, mensaje , null, null);


                Toast.makeText(getApplicationContext(), "Enviando SMS!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

        if(!datosContacto.gettel3(this).equals(""))
        {
            try {
                SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(datosContacto.gettel3(this), null, mensaje , null, null);


                Toast.makeText(getApplicationContext(), "Enviando SMS!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

         }




    private void takePhoto() {

        Camera camera = null;
        int cameraCount = 1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            SystemClock.sleep(1000);

            Camera.getCameraInfo(camIdx, cameraInfo);

            try {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                Camera.Parameters parameters = camera.getParameters();
                parameters.set("jpeg-quality", 100);
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPictureSize(640, 480);
                parameters.setAutoWhiteBalanceLock(true);
                parameters.setRotation(90);
                camera.setParameters(parameters);



            } catch (RuntimeException e)
            {
                System.out.println("Camera no disponible: " + camIdx);
                camera = null;
                //e.printStackTrace();
            }
            try {
                if (null == camera)
                {
                    System.out.println("No se puede obtener una instancia de la camara");
                } else {
                    try {


                        camera.setPreviewTexture(new SurfaceTexture(0));

                        camera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera)
                        {

                            File pictureFileDir = getFilesDir();
                            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                                return;
                            }
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                            String date = dateFormat.format(new Date());
                            String photoFile =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, "") + date;
                            mFileName1 = Environment.getExternalStorageDirectory().getAbsolutePath()+photoFile;
                            nobre= Environment.getExternalStorageDirectory().getAbsolutePath();
                            nobre += "/foto.jpg";

                            File mainPicture = new File(nobre);

                            try {
                                FileOutputStream fos = new FileOutputStream(mainPicture);
                                fos.write(data);
                                fos.close();
                                System.out.println("Imagen guardada");
                                String a=  Base64.encodeToString(Utils.loadFile(mainPicture),0);

                                //     System.out.println(a);

                                multimedia3.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
                                multimedia3.setArchivo(a);
                                multimedia3.setNombre_archivo(photoFile);
                                multimedia3.setExtension("jpg");
                                multimedia3.setContactos(datosContacto.gettel1(getApplicationContext())+","+datosContacto.gettel2(getApplicationContext())+","+datosContacto.gettel3(getApplicationContext()));
                                multimedia3.setTamano(mainPicture.getTotalSpace() + "");
                                Long t=mainPicture.getTotalSpace();

                                multimedias.add(multimedia3);

                      //        enviarMultimedia(multimedia);
                            } catch (Exception error)
                            {
                                System.out.println("La imagen no pudo ser guardada");
                            }
                            camera.release();
                            frontalFoto();
                        }
                    });
                }
            } catch (Exception e) {
                if (camera != null) {
                    camera.release();
                }

            }
        }
    }

    private void frontalFoto() {

        Camera camera = null;
        int cameraCount = 1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            SystemClock.sleep(1000);

            Camera.getCameraInfo(camIdx, cameraInfo);

            try {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                Camera.Parameters parameters = camera.getParameters();
                parameters.set("jpeg-quality", 70);
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPictureSize(640, 480);
                parameters.setRotation(90);
                camera.setParameters(parameters);

            } catch (RuntimeException e) {
                System.out.println("Camera no disponible: " + camIdx);
                camera = null;
                //e.printStackTrace();
            }
            try {
                if (null == camera) {
                    System.out.println("No se puede obtener una instancia de la camara");
                } else {
                    try {
                        camera.setPreviewTexture(new SurfaceTexture(0));
                        camera.startPreview();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            File pictureFileDir = getFilesDir();
                            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                                return;
                            }
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                            String date = dateFormat.format(new Date());
                            String photoFile =getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, "") + date +"";
                            mFileName2= Environment.getExternalStorageDirectory().getAbsolutePath()+photoFile;
                            nobre= Environment.getExternalStorageDirectory().getAbsolutePath();
                            nobre += "/foto_frontal.jpg";

                            File mainPicture = new File(nobre);

                            //  MMS(mainPicture);

                            // addImageFile(mainPicture);

                            try {
                                FileOutputStream fos = new FileOutputStream(mainPicture);
                                fos.write(data);
                                fos.close();
                                System.out.println("Imagen guardada");
                                String a=  Base64.encodeToString(Utils.loadFile(mainPicture),0);

                                //     System.out.println(a);

                                multimedia4.setId_numero_empleado(getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.SP_ID, ""));
                                multimedia4.setArchivo(a);
                                multimedia4.setNombre_archivo(photoFile);
                                multimedia4.setExtension("jpg");
                                multimedia4.setContactos(datosContacto.gettel1(getApplicationContext())+","+datosContacto.gettel2(getApplicationContext())+","+datosContacto.gettel3(getApplicationContext()));
                                multimedia4.setTamano(mainPicture.getTotalSpace() + "");
                                Long t=mainPicture.getTotalSpace();

                              //  multimedias.add(multimedia4);

                            //   enviarMultimedia(multimedia);
                            } catch (Exception error) {
                                System.out.println("La imagen no pudo ser guardada");
                            }
                            camera.release();
                        }
                    });
                }
            } catch (Exception e) {
                if (camera != null) {
                    camera.release();
                }
            }
        }}


    public void enviarMultimedia(Multimedia multimedia)
    {
        MultimediaAsync multimediaAsync= new MultimediaAsync(this);
        multimediaAsync.execute(multimedia);


    }

    public void resultado(Multimedia resul)
    {
        if (resul.isSuccess())
        {
//            timer.cancel();

            File file= new File("/storage/emulated/0/video.mp4");
            File file1= new File("/storage/emulated/0/foto_frontal.jpg");
            File file2= new File("/storage/emulated/0/foto.jpg");
            File file3= new File("/storage/emulated/0/grabando1.wav");
            File file4= new File("/storage/emulated/0/grabando2.wav");
            File file5= new File("/storage/emulated/0/grabando3.wav");



            file.delete();
            file1.delete();
            file2.delete();
            file3.delete();
            file4.delete();
            file5.delete();
            stopService(new Intent(getBaseContext(), ServicePanico.class));


        }



    }


    }
