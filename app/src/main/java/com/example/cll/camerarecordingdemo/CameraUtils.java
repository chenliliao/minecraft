package com.example.cll.camerarecordingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;

/**
 * Created by cll on 2017/12/7.
 */

public class CameraUtils {

    private static Camera.Size size;
    private static Camera mCamera ;
    public static Camera getCamera(Context context, SurfaceHolder mHolder){

        try {
            if (mCamera != null){
                mCamera.release();
                mCamera = null;
            }

            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
            for (Camera.Size size : mCamera.getParameters().getSupportedPreviewSizes()) {

            }
            size = mCamera.getParameters().getSupportedPreviewSizes().get(0);
            Log.w("TAG","test getCamera  size.width = "+size.width +"  size.height = "+ size.height);
            Camera.Parameters parameter = mCamera.getParameters();
            parameter.setPreviewSize(size.width,size.height);
            parameter.setPictureSize(size.width,size.height);
            parameter.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(parameter);
            SurfaceTexture surface = new SurfaceTexture(0);
            mCamera.setPreviewTexture(surface);
//            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("TAG","test getCamera  IOException = ");
        }
        return mCamera;
    }

    private static String name = "VIDEO_"+System.currentTimeMillis()+".mp4";
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + name;
    private static MediaRecorder initMediaRecorder(Camera mCamera){
        if (mCamera == null){
            return null;
        }
        try {
            MediaRecorder mRecorder = new MediaRecorder();
            mCamera.unlock();  //start-failed-19
            mRecorder.setCamera(mCamera);
            mRecorder.setOrientationHint(90);
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//            mRecorder.setMaxDuration(10000);
            mRecorder.setVideoSize(640, 480);
            mRecorder.setVideoFrameRate(15);
            mRecorder.setVideoEncodingBitRate(640 * 480 * 15 / 3);
//            Log.w("TAG","test getCamera  size.MediaRecorder = "+size.width * size.height * 30);
//            Log.w("TAG","test getCamera  size.MediaRecorder = "+1280 * 720 * 30 / 3);

            mRecorder.setOutputFile(path);
            mRecorder.prepare();
            return mRecorder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    static MediaRecorder mRecorder;
    private static boolean isRecording = false;
    public static MediaRecorder startRecording(Camera mCamera, Activity activity){
        if (isRecording){
            return null;
        }
        mRecorder = initMediaRecorder( mCamera);
        mRecorder.start();
        mRecorder.setOnInfoListener(mOnInfoListener);
        isRecording = true;

        return mRecorder;
    }

    public static void stopRecording(MediaRecorder mRecorder,Camera mCamera){
        Log.w("TAG","stopRecording width ");
        isRecording = false;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        if (mCamera != null){
            mCamera.stopPreview();
//            mCamera.release();
            mCamera = null;
        }

    }

    private static  MediaRecorder.OnInfoListener mOnInfoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.w("TAG","stopRecording width what = "+what);
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                stopRecording(mRecorder,mCamera);
            }
        }
    };
}
