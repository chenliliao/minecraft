package com.example.cll.camerarecordingdemo.utils;

import android.hardware.Camera;

import com.example.cll.camerarecordingdemo.SizeData;

import java.util.ArrayList;

/**
 * Created by cll on 2017/12/8.
 */

public class DisplayUtils {

    public static ArrayList<SizeData> getResolution(Camera mCamera, ArrayList<SizeData> list){
        for (Camera.Size size : mCamera.getParameters().getSupportedPreviewSizes()) {
            SizeData data = new SizeData();
            data.setWidth(size.width);
            data.setHeight(size.height);
            list.add(data);
        }
        return list;
    }
}
