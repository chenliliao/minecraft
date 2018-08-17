package com.example.cll.camerarecordingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cll.camerarecordingdemo.constant.AudioFormat;
import com.example.cll.camerarecordingdemo.constant.RecorderType;
import com.example.cll.camerarecordingdemo.utils.AudioConverterUtils;
import com.example.cll.camerarecordingdemo.utils.CallbackUtils;
import com.example.cll.camerarecordingdemo.utils.DynamicPermissionUtils;
import com.example.cll.camerarecordingdemo.utils.RecordUtil;

/**
 * Created by cll on 2017/12/10.
 */

public class MenuActivity extends AppCompatActivity {


    private Button recording_surfaceview;
    private Button recording_surfacetexture;
    private Button btn_audio_convert;
    private Button btn_audio_record;
    private Button btn_video_record;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        DynamicPermissionUtils.up6Premission(this);
        initWidgets();
        initListener();
    }

    private void initWidgets(){
        recording_surfaceview = findViewById(R.id.btn_surfaceview_recording);
        recording_surfacetexture = findViewById(R.id.btn_surfacetexture_recording);
        btn_audio_convert = findViewById(R.id.btn_audio_convert);
        btn_audio_record = findViewById(R.id.btn_audio_record);
        btn_video_record = findViewById(R.id.btn_video_record);

    }

    private void initListener(){
        recording_surfaceview.setOnClickListener(mSurfaceviewListener);
        recording_surfacetexture.setOnClickListener(mSurfacetextureListener);
        btn_audio_convert.setOnClickListener(mAudioConvert);
        btn_audio_record.setOnClickListener(mAudioRecord);
        btn_video_record.setOnClickListener(mVideoRecord);
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
//            RotateAnimation rotateAnimation = new RotateAnimation(0f,90f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5F);
//            rotateAnimation.setFillAfter(true);
//            recording_surfaceview.startAnimation(rotateAnimation);
//            recording_surfaceview.startAnimation(AnimationUtils.loadAnimation(MenuActivity.this,R.anim.surface));
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

    private boolean isRecording = false;
    private View.OnClickListener mAudioRecord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isRecording){
                RecordUtil.start();
                isRecording = true;
                Toast.makeText(MenuActivity.this, "start", Toast.LENGTH_SHORT).show();
            }else {
                RecordUtil.stop();
                isRecording = false;
                Toast.makeText(MenuActivity.this, "stop", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private View.OnClickListener mVideoRecord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//           startActivity(new Intent(MenuActivity.this, DecodeActivity.class));
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id){
            case R.id.setting_0:
                Toast.makeText(this, "setting_0", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting_1:
                Toast.makeText(this, "setting_1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting_2:
                Toast.makeText(this, "setting_2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting_3:
                Toast.makeText(this, "setting_3", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
