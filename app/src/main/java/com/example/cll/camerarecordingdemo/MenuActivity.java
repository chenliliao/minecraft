package com.example.cll.camerarecordingdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cll.camerarecordingdemo.constant.AudioFormat;
import com.example.cll.camerarecordingdemo.constant.RecorderType;
import com.example.cll.camerarecordingdemo.utils.AudioConverterUtils;
import com.example.cll.camerarecordingdemo.utils.CallbackUtils;

/**
 * Created by cll on 2017/12/10.
 */

public class MenuActivity extends Activity {


    private Button recording_surfaceview;
    private Button recording_surfacetexture;
    private Button btn_audio_convert;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        up6Premission(this);
        initWidgets();
        initListener();
    }

    private void initWidgets(){
        recording_surfaceview = findViewById(R.id.btn_surfaceview_recording);
        recording_surfacetexture = findViewById(R.id.btn_surfacetexture_recording);
        btn_audio_convert = findViewById(R.id.btn_audio_convert);

    }

    private void initListener(){
        recording_surfaceview.setOnClickListener(mSurfaceviewListener);
        recording_surfacetexture.setOnClickListener(mSurfacetextureListener);
        btn_audio_convert.setOnClickListener(mAudioConvert);
    }

    private View.OnClickListener mSurfaceviewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, RecorderActivity.class);
            intent.putExtra(RecorderType.STATUS.getValue(),RecorderType.PREVIEW.getValue());
            MenuActivity.this.startActivity(intent);
        }
    };

    private View.OnClickListener mSurfacetextureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuActivity.this, RecorderActivity.class);
            intent.putExtra(RecorderType.STATUS.getValue(),RecorderType.NO_PREVIEW.getValue());
            MenuActivity.this.startActivity(intent);
        }
    };

    private View.OnClickListener mAudioConvert = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /***
             *    prepare
             * 1.asset 添加armeabi-v7a文件夹，导入ffmpeg文件
             * 2.添加ARM_ARCH的so库
             * 3.导入ffmpeg-android-java.jar
             */

            AudioConverterUtils.Builder()
                    .load(MenuActivity.this)
                    .setAudioFormat(AudioFormat.AAC)
                    .setAudioSource(Environment.getExternalStorageDirectory().getPath() + "/360/2017-12-28_17-19-03_audio.mp3")
                    .convert();
            AudioConverterUtils.setConcertListener(new CallbackUtils.ConvertCallback() {
                @Override
                public void loadStatus(boolean isSuccess) {
                    Log.w("tag", "test setConcertListener 1"+isSuccess);
                }

                @Override
                public void isSuccess(boolean isSuccess) {
                    Log.w("tag", "test setConcertListener 2"+isSuccess);
                }
            });
            ;
        }
    };



    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO };
    private static void up6Premission(Activity activity){
        try {
            //6.0以上 检测是否有写的权限
            int permission1 = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission2 = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permission3 = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission1 != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED && permission3 !=PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
