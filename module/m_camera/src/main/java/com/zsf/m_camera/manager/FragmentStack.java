package com.zsf.m_camera.manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zsf.m_camera.R;
import com.zsf.m_camera.ui.BaseFragment;

import java.util.List;

/**
 * Author: zsf
 * Date: 2020-06-19 11:25
 */
public class FragmentStack {

    /**
     * 用途: 标记Fragment & 目录访问标记
     */
    public static final String BUNDLE_KEY_TAG = "tag";

    /**
     * fragment 栈管理
     * @param fragmentManager
     * @param fragmentClass
     * @param containerId
     * @param args
     */
    public static void addFragment(FragmentManager fragmentManager, Class<? extends BaseFragment> fragmentClass, int containerId, Bundle args){
        stackFragment(fragmentManager, null, fragmentClass, -1, containerId, args);
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fromFragment, Class<? extends BaseFragment> toFragment, int requestCode, int containerId, Bundle args){
        stackFragment(fragmentManager, fromFragment, toFragment, requestCode, containerId, args);
    }


    private static void stackFragment(FragmentManager fragmentManager, Fragment fromFragment, Class<? extends BaseFragment> fragmentClass, int requestCode, int containerId, Bundle args){
        String tag = args.getString(BUNDLE_KEY_TAG);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment == null){
            try {
                fragment = fragmentClass.newInstance();
                BaseFragment baseFragment = (BaseFragment) fragment;
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
                transaction.add(containerId, fragment, tag);
                if (baseFragment.isNeedAddStack()){
                    transaction.addToBackStack(tag);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            if (fragment.isAdded()){
                if (fragment.isHidden()){
                    transaction.show(fragment);
                }
            } else {
                transaction.add(containerId, fragment, tag);
            }
        }
        if (fragment != null){
            fragment.setArguments(args);
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment item : fragments) {
                if (!item.getTag().equals(tag) && !item.isHidden()) {
                    // 兼容手势滑动需要显示栈底页面，不做隐藏操作
//                    transaction.hide(item);
                }
            }
            if (fromFragment != null) {
                fragment.setTargetFragment(fromFragment, requestCode);
            }
            transaction.commitAllowingStateLoss();
        }
    }
}
