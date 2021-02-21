package com.zsf.m_camera.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zsf.m_camera.R;
import com.zsf.m_camera.ZLog;
import com.zsf.m_camera.adapter.BaseViewHolder;
import com.zsf.m_camera.adapter.bean.CollectionFileBean;
import com.zsf.m_camera.ui.BaseFragment;

/**
 * @author : zsf
 * @date : 2021/2/4 2:14 PM
 * @desc :
 */
public class RecentHolder extends BaseViewHolder implements View.OnClickListener {

    private final String TAG = "AQCJ_RecentHolder";

    private ImageView imageView;
    private TextView fileName, fileSize;

    private BaseFragment fragment;


    public RecentHolder(BaseFragment fragment, @NonNull View itemView) {
        super(itemView);
        this.fragment = fragment;
        imageView = itemView.findViewById(R.id.m_camera_ImageView_item_icon);
        fileName = itemView.findViewById(R.id.m_camera_TextView_item_file_name);
        fileSize = itemView.findViewById(R.id.m_camera_TextView_item_file_size);
        imageView.setOnClickListener(this);
        fileName.setOnClickListener(this);
        fileSize.setOnClickListener(this);
    }


    @Override
    public void bindView(CollectionFileBean collectionFileBean, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(fragment)
                .load(collectionFileBean.getFilePath())
                .apply(options)
                .into(imageView);
        fileName.setText(collectionFileBean.getFileName());
        int fileLength = Integer.parseInt(collectionFileBean.getFileSize());
        double kb = (double) fileLength / 1024;
        double m = (double) fileLength / 1024 / 1024;
        if (kb < 1000) {
            fileSize.setText(String.format("%.2f", kb) + " KB");
        } else {
            fileSize.setText(String.format("%.2f", m) + " M");
        }
    }

    @Override
    public void onClick(View v) {
        ZLog.d(TAG, "点击了");
    }
}
