package com.zsf.m_camera.manager;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.text.TextUtils;

import com.google.android.cameraview.CameraView;

import java.io.IOException;

/**
 * Created by wangyongli on 2018/1/11.
 */

public class MediaRecorderController implements IMediaTelecontroller {

    private ITelecontroller telecontroller;
    private MediaRecorder mediaRecorder;
    private Camera camera;
    public MediaRecorderController(ITelecontroller telecontroller){
        this.telecontroller = telecontroller;
    }

    @Override
    public void start() {
        startRecording();
    }

    @Override
    public void stop() {
        try {
            stopRecording();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void pause() {
        pauseRecording();
    }

    @Override
    public void resume() {
        resumeRecording();
    }

    @Override
    public boolean isStart() {
        return telecontroller.isStart();
    }

    @Override
    public boolean isResume() {
        return telecontroller.isResume();
    }

    @Override
    public boolean stopEnable() {
        return telecontroller.stopEnable();
    }


    private boolean prepareMediaRecorder(CameraView cameraView, String path) {
        if (TextUtils.isEmpty(path)) return false;
        if (mediaRecorder != null) return true;
        this.camera = cameraView.getCamera();

        mediaRecorder = new MediaRecorder();
        int facing = cameraView.getFacing();
        mediaRecorder.setOrientationHint(facing == CameraView.FACING_FRONT ? 270 : 90);
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
        if (camera != null) {
            this.camera.setDisplayOrientation(90);
            camera.unlock();
            mediaRecorder.setCamera(camera);
        }
        int w = profile.videoFrameWidth;
        int h = profile.videoFrameHeight;
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(profile);
        mediaRecorder.setPreviewDisplay(cameraView.getSurface());
        mediaRecorder.setOutputFile(path);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            releaseMediaRecorder();
            e.printStackTrace();
        }
        return mediaRecorder != null;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            if (camera != null) {
                camera.lock();
            }
        }
    }
    public void startRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.start();
        }
        if (telecontroller != null) {
            telecontroller.start();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
        }
        if (telecontroller != null) {
            telecontroller.stop();
        }
        releaseMediaRecorder();
    }

    public void pauseRecording(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return;
        if (mediaRecorder != null) {
            mediaRecorder.pause();
        }
        if (telecontroller != null) {
            telecontroller.pause();
        }
    }

    public void resumeRecording(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return;
        if (mediaRecorder != null) {
            mediaRecorder.resume();
        }
        if (telecontroller != null) {
            telecontroller.resume();
        }
    }

    @Override
    public void startRecord(CameraView cameraView, String path) {
        if (!telecontroller.isStart() && prepareMediaRecorder(cameraView, path)){
            startRecording();
        }
    }

    @Override
    public void setOrientationHint(int orientationHint) {

    }
}
