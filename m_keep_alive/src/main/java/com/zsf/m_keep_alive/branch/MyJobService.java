package com.zsf.m_keep_alive.branch;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.zsf.global.GlobalData;
import com.zsf.m_keep_alive.KeepAliveMainActivity;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/8/20
 */
@SuppressLint("NewApi")
public class MyJobService extends JobService {
    private JobScheduler jobScheduler;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ZsfLog.d(MyJobService.class, "JobScheduler 服务被创建");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getPackageName(), MyJobService.class.getName()))
                    // 是否需要充电
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    // 设备重启
                    .setPersisted(true);
            if (jobScheduler.schedule(builder.build()) <= 0){
                ZsfLog.d(MyJobService.class, "JobScheduler 任务执行失败");
            } else {
                ZsfLog.d(MyJobService.class, "JobScheduler 任务执行成功");
            }
        }
        return START_STICKY;
    }

    /**
     * 返回false 表明任务执行完毕，系统解绑jobService，最终调用JobService的onDestroy效果等同于jobFinished()
     * 返回true 表明任务启动成功，凡是没有做做完，需要应用自行调用jobFinished()
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        ZsfLog.d(MyJobService.class, "JobScheduler onStartJob");
        ToastUtils.showToast(GlobalData.getContext(), "JobScheduler onStartJob");
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
        ZsfLog.d(MyJobService.class, "JobScheduler onStopJob");
        ToastUtils.showToast(GlobalData.getContext(), "JobScheduler onStopJob");
        return false;
    }

}
