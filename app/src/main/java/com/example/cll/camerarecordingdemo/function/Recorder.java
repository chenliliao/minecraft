package com.example.cll.camerarecordingdemo.function;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cll on 2018/8/15.
 */

public class Recorder {

    private static final String TAG = "Recorder";
    private Recorder mManager;


    public static MediaCodec getAudioMediaCodec() {
        MediaCodec mAudioCodec = null;
        MediaFormat mAudioFormat = null;
        try {
            if (mAudioFormat == null){
                mAudioFormat = new MediaFormat();
            }
            mAudioFormat.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC);
            mAudioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mAudioFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE,44100);
            mAudioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 125000);
            mAudioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 2);
            mAudioCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.w(TAG,"test test e " + e.getMessage());
        }
        mAudioCodec.configure(mAudioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mAudioCodec.start();
        return mAudioCodec;
    }

//    public static MediaCodec getVideoMediaCodec(Surface surface){
//        MediaCodec mVideoCodec = null;
//        MediaFormat mVideoFormat = null;
//        try {
//            mVideoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 1280, 720);
//
//            mVideoCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        mVideoCodec.configure(mVideoFormat, surface, null, 0);
//        mVideoCodec.start();
//        return mVideoCodec;
//    }

//************************************   audio   *******************************
    public static class Audio{

        private ExecutorService sExecutorService;
        private MediaCodec sMediaCodec;
        private boolean isRecording = false;
        private MediaCodec.BufferInfo sAudioBufferInfo;
        private String sPath;
        private MediaMuxerAide sMuxerAide;
        private int sTrackIndex;
        private long startTime;
        private long startTimeTag = 0;

        public Audio setMediaCodec(MediaCodec mediaCodec){
            sMediaCodec = mediaCodec;
            return this;
        }

        public Audio setOutputPath(String path){
            sPath = path;
            return this;
        }

        public Audio create(){
            sTrackIndex = 0;
            isRecording = false;
            sAudioBufferInfo = new MediaCodec.BufferInfo();
            if (sExecutorService == null){
                sExecutorService = Executors.newSingleThreadExecutor();
            }
            sMuxerAide = new MediaMuxerAide(sPath);
            isRecording = true;
            return this;
        }

        public void encode(final byte[] input){
            if (!sExecutorService.isShutdown()){
                sExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        immitBuffer(input, System.nanoTime());
                        drainEncoder();
                    }
                });
            }
        }

        public void decode(final byte[] input){
            if (!sExecutorService.isShutdown()){
                sExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        immitBuffer(input, System.nanoTime());
                    }
                });
            }
        }


        public void stop(){
            isRecording = false;
        }

//      pts:  Presentation Time Stamp
        private void immitBuffer(final byte[] input, final long pts){

            if (startTimeTag == 0){
                startTime = pts;
            }
            startTimeTag += input.length;
            long presentationTimeUs = 0;
            ByteBuffer[] inputBuffer = sMediaCodec.getInputBuffers();
            int encoderInIndex = sMediaCodec.dequeueInputBuffer(-1);
            if (encoderInIndex >= 0){
                ByteBuffer data = inputBuffer[encoderInIndex];
                data.clear();
                if (input != null){
                    data.put(input);
                }
                presentationTimeUs = (pts - startTime) / 1000;
                if (isRecording){
                    sMediaCodec.queueInputBuffer(encoderInIndex, 0, input.length, presentationTimeUs, 0);
                }else{
                    sMediaCodec.queueInputBuffer(encoderInIndex, 0, input.length, presentationTimeUs, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    sMuxerAide.stop();
                    sExecutorService.shutdown();
                }

            }
        }

        private void drainEncoder(){
            ByteBuffer[] outputBuffer = sMediaCodec.getOutputBuffers();
            while (true){
                int encoderOutIndex = sMediaCodec.dequeueOutputBuffer(sAudioBufferInfo, 100);
               if (encoderOutIndex == MediaCodec.INFO_TRY_AGAIN_LATER){
                   if (isRecording){
                       break;
                   }
                }else if (encoderOutIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
                   if (sMuxerAide.isStart()){
                       throw new IllegalStateException("muxer has started");
                   }
                    MediaFormat mediaFormat = sMediaCodec.getOutputFormat();
                   sTrackIndex = sMuxerAide.addTrack(mediaFormat);
                   sMuxerAide.start();
                }else if (encoderOutIndex < 0 ){

                }else {
                    ByteBuffer data = outputBuffer[encoderOutIndex];
                    if (data.hasArray()){
                        Log.w("tag","TEST TEST data.array() =            " + data.array());
                    }
                    if (data != null && sAudioBufferInfo.size != 0){
                        data.position(sAudioBufferInfo.offset);
                        data.limit(sAudioBufferInfo.offset + sAudioBufferInfo.size);
                        sMuxerAide.writeSampleData(sTrackIndex, data, sAudioBufferInfo);
                    }
                   sMediaCodec.releaseOutputBuffer(encoderOutIndex, false);
                    if((sAudioBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM)!=0) {
                        break;
                    }
                }
            }
        }
    }

    //************************************   audio  end  *******************************

    //************************************   video  start  *******************************

    public static class Video{

        private MediaCodec mMediaCodec;

        public void encode(){
            ByteBuffer[] inputBuffer = mMediaCodec.getInputBuffers();
            ByteBuffer[] outputBuffer = mMediaCodec.getOutputBuffers();
            int inBufferIndex = mMediaCodec.dequeueInputBuffer(100);
            if (inBufferIndex >= 0){
                ByteBuffer byteBuffer = inputBuffer[inBufferIndex];

            }
        }

    }

    private static class MediaMuxerAide{

        private MediaMuxer mMediaMuxer;
        private boolean isStart;
        public MediaMuxerAide(String path){
            try {
                isStart = false;
                mMediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isStart(){
            return isStart;
        }

        public synchronized void start(){
            if (!isStart){
                mMediaMuxer.start();
                isStart = true;
            }
        }

        public synchronized void stop(){
            if (isStart){
                if (mMediaMuxer != null){
                    mMediaMuxer.stop();
                    mMediaMuxer.release();
                    mMediaMuxer = null;
                    isStart = false;
                }
            }

        }

        public synchronized int addTrack(MediaFormat format){
            if (isStart){
                throw new IllegalStateException("muxer already started");
            }
            int trackIndex = mMediaMuxer.addTrack(format);
            return trackIndex;
        }

        public synchronized void writeSampleData(int trackIndex, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo){
            mMediaMuxer.writeSampleData(trackIndex,byteBuffer,bufferInfo);
        }
    }
}
