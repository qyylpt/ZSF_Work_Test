package com.zsf.m_suspended_window;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.utils.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;


@Route(path = "/m_float_window/FloatWindowActivity")
public class FloatWindowActivity extends AppCompatActivity {

    private static final String TAG = "FileListActivity";

    private Handler handler = new Handler();

    private AlertDialog alertDialog;

    /**
     * 屏幕关闭 标记(关闭屏幕设置标记,在恢复onResume时弹出根目录之上元素. 避免在Activity在无效生命周期中更新fragment造成的状态丢失)
     */
    private boolean screenOffSign = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspended_window);
        WindowController.getInstance().init(this, new WindowController.ClickFloatWindowListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(GlobalData.getContext(),"您点击了悬浮窗!!!");
            }
        },true);
        if (alertDialog == null) {
            createDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!WindowController.getInstance().canDrawOverlayViews()) {
            alertDialog.show();
        } else {

            WindowController.getInstance().showWindow();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        WindowController.getInstance().hiddenWindow();
    }

    private void createDialog() {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false);
            builder.setTitle(getResources().getString(R.string.float_window_title))
                    .setMessage(getResources().getString(R.string.float_window_content))
                    .setPositiveButton(R.string.float_window_determine, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            WindowController.getInstance().requestOverlayDrawPermission();
                            alertDialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.float_window_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog = builder.create();
        }
    }

}
