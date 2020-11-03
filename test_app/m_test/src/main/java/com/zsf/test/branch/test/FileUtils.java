package com.zsf.test.branch.test;

import com.zsf.utils.ZsfLog;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author : zsf
 * @date : 2020/11/3 10:57 AM
 * @desc : 广度与深度执行时间差不多，只是连续执行的时候，后执行的因为有缓存在所以相对快点
 */
public class FileUtils {

    static long dirCount = 0;
    static long fileCount = 0;

    public static void main(String[] args) {
        File file = new File("/sdcard/");
        traverQueue(file);

        // 广度与深度执行时间差不多，只是连续执行的时候，后执行的因为有缓存在所以相对快点。可以注释之后对比效果
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        traverStack(file);

        long startTime = System.currentTimeMillis();
        traverFile1(file);
        String result = String.format("递归遍历 扫描完成! 文件[%d] 目录[%d]", fileCount, dirCount);
        System.out.println(result);
        long endTime = System.currentTimeMillis();
        long cost = (endTime - startTime) / 1000;
        System.out.println("递归遍历 cost: " + cost);
        ZsfLog.d(FileUtils.class, ":" + cost);
    }

    /**
     * 广度遍历
     *
     * @param file
     */
    private static void traverQueue(File file) {
        long startTime = System.currentTimeMillis();
        long dirCount = 0;
        long fileCount = 0;
        LinkedList<File> list = new LinkedList<>();
        list.add(file);
        while (!list.isEmpty()) {
            File f = list.poll();
            if (f.isFile()) {
//                System.out.println("F: " + f);
                fileCount++;
            } else if (f.isDirectory()) {
//                System.out.println("D: " + f);
                dirCount++;
                File[] files = f.listFiles();
                if (files != null) {
//                    list.addAll(Arrays.asList(files));
                    for (File fil : files) {
                        list.addLast(fil);
                    }
                }
            }
        }
        String result = String.format("广度遍历 扫描完成! 文件[%d] 目录[%d]", fileCount, dirCount);
        System.out.println(result);
        long endTime = System.currentTimeMillis();
        long cost = (endTime - startTime);
        System.out.println("广度遍历 cost: " + cost);
        ZsfLog.d(FileUtils.class, ":" + cost);
    }

    /**
     * 深度遍历
     *
     * @param file
     */
    private static void traverStack(File file) {
        long startTime = System.currentTimeMillis();
        long dirCount = 0;
        long fileCount = 0;
        LinkedList<File> list = new LinkedList<>();
        list.add(file);
        while (!list.isEmpty()) {
            File f = list.pop();
            if (f.isFile()) {
//                System.out.println("F: " + f);
                fileCount++;
            } else if (f.isDirectory()) {
//                System.out.println("D: " + f);
                dirCount++;
                File[] files = f.listFiles();
                if (files != null) {
                    for (File fil : files) {
                        list.push(fil);
                    }
                }
            }
        }
        String result = String.format("深度遍历 扫描完成! 文件[%d] 目录[%d]", fileCount, dirCount);
        System.out.println(result);
        long endTime = System.currentTimeMillis();
        long cost = (endTime - startTime);
        System.out.println("深度遍历 cost: " + cost);
        ZsfLog.d(FileUtils.class, ":" + cost);
    }

    private static void traverFile1(File file) {
//        long startTime = System.currentTimeMillis();
//        long dirCount = 0;
//        long fileCount = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    dirCount++;
//                    System.out.println("D: " + f);
                    traverFile1(f);
                } else if (f.isFile()) {
                    fileCount++;
//                    System.out.println("F: " + f);
                }
            }
        }
//        String result = String.format("递归遍历 扫描完成! 文件[%d] 目录[%d]", fileCount, dirCount);
//        System.out.println(result);
//        long endTime = System.currentTimeMillis();
//        long cost = (endTime - startTime) / 1000;
//        System.out.println("递归遍历 cost: " + cost);
    }


}
