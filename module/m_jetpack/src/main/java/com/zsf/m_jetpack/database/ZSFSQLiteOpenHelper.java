package com.zsf.m_jetpack.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.zsf.utils.ZsfLog;

/**
 * @author zsf
 * @date 2019/12/7
 * @Usage
 */
public class ZSFSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 构造函数
     * @param context
     * @param name  数据库名称
     * @param factory   可选参数
     * @param version   当前数据库版本，值必须是整数并且是递增状态
     */
    public ZSFSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public ZSFSQLiteOpenHelper(Context context, String name,int version) {
        this(context, name, null, version);
    }

    /**
     * 调用时机： 当数据库第一次创建时调用
     * 作用： 创建数据库表 & 初始化数据
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        ZsfLog.d(this.getClass(), "创建数据库和表");
        String sql = "create table user(id int primary key, name varchar(20))";
        // execSQL 用于执行SQL语句
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 调用时机：当数据库升级时自动调用（数据库版本变化时）
     * 作用：更新数据库表结构
     * @param sqLiteDatabase
     * @param oldVersion  旧版本数据库
     * @param newVersion  新版本数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "alter table user add sex varchar(8) default ('2004')";
        sqLiteDatabase.execSQL(sql);
        ZsfLog.d(this.getClass(), "更细数据库版本：" + newVersion);
    }
}
