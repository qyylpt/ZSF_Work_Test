package com.zsf.m_keep_alive.branch;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.m_keep_alive.R;
import com.zsf.utils.ToastUtils;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * @author zsf
 * @date 2019/10/31
 */
@Route(path = "/keep_alive/JobSchedulerActivity")
public class JobSchedulerActivity extends BaseActivity {
    /**
     * 开启JobScheduler任务
     */
    private Button buttonDoJobScheduler;

    /**
     * 跨进程通信messenger key
     */
    public static final String MESSENGER_INTENT_KEY = JobSchedulerActivity.class.getSimpleName() + ".MESSENGER_INTENT_KEY";

    /**
     * 任务执行周期 key
     */
    public static final String WORK_DURATION_KEY = JobSchedulerActivity.class.getSimpleName() + "WORK_DURATION_KEY";

    /**
     * 任务开始执行
     */
    public static final int MSG_JOB_START = 0;

    /**
     * 任务停止执行
     */
    public static final int MSG_JOB_STOP = 1;

    /**
     * 任务执行完毕消息
     */
    public static final int MSG_ONJOB_START = 2;

    /**
     * 任务停止消息
     */
    public static final int MSG_OBJOB_STOP = 3;

    /**
     * 执行的JobId
     */
    private int mJobId = 0;

    /**
     * 设置delay时间
     */
    private EditText mEtDelay;

    /**
     * 设置最长的截止时间
     */
    private EditText mEdDeadline;

    /**
     * setPeriodic 周期
     */
    private EditText mEdDurationTime;

    /**
     * 设置builder中的是否有wifi连接
     */
    private RadioButton mRdWifi;

    /**
     * 设置builder中是否有任何网络
     */
    private RadioButton mRdNetAny;

    /**
     *设置builder中的是否需要充电
     */
    private CheckBox mCbCharging;

    /**
     * 设置builder中的是否设备空闲
     */
    private CheckBox mCbcRequiresIdle;

    private Button mBtnStartJob;
    private Button mBtnStopJob;

    /**
     * 获取两个View,展示Job运行状态(颜色变化)
     */
    private TextView showStartView;
    private TextView showStopView;
    private TextView jobSchedulerInfo;

    /**
     * JobService组件
     */
    ComponentName mServiceComponent;
    private IncomingMessageHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        // 启动服务并提供一种与此类通信的方法
        Intent startServiceIntent = new Intent(this, SettingJobService.class);
        Messenger messenger = new Messenger(mHandler);
        startServiceIntent.putExtra(MESSENGER_INTENT_KEY, messenger);
        startService(startServiceIntent);
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 这里关闭,以便在退出页面的时候依然保持可以执行任务
//        stopService(new Intent(this, SettingJobService.class));
        super.onStop();
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_job_scheduler);
        buttonDoJobScheduler = findViewById(R.id.button_m_keep_alive_do_JobScheduler);
        buttonDoJobScheduler.setOnClickListener(this);
        mEtDelay = findViewById(R.id.m_keep_alive_editText_delay_time);
        mEdDurationTime = findViewById(R.id.m_keep_alive_editText_Duration_time);
        mEdDeadline = findViewById(R.id.m_keep_alive_editText_Deadline_time);
        mRdWifi = findViewById(R.id.m_keep_alive_radioButton_wifi);
        mRdNetAny = findViewById(R.id.m_keep_alive_editText_net_any);
        mCbCharging = findViewById(R.id.m_keep_alive_checkBox_charging);
        mCbcRequiresIdle = findViewById(R.id.m_keep_alive_checkBox_requiresIdle);
        mBtnStartJob = findViewById(R.id.m_keep_alive_button_startJob);
        mBtnStartJob.setOnClickListener(this);
        mBtnStopJob = findViewById(R.id.m_keep_alive_button_stopJob);
        mBtnStopJob.setOnClickListener(this);
        showStartView = findViewById(R.id.m_keep_alive_textView_startJob);
        showStopView = findViewById(R.id.m_keep_alive_textView_stopJob);
        jobSchedulerInfo = findViewById(R.id.m_keep_alive_textView_update_job_info);
    }

    @Override
    public void initData(Activity activity) {
        mHandler = new IncomingMessageHandler(this);
        mServiceComponent = new ComponentName(this, SettingJobService.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_m_keep_alive_do_JobScheduler){
            Intent intent = new Intent();
            intent.setClass(JobSchedulerActivity.this, MyJobService.class);
            startService(intent);
        } else if (id == R.id.m_keep_alive_button_startJob){
            schedulerJob();
        } else if (id == R.id.m_keep_alive_textView_stopJob){
            cancelAllJobs();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void schedulerJob(){
        // 开始配置JobInfo
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++ , mServiceComponent);

        // 设置任务的执行时间,单位毫秒
        String delay = mEtDelay.getText().toString();
        if (!TextUtils.isEmpty(delay)){
            builder.setMinimumLatency(Long.valueOf(delay) * 1000);
        }

        // 设置任务最晚的延迟时间.如果到了规定的时间其他条件未满足,任务开始启动
        String deadline = mEdDeadline.getText().toString();
        if (!TextUtils.isEmpty(deadline)){
            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
        }

        boolean requiresWifi = mRdWifi.isChecked();
        boolean requiresAnyNet = mRdNetAny.isChecked();

        // 设置任务在满足指定网络条件才被执行
        if (requiresWifi){
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        } else if (requiresAnyNet){
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        }

        // 设备空闲才可以执行该任务
        builder.setRequiresDeviceIdle(mCbcRequiresIdle.isChecked());
        // 设置充电才可以执行
        builder.setRequiresCharging(mCbCharging.isChecked());

        // 任务持续时间
        PersistableBundle extras = new PersistableBundle();
        String workDuration = mEdDurationTime.getText().toString();
        if (TextUtils.isEmpty(workDuration)){
            workDuration = "1";
        }
        extras.putLong(WORK_DURATION_KEY, Long.valueOf(workDuration) * 1000);
        builder.setExtras(extras);

        // JobScheduler
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        // 设置在JobService里面处理配置好的Job;  返回一个int值：如果失败返回一个小于0的错误码 or 成功返回jobId
        int result = mJobScheduler.schedule(builder.build());
        ZsfLog.d(JobSchedulerActivity.class, "JobScheduler result = " + result);
    }

    /**
     * 取消所有执行
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelAllJobs(){
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancelAll();
        ToastUtils.showToast(GlobalData.getContext(), GlobalData.getContext().getResources().getString(R.string.m_keep_alive_button_do_JobScheduler_label));
    }

    /**
     * {@link android.os.Messenger} 使用此处理程序从{@link SettingJobService}进行通信
     */
    private static class IncomingMessageHandler extends Handler{
        // 使用弱引用防止内存卸扣
        WeakReference<JobSchedulerActivity> activityWeakReference;

        IncomingMessageHandler(JobSchedulerActivity activity){
            super();
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(@NonNull Message msg) {
            JobSchedulerActivity jobSchedulerActivity = activityWeakReference.get();
            if (jobSchedulerActivity == null){
                ZsfLog.d(JobSchedulerActivity.class, "Activity 已回收 jobId = " + msg.obj);
                return;
            }
            Message m;
            switch (msg.what){
                case MSG_JOB_START:
                    ToastUtils.showToast(GlobalData.getContext(), "start jobId : " + msg.obj);
                    ZsfLog.d(JobSchedulerActivity.class, "start jobId : " + msg.obj);
                    // 接收开始信号,打开绿色信号灯
                    jobSchedulerActivity.showStartView.setBackgroundColor(GlobalData.getContext().getResources().getColor(R.color.C_0f0));
                    updateParamsTextView(msg.obj, "started", jobSchedulerActivity);
                    // 发送一条3秒延时消息,用于模拟任务执行时间
                    m = Message.obtain(this, MSG_ONJOB_START);
                    m.obj = msg.obj;
                    sendMessageDelayed(m, 3000L);
                    break;
                case MSG_JOB_STOP:
                    ToastUtils.showToast(GlobalData.getContext(), "stop jobId : " + msg.obj);
                    ZsfLog.d(JobSchedulerActivity.class, "stop jobId : " + msg.obj);
                    // 接收停止信号,打开红色指示灯
                    jobSchedulerActivity.showStopView.setBackgroundColor(GlobalData.getContext().getResources().getColor(R.color.colorAccent));
                    updateParamsTextView(msg.obj, "stopped", jobSchedulerActivity);

                    // 发送一条模拟关闭过程
                    m = Message.obtain(this, MSG_OBJOB_STOP);
                    m.obj = msg.obj;
                    sendMessageDelayed(m, 3000L);
                    break;
                case MSG_ONJOB_START:
                    ToastUtils.showToast(GlobalData.getContext(), "started jobId : " + msg.obj);
                    ZsfLog.d(JobSchedulerActivity.class, "started jobId : " + msg.obj);
                    jobSchedulerActivity.showStartView.setBackgroundColor(GlobalData.getContext().getResources().getColor(R.color.C_FFFFFF));
                    updateParamsTextView(msg.obj, "Job had started", jobSchedulerActivity);
                    break;
                case MSG_OBJOB_STOP:
                    ToastUtils.showToast(GlobalData.getContext(), "stopped jobId : " + msg.obj);
                    ZsfLog.d(JobSchedulerActivity.class, "stopped jobId : " + msg.obj);
                    jobSchedulerActivity.showStopView.setBackgroundColor(GlobalData.getContext().getResources().getColor(R.color.C_FFFFFF));
                    updateParamsTextView(msg.obj, "Job had stopped", jobSchedulerActivity);
                    break;
                default:
                    break;
            }
        }

        /**
         * 更新JobScheduler执行状态
         * @param jobId 任务Id
         * @param action 任务节点
         */
        private void updateParamsTextView(Object jobId, String action, JobSchedulerActivity activity) {
            if (jobId == null){
                activity.jobSchedulerInfo.setText("");
                return;
            }
            activity.jobSchedulerInfo.setText(String.format("Job Id %s %s", String.valueOf(jobId), action));
        }
    }
}
