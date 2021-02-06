package com.zsf.m_camera.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.zsf.m_camera.adapter.bean.CollectionFileBean;

import java.util.List;

/**
 * @author : zsf
 * @date : 2021/2/6 5:13 PM
 * @desc :
 */
public class CollectionDiffCallback extends DiffUtil.Callback {

    private List<CollectionFileBean> oldList;

    private List<CollectionFileBean> newList;

    public CollectionDiffCallback(List<CollectionFileBean> oldList, List<CollectionFileBean> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getFileName().equals(newList.get(newItemPosition).getFileName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getFileMd5().equals(newList.get(newItemPosition).getFileMd5());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition);
    }
}
