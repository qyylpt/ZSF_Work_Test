package com.zsf.test.branch.memory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.test.R;
import com.zsf.test.branch.diffutil.TestBean;
import com.zsf.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author zsf
 * @date 2019/08/07
 */
@Route(path = "/branch/Memory/MemoryOptimizeActivity")
public class MemoryOptimizeActivity extends BaseActivity {
    private Button buttonAddMemory;
    private Button buttonDeleteMemory;
    private List<TestBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_memory_optimize);
        buttonAddMemory = findViewById(R.id.add_object);
        buttonAddMemory.setOnClickListener(this);
        buttonDeleteMemory = findViewById(R.id.delete_object);
        buttonDeleteMemory.setOnClickListener(this);

    }

    @Override
    public void initData(Activity activity) {
        list = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.add_object) {
            addObject();
        } else if (i == R.id.delete_object) {
            deleteObject();
        }
    }

    /**
     * 添加对象
     */
    private void addObject() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 通过把比较耗时的cpu活动放入到闲时任务中执行来提高UI的流畅度
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                for (int i = 0; i < 10000; i++){
                    list.add(new TestBean("name" + i, "描述" + i, i));
                }
                return false;
            }
        });


    }

    /**
     * 删除对象
     */
    private void deleteObject() {
        list.clear();
        list = null;
        finish();
    }
}
