package com.zsf.m_suspended_window;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author zsf
 * @date 2020/9/15 10:55 AM
 * @use  悬浮窗自定义移动方式
 */
public abstract class WindowContentView extends ConstraintLayout {

    public WindowContentView(Context context) {
        super(context);
        init(context);
    }

    public WindowContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WindowContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public abstract void init(Context context);

    public abstract void showLeft();

    public abstract void showRight();

    public abstract void doMove();

}
