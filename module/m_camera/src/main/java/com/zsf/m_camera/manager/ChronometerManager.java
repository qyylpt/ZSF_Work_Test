package com.zsf.m_camera.manager;

import android.os.SystemClock;
import android.widget.Chronometer;


/**
 * @author zhangzhang
 */
public class ChronometerManager implements Chronometer.OnChronometerTickListener, ITelecontroller {
    
    private Chronometer chronometer;
    private int recordTime;
    private int pauseTime;
    private boolean isResume = false;
    private boolean isStart = false;
    private int maxTime;
    private RecordListener recordListener;
    public ChronometerManager(Chronometer chronometer, int maxTime, RecordListener recordListener){
        this.maxTime = maxTime;
        this.recordListener = recordListener;
        this.chronometer = chronometer;
        chronometer.setOnChronometerTickListener(this);
    }

    public ChronometerManager(Chronometer chronometer, int maxTime){
        this.maxTime = maxTime;
        this.chronometer = chronometer;
        chronometer.setOnChronometerTickListener(this);
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        long base = chronometer.getBase();
        long realTime = SystemClock.elapsedRealtime();
        recordTime = (int) (realTime - base) / 1000;
        int lastTime = (recordTime + pauseTime) % 60;
        if (lastTime < maxTime) {
            chronometer.setText(getStringTime(recordTime + pauseTime));
        }
        else {
            if (recordListener != null) {
                recordListener.onStopRecord(lastTime);
            }
        }
    }



    private String getStringTime(int cnt) {
        int hour = cnt/3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format("%02d:%02d:%02d",hour,min,second);
    }
    @Override
    public void pause(){
        isResume = false;
        pauseTime += recordTime;
        chronometer.stop();
    }
    @Override
    public void resume(){
        isResume = true;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @Override
    public void start(){
        isStart = true;
        isResume = true;
        recordTime = 0;
        pauseTime = 0;
        resume();
    }
    @Override
    public void stop(){
        isStart = false;
        isResume = false;
        pause();
    }
    @Override
    public boolean isStart(){
        return isStart;
    }

    @Override
    public boolean isResume() {
        return isResume;
    }

    @Override
    public boolean stopEnable() {
        return (recordTime + pauseTime) % 60 > 2;
    }

    public interface RecordListener {
        void onStartRecord();
        void onStopRecord(int time);
    }

}
