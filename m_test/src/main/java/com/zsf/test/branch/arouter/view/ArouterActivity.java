package com.zsf.test.branch.arouter.view;

import androidx.annotation.Nullable;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zsf.arouter.AbstractArouterNavCallback;
import com.zsf.global.GlobalData;
import com.zsf.test.R;
import com.zsf.test.branch.arouter.entry.TestObj;
import com.zsf.test.branch.arouter.entry.TestObjSerializable;
import com.zsf.test.branch.arouter.service.IService;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;
import com.zsf.view.fragment.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zsf; 2019/7/31
 */
@Route(path = "/branch/ARouter/ARouter_Activity")
public class ArouterActivity extends BaseActivity {

    /**
     * 跳转到Activity
     */
    private Button buttonArouterGoActivity;
    /**
     * 跳转到Fragment
     */
    private Button getButtonArouterGoFragment;
    private Button buttonArouterService;
    private Button buttonArouterInterceptor;
    private Button buttonArouterDegrade;
    private Button buttonArouterPretreatment;
    private int initNum = 5;
    private List<TestObj> list = new ArrayList<>();
    private List<TestObjSerializable> listSerializable = new ArrayList<>();

    /**
     * 以下url请求,注入参数
     */
    @Autowired
    String name;

    @Autowired
    int age;

    @Autowired
    boolean boy;

    @Autowired
    int height;

    @Autowired
    String obj;

    /**
     * 用于判断是通过应用内路由打开 or 浏览器请求转发
     */
    @Autowired
    String requestType;

    /**
     * 显示通过浏览器请求数据
     */
    private TextView textGetUrlParams;

    /**
     * ARouter 暴露服务
     * Autowired注解中标注name之后，将会使用byName的方式注入对应的字段，不设置name属性，会默认使用byType的方式发现服务
     * (当同一接口有多个实现的时候，必须使用byName的方式发现服务)
     */
    @Autowired(name = "/ARouter/ARouterService")
    IService service;
    /**
     * ARouter 暴露服务 无name标注
     */
    @Autowired
    IService no_name_service;

    /**
     * 使用依赖查找的方式发现服务，主动去发现服务并使用，下面两种方式分别是byName和byType
     */
    IService by_name_Arouter_service;
    IService by_type_Arouter_service;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUrlRequest();
        ARouter.getInstance().inject(this);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_arouter);
        buttonArouterGoActivity = findViewById(R.id.button_ARouter_go_activity);
        buttonArouterGoActivity.setOnClickListener(this);
        getButtonArouterGoFragment = findViewById(R.id.button_ARouter_go_fragment);
        getButtonArouterGoFragment.setOnClickListener(this);
        textGetUrlParams = findViewById(R.id.text_get_url_params);
        buttonArouterService = findViewById(R.id.button_ARouter_service);
        buttonArouterService.setOnClickListener(this);
        buttonArouterInterceptor = findViewById(R.id.button_ARouter_interceptor);
        buttonArouterInterceptor.setOnClickListener(this);
        buttonArouterDegrade = findViewById(R.id.button_ARouter_Degrade_service);
        buttonArouterDegrade.setOnClickListener(this);
        buttonArouterPretreatment = findViewById(R.id.button_ARouter_pretreatment_service);
        buttonArouterPretreatment.setOnClickListener(this);
    }
    /**
     * 初始化ARouter传递数据
     */
    @Override
    public void initData(Activity activity) {
        for (int i = 0; i < initNum; i++){
            TestObj testObj = new TestObj("TestObj" + i, i);
            list.add(testObj);
            TestObjSerializable testObjSerializable = new TestObjSerializable("TestObj_Serializable" + i, i);
            listSerializable.add(testObjSerializable);
        }
    }

    /**
     * 浏览器启动转发类型参数获取
     */
    private void initUrlRequest() {
        ARouter.getInstance().inject(this);
        if (requestType != null){
            if (requestType.equals(GlobalData.getContext().getResources().getString(R.string.m_test_open_ARouter_type))){
                textGetUrlParams.setVisibility(View.VISIBLE);
                textGetUrlParams.setText("通过浏览器获取到的参数是： " + "name: " + name + "  age: " + age + " boy: " + boy  + " obj: " + obj);
                ZsfLog.d(ArouterActivity.class, "参数是： " + "name: " + name + "  age: " + age + " boy: " + boy  + " obj: " + obj);
            }
        }
    }




    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_ARouter_go_activity){
            ARouter.getInstance().build("/ARouter/Go_ARouter_Activity")
                    /**
                     * 1.使用 withObject 传递 List 和 Map 的实现了Serializable 接口的实现类(ArrayList/HashMap)的时候，
                     *   接收该对象的地方不能标注具体的实现类类型, 应仅标注为 List 或 Map，否则会影响序列化中类型,的判断,
                     *   其他类似情况需要同样处理
                     * 2.可以采用withParcelable，在目标Activity中直接通过注入的方式获取对象即可。
                     * 3.对于withObject，ARouter会将其转换为json字符串，所以在目标Activity中获取的时候，可以通过
                     *   @Autowired public UserBean key2;注解来注入直接使用，但是前提是实现ARouter的SerializationService接口。
                     */
                    .withObject("list", list)
                    .withSerializable("pac", (Serializable)listSerializable)
                    .withString("keyOne", "value_one")
                    .withInt("keyTwo", 888)
                    .navigation(this, 100, new AbstractArouterNavCallback() {
                        @Override
                        public void onFound(Postcard postcard) {
                            super.onFound(postcard);
                        }

                        @Override
                        public void onLost(Postcard postcard) {
                            super.onLost(postcard);
                        }

                        @Override
                        public void onArrival(Postcard postcard) {
                            super.onArrival(postcard);
                        }

                        @Override
                        public void onInterrupt(Postcard postcard) {
                            super.onInterrupt(postcard);
                        }
                    });
        } else if (view.getId() == R.id.button_ARouter_go_fragment){
            BaseFragment baseFragment = (GoArouterFragment) ARouter.getInstance()
                    .build("/branch/ARouter/Go_ARouter_Fragment")
                    .withString("key_one", "value_one")
                    .navigation();
            ZsfLog.d(ArouterActivity.class, baseFragment.getClass().getSimpleName());
            BaseFragment.addFragment(getSupportFragmentManager(), baseFragment.getClass(), R.id.fragment_ARouter, null);
        } else if (view.getId() == R.id.button_ARouter_service){
            service.sayHello(GlobalData.getContext().getString(R.string.m_test_ARouter_IProvider_service_name));
            no_name_service.sayHello(GlobalData.getContext().getString(R.string.m_test_ARouter_IProvider_service_default));
            by_type_Arouter_service = ARouter.getInstance().navigation(IService.class);
            by_type_Arouter_service.sayHello(GlobalData.getContext().getString(R.string.m_test_ARouter_IProvider_service_by_type));
            by_name_Arouter_service = (IService) ARouter.getInstance().build("/ARouter/ARouterService").navigation();
            by_name_Arouter_service.sayHello(GlobalData.getContext().getString(R.string.m_test_ARouter_IProvider_service_by_name));
        } else if (view.getId() == R.id.button_ARouter_interceptor){
            // 路由拦截
            ARouter.getInstance()
                    .build("/branch/ARouter/NeedLoginActivity")
                    .navigation();
        } else if (view.getId() == R.id.button_ARouter_pretreatment_service){
            // 这里随意定义一个路由用来预处理
            ARouter.getInstance()
                    .build("/branch/ARouter/NeedLoginActivity")
                    .setTag("预处理")
                    .navigation();
        } else if (view.getId() == R.id.button_ARouter_Degrade_service){
            // 这里随意定义一个不存在的路由用于降级测试
            ARouter.getInstance()
                    .build("/branch/ARouter/NoActivity")
                    .navigation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "请求Code = " + requestCode + ", 返回Code = " + resultCode, Toast.LENGTH_LONG).show();
    }



}
