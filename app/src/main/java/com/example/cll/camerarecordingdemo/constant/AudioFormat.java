package com.example.cll.camerarecordingdemo.constant;

/**
 * Created by cll on 2017/12/29.
 */

public enum AudioFormat {
    AAC,
    MP3,
    M4A,
    WMA,
    WAV,
    FLAC;
    public String getFormat(){
        //小写
        return name().toLowerCase();
    }
}
