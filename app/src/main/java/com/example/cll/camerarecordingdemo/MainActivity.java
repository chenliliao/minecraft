package com.example.cll.camerarecordingdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private SurfaceView mSurfaceView;
    private Button mResolution;
    private ListView listView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        up6Premission(this);
        initWidgets();
        initListener();
    }



    private void initWidgets(){
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);

        mCamera = CameraUtils.getCamera(MainActivity.this,null);
        if (mCamera != null){
            mCamera.startPreview();
        }

//        mResolution = (Button) findViewById(R.id.btn_resolution);
    }

    private Camera mCamera;
    private MediaRecorder mRecorder;
    private void initListener(){
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder = CameraUtils.startRecording(mCamera,MainActivity.this);

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
                Dialog dialog = new Dialog(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_layout, null);
                listView = (ListView) view.findViewById(R.id.listview);
                ArrayList<SizeData> data = new ArrayList<>();

                Log.w("TAG","TEST DisplayUtils.getResolution(mCamera,data)  "+DisplayUtils.getResolution(mCamera,data).size());
                DataAdapter adapters = new DataAdapter(MainActivity.this,DisplayUtils.getResolution(mCamera,data));
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
    }






    private static void up6Premission(Activity activity){
        try {
            //检测是否有写的权限
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }

    }
}
