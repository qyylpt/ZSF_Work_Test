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
public class DefaultWindowContentView extends WindowContentView {

    private ImageView imageViewBackgroundLeft;

    private ImageView imageViewBackgroundRight;

    private ImageView imageViewBackgroundMove;

    public ImageView imageViewOpen;

    public DefaultWindowContentView(Context context) {
        super(context);
    }

    public DefaultWindowContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultWindowContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.window_content_view_layout, this, true);
        imageViewBackgroundLeft = findViewById(R.id.window_left);
        imageViewBackgroundRight = findViewById(R.id.window_right);
        imageViewOpen = findViewById(R.id.window_open);
        imageViewBackgroundMove = findViewById(R.id.window_move);
    }

    @Override
    public void showLeft(){
        imageViewBackgroundRight.setVisibility(GONE);
        imageViewBackgroundMove.setVisibility(GONE);
        imageViewBackgroundLeft.setVisibility(VISIBLE);
    }

    @Override
    public void showRight(){
        imageViewBackgroundLeft.setVisibility(GONE);
        imageViewBackgroundMove.setVisibility(GONE);
        imageViewBackgroundRight.setVisibility(VISIBLE);
    }

    @Override
    public void doMove(){
        imageViewBackgroundLeft.setVisibility(GONE);
        imageViewBackgroundRight.setVisibility(GONE);
        imageViewBackgroundMove.setVisibility(VISIBLE);
    }



}
