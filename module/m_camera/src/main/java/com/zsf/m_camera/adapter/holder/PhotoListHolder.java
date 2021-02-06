package com.zsf.m_camera.adapter.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zsf.m_camera.R;
import com.zsf.m_camera.adapter.BaseViewHolder;
import com.zsf.m_camera.adapter.bean.CollectionFileBean;

/**
 * @author : zsf
 * @date : 2021/2/5 11:23 AM
 * @desc :
 */
public class PhotoListHolder  extends BaseViewHolder implements View.OnClickListener {

    private Fragment fragment;
    private ImageView image, select;

    public PhotoListHolder(Fragment fragment, @NonNull View itemView) {
        super(itemView);
        this.fragment = fragment;
        image = itemView.findViewById(R.id.m_camera_ImageView_photo_list_item_image);
        select = itemView.findViewById(R.id.m_camera_ImageView_photo_list_select);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void bindView(CollectionFileBean collectionFileBean, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(fragment)
                .load(collectionFileBean.getFilePath())
                .apply(options)
                .into(image);
    }
}
