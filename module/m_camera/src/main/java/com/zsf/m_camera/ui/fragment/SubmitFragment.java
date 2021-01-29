package com.zsf.m_camera.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zsf.m_camera.R;
import com.zsf.m_camera.ui.BaseFragment;

import java.sql.Time;

/**
 * @author : zsf、
 * @date : 2021/1/27 2:18 PM
 * @desc :
 */
public abstract class SubmitFragment extends BaseFragment implements View.OnClickListener {

    private TextView exit, title, longitude, latitude, radius, selectScenes, submitItemTitle;
    private ImageView icon;
    private EditText content;
    private Button submit;

    private double longitudeNum = 4.9E-324D;
    private double latitudeNum = 4.9E-324D;
    private float radiusNum = 0.0F;
    private String filePath;
    private String time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            filePath = args.getString("path");
            time = args.getString("time");
            longitudeNum = args.getDouble("longitude");
            latitudeNum = args.getDouble("latitude");
            radiusNum = args.getFloat("radius");
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

        }
        if (id == R.id.m_camera_Button_submit) {

        }
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
}
