package com.zsf.test.branch.test;


import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.zsf.global.GlobalData;
import com.zsf.test.Manifest;
import com.zsf.utils.ToastUtils;

import java.util.Random;


/**
 * @author zsf
 * @date 2019/10/22
 */
public class TestUtils {


    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            ToastUtils.showToast(context, packagename + " : 没有安装!");
            //System.out.println("没有安装");
            return false;
        } else {
            ToastUtils.showToast(context, packagename + " : 已经安装!");
            //System.out.println("已经安装");
            return true;
        }
    }

    public static void main(String[] args) {
        Store store = new Store();
        // 开启两个生产线程
        new Thread(new Producer(store), "生产者1").start();
        new Thread(new Producer(store), "生产者2").start();
        new Thread(new Producer(store), "生产者3").start();
        new Thread(new Producer(store), "生产者4").start();
        new Thread(new Producer(store), "生产者5").start();

        // 开启三个消费线程
        new Thread(new Consumer(store), "消费者1").start();
        new Thread(new Consumer(store), "消费者2").start();
        new Thread(new Consumer(store), "消费者3").start();
    }

/**
 * =================================================================================================
 */
    /** 仓库 */
    static class Store {

        // 已经存储的数量
        public volatile int count = 0;

        // 最大容量
        private int MAX_COUNT = 5;

        public synchronized void store() {
            try {
                Thread.sleep(new Random().nextInt(300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 当超过最大容量的时候，生成操作开始等待
            while (count >= MAX_COUNT) {
                try {
                    System.out.println(Thread.currentThread().getName() + " begin waiting, store count: " + count);
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 否则进行生产
            count++;
            System.out.println(Thread.currentThread().getName() + " produced, store count: " + count);
            // 叫醒所有的等待线程，主要是为了叫醒等待的消费者线程
            notifyAll();
        }

        public synchronized void consum() {
            try {
                Thread.sleep(new Random().nextInt(150));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 当仓库为空时，消费操作开始等待
            while (count <= 0) {
                try {
                    System.out.println(Thread.currentThread().getName() + " begin waiting, store count: " + count);
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 否则进行消费
            count--;
            System.out.println(Thread.currentThread().getName() + " consumed, store count: " + count);
            // 叫醒所有的等待线程，主要是为了叫醒等待的生产者线程
            notifyAll();
        }
    }

    /** 生产者 */
    static class Producer implements Runnable {

        private Store store;

        public Producer(Store store) {
            this.store = store;
        }

        @Override
        public void run() {
            // 开始不断生成
            while (true) {
                store.store();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 消费者 */
    static class Consumer implements Runnable {

        private Store store;

        public Consumer(Store store) {
            this.store = store;
        }

        @Override
        public void run() {
            // 开始不断消费
            while (true) {
                store.consum();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
