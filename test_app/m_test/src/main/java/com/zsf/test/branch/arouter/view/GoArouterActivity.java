package com.zsf.test.branch.arouter.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zsf.test.R;
import com.zsf.test.branch.arouter.entry.TestObj;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

import java.util.List;

/**
 * @author zsf; 2019/7/31
 */
@Route(path = "/branch/ARouter/Go_ARouter_Activity")
public class GoArouterActivity extends BaseActivity {
    @Autowired
    public String keyOne;

    @Autowired
    public int keyTwo;

    @Autowired
    public List<TestObj> list;


    private TextView backArouterActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);

    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_go__arouter);
        backArouterActivity = findViewById(R.id.back_ARouter_Activity);
        backArouterActivity.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {
        // 通过Intent的方式获取参数
        List<TestObj> chooseList = (List<TestObj>) getIntent().getSerializableExtra("pac");
        if (chooseList != null){
            ZsfLog.d(this.getClass(), "ARouter传递序列化list_serializables size = " + chooseList.size());
        }
        ZsfLog.d(this.getClass(), "ARouter获取传递参数：key_one = " + keyOne + "; key_two = " + keyTwo);
        ZsfLog.d(this.getClass(), "ARouter获取传递参数List ： " + list);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_ARouter_Activity){
            setResult(200, null);
            list.clear();
            list = null;
            finish();
        }
    }
}
