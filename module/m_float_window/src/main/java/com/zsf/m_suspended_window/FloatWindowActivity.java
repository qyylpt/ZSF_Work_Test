package com.zsf.m_suspended_window;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.m_suspended_window.sdk.DefaultWindowContentView;
import com.zsf.m_suspended_window.sdk.FloatWindow;
import com.zsf.utils.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;


@Route(path = "/m_float_window/FloatWindowActivity")
public class FloatWindowActivity extends AppCompatActivity {

    private static final String TAG = "FileListActivity";

    private Handler handler = new Handler();

    private AlertDialog alertDialog;


    private FloatWindow floatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspended_window);
        floatWindow = new FloatWindow(this);
        floatWindow.setClickFloatWindowListener(new FloatWindow.ClickFloatWindowListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatWindowActivity.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
        DefaultWindowContentView defaultWindowContentView = new DefaultWindowContentView(this);
        floatWindow.setWindowContentView(defaultWindowContentView);
        floatWindow.createWindow();
        floatWindow.showWindow();
        if (alertDialog == null) {
            createDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!floatWindow.canDrawOverlayViews()) {
            alertDialog.show();
        } else {
            floatWindow.showWindow();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (floatWindow != null){
            floatWindow.hiddenWindow();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        if (floatWindow != null) {
            floatWindow.destroyWindow();
        }
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
                            floatWindow.requestOverlayDrawPermission();
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
