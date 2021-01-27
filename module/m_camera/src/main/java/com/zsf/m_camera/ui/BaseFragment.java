package com.zsf.m_camera.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * @author : zsf
 * @date : 2021/1/25 3:52 PM
 * @desc :
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 是否需要添加到栈
     * @return true : 添加 false : 不添加
     */
    public abstract boolean isNeedAddStack();

    public void back() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack(null, 0);
        }
    }

}
