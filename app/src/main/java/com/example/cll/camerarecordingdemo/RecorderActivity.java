package com.example.cll.camerarecordingdemo;

import android.app.Dialog;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cll.camerarecordingdemo.constant.RecorderType;
import com.example.cll.camerarecordingdemo.utils.CameraUtils;
import com.example.cll.camerarecordingdemo.utils.DisplayUtils;

import java.util.ArrayList;

public class RecorderActivity extends AppCompatActivity {


    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Button mResolution;
    private ListView listView;
    private String recorderType;
    private EditText duration_edit;
    private TextView duration_text;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIntents();
        initWidgets();
        initListener();
    }

    private void getIntents(){
        Intent intetnt = getIntent();
        recorderType = intetnt.getStringExtra(RecorderType.STATUS.getValue());
    }
    private void initWidgets(){
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        duration_edit = findViewById(R.id.duration_edit);
        duration_text = findViewById(R.id.duration_text);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(callback);

//        FrameLayout.LayoutParams linearParams =(FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
//        linearParams.height = linearParams.width;
//        linearParams.width = linearParams.height;
//        RotateAnimation rotateAnimation = new RotateAnimation(0f,90f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5F);
//        rotateAnimation.setFillAfter(true);
//        mSurfaceView.setLayoutParams(linearParams);
//        mSurfaceView.startAnimation(rotateAnimation);
    }

    private Camera mCamera;
    private MediaRecorder mRecorder;
    private void initListener(){
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mCamera.setDisplayOrientation(180);
//                mCamera.startPreview();
                mRecorder = CameraUtils.startRecording(mCamera,RecorderActivity.this);

            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder == null){
                    return;
                }
                CameraUtils.stopRecording(mRecorder,mCamera);
            }
        });
        findViewById(R.id.btn_resolution).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCamera == null){
                    return;
                }
                Dialog dialog = new Dialog(RecorderActivity.this);
                View view = LayoutInflater.from(RecorderActivity.this).inflate(R.layout.dialog_layout, null);
                listView = (ListView) view.findViewById(R.id.listview);
                ArrayList<SizeData> data = new ArrayList<>();

                DataAdapter adapters = new DataAdapter(RecorderActivity.this, DisplayUtils.getResolution(mCamera,data));
                listView.setAdapter(adapters);
                dialog.setContentView(view);
                Window dialogWindow = dialog.getWindow();
                dialogWindow.setGravity( Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.y = 20;
                dialogWindow.setAttributes(lp);
                dialog.show();
            }
        });
        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duration_edit.getVisibility() == View.VISIBLE){
                    duration_edit.setVisibility(View.GONE);
                    duration_text.setVisibility(View.VISIBLE);
                    duration_text.setText(duration_edit.getText());
                }else{
                    duration_edit.setVisibility(View.VISIBLE);
                    duration_edit.setText(duration_text.getText());
                    duration_text.setVisibility(View.GONE);
                }
            }
        });
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera = CameraUtils.getCamera(RecorderActivity.this,mHolder,recorderType);
            if (mCamera != null){
                mCamera.startPreview();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null){
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    };







    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }

    }
}
