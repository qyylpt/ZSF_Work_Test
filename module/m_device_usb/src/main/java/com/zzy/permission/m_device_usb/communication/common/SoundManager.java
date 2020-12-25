package com.zzy.permission.m_device_usb.communication.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.UiThread;

import java.io.IOException;

/**
 * @author : zsf
 * @date   : 2020/10/13 9:52 AM
 * @desc   : 声音管理
 */
public class SoundManager {

    private int mCurVolume;
    private AudioManager mAM;
    private MediaPlayer mMP;

    private final Context mContext;

    public SoundManager(Context context) {
        mContext = context;
    }

    @UiThread
    public synchronized void startPlayRing(String playerFile) {
        if (null == mMP) {
            prepareRingtoneInternal(mContext, playerFile);
        } else {
            try {
                mMP.reset();
                aftermath(mContext, playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @UiThread
    public void stopPlayRing() {
        if (mMP != null) {
            mMP.stop();
            mMP.release();
            mMP = null;
        }
        if (mAM != null) {
            mAM.setStreamVolume(AudioManager.STREAM_MUSIC, mCurVolume, 0);
            mAM = null;
        }
    }

    /**
     * 回收资源
     */
    public void release() {
        stopPlayRing();
    }

    private void prepareRingtoneInternal(Context context, String playerFile) {
        try {
            mAM = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = mAM.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mCurVolume = mAM.getStreamVolume(AudioManager.STREAM_MUSIC);
            mAM.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
            mMP = new MediaPlayer();
            mMP.setLooping(false);
            mMP.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMP.setVolume(1f, 1f);
            aftermath(context, playerFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void aftermath(Context context, String playerFile) throws IOException {
        String strUri = "android.resource://" + context.getPackageName() + "/" + playerFile;
        mMP.setDataSource(context, Uri.parse(strUri));
        mMP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMP.start();
            }
        });
        mMP.prepare();
        mMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayRing();
            }
        });
    }
}
