package com.zsf.test.branch.diffutil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zsf.test.R;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsf
 * @date 2019/08/06
 */
@Route(path = "/branch/DiffUtil/DiffUtilActivity")
public class DiffUtilActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private DiffAdapter diffAdapter;
    private List<TestBean> mDatas;
    private int[] picArr;
    private List<TestBean> newDatas;

    private static final int H_CODE_UPDATE = 1;
    private Handler mHandler = new CustomHandler();


    // 测试issue_404test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_diff_util);
        smartRefreshLayout = findViewById(R.id.smart_refresh_layout);
        smartRefreshLayout.setOnRefreshLoadMoreListener(this);
        recyclerView = findViewById(R.id.diff_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData(Activity activity) {
        newDatas = new ArrayList<>();
        picArr = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6, R.drawable.pic7};
        mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            mDatas.add(new TestBean("Name" + i, "描述" + i, picArr[i]));
        }
        diffAdapter = new DiffAdapter(this, mDatas);
        recyclerView.setAdapter(diffAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        try {
            for (TestBean bean: mDatas){
                // clone一遍旧数据,模拟刷新操作
                newDatas.add(bean.clone());
            }
            // 模拟数据增加
            newDatas.add(new TestBean("Name100","Name100描述", picArr[5]));
            // 模拟数据更新
            newDatas.get(0).setDesc("刷新更新描述");
            newDatas.get(0).setPic(picArr[6]);
            // 模拟数据位移
            TestBean testBean = newDatas.get(1);
            newDatas.remove(testBean);
            newDatas.add(testBean);

            // 对比需要耗时,需要开启新的线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 子线程计算DiffResult
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(mDatas, newDatas), true);
                    Message message = mHandler.obtainMessage(H_CODE_UPDATE);
                    message.obj = diffResult;
                    message.sendToTarget();
                }
            }).start();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private class CustomHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            ZsfLog.d(DiffUtilActivity.class, "what" + msg.what);
            switch (msg.what){
                case H_CODE_UPDATE:
                    // 去除result
                    DiffUtil.DiffResult result = (DiffUtil.DiffResult) msg.obj;
                    //利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter
                    result.dispatchUpdatesTo(diffAdapter);
                    mDatas = newDatas;
                    diffAdapter.setDatas(mDatas);
                    smartRefreshLayout.finishLoadMore();
                    break;
                 default:
                     break;
            }
        }
    }
}
