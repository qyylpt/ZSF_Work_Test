package com.zsf.test.branch.animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.zsf.test.R;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2020/9/9 5:52 PM
 * @use
 */
public class SpinCircleView extends View {

    // 宽高
    private int width;
    private int height;
    // 间隔
    private float interval_w;
    private float interval_h;
    // 半径
    private float radius;
    // 小球密度
    private int density;
    // 小球总数
    private int total;

    // 旋转动画
    private ValueAnimator mAnimator;
    // 选装点
    private int index;

    // 点 颜色
    private int pointColor;
    // 移动点 颜色
    private int movePointColor;
    // 速度
    private int movePointSpeed;

    private Paint mPaint = new Paint();


    public SpinCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpinCircleView);
        pointColor = ta.getColor(R.styleable.SpinCircleView_point_color, Color.RED);
        mPaint.setColor(pointColor);
        movePointColor = ta.getColor(R.styleable.SpinCircleView_move_point_color, Color.BLUE);
        movePointSpeed = ta.getInt(R.styleable.SpinCircleView_move_point_speed, 4000);
        radius = ta.getDimension(R.styleable.SpinCircleView_radius, (float) 20);
        density = ta.getInt(R.styleable.SpinCircleView_density, 4);
        ta.recycle();
        total = (density - 1) * 4;
        init();
    }

    public SpinCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(){
        mAnimator = ValueAnimator.ofInt(0, total);
        mAnimator.setDuration(movePointSpeed);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(-1);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                index = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        interval_w = (w - (radius * 2) * density) / (density - 1);
        interval_h = (h - (radius * 2) * density) / (density - 1);
        ZsfLog.d(SpinCircleView.class, "width = " + width + "; height = " + height + "; interval_w = " + interval_w + "; interval_h = " + interval_h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < total; i++) {
            if (i == index){
                mPaint.setColor(pointColor);
            } else {
                mPaint.setColor(movePointColor);
            }
            // top
            if (i == 0) {
                canvas.drawCircle(radius, radius, radius, mPaint);
                continue;
            }
            if (i < (density -1)) {
                canvas.drawCircle(radius + (radius * 2 + interval_w) * i, radius, radius, mPaint);
                continue;
            }

            // right
            if (i == density - 1) {
                canvas.drawCircle(width - radius, radius, radius, mPaint);
                continue;
            }
            if (i < (density -1) * 2) {
                canvas.drawCircle(width - radius,radius + (radius * 2 + interval_h) * (i - (density -1)), radius, mPaint);
                continue;
            }

            // bottom
            if (i == (density - 1) * 2) {
                canvas.drawCircle(width - radius,height - radius, radius, mPaint);
                continue;
            }
            if (i < (density -1) * 3) {
                canvas.drawCircle(width - radius - (radius * 2 + interval_w) * (i - (density - 1) * 2), height - radius, radius, mPaint);
                continue;
            }

            // left
            if (i == (density - 1) * 3) {
                canvas.drawCircle(radius,height - radius, radius, mPaint);
                continue;
            }
            if (i < (density -1) * 4) {
                canvas.drawCircle(radius, height - radius - (radius * 2 + interval_h) * (i - (density - 1) * 3) , radius, mPaint);
                continue;
            }
        }
    }
}
