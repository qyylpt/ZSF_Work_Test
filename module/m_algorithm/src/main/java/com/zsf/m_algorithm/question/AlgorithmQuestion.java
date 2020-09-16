package com.zsf.m_algorithm.question;

import com.zsf.utils.Singleton;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author: zsf
 * @Date: 2020-08-26 15:37
 */
public class AlgorithmQuestion {

    private static AlgorithmQuestion ALGORITHM_QUESTION;

    static Singleton<AlgorithmQuestion> algorithmQuestionSingleton = new Singleton<AlgorithmQuestion>() {
        @Override
        protected AlgorithmQuestion create() {
            return new AlgorithmQuestion();
        }
    };

    public static AlgorithmQuestion getInstance() {
        ALGORITHM_QUESTION = algorithmQuestionSingleton.get();
        return ALGORITHM_QUESTION;
    }

    /**
     * 查找无序数组交集数据
     * 提示: 1.把一个数组值为键, 1 为键放入一个map中
     *      2.遍历另外一个数组如果map包含这个key,将value加1
     *      3,遍历map中所有value大于 1 的元素,既可以得到所有交集元素及出现次数。
     *
     * @return
     */
    public String arrIntersection() {
        // 初始化数据
        int[] arrLong = {1, 3, 5, 3, 7, 3, 9, 0 ,2, 4, 6, 8};
        int[] arrShort = {1, 2, 3, 4};

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("原始数据 1 : " + Arrays.toString(arrLong) +  "\n原始数据 2 : " + Arrays.toString(arrShort));

        String intersection = "";
        // 寻找最小长度数组(这是交集最大的限度)
        if (arrLong.length >= arrShort.length){
            intersection = Intersection(arrLong, arrShort);
        } else {
            intersection = Intersection(arrShort, arrLong);
        }
        stringBuilder.append("\n无序数组交集 : " + intersection);

        stringBuilder.append(arrIntersectionOrderly());
        return stringBuilder.toString();
    }

    private String Intersection(int[] arrLong, int[] arrShort) {
        HashMap<Integer, Integer> hashMap = new HashMap();
        ArrayList<Integer> tmp = new ArrayList<>();

        for (int i = 0; i < arrShort.length; i++) {
            hashMap.put(arrShort[i], 1);
        }
        for (int i = 0; i < arrLong.length; i++) {
            if (hashMap.containsKey(arrLong[i])) {
                if (!tmp.contains(arrLong[i])) {
                    tmp.add(arrLong[i]);
                }
            }
        }
        return Arrays.toString(tmp.toArray());
    }


    /**
     * 查找有序数组交集
     * 提示: 双指针交换法
     *      1.选择短的数组为基准数组
     *      2.遍历两个数组
     *      3.对比数组元素,如果元素小于另外一个数组元素,指针往后移动一位继续对比
     *      4.如果数组元素相等,修改标记位元素。
     *      5.截取 0 - 标记位 元素为有序数组交集
     *
     * @return
     */
    public String arrIntersectionOrderly() {
        // 默认数据
        int[] arrLong = {1, 2, 3, 4, 5, 6};
        int[] arrShort = {1, 2, 5};

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n有序数组交集 : \n");
        stringBuilder.append("原始数据 1: " + Arrays.toString(arrLong) + "\n");
        stringBuilder.append("原始数据 2: " + Arrays.toString(arrShort) + "\n");

        int iLong = 0, iShort = 0, iIndex = 0;

        while (iLong < arrLong.length && iShort < arrShort.length) {
            if (arrLong[iLong] > arrShort[iShort]) {
                iShort++;
            }
            if (arrLong[iLong] < arrShort[iShort]) {
                iLong++;
            }
            if (arrLong[iLong] == arrShort[iShort]) {
                arrLong[iIndex] = arrLong[iLong];
                iIndex++;
                iLong++;
                iShort++;
            }
        }
        stringBuilder.append("有序数组交集 :" + Arrays.toString(Arrays.copyOfRange(arrLong, 0, iIndex)));
        return stringBuilder.toString();
    }

    /**
     * 获取数组的公共前缀
     * 提示: 由于是字符串的公共前缀，就意味着每个字符串中都包含这些元素。
     *      1.定义一个基准元素
     *      2.使用这个基准元素依次与后面元素对比,如果不相等就把基准元素缩短一位,直到寻找到对比相等的位置。
     *      3.然后重复第二步的操作
     *
     * @return
     */
    public String arrPublicPrefix() {
        // 初始化数据
        String[] arrString = {"sdvavsdc", "sdfasdv", "sdvavvv", "sdsfwaeb" };

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("原始数据 : " + Arrays.toString(arrString) + "\n");

        // 基准元素
        String prefix = arrString[0];
        for (int i = 1; i < arrString.length; i++) {
            while (!arrString[i].startsWith(prefix)) {
                int length = prefix.length() - 1;
                if (length == 0) {
                    prefix = "";
                    break;
                }
                prefix = prefix.substring(0, length);
            }
        }
        stringBuilder.append("公共前缀 : " + prefix);
        return stringBuilder.toString();
    }


    /**
     * 获取股票最大利益
     * 提示: 寻找 (符合 k < k + 1 条件) 购买点 k;
     *      寻找 (符合 k > k + 1 条件) 卖出点 k;
     *
     * @return
     */
    public String stockBestInterest() {
        // 初始化数据
//        int[] arrStock = {7, 2, 4, 4, 3, 7};

        int[] arrStock = {1, 4, 10, 3, 3, 2, 7};

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("原始数据 : " + Arrays.toString(arrStock) + "\n");

        // 买卖初始值
        int buy = arrStock[0];
        int sell = 0;

        int maxInterest = 0;
        stringBuilder.append("初始购买点 : " + buy + "\n");
        for (int i = 1; i < arrStock.length; i++) {
            if (arrStock[i] > buy){
                if (i == arrStock.length -1) {
                    stringBuilder.append("卖点 : " + arrStock[i] + "\n");
                    maxInterest += arrStock[i] - buy;
                    break;
                }
                if (arrStock[i] > arrStock[i + 1]) {
                    maxInterest += arrStock[i] - buy;
                    stringBuilder.append("卖点 : " + arrStock[i] + "\n");
                    buy = arrStock[i+1];
                    stringBuilder.append("购买点 : " + buy + "\n");
                }
            } else if (arrStock[i] < buy){
                buy = arrStock[i];
                stringBuilder.append("购买点 : " + buy + "\n");
            }
        }

        stringBuilder.append("股票最大收益 : " + maxInterest);

        return stringBuilder.toString();
    }


    /**
     * 数组翻转
     *
     * 例如: {1,2,3,4,5,6,7,8}  k = 3 预期结果: {6,7,8,1,2,3,4,5}
     * 1.翻转整个数组结果: {8,7,6,5,4,3,2,1}
     * 2.根据K值把数组分为三段 8,7,6 | 5,4,3,2,1 两段,分别翻转。
     *
     * @return
     */
    public String arrSpin() {
        // 初始化数据
        int[] arrSpin = {1, 2, 3, 4, 5, 6, 7, 8};
        int k = 3;
        int length = arrSpin.length;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("原始数据 : "  + Arrays.toString(arrSpin) + "k = " + 3 + "\n");

        // 翻转整个数组
        int index = length / 2;
        for (int i = 0; i < index; i++) {
            int tmp = arrSpin[i];
            arrSpin[i] = arrSpin[length - 1 - i];
            arrSpin[length - 1 - i] = tmp;
        }
        stringBuilder.append("翻转之后 : " + Arrays.toString(arrSpin) + "\n");



        return stringBuilder.toString();

    }


}
