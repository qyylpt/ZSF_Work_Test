package com.zsf.m_camera.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zsf.m_camera.ui.BaseFragment;

/**
 * @author : zsf
 * @date : 2021/1/27 2:18 PM
 * @desc :
 */
public abstract class SubmitFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean isNeedAddStack() {
        return true;
    }
}
