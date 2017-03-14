package com.gruposalinas.elektra.movilidadgs.geolocation;

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.gruposalinas.elektra.movilidadgs.Activities.capture_video;
import java.io.IOException;
import java.util.List;

public class RecorderService extends Service {
    private static final String TAG = "RecorderService";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static Camera mServiceCamera;
    private boolean mRecordingStatus;
    private MediaRecorder mMediaRecorder;


    @Override
    public void onCreate() {
        mRecordingStatus = false;
        mSurfaceHolder = capture_video.holder;
        mServiceCamera = capture_video.mCamera;
        mSurfaceView = capture_video.cameraView;


        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (mRecordingStatus == false)
            startRecording();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopRecording();
        mRecordingStatus = false;

        super.onDestroy();
    }

    public boolean startRecording(){
        try {
           // Toast.makeText(getBaseContext(), "Recording Started", Toast.LENGTH_SHORT).show();

            mServiceCamera = Camera.open();
            Camera.Parameters params = mServiceCamera.getParameters();
            params.setRotation(90);
            mServiceCamera.setParameters(params);
            Camera.Parameters p = mServiceCamera.getParameters();

            final List<Camera.Size> listPreviewSize = p.getSupportedPreviewSizes();
            for (Camera.Size size : listPreviewSize) {
                Log.i(TAG, String.format("Supported Preview Size (%d, %d)", size.width, size.height));
            }

            Camera.Size previewSize = listPreviewSize.get(0);
            params.setRotation(90);
            p.setPreviewSize(previewSize.width, previewSize.height);
            mServiceCamera.setParameters(p);

            try {
                mServiceCamera.setPreviewDisplay(mSurfaceHolder);
                mServiceCamera.setDisplayOrientation(90);
                mServiceCamera.startPreview();
            }
            catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            mServiceCamera.setDisplayOrientation(90);
            mServiceCamera.unlock();

            String mFileName= mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/video.mp4";
            System.out.println(mFileName);

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setCamera(mServiceCamera);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoEncodingBitRate(1000000);
           mMediaRecorder.setVideoSize(720,480);
           // mMediaRecorder.setVideoSize(1280,720);

            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setOutputFile(mFileName);


            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mMediaRecorder.setOrientationHint(90);


            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mRecordingStatus = true;

            return true;

        } catch (IllegalStateException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void stopRecording() {
    //    Toast.makeText(getBaseContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
        try {
            mServiceCamera.reconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaRecorder.stop();
        mMediaRecorder.reset();

        mServiceCamera.stopPreview();
        mMediaRecorder.release();

        mServiceCamera.release();
        mServiceCamera = null;
        mSurfaceView=null;
        mSurfaceHolder=null;
         mMediaRecorder=null;
        System.gc();

        startService(new Intent(getBaseContext(), ServicePanico.class));
    }
}
