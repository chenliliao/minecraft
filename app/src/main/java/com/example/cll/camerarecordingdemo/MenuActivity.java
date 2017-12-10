package com.example.cll.camerarecordingdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.example.cll.camerarecordingdemo.constant.RecorderType;

/**
 * Created by cll on 2017/12/10.
 */

public class MenuActivity extends Activity {


    private Button recording_surfaceview;
    private Button recording_surfacetexture;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        up6Premission(this);
        initWidgets();
        initListener();
    }

    private void initWidgets(){
        recording_surfaceview = (Button) findViewById(R.id.btn_surfaceview_recording);
        recording_surfacetexture = (Button) findViewById(R.id.btn_surfacetexture_recording);

    }

    private void initListener(){
        recording_surfaceview.setOnClickListener(mSurfaceviewListener);
        recording_surfacetexture.setOnClickListener(mSurfacetextureListener);
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
