package com.zsf.m_camera.manager;

import com.zsf.m_camera.adapter.bean.CollectionFileBean;

import java.util.List;

/**
 * @author : zsf
 * @date : 2021/2/6 6:19 PM
 * @desc :
 */
public class CollectionDataManager {

    public long getExpiredTime() {
        // TODO: 2021/2/6 服务器设置文件过期时间
        return 0;
    }

    public void setExpiredTime(long expiredTime) {
        // TODO: 2021/2/6 设置文件过期时间
    }

    public void filterExpiredFile() {
        // TODO: 2021/2/6 过滤过期文件
    }

    public void reportCollectionData(List<CollectionFileBean> collectionFileBeans) {
        // TODO: 2021/2/6 上报文件
    }

    public void setMaskTransparent() {
        // TODO: 2021/2/6 设置图片文字透明度
    }

    public float getMaskConfig() {
        // TODO: 2021/2/6 获取图片文字透明度
        return Float.parseFloat(null);
    }

    public void setMaskAngle() {
        // TODO: 2021/2/21 设置图片文字角度
    }

    public void getMaskAngle() {
        // TODO: 2021/2/21 获取图片文件角度
    }
}
