package com.example.cll.camerarecordingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.example.cll.camerarecordingdemo.constant.RecorderType;
import com.example.cll.camerarecordingdemo.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by cll on 2017/12/7.
 */

public class CameraUtils {

    private static Camera.Size size;
    private static Camera mCamera ;
    public static Camera getCamera(Context context, SurfaceHolder mHolder, final String recorderType){

        try {
            if (mCamera != null){
                mCamera.release();
                mCamera = null;
            }

            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
            size = mCamera.getParameters().getSupportedPreviewSizes().get(0);
            Camera.Parameters parameter = mCamera.getParameters();
            parameter.setPreviewSize(size.width,size.height);
            parameter.setPictureSize(size.width,size.height);
            parameter.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(parameter);

            Log.w("tag", "test recorderType = " + recorderType);
            if (TextUtils.equals(recorderType, RecorderType.PREVIEW.getValue())){
                mCamera.setPreviewDisplay(mHolder);
            }else if (TextUtils.equals(recorderType, RecorderType.NO_PREVIEW.getValue())){
                SurfaceTexture surface = new SurfaceTexture(0);
                mCamera.setPreviewTexture(surface);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.w("TAG","test getCamera  IOException = ");
        }
        return mCamera;
    }

    private static String name = "VIDEO_"+System.currentTimeMillis()+".mp4";
    private static String dir = "minecraft";
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dir + File.separator + name;
    private static MediaRecorder initMediaRecorder(Camera mCamera){
        if (mCamera == null){
            return null;
        }
        try {
            MediaRecorder mRecorder = new MediaRecorder();
            mCamera.unlock();  //start-failed-19
            mRecorder.setCamera(mCamera);
            mRecorder.setOrientationHint(90);
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // need mic permission
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  // need camera permission
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mRecorder.setAudioChannels(2);
//            mRecorder.setMaxDuration(10000);  // recording max time
            mRecorder.setVideoSize(640, 480);
            mRecorder.setVideoFrameRate(15);
            mRecorder.setVideoEncodingBitRate(640 * 480 * 15 / 3); //width * height * frameRate
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
        path = FileUtils.createFile(path);
        if (path == null){
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


//        if (mCamera != null){
//            mCamera.stopPreview();
////            mCamera.release();  //release后Camera要重新开启  不适合重复录像
//            mCamera = null;
//        }

    }

    private static  MediaRecorder.OnInfoListener mOnInfoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.w("TAG","stopRecording width what = "+what);
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                //mediarecorder录制时间到
                stopRecording(mRecorder,mCamera);
            }
        }
    };
}
