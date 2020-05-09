package com.zsf.m_jetpack.view;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zsf.global.GlobalData;
import com.zsf.m_jetpack.R;
import com.zsf.m_jetpack.database.ZSFSQLiteOpenHelper;
import com.zsf.utils.ZsfLog;
import com.zsf.view.activity.BaseActivity;

@Route(path = "/m_jetpack/database/DatabaseActivity")
public class DatabaseActivity extends BaseActivity {
    private Button createDatabaseButton;
    private Button insertDatabaseButton;
    private Button upgradeDatabaseButton;
    private Button modifyDatabaseButton;
    private Button databaseDeleteButton;
    private Button queryDatabaseButton;
    private Button deleteDatabaseButton;
    private ZSFSQLiteOpenHelper zsfsqLiteOpenHelper;
    private String tableName = "user";
    private SQLiteDatabase sqLiteDatabaseWrite;
    private SQLiteDatabase sqLiteDatabaseRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Activity activity) {
        setContentView(R.layout.activity_database);
        createDatabaseButton = findViewById(R.id.m_jetpack_button_create_database);
        insertDatabaseButton = findViewById(R.id.m_jetopack_button_insert_database);
        upgradeDatabaseButton = findViewById(R.id.m_jetpack_button_upgrade_database);
        modifyDatabaseButton = findViewById(R.id.m_jetpack_button_modify_database);
        databaseDeleteButton = findViewById(R.id.m_jetpack_button_database_delete);
        queryDatabaseButton = findViewById(R.id.m_jetpack_button_query_database);
        deleteDatabaseButton = findViewById(R.id.m_jetpack_button_delete_database);
        createDatabaseButton.setOnClickListener(this);
        insertDatabaseButton.setOnClickListener(this);
        upgradeDatabaseButton.setOnClickListener(this);
        modifyDatabaseButton.setOnClickListener(this);
        databaseDeleteButton.setOnClickListener(this);
        queryDatabaseButton.setOnClickListener(this);
        deleteDatabaseButton.setOnClickListener(this);
    }

    @Override
    public void initData(Activity activity) {
        zsfsqLiteOpenHelper = new ZSFSQLiteOpenHelper(this,tableName,1);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.m_jetpack_button_create_database) {
            // 创建数据库：数据库实际上是没有被创建或者打开的，直到getWritableDatabase 或者 getReadableDatabase() 调用时才会创建
            sqLiteDatabaseWrite = zsfsqLiteOpenHelper.getWritableDatabase();
            sqLiteDatabaseRead = zsfsqLiteOpenHelper.getReadableDatabase();
            ZsfLog.d(DatabaseActivity.class, "创建数据库");
        } else if (id == R.id.m_jetopack_button_insert_database){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", 1);
            contentValues.put("name", "zsf");
            sqLiteDatabaseWrite.insert(tableName, null, contentValues);
        } else if (id == R.id.m_jetpack_button_upgrade_database){
            ZsfLog.d(DatabaseActivity.class, "更新数据库版本");
            zsfsqLiteOpenHelper = new ZSFSQLiteOpenHelper(GlobalData.getContext(), tableName, 2);
        } else if (id == R.id.m_jetpack_button_modify_database){
            ZsfLog.d(DatabaseActivity.class, "更新数据库数据");
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", "zgs");
            sqLiteDatabaseWrite.update(tableName, contentValues, "id=?", new String[]{"1"});
        } else if (id == R.id.m_jetpack_button_database_delete){
            sqLiteDatabaseWrite.delete(tableName, "id=?", new String[]{"1"});
        } else if (id == R.id.m_jetpack_button_query_database){
            Cursor cursor = sqLiteDatabaseRead.query(tableName, new String[]{"id", "name"}, "id=?", new String[]{"1"}, null, null, null);
            while (cursor.moveToNext()){
                String _id = cursor.getString(cursor.getColumnIndex("id"));
                String _name = cursor.getString(cursor.getColumnIndex("name"));
                String _sex = null;
                if (cursor.getColumnIndex("sex") != -1){
                    _sex = cursor.getString(cursor.getColumnIndex("sex"));
                }
                ZsfLog.d(DatabaseActivity.class, "查询数据：id = " + _id + "; _name = " + _name + "; _sex = " + _sex);
            }
        } else if (id == R.id.m_jetpack_button_delete_database){
            sqLiteDatabaseWrite.execSQL("DROP TABLE IF EXISTS "+ tableName);
        }
    }
}
