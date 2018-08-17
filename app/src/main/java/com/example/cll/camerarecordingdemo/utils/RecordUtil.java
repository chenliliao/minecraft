package com.example.cll.camerarecordingdemo.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Environment;

import com.example.cll.camerarecordingdemo.function.Recorder;

import java.nio.ByteBuffer;


/**
 * Created by cll on 2018/8/15.
 */

public class RecordUtil {

    private static String MIME_TYPE_AVC = MediaFormat.MIMETYPE_VIDEO_AVC;
    private static String MIME_TYPE_AUDIO_AVC = MediaFormat.MIMETYPE_AUDIO_AAC;
    private static MediaCodec encoder = null;
    private static String path = Environment.getExternalStorageDirectory() + "/007/muxer.mp3";
    private static AudioRecord audioRecord;
    public static boolean isRecording = false;

    public static void start(){
        setAudiaRecod();
    }
    public static void stop(){
        isRecording = false;
        if (audio != null){
            audio.stop();
        }
        release();
    }

    private static Recorder.Audio audio;
    private static int mReadResult;
    private static void setAudiaRecod(){
        int minBufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize);
        audioRecord.startRecording();
        if (isRecording) {
            return;
        }
        isRecording = true;
        encode();
    }

    private static void encode(){
        final MediaCodec mediaCodec = Recorder.getAudioMediaCodec();
        audio = new Recorder.Audio()
                .setMediaCodec(mediaCodec)
                .setOutputPath(path)
                .create();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRecording){
                    byte[] buffer = new byte[1024];
                    if (audioRecord != null){
                        mReadResult = audioRecord.read(buffer, 0, 1024);
                        if (mReadResult >= 0){
                            audio.encode(buffer);
                        }
                    }
                }
            }
        }).start();
    }

    public static void release(){

        if (audioRecord != null){
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (encoder != null){
            encoder.stop();
            encoder.release();
            encoder = null;
        }

    }
}
