package com.zsf.m_camera.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author : zsf
 * @date : 2021/1/25 3:52 PM
 * @desc :
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 是否需要添加到栈
     *
     * @return true : 添加 false : 不添加
     */
    public abstract boolean isNeedAddStack();

    /**
     * 刷新样式
     *
     * @return
     */
    public abstract void refreshStyle();

    public void back() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack(null, 0);
            Fragment fragment = getTargetFragment();
            if (fragment != null) {
                ((BaseFragment)fragment).refreshStyle();
            } else {
                ((BaseCollectionActivity)getActivity()).refreshStyle();
            }
        }
    }
    public void switchStyle(boolean enable) {
        Window window = getActivity().getWindow();
        if (enable) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshStyle();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStyle();
        getFocus();
    }

    public void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    back();
                    return true;
                }
                return false;
            }
        });
    }
}
