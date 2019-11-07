package com.zsf.test.branch.arouter.service;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author zsf; 2019/7/31
 */
public interface IService extends IProvider {
    /**
     * 对外提供接口服务
     * @param msg
     */
    void sayHello(String msg);

}
