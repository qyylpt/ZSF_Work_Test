package com.zsf.m_algorithm;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zsf.m_algorithm.binaryTree.BinaryTreeDisplay;
import com.zsf.m_algorithm.sort.SortDisplay;
import com.zsf.view.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: zsf
 * @Date: 2020-07-27 14:12
 */
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
    }

    @Override
    public void initData(Activity activity) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.m_algorithm_2tree_before:
                setResultText("前序遍历: " + BinaryTreeDisplay.getInstance().binaryTreeBefore());
                break;
            case R.id.m_algorithm_2tree_in:
                setResultText("中序遍历: " + BinaryTreeDisplay.getInstance().binaryTreeIn());
                break;
            case R.id.m_algorithm_2tree_Rear:
                setResultText("后序遍历: " + BinaryTreeDisplay.getInstance().binaryTreeRear());
                break;
            case R.id.m_algorithm_create_2tree:
                setResultText("创建二叉树: " + BinaryTreeDisplay.getInstance().createBinaryTree());
                break;
            case R.id.m_algorithm_2tree_node_count:
                setResultText("节点个数: " + BinaryTreeDisplay.getInstance().getNodeCount());
                break;
            case R.id.m_algorithm_clear:
                clearLogText();
                break;
            case R.id.m_algorithm_bubble:
                setResultText("冒泡排序: \n" + SortDisplay.getInstance().sortBubble());
            default:
                break;
        }
    }

    private void clearLogText(){
        algorithmLog.setText("");
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
