package com.zsf.test.branch.diffutil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * @author zsf
 * @date 2019/8/7
 */
public class DiffCallback extends DiffUtil.Callback {
    private List<TestBean> mOldDatas, mNewDatas;

    public DiffCallback(List<TestBean> mOldDatas, List<TestBean> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    /**
     * 老数据集size
     * @return
     */
    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    /**
     * 新数据集size
     * @return
     */
    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }

    /**
     * 当item一致并且内容不一致的情况下进行调用并返回差异结果
     * 例如：如果使用RecycleView配合DiffUtils,可以Item可以改变哪些字段
     *       默认实现是返回null
     *       返回一个代表新老Item内容改变的payload对象
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        TestBean oldBean = mOldDatas.get(oldItemPosition);
        TestBean newBean = mNewDatas.get(newItemPosition);

        Bundle payload = new Bundle();
        if (!oldBean.getDesc().equals(newBean.getDesc())){
            payload.putString("KEY_DESC", newBean.getDesc());
        }
        if (oldBean.getPic() != newBean.getPic()){
            payload.putInt("KEY_PIC", newBean.getPic());
        }
        if (payload.size() == 0){
            return null;
        }
        return payload;
    }

    /**
     * 这里对比item，是否是同一个。可以根据自己的现实情况自定义对比规则.
     * 如果返回：true,说明是同一个item,继续执行callback的下一层验证（对比内容是否一致）
     *         ：false,说明不是同一个item,不会继续执行下层的内容验证
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getName().equals(mNewDatas.get(newItemPosition).getName());
    }


    /**
     * 被DiffUtil调用,用来检查两个item是否含有相同的数据
     * 这里在areItemsTheSame对比,确认item相同的情况下调用,用来检测当前item的内容是否一致
     * 如果返回：true,说明内容一致,不会继续执行下一层
     *         ：false,说明内容不一致,需要执行下一次进行差异更新
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TestBean beanOld = mOldDatas.get(oldItemPosition);
        TestBean beanNew = mNewDatas.get(newItemPosition);
        if (!beanOld.getDesc().equals(beanNew.getDesc())){
            return false;
        }
        if (beanNew.getPic() != beanNew.getPic()){
            return false;
        }
        return true;
    }
}
