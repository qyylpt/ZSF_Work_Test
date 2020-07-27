package com.zsf.m_suspended_window;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Author: zsf
 * @Date: 2020-07-21 10:38
 */
class WindowContentView extends ConstraintLayout {

    private ImageView imageViewBackgroundLeft;

    private ImageView imageViewBackgroundRight;

    private ImageView imageViewBackgroundMove;

    public ImageView imageViewOpen;


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

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.window_content_view_layout, this, true);
        imageViewBackgroundLeft = findViewById(R.id.window_left);
        imageViewBackgroundRight = findViewById(R.id.window_right);
        imageViewOpen = findViewById(R.id.window_open);
        imageViewBackgroundMove = findViewById(R.id.window_move);
    }

    public void showLeft(){
        imageViewBackgroundRight.setVisibility(GONE);
        imageViewBackgroundMove.setVisibility(GONE);
        imageViewBackgroundLeft.setVisibility(VISIBLE);
    }

    public void showRight(){
        imageViewBackgroundLeft.setVisibility(GONE);
        imageViewBackgroundMove.setVisibility(GONE);
        imageViewBackgroundRight.setVisibility(VISIBLE);
    }

    public void doMove(){
        imageViewBackgroundLeft.setVisibility(GONE);
        imageViewBackgroundRight.setVisibility(GONE);
        imageViewBackgroundMove.setVisibility(VISIBLE);
    }



}
