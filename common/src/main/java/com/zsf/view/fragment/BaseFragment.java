package com.zsf.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;


/**
 * @author zsf; 2019/7/31
 *  * https://mp.weixin.qq.com/s/cvvUb4xged0NpV8hnctyLg
 *  * 解决以下问题
 *  * 1.add/replace或者show/hide都会new 一个新的实例,在用户大量触发的情况下很快会OOM的情况。
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 添加fragment操作
     * @param fragmentManager Fragment管理器
     * @param fragmentClass 对应Fragment对象的getClass
     * @param containerId 容器Id
     * @param args 需要传值填入Bundle,否则null
     */
    public static void addFragment(FragmentManager fragmentManager, Class<? extends BaseFragment> fragmentClass, int containerId, Bundle args){
        // 获取Fragment名称
        String fragmentName = fragmentClass.getName();
        // 获取是否加载过的Fragment
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentName);
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 没有添加
        if (fragment == null){
            try {
                // 通过反射 new 出一个fragment实例
                fragment = fragmentClass.newInstance();
                // 统一转为BaseFragment
                BaseFragment baseFragment = (BaseFragment)fragment;

                // 设置fragment 进入、退出、弹进、弹出动画
                // transaction.setCustomAnimations(baseFragment.enter(), baseFragment.exit(), baseFragment.popEnter(), baseFragment.popExit());

                // 添加fragment
                transaction.add(containerId, fragment, fragmentName);
                // 判断是否需要加入回退栈
                if (baseFragment.isNeedToAddBackStack()){
                    transaction.addToBackStack(fragmentName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false
            if (fragment.isAdded()){
                // 该Fragment对象已经被隐藏，那么它返回true。默认情况下，Fragment是被显示的。能够用onHiddenChanged(boolean)回调方法获取该Fragment对象状态的改变
                if (fragment.isHidden()){
                    transaction.show(fragment);
                }
            } else {
                transaction.add(containerId, fragment, fragmentName);
            }
        }

        if (fragment != null){
            // 给目标fragment设置参数
            fragment.setArguments(args);
            hideBeforeFragment(fragmentManager, transaction, fragment);
            /**
             * 1.commitNow()：如果你需要同步提交Fragment并且无需添加到回退栈中，则使用commitNow()。Support库中在 FragmentPagerAdapter中使用这个函数，来确保更新Adapter的时候页面被正确的添加和删除。
             *   一般来说，只要不添加到回退栈中，都可以使用这个函数来提交;
             * 2.commitNowAllowingStateLoss()：如果你需要在Activity执行完onSaveInstanceState()之后还要进行提交，而且不关心恢复时是否会丢失此次提交
             * 注：.executePendingTransactions()：   commitNow() 只同步的执行当前的提交操作，而 executePendingTransactions() 则会执行所有等得执行的操作。 commitNow() 可以避免您执行之前提交的但是无需立刻执行的操作。
             *
             * 3.commitAllowingStateLoss()或commitNowAllowingStateLoss()：如果你需要在Activity执行完onSaveInstanceState()之后还要进行提交，而且不关心恢复时是否会丢失此次提交，那么可以使用commitAllowingStateLoss()或commitNowAllowingStateLoss()。
             * 4.commit()：如果执行的提交不需要是同步的，或者需要将提交都添加到回退栈中，那么就使用commit()
             */
            transaction.commit();
        }
    }

    /**
     * 是否加入回退栈
     * @return
     */
    public abstract boolean isNeedToAddBackStack();


    /**
     * 除当前 fragment 以外的所有 fragment 进行隐藏
     *
     * @param manager
     * @param transaction
     * @param currentFragment
     */
    private static void hideBeforeFragment(FragmentManager manager, FragmentTransaction transaction, Fragment currentFragment) {

        List<Fragment> fragments = manager.getFragments();

        for (Fragment fragment : fragments) {
            if (fragment != currentFragment && !fragment.isHidden()) {
                transaction.hide(fragment);
            }
        }
    }
}
