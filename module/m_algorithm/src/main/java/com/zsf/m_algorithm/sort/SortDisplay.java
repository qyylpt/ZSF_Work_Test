package com.zsf.m_algorithm.sort;

import com.zsf.utils.Singleton;
import com.zsf.utils.ZsfLog;

import java.util.Arrays;

/**
 * @Author: zsf
 * @Date: 2020-07-31 11:27
 */
public class SortDisplay {

    private int[] sort;

    private static Singleton<SortDisplay> sortDisplaySingleton = new Singleton<SortDisplay>() {
        @Override
        protected SortDisplay create() {
            return new SortDisplay();
        }
    };

    public static SortDisplay getInstance() {
        return sortDisplaySingleton.get();
    }

    private SortDisplay() {
        initData();
    }

    private void initData() {

    }

    /**
     * 冒泡排序
     */
    public String sortBubble() {
        boolean flag = false;
        StringBuilder stringBuilder = new StringBuilder();
//        sort = new int[]{7, 9, 3, 4, 8, 5, 6, 1, 2};
        sort = new int[]{1,2,3,4,5,6,7,8,9};
        stringBuilder.append("原始数据: " + Arrays.toString(sort) + "\n");
        for (int i = 0; i < sort.length; i++) {

            for (int j = 0; j < sort.length - i -1; j++) {
                if (sort[j] > sort[j+1]) {
                    int tmp = sort[j];
                    sort[j] = sort[j+1];
                    sort[j+1] = tmp;
                    flag = true;
                }
            }
            if (!flag) {
                ZsfLog.d(SortDisplay.class, "111");
                break;
            }

        }
        stringBuilder.append("冒泡排序: " + Arrays.toString(sort));
        return stringBuilder.toString();
    }


}
