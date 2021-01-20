package com.zzy.permission.m_device_usb.communication.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.tts.TextToSpeech;

import androidx.annotation.UiThread;

import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;
import com.zzy.permission.m_device_usb.R;

import java.io.IOException;
import java.util.Locale;
import java.util.Queue;

/**
 * @author : zsf
 * @date   : 2020/10/13 9:52 AM
 * @desc   : 声音管理
 */
public class SoundManager implements TextToSpeech.OnInitListener{

    private int mCurVolume;
    private AudioManager mAM;
    private MediaPlayer mMP;

    private final Context mContext;

    private final TextToSpeech textToSpeech;

    public SoundManager(Context context) {
        mContext = context;
        textToSpeech = new TextToSpeech(context, this);
        textToSpeech.setPitch(0.2f);
        textToSpeech.setSpeechRate(0.9f);
    }

    @UiThread
    public synchronized void startPlayRing(int playerFileId) {
        if (null == mMP) {
            prepareRingtoneInternal(mContext, String.valueOf(playerFileId));
        } else {
            try {
                mMP.reset();
                aftermath(mContext, String.valueOf(playerFileId));
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
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                ZsfLog.d(SoundManager.class, "设备不支持自定义语音播报!" + "; result = " + result);
            }
        }
    }

    /**
     * 在需要多音字发声的地方这么写：
     * 比如朝[=chao2]阳区
     * @param playingInfo
     */
    public void startPlayRing(String playingInfo) {
        textToSpeech.stop();
        if (textToSpeech != null) {
            textToSpeech.speak(playingInfo, TextToSpeech.QUEUE_FLUSH, null, playingInfo + System.currentTimeMillis());
        }
    }

}
