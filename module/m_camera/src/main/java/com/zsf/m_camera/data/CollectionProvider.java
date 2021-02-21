package com.zsf.m_camera.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zsf.global.GlobalData;
import com.zsf.m_camera.ZLog;

import java.util.Arrays;

/**
 * @author : zsf
 * @date : 2021/2/3 11:57 AM
 * @desc :
 */
public class CollectionProvider extends ContentProvider {

    private final String TAG = "CollectionProvider";
    private CollectionSqliteOpenHelper collectionSqliteOpenHelper;

    public static final String AUTOHORITY = "com.zsf.m_camera.CollectionProvider.FileReport";;

    public static final int COLLECTION_INFO_CODE = 0;
    private static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTOHORITY, DataContract.COLLECTION_FILE_TABLE, COLLECTION_INFO_CODE);
    }

    public static final Uri collectionUri = Uri.parse("content://" + CollectionProvider.AUTOHORITY + "/" + CollectionProvider.DataContract.COLLECTION_FILE_TABLE);

    @Override
    public boolean onCreate() {
        collectionSqliteOpenHelper = new CollectionSqliteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTable(uri);
        if (TextUtils.isEmpty(tableName)) {
            return null;
        }
        SQLiteDatabase db = collectionSqliteOpenHelper.getReadableDatabase();
        Cursor c = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        ZLog.d(TAG, "query : projection = " + Arrays.toString(projection) + "; selection = " + selection + "; selectionArgs = " + Arrays.toString(selectionArgs) + "; sortOrder = " + sortOrder);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTable(uri);
        if (TextUtils.isEmpty(tableName)) {
            return null;
        }
        final SQLiteDatabase db = collectionSqliteOpenHelper.getWritableDatabase();
        long id = db.replace(tableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        ZLog.d(TAG, "insert : values = " + values + "; insert id is " + id);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTable(uri);
        if (TextUtils.isEmpty(tableName)) {
            return 0;
        }
        final SQLiteDatabase db = collectionSqliteOpenHelper.getWritableDatabase();
        int rows = db.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        ZLog.d(TAG, "delete : selection = " + selection + "; selectionArgs = " + Arrays.toString(selectionArgs) + "; delete rows = " + rows);
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTable(uri);
        if (TextUtils.isEmpty(tableName)) {
            return 0;
        }
        SQLiteDatabase db = collectionSqliteOpenHelper.getWritableDatabase();
        int rows = db.update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        ZLog.d(TAG, "update : values = " + values + "; selection = " + selection + "; selectionArgs = " + selectionArgs + "; update rows = " + rows);
        return 0;
    }

    private String getTable(Uri uri) {
        String tableName = null;
        switch (mUriMatcher.match(uri)) {
            case COLLECTION_INFO_CODE:
                tableName = DataContract.COLLECTION_FILE_TABLE;
                break;
            default:
                break;
        }
        return tableName;
    }

    public static class DataContract {

        public static final String COLLECTION_DATABASE = "collection_database.db";                  // 数据库名称
        public static final int COLLECTION_DATABASE_VERSION = 1;                                    // 版本号
        public static final String COLLECTION_FILE_TABLE = "collection_file_info";                  // 表

        public static final int FILE_TYPE_PHOTO = 0;                                                // 图片
        public static final int FILE_TYPE_VIDEO = 1;                                                // 视频
        public static final int FILE_TYPE_FILE = 2;                                                 // 文档
        public static final int FILE_TYPE_SOUND = 3;                                                // 声音

        public static final int FILE_REPORT_STATUS_OVER = 1;                                        // 上传完成标记

        public static final String FILE_NAME = "file_name";                                         // 名称
        public static final String FILE_PATH = "file_path";                                         // 路径
        public static final String FILE_SIZE = "file_size";                                         // 尺寸
        public static final String FILE_MD5 = "file_md5";                                           // md5
        public static final String FILE_TYPE = "file_type";                                         // 类型
        public static final String FILE_CREATE_TIME = "file_create_time";                           // 创建时间
        public static final String FILE_REPORT_SCENES = "file_report_scenes";                       // 文件场景
        public static final String FILE_REPORT_DESCRIPTION = "file_report_description";             // 文件描述
        public static final String FILE_REPORT_LONGITUDE = "file_report_longitude";                 // 经度
        public static final String FILE_REPORT_LATITUDE = "file_report_latitude";                   // 纬度
        public static final String FILE_REPORT_RADIUS = "file_report_radius";                       // 范围
        public static final String FILE_REPORT_PROGRESS = "file_report_progress";                   // 上传进度
        public static final String FILE_REPORT_STATUS = "file_report_status";                       // 上传标记 默认:0, 完成 1



        public static final int INDEX_FILE_NAME = 0;
        public static final int INDEX_FILE_PATH = 1;
        public static final int INDEX_FILE_SIZE = 2;
        public static final int INDEX_FILE_TYPE = 3;
        public static final int INDEX_FILE_MD5 = 4;
        public static final int INDEX_FILE_CREATE_TIME = 5;
        public static final int INDEX_FILE_REPORT_PROGRESS = 6;
        public static final int INDEX_FILE_REPORT_STATUS = 7;
        public static final int INDEX_FILE_REPORT_SCENES = 8;
        public static final int INDEX_FILE_REPORT_DESCRIPTION = 9;
        public static final int INDEX_FILE_REPORT_LONGITUDE = 10;
        public static final int INDEX_FILE_REPORT_LATITUDE = 11;
        public static final int INDEX_FILE_REPORT_RADIUS = 12;
    }

    public class CollectionSqliteOpenHelper extends SQLiteOpenHelper {

        public CollectionSqliteOpenHelper(@Nullable Context context) {
            super(context, DataContract.COLLECTION_DATABASE, null, DataContract.COLLECTION_DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DataContract.COLLECTION_FILE_TABLE + "("
                    + DataContract.FILE_NAME + " TEXT NOT NULL,"
                    + DataContract.FILE_PATH + " TEXT NOT NULL,"
                    + DataContract.FILE_SIZE + " TEXT,"
                    + DataContract.FILE_TYPE + " INTEGER,"
                    + DataContract.FILE_MD5 + " TEXT,"
                    + DataContract.FILE_CREATE_TIME + " INTEGER,"
                    + DataContract.FILE_REPORT_PROGRESS + " TEXT,"
                    + DataContract.FILE_REPORT_STATUS + " INTEGER,"
                    + DataContract.FILE_REPORT_SCENES + " TEXT,"
                    + DataContract.FILE_REPORT_DESCRIPTION + " TEXT,"
                    + DataContract.FILE_REPORT_LONGITUDE + " TEXT,"
                    + DataContract.FILE_REPORT_LATITUDE + " TEXT,"
                    + DataContract.FILE_REPORT_RADIUS + " TEXT"
                    + ");");
            ZLog.d(TAG, "CollectionSqliteOpenHelper -> onCreate end");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
