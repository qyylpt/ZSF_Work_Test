package com.zsf.m_keep_alive.branch;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/10/31
 * @Usage
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SettingJobService extends JobService {
    /**
     * 来自于activity的messenger 用于跨进程通信
     */
    private Messenger fromActivityMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        ZsfLog.d(SettingJobService.class, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZsfLog.d(SettingJobService.class, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fromActivityMessenger = intent.getParcelableExtra(JobSchedulerActivity.MESSENGER_INTENT_KEY);
        return START_NOT_STICKY;
    }

    /**
     * 返回false 表明任务执行完毕，系统解绑jobService，最终调用JobService的onDestroy效果等同于jobFinished()
     * 返回true 表明任务启动成功，凡是没有做做完，需要应用自行调用jobFinished()
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        ZsfLog.d(SettingJobService.class, "onStartJob -> jobParameters = " + jobParameters.getJobId());
        // 该服务做得工作只是等待一定的持续的时间并完成作业（在另外一个进程）
        sendMessage(JobSchedulerActivity.MSG_JOB_START, jobParameters.getJobId());

        // 当然这里可以处理一些其他的业务

        // 获取在activity里面设置的每个任务的周期
        long duration = jobParameters.getExtras().getLong(JobSchedulerActivity.WORK_DURATION_KEY);

        Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMessage(JobSchedulerActivity.MSG_JOB_STOP, jobParameters.getJobId());
                jobFinished(jobParameters, true);
            }
        }, duration);

        // 返回true，很多工作都会执行这个地方，我们手动结束这个任务
        return true;
    }

    /**
     * 任务中断时调用,不满足执行条件的时候的中断(比如需要在充电时运行，但是在JobFinished()之前拔掉充电器，onStopJob方法会被调用)
     * 返回true: 下次继续执行
     * 返回false: 下次不再执行
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        sendMessage(JobSchedulerActivity.MSG_JOB_STOP, jobParameters.getJobId());
        ZsfLog.d(SettingJobService.class, "onStopJob -> jobParameters = " + jobParameters.toString());
        return true;
    }

    private void sendMessage(int messageID, Object params){
        // 如果此服务是有JobScheduler启动,则没有回调Messenger
        // 它仅在由JobSchedulerActivity在Intent中使用回调函数中使用函数调用startService时存在
        if (fromActivityMessenger == null){
            ZsfLog.d(SettingJobService.class, "SettingJobService 是有 JobScheduler 启动");
            return;
        }
        Message m = Message.obtain();
        m.what = messageID;
        m.obj = params;
        try {
            fromActivityMessenger.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
