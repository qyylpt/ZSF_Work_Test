package com.zsf.view.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.example.common.R;
import com.zsf.global.GlobalData;
import com.zsf.utils.ToastUtils;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zsf
 * @date 2019/8/2
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 双击退出标记
     */
    private static boolean isExit = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(this);
        initData(this);
    }

    /**
     * 在onCreate初始化View控件
     * @param activity
     */
    public abstract void initView(Activity activity);

    /**
     * 在onCreate中初始化数据
     * @param activity
     */
    public abstract void initData(Activity activity);

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            PackageManager pm = Utils.getApp().getPackageManager();
            List<ResolveInfo> info = pm.queryIntentActivities(ActivityUtils.getTopActivity().getIntent(), 0);
            ResolveInfo next = info.iterator().next();
            String currentActivityName = "";
            if (next != null) {
                currentActivityName = next.activityInfo.name;
            }
            if (currentActivityName.equals(ActivityUtils.getLauncherActivity())){
                // 快速双击退出应用
                exitBy2Click();
                return true;
            } else {
                finish();
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 快速点击退出应用
     */
    private void exitBy2Click() {
        Timer timer = null;
        if (!isExit){
            isExit = true;
            ToastUtils.showToast(this, GlobalData.getContext().getResources().getString(R.string.common_exit_app_toast));
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 重置退出标记
                    isExit = false;
                }
            }, 2000);
        }else {
            finish();
            System.exit(0);
        }
    }
}
