package com.zsf.m_camera.ui.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private TextView title;

    private boolean isPhoto;
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
    }

    public class LoadPhotoTask extends AsyncTask<Void, Void, List<CollectionFileBean>> {

        @Override
        protected List<CollectionFileBean> doInBackground(Void... voids) {
            List<CollectionFileBean> collectionFileBeans = new ArrayList<>();
            Cursor cursor = GlobalData.getContext().getContentResolver().query(CollectionProvider.collectionUri,
                    null,
                    CollectionProvider.DataContract.FILE_TYPE + " = ?",
                    new String[]{String.valueOf(isPhoto ? CollectionProvider.DataContract.FILE_TYPE_PHOTO : CollectionProvider.DataContract.FILE_TYPE_VIDEO)},
                    CollectionProvider.DataContract.FILE_CREATE_TIME + " desc");
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
