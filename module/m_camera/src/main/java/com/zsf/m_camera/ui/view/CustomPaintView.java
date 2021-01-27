package com.zsf.m_camera.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zsf.m_camera.utils.Matrix3;

import java.util.ArrayList;

/**
 * Created by panyi on 17/2/11.
 */

public class CustomPaintView extends View {
    private Paint mPaint;
    private Bitmap mDrawBit;
    private Path path;
    private float mX;
    private float mY;
    private float dX, dY;
    private Canvas mPaintCanvas = null;
    private ArrayList<Path> paths;
    private OnUndoListener listener;
    private int mColor;

    public CustomPaintView(Context context) {
        super(context);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //System.out.println("width = "+getMeasuredWidth()+"     height = "+getMeasuredHeight());
        if (mDrawBit == null) {
            generatorBit();
        }
    }

    private void generatorBit() {
        mDrawBit = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mPaintCanvas = new Canvas(mDrawBit);
    }

    private void init(Context context) {
        paths = new ArrayList<>();
        onListener(false);
        mPaint = new Paint();
        path = new Path();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    public void setColor(int color) {
        this.mColor = color;
        this.mPaint.setColor(mColor);
    }

    public void setWidth(float width) {
        this.mPaint.setStrokeWidth(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, mPaint);
        for (Path oldPath : paths) {
            canvas.drawPath(oldPath, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ret = true;
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                ret = true;
                touchMove(event);
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchUp(event);
                ret = false;
                break;
        }
        return ret;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }
    }

    public void undo() {
        int size = paths.size();
        if (size > 0) {
            paths.remove(size - 1);
            path.reset();
            onListener(paths.size() > 0);
            this.postInvalidate();
        }
    }

    private void touchDown(MotionEvent event) {
        path.reset();
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        path.moveTo(x, y);
    }

    //手指在屏幕上滑动时调用
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        if (dx >= 3 || dy >= 3) {
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;
            dX += Math.abs(cX - previousX);
            dY += Math.abs(cY - previousY);
            path.quadTo(previousX, previousY, cX, cY);
            mX = x;
            mY = y;
        }
    }

    private void touchUp(MotionEvent event) {
        if (dX > 1.8 || dY > 1.8) {
            paths.add(new Path(path));
            onListener(true);
            dY = 0;
            dX = 0;
        }
    }

    public void save(Bitmap bitmap, Matrix matrix) {
        if (bitmap == null) return;

        if (paths != null && paths.size() > 0) {
            if (mPaintCanvas != null) {
                for (Path path : paths) {
                    mPaintCanvas.drawPath(path, mPaint);
                }
            }
            float[] data = new float[9];
            matrix.getValues(data);// 底部图片变化记录矩阵原始数据
            Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
            Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
            Canvas canvas = new Canvas(bitmap);
            float[] f = inverseMatrix.getValues();
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            canvas.drawBitmap(mDrawBit, 0, 0, paint);
            canvas.restore();
            if (!mDrawBit.isRecycled()) {
                mDrawBit.recycle();
                mDrawBit = null;
            }
        }
    }

    public void cancel() {
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
            mDrawBit = null;
        }
        mPaintCanvas = null;
        if (paths != null && paths.size() > 0) {
            paths.clear();
            onListener(false);
            paths = null;
        }
    }

    public boolean isUndoEnable() {
        return paths != null && paths.size() > 0;
    }

    public interface OnUndoListener {
        void OnUndo(boolean isUndo);
    }

    private void onListener(boolean isUndo) {
        if (listener != null) {
            listener.OnUndo(isUndo);
        }
    }

    public void setUndoListener(OnUndoListener listener) {
        this.listener = listener;
    }

    public Bitmap getDrawBit() {
        return mDrawBit;
    }
}//end class
