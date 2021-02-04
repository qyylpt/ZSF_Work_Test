package com.zsf.m_camera.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsf.m_camera.adapter.bean.CollectionFileBean;


/**
 * @author : zsf
 * @date : 2021/2/4 12:10 PM
 * @desc :
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * 绑定View
     *
     * @param collectionFileBean
     * @param position
     */
    public abstract void bindView(CollectionFileBean collectionFileBean, int position);

}
