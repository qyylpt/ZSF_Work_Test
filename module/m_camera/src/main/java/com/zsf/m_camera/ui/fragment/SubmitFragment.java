package com.zsf.m_camera.ui.fragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zsf.global.GlobalData;
import com.zsf.m_camera.R;
import com.zsf.m_camera.ZLog;
import com.zsf.m_camera.data.CollectionProvider;
import com.zsf.m_camera.ui.BaseCollectionActivity;
import com.zsf.m_camera.ui.BaseFragment;
import com.zsf.m_camera.utils.FileUtils;
import com.zsf.utils.ZsfLog;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Time;

/**
 * @author : zsf、
 * @date : 2021/1/27 2:18 PM
 * @desc :
 */
public abstract class SubmitFragment extends BaseFragment implements View.OnClickListener, NumberPicker.OnValueChangeListener, View.OnKeyListener {

    private final String TAG = "SubmitFragment";

    private TextView exit, title, longitude, latitude, radius, selectScenes, submitItemTitle;
    private ImageView icon;
    private EditText content;
    private Button submit;

    private Dialog mDialog;
    private NumberPicker numberPicker;
    private TextView dialogSelect, dialogCancel;

    private double longitudeNum = 4.9E-324D;
    private double latitudeNum = 4.9E-324D;
    private float radiusNum = 0.0F;
    private String filePath;
    private String time;
    private int fileType;

    private String selectResult;
    private final int DEFAULT_SELECT_ITEM = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switchStyle(false);
        Bundle args = getArguments();
        if (args != null) {
            filePath = args.getString("path");
            time = args.getString("time");
            longitudeNum = args.getDouble("longitude");
            latitudeNum = args.getDouble("latitude");
            radiusNum = args.getFloat("radius");
            fileType = args.getInt("type");
        }
        View view = inflater.inflate(R.layout.fragment_submit, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        exit = view.findViewById(R.id.m_camera_TextView_title_back);
        title = view.findViewById(R.id.m_camera_TextView_title);
        title.setText(getSubmitTitle());
        submitItemTitle = view.findViewById(R.id.m_camera_TextView_item_content_type);
        submitItemTitle.setText(getSubmitType());
        longitude = view.findViewById(R.id.m_camera_textView_longitude);
        longitude.setText(String.valueOf(longitudeNum));
        latitude = view.findViewById(R.id.m_camera_TextView_latitude);
        latitude.setText(String.valueOf(latitudeNum));
        radius = view.findViewById(R.id.m_camera_TextView_latitude_longitude_radius);
        radius.setText(String.valueOf(radiusNum));
        exit.setOnClickListener(this);
        title.setOnClickListener(this);
        longitude.setOnClickListener(this);
        latitude.setOnClickListener(this);
        radius.setOnClickListener(this);

        selectScenes = view.findViewById(R.id.m_camera_TextView_scenes_select);
        selectScenes.setOnClickListener(this);

        content = view.findViewById(R.id.m_camera_EditText_content);
        content.setOnKeyListener(this);

        submit = view.findViewById(R.id.m_camera_Button_submit);
        submit.setOnClickListener(this);

        icon = view.findViewById(R.id.m_camera_ImageView_icon);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .load(filePath)
                .apply(options)
                .into(icon);
        initSelectScenes();
    }

    @Override
    public boolean isNeedAddStack() {
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_camera_TextView_title_back) {
            back();
        }
        if (id ==  R.id.m_camera_TextView_scenes_select) {
            mDialog.show();
        }
        if (id == R.id.m_camera_Button_submit) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CollectionProvider.DataContract.FILE_NAME, time);
            contentValues.put(CollectionProvider.DataContract.FILE_PATH, filePath);
            File file = new File(filePath);
            contentValues.put(CollectionProvider.DataContract.FILE_SIZE, file.length());
            contentValues.put(CollectionProvider.DataContract.FILE_TYPE, fileType);
            contentValues.put(CollectionProvider.DataContract.FILE_MD5, FileUtils.getFileMD5(file));
            contentValues.put(CollectionProvider.DataContract.FILE_CREATE_TIME, System.currentTimeMillis());
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_PROGRESS, 0);
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_STATUS, 0);
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_SCENES, selectResult);
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_DESCRIPTION, content.getText().toString());
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_LONGITUDE, longitudeNum);
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_LATITUDE, latitudeNum);
            contentValues.put(CollectionProvider.DataContract.FILE_REPORT_RADIUS, radiusNum);
            GlobalData.getContext().getContentResolver().insert(CollectionProvider.collectionUri, contentValues);
            back();
        }
        if (id == R.id.m_camera_TextView_cancel) {
            mDialog.dismiss();
        }
        if (id == R.id.m_camera_TextView_determine) {
            mDialog.dismiss();
            if (!TextUtils.isEmpty(selectResult)) {
                selectScenes.setText(selectResult);
            }
        }
    }

    private void initSelectScenes() {
        mDialog = new Dialog(getActivity(), R.style.BottomDialog);
        ConstraintLayout root = (ConstraintLayout) LayoutInflater.from(getActivity()).inflate(R.layout.bottom_float_window_layout, null);
        numberPicker = root.findViewById(R.id.m_camera_NumberPicker_scenes_select);
        dialogCancel = root.findViewById(R.id.m_camera_TextView_cancel);
        dialogCancel.setOnClickListener(this);
        dialogSelect = root.findViewById(R.id.m_camera_TextView_determine);
        dialogSelect.setOnClickListener(this);
        String[] data = getNumberPickerData();
        numberPicker.setDisplayedValues(data);
        setNumberPickerDividerColor(numberPicker);
        numberPicker.setMaxValue(data.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setValue(DEFAULT_SELECT_ITEM);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setOnValueChangedListener(this);
        selectResult = data[DEFAULT_SELECT_ITEM];

        mDialog.setContentView(root);

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        root.measure(widthMeasureSpec, heightMeasureSpec);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = getResources().getDisplayMetrics().widthPixels;
        lp.height = root.getMeasuredHeight();
        dialogWindow.setAttributes(lp);

    }

    protected String[] getNumberPickerData() {
        String[] numbers = {"场景 1", "场景 2", "场景 3", "场景 4", "场景 5", "场景 6", "场景 7", "场景 8", "场景 9", "场景 10", "场景 10+"};
        return numbers;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        ZsfLog.d(SubmitFragment.class, "oldVal : " + oldVal + "; newVal : " + newVal);
        selectResult = getNumberPickerData()[newVal];
    }

    /**
     * 保存主题
     * @return
     */
    public abstract String getSubmitTitle();

    /**
     * 保存类型
     * @return
     */
    public abstract String getSubmitType();

    public void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值 透明
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.m_camera_divider_color)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        // 分割线高度
        for (Field pf2 : pickerFields) {
            if (pf2.getName().equals("mSelectionDividerHeight")) {
                pf2.setAccessible(true);
                try {
                    int result = 2;
                    pf2.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        ZLog.d(TAG, "keyCode = " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            //使得根View重新获取焦点，以监听返回键
            back();
        }
        return true;
    }
}
