package com.zsf.arouter;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf; 2019/7/31
 */
public abstract class AbstractArouterNavCallback implements NavigationCallback {
    @Override
    public void onFound(Postcard postcard) {
        ZsfLog.d(AbstractArouterNavCallback.this.getClass(),"路由被目标发现：path = " + postcard.getPath() + " ； " + "destination = " + postcard.getDestination());
    }

    @Override
    public void onLost(Postcard postcard) {
        ZsfLog.d(AbstractArouterNavCallback.this.getClass(),"路由丢失：path = " + postcard.getPath() + " ；" + "destination：" + postcard.getDestination());
    }

    @Override
    public void onArrival(Postcard postcard) {
        ZsfLog.d(AbstractArouterNavCallback.this.getClass(),"路由到达：path = " + postcard.getPath() + " ；" + "destination：" + postcard.getDestination());
    }

    @Override
    public void onInterrupt(Postcard postcard) {
        ZsfLog.d(AbstractArouterNavCallback.this.getClass(),"路由被拦截：path = " + postcard.getPath() + " ；" + "destination：" + postcard.getDestination());
    }
}
