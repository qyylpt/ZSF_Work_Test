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
        sort = new int[]{7, 9, 3, 4, 4, 8, 5, 6, 1, 2};
//        sort = new int[]{1,2,3,4,5,6,7,8,9};
    }

    public void reset() {
        initData();
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
            // 如果原始数据本来就是有序的,flag值将不会改变

            if (!flag) {
                ZsfLog.d(SortDisplay.class, "111");
                break;
            }

        }
        stringBuilder.append("冒泡排序: " + Arrays.toString(sort));
        return stringBuilder.toString();
    }

    /**
     * 快速排序 挖坑法
     * https://www.cxyxiaowu.com/5262.html
     * @return
     */
    public String sortFastPit() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("原生数据: " + Arrays.toString(sort) + "\n");
        quickSortPit(sort, 0, sort.length - 1);
        stringBuilder.append("快速排序(坑): " + Arrays.toString(sort));
        return stringBuilder.toString();
    }

    private void quickSortPit(int[] arr, int start, int end) {
        // 结束递归
        if (start >= end) {
            return;
        }

        // 获取基准元素位置
        int pivotIndex = partitionPit(arr, start, end);

        // 分治法递归基准点两侧数据
        quickSortPit(arr, start, pivotIndex - 1);
        quickSortPit(arr, pivotIndex + 1, end);
    }

    private int partitionPit(int[] arr, int start, int end) {

        // 获取数组第一个元素为 <基准元素>
        int pivot = arr[start];
        // 坑位置,数组第一个初始为坑
        int pitIndex = start;

        // 左侧开始位置
        int leftIndex = start;
        // 右侧开始位置
        int rightIndex = end;

        // 大循环在左右指针重合或者交错的时候结束
        while (rightIndex >= leftIndex) {

            // right指针从尾部开始比较
            while (rightIndex > leftIndex) {
                if (arr[rightIndex] < pivot) {
                    // 把小于 <基准元素> 的rightIndex值填到坑(pitIndex)坐标
                    arr[pitIndex] = arr[rightIndex];
                    //  更新坑的位置: 把当前rightIndex位置作为新的坑
                    pitIndex = rightIndex;
                    // 更新 left 指针到坑右侧位置
                    leftIndex++;

                    // 这里仅用于打印的时候区分坑的数据来源
                    arr[rightIndex] = -100;
                    break;
                }
                // 如果移动right指针到新的位置
                rightIndex = rightIndex - 1;
            }

            // 打印第一轮
            systemOut(arr);

            // left指针从头部开始比较
            while (rightIndex >= leftIndex) {
                if (arr[leftIndex] > pivot) {
                    // 把大于 <基准元素> 的leftIndex值填写到(pitIndex)坐标
                    arr[pitIndex] = arr[leftIndex];
                    // 更新坑的位置: 把当前leftIndex位置作为新的坑
                    pitIndex = leftIndex;
                    // 更新 right 指针到坑的左侧位置
                    rightIndex--;

                    // 这里仅用于打印的时候区分坑的数据来源
                    arr[leftIndex] = -100;
                    break;
                }
                leftIndex = leftIndex + 1;
            }

            // 打印第二轮
            systemOut(arr);

        }
        // 最后把初始的 <基准元素> 填入最后的坑中
        arr[pitIndex] = pivot;
        return pitIndex;
    }

    /**
     * 快速排序(指针)
     * https://www.cxyxiaowu.com/5262.html
     * @return
     */
    public String sortFastPointer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("原始数据: " + Arrays.toString(sort) + "\n");
        quickSortPointer(sort, 0, sort.length - 1);
        stringBuilder.append("快速排序(指针): " + Arrays.toString(sort));
        return stringBuilder.toString();
    }

    private void quickSortPointer(int[] arr, int start, int end) {
        if (start >= end){
            return;
        }

        int pivotIndex = partitionPointer(arr, start, end);
        quickSortPointer(arr, start, pivotIndex - 1);
        quickSortPointer(arr, pivotIndex + 1, end);
    }

    private int partitionPointer(int[] arr, int start, int end) {
        // 基准元素值
        int pivot = arr[start];

        // 左侧指针开始位置
        int leftIndex = start;
        // 右侧指针开始位置
        int rightIndex = end;

        // 指针重合结束本轮指针互换
        while (leftIndex != rightIndex) {

            while (leftIndex < rightIndex && arr[rightIndex] > pivot) {
                rightIndex--;
            }

            while (leftIndex < rightIndex && arr[leftIndex] <= pivot) {
                leftIndex++;
            }

            if (leftIndex < rightIndex) {
                int tmp = arr[leftIndex];
                arr[leftIndex] = arr[rightIndex];
                arr[rightIndex] = tmp;
            }
            systemOut(arr);
        }
        int p = arr[leftIndex];
        arr[leftIndex] = pivot;
        arr[start] = p;
        return leftIndex;
    }

    /**
     * 打印当前数组顺序
     * @param arr
     */
    private void systemOut(int[] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length -1){
                stringBuilder.append(arr[i] + ";");
            } else {
                stringBuilder.append(arr[i] + ",");
            }
        }
        ZsfLog.d(SortDisplay.class, stringBuilder.toString());
    }

}
