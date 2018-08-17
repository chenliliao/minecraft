package com.example.cll.camerarecordingdemo.utils;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaMuxerWrapper {
    private static final String TAG = "MediaMuxerWrapper";
    private MediaMuxer mMediaMuxer;
    private boolean muxerStarted = false;
    public static boolean isInstance  = false;
    public MediaMuxerWrapper(String filepath) {
        setMuxer(filepath);
    }
    public synchronized void setMuxer(String filepath) {
        try {
            mMediaMuxer = new MediaMuxer(filepath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            Log.d(TAG,"MediaMuxerWrapper--"+mMediaMuxer);
            isInstance = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isStarted() {
        Log.d(TAG,"isStarted");
        return muxerStarted;
    }

    public synchronized boolean start() {
        mMediaMuxer.start();
        muxerStarted = true;
        return muxerStarted;
    }

    public synchronized void stop() {
        mMediaMuxer.stop();
        mMediaMuxer.release();
        muxerStarted = false;
    }
    public synchronized int addTrack(MediaFormat format) {
        if(muxerStarted) {
            throw new IllegalStateException("muxer already started");
        }
        int trackIndex = mMediaMuxer.addTrack(format);
        return trackIndex;
    }

    public synchronized void writeSampleData(int trackIndex, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        mMediaMuxer.writeSampleData(trackIndex,byteBuffer,bufferInfo);
    }

    public synchronized void release() {
        if(muxerStarted) {
            if(mMediaMuxer!=null) {
                mMediaMuxer.stop();
                mMediaMuxer.release();
            }
        }
    }

}
