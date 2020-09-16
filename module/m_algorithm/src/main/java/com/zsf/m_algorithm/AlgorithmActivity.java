package com.zsf.m_algorithm;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.m_algorithm.binaryTree.BinaryTreeDisplay;
import com.zsf.m_algorithm.question.AlgorithmQuestion;
import com.zsf.m_algorithm.sort.SortDisplay;
import com.zsf.view.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: zsf
 * @Date: 2020-07-27 14:12
 */
@Route(path = "/m_algorithm/AlgorithmActivity")
public class AlgorithmActivity extends BaseActivity {

    /**
     * 二叉树前序
     */
    private Button algorithmBefore;
    /**
     * 二叉树中序
     */
    private Button algorithmIn;
    /**
     * 二叉树后续
     */
    private Button algorithmRear;
    /**
     * 创建二叉树
     */
    private Button algorithmCreateTree;
    /**
     * 节点个数
     */
    private Button algorithmNodeCount;

    /**
     * 冒泡排序
     */
    private Button algotithmSortBubble;

    /**
     * 快速排序(坑)
     */
    private Button algorithmSortFastPit;

    /**
     * 快速排序(指针)
     */
    private Button algorthmSortFastPointer;

    /**
     * 数组交集
     */
    private Button algorithmArrIntersection;

    /**
     * 数组-最长公共前缀
     */
    private Button algorithmArrPublicPrefix;

    /**
     * 数组-股票最佳买卖时间
     */
    private Button algorithmArrSaleTime;

    /**
     * 数组翻转
     */
    private Button algorithmArrSpin;

    /**
     * 原地删除
     */
    private Button algorithmArrInSituDelete;

    /**
     * 加一
     */
    private Button algorithmArrAddOne;

    /**
     * 清除按钮
     */
    private Button algorithmClear;
    /**
     * 日志
     */
    private TextView algorithmLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_algorithm);
        algorithmBefore = findViewById(R.id.m_algorithm_2tree_before);
        algorithmBefore.setOnClickListener(this);
        algorithmIn = findViewById(R.id.m_algorithm_2tree_in);
        algorithmIn.setOnClickListener(this);
        algorithmRear = findViewById(R.id.m_algorithm_2tree_Rear);
        algorithmRear.setOnClickListener(this);
        algorithmClear = findViewById(R.id.m_algorithm_clear);
        algorithmClear.setOnClickListener(this);
        algorithmLog = findViewById(R.id.m_algorithm_text);
        algorithmLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        algorithmCreateTree = findViewById(R.id.m_algorithm_create_2tree);
        algorithmCreateTree.setOnClickListener(this);
        algorithmNodeCount = findViewById(R.id.m_algorithm_2tree_node_count);
        algorithmNodeCount.setOnClickListener(this);
        algotithmSortBubble = findViewById(R.id.m_algorithm_bubble);
        algotithmSortBubble.setOnClickListener(this);
        algorithmSortFastPit = findViewById(R.id.m_algorithm_sort_fast_pit);
        algorithmSortFastPit.setOnClickListener(this);
        algorthmSortFastPointer = findViewById(R.id.m_algorithm_sort_fast_pointer);
        algorthmSortFastPointer.setOnClickListener(this);
        algorithmArrIntersection = findViewById(R.id.m_algorithm_arr_Intersection);
        algorithmArrIntersection.setOnClickListener(this);
        algorithmArrPublicPrefix = findViewById(R.id.m_algorithm_arr_public_prefix);
        algorithmArrPublicPrefix.setOnClickListener(this);
        algorithmArrSaleTime = findViewById(R.id.m_algorithm_arr_sale_time);
        algorithmArrSaleTime.setOnClickListener(this);
        algorithmArrSpin = findViewById(R.id.m_algorithm_arr_spin);
        algorithmArrSpin.setOnClickListener(this);
        algorithmArrInSituDelete = findViewById(R.id.m_algorithm_arr_in_situ_delete);
        algorithmArrInSituDelete.setOnClickListener(this);
        algorithmArrAddOne = findViewById(R.id.m_algorithm_arr_add_one);
        algorithmArrAddOne.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_algorithm_2tree_before) {
            setResultText("【前序遍历】: " + BinaryTreeDisplay.getInstance().binaryTreeBefore());
        } else if (id == R.id.m_algorithm_2tree_in) {
            setResultText("【中序遍历】: " + BinaryTreeDisplay.getInstance().binaryTreeIn());
        } else if (id == R.id.m_algorithm_2tree_Rear) {
            setResultText("【后序遍历】: " + BinaryTreeDisplay.getInstance().binaryTreeRear());
        } else if (id == R.id.m_algorithm_create_2tree) {
            setResultText("【创建二叉树】: " + BinaryTreeDisplay.getInstance().createBinaryTree());
        } else if (id == R.id.m_algorithm_2tree_node_count) {
            setResultText("【节点个数】: " + BinaryTreeDisplay.getInstance().getNodeCount());
        } else if (id == R.id.m_algorithm_clear) {
            clearLogText();
        } else if (id == R.id.m_algorithm_bubble) {
            setResultText("【冒泡排序】: \n" + SortDisplay.getInstance().sortBubble());
        } else if (id == R.id.m_algorithm_sort_fast_pit) {
            setResultText("【快速排序(坑)】: \n" + SortDisplay.getInstance().sortFastPit());
        } else if (id == R.id.m_algorithm_sort_fast_pointer) {
            setResultText("【快速排序(坑)】: \n" + SortDisplay.getInstance().sortFastPointer());
        } else if (id == R.id.m_algorithm_arr_Intersection) {
            setResultText("【数组交集】: \n" + AlgorithmQuestion.getInstance().arrIntersection());
        } else if (id == R.id.m_algorithm_arr_public_prefix) {
            setResultText("【数组公共前缀】: \n" + AlgorithmQuestion.getInstance().arrPublicPrefix());
        } else if (id == R.id.m_algorithm_arr_sale_time) {
            setResultText("【股票最佳买卖时间及收益】: \n" + AlgorithmQuestion.getInstance().stockBestInterest());
        } else if (id == R.id.m_algorithm_arr_spin) {
            setResultText("【旋转数组】: \n" );
        }
    }

    private void clearLogText(){
        algorithmLog.setText("");
        // 重置数据
        SortDisplay.getInstance().reset();
    }

    private void setResultText(final String result){
        algorithmLog.post(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat alldate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");//获取日期时间
                algorithmLog.append("\n" + alldate.format(new Date()) + "   " + result);
                int scrollAmount = algorithmLog.getLayout().getLineTop(algorithmLog.getLineCount()) - algorithmLog.getHeight();
                if (scrollAmount > 0) {
                    algorithmLog.scrollTo(0, scrollAmount + 100);
                } else {
                    algorithmLog.scrollTo(0, 0);
                }
            }
        });
    }
}
