package com.zsf.m_camera.ui.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zsf.global.GlobalData;
import com.zsf.m_camera.R;
import com.zsf.m_camera.adapter.CollectionFileAdapter;
import com.zsf.m_camera.adapter.bean.CollectionFileBean;
import com.zsf.m_camera.data.CollectionProvider;
import com.zsf.m_camera.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zsf
 * @date : 2021/2/5 9:45 AM
 * @desc :
 */
public class MediaListFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView photoList;

    private LoadPhotoTask loadPhotoTask;

    private TextView title, filterTime, filterReport, filterSize, filterOver;

    private boolean isPhoto;

    private ImageView imageView;

    private Dialog mDialog;

    /**
     * 0:时间 1:大小 2:未上传
     */
    private int filterIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);
        Bundle bundle = getArguments();
        isPhoto = bundle.getBoolean("isPhoto");
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        photoList = view.findViewById(R.id.m_camera_RecyclerView_photo_list);
        title = view.findViewById(R.id.m_camera_TextView_photo_list_title);
        if (!isPhoto){
            title.setText(getResources().getString(R.string.m_camera_image_title_video));
        }
        title.setOnClickListener(this);
        imageView = view.findViewById(R.id.m_camera_imageView_photo_list_filter);
        imageView.setOnClickListener(this);
        mDialog = new Dialog(getActivity(), R.style.BottomDialog);
        ConstraintLayout root = (ConstraintLayout) LayoutInflater.from(getActivity()).inflate(R.layout.bottom_float_window_sort_layout, null);
        filterOver = root.findViewById(R.id.m_camera_TextView_sort_over);
        filterOver.setOnClickListener(this);
        filterReport = root.findViewById(R.id.m_camera_TextView_report_sort);
        filterReport.setOnClickListener(this);
        filterTime = root.findViewById(R.id.m_camera_TextView_time_sort);
        filterTime.setOnClickListener(this);
        filterSize = root.findViewById(R.id.m_camera_TextView_size_sort);
        filterSize.setOnClickListener(this);


        mDialog.setContentView(root);

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        root.measure(widthMeasureSpec, heightMeasureSpec);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = getResources().getDisplayMetrics().widthPixels;
        lp.height = root.getMeasuredHeight();
        dialogWindow.setAttributes(lp);
    }

    private void initData() {
        loadPhotoTask = new LoadPhotoTask();
        loadPhotoTask.execute();
    }

    @Override
    public boolean isNeedAddStack() {
        return true;
    }

    @Override
    public void refreshStyle() {
        switchStyle(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.m_camera_TextView_photo_list_title) {
            back();
        }
        if (id == R.id.m_camera_imageView_photo_list_filter) {
            mDialog.show();
        }
        if (id == R.id.m_camera_TextView_sort_over) {
            mDialog.dismiss();
        }
        if (id == R.id.m_camera_TextView_report_sort) {
            filterIndex = 2;
            resetColor();
            filterReport.setTextColor(getResources().getColor(R.color.m_camera_common_background_color));
        }
        if (id == R.id.m_camera_TextView_size_sort) {
            filterIndex = 1;
            resetColor();
            filterSize.setTextColor(getResources().getColor(R.color.m_camera_common_background_color));
        }
        if (id == R.id.m_camera_TextView_time_sort) {
            filterIndex = 0;
            resetColor();
            filterTime.setTextColor(getResources().getColor(R.color.m_camera_common_background_color));
        }
        if (id == R.id.m_camera_TextView_sort_over) {
            mDialog.dismiss();
            if (loadPhotoTask != null) {
                loadPhotoTask.cancel(true);
            }
            loadPhotoTask = new LoadPhotoTask();
            loadPhotoTask.execute();
        }
    }

    private void resetColor() {
        filterReport.setTextColor(getResources().getColor(R.color.m_camera_common_text));
        filterSize.setTextColor(getResources().getColor(R.color.m_camera_common_text));
        filterTime.setTextColor(getResources().getColor(R.color.m_camera_common_text));
    }

    public class LoadPhotoTask extends AsyncTask<Void, Void, List<CollectionFileBean>> {

        @Override
        protected List<CollectionFileBean> doInBackground(Void... voids) {
            List<CollectionFileBean> collectionFileBeans = new ArrayList<>();
            String sortOrder = CollectionProvider.DataContract.FILE_CREATE_TIME + " desc";
            String selection = CollectionProvider.DataContract.FILE_TYPE + " = ?";
            if (filterIndex == 1) {
                sortOrder = CollectionProvider.DataContract.FILE_SIZE + " desc";
            }
            if (filterIndex == 2) {
                selection = CollectionProvider.DataContract.FILE_TYPE + " = ? AND " + CollectionProvider.DataContract.FILE_REPORT_STATUS + " = 0";
            }
            Cursor cursor = GlobalData.getContext().getContentResolver().query(CollectionProvider.collectionUri,
                    null,
                    selection,
                    new String[]{String.valueOf(isPhoto ? CollectionProvider.DataContract.FILE_TYPE_PHOTO : CollectionProvider.DataContract.FILE_TYPE_VIDEO)},
                    sortOrder);
            while (cursor.moveToNext()) {
                CollectionFileBean collectionFileBean = new CollectionFileBean();
                collectionFileBean.setFileName(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_NAME));
                collectionFileBean.setFilePath(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_PATH));
                collectionFileBean.setFileSize(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_SIZE));
                collectionFileBean.setFileType(cursor.getInt(CollectionProvider.DataContract.INDEX_FILE_TYPE));
                collectionFileBean.setFileMd5(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_MD5));
                collectionFileBean.setFileCreateTime(cursor.getLong(CollectionProvider.DataContract.INDEX_FILE_CREATE_TIME));
                collectionFileBean.setFileReportProgress(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_REPORT_PROGRESS));
                collectionFileBean.setFileReportStatus(cursor.getInt(CollectionProvider.DataContract.INDEX_FILE_REPORT_STATUS));
                collectionFileBean.setFileReportScenes(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_REPORT_SCENES));
                collectionFileBean.setFileReportDescription(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_REPORT_DESCRIPTION));
                collectionFileBean.setFileReportLongitude(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_REPORT_LONGITUDE));
                collectionFileBean.setFileReportLatitude(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_REPORT_LATITUDE));
                collectionFileBean.setFileReportRadius(cursor.getString(CollectionProvider.DataContract.INDEX_FILE_REPORT_RADIUS));
                collectionFileBeans.add(collectionFileBean);
            }
            return collectionFileBeans;
        }

        @Override
        protected void onPostExecute(List<CollectionFileBean> collectionFileBeans) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            photoList.setLayoutManager(gridLayoutManager);
            CollectionFileAdapter photoListManagerAdapter = new CollectionFileAdapter(MediaListFragment.this, collectionFileBeans, CollectionFileAdapter.AdapterType.ADAPTER_PHOTO);
            photoList.setAdapter(photoListManagerAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadPhotoTask != null) {
            loadPhotoTask.cancel(true);
            loadPhotoTask = null;
        }
    }

}
