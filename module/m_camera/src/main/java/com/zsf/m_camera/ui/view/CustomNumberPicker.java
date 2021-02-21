package com.zsf.m_camera.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.core.content.ContextCompat;

import com.zsf.m_camera.R;

/**
 * @author : zsf
 * @date : 2021/2/1 4:18 PM
 * @desc :
 */
public class CustomNumberPicker extends NumberPicker {
    public CustomNumberPicker(Context context) {
        super(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setNumberPickerView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setNumberPickerView(child);
    }

    public void setNumberPickerView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextColor(ContextCompat.getColor(getContext(), R.color.m_camera_bottom_selector_text_color));
            ((EditText) view).setTextSize(20f);
        }
    }
}
