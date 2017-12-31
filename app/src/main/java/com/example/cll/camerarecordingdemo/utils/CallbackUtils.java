package com.example.cll.camerarecordingdemo.utils;

/**
 * Created by cll on 2017/12/29.
 */

public class CallbackUtils {

    public static interface ConvertCallback{
        void loadStatus(boolean isSuccess);
        void isSuccess(boolean isSuccess);
    }
}
