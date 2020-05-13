package com.zsf.ipc_core.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: zsf
 * Date: 2020-05-11 14:40
 * Desc: 请求标签(这里表示服务端提供的服务实现类的超类)
 *       客户端获取服务端提供的服务实现类标签,服务端注册服务能力注册是标签，用于客户端请求服务时方便映射
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLable {
    String value();
}
