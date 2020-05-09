package com.zsf.test.branch.arouter.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zsf.test.R;
import com.zsf.utils.ZsfLog;
import com.zsf.view.fragment.BaseFragment;

/**
 * @author zsf; 2019/7/31
 */
@Route(path = "/ARouter/Go_ARouter_Fragment")
public class GoArouterFragment extends BaseFragment {

    @Autowired
    public String keyOne;

    public GoArouterFragment() {
        ARouter.getInstance().inject(GoArouterFragment.this);
        ZsfLog.d(GoArouterFragment.this.getClass(),"ARouter->Fragment->参数:" + keyOne);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_arouter, container, false);
        TextView textView = view.findViewById(R.id.text_param);
        textView.setText(keyOne);
        return view;
    }

    /**
     * 加入回退栈
     * @return
     */
    @Override
    public boolean isNeedToAddBackStack() {
        return true;
    }
}
