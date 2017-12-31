package com.example.cll.camerarecordingdemo.utils;

import android.content.Context;
import android.util.Log;

import com.example.cll.camerarecordingdemo.constant.AudioFormat;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.IOException;

/**
 * Created by cll on 2017/12/29.
 */

public class AudioConverterUtils {

    static {
        System.loadLibrary("ARM_ARCH");
    }

    public static Convert Builder(){
        return new Convert();
    }

    private static CallbackUtils.ConvertCallback callback;
    public static class Convert{
        private String audioPath;
        private AudioFormat format;
        private Context context;
        public Convert(){
        }
        public Convert setAudioSource(String audioPath){
            this.audioPath = audioPath;
            return this;
        }
        public Convert setAudioFormat(AudioFormat format){
            this.format = format;
            return this;
        }
        public Convert load(final Context context){
            try {
                this.context = context;
                FFmpeg.getInstance(context).loadBinary(new FFmpegLoadBinaryResponseHandler() {
                    @Override
                    public void onFailure() {
                        if (callback != null){
                            callback.loadStatus(false);
                        }
                    }
                    @Override
                    public void onSuccess() {
                        if (callback != null){
                            callback.loadStatus(true);
                        }
                    }
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onFinish() {
                    }
                });
            } catch (FFmpegNotSupportedException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Convert convert(){
            try {
                final File convertedFile = getConvertedFile(new File(audioPath), format);
                final String[] cmd = new String[]{"-y", "-i", audioPath, convertedFile.getPath()};
                FFmpeg.getInstance(context).execute(cmd, new FFmpegExecuteResponseHandler() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onProgress(String message) {
                    }
                    @Override
                    public void onSuccess(String message) {
                        if (callback != null){
                            callback.isSuccess(true);
                        }
                    }
                    @Override
                    public void onFailure(String message) {
                        if (callback != null){
                            callback.isSuccess(false);
                        }
                    }
                    @Override
                    public void onFinish() {
                    }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                e.printStackTrace();
            }
            return this;
        }
    }

    public static void setConcertListener(CallbackUtils.ConvertCallback callbacks){
        callback = callbacks;
    }


    private static File getConvertedFile(File originalFile, AudioFormat format){
        String[] f = originalFile.getPath().split("\\.");
        String filePath = originalFile.getPath().replace(f[f.length - 1], format.getFormat());
        Log.w("TAG","test convertedFile = "+format.getFormat());
        return new File(filePath);
    }

}
