package com.zsf.m_camera.ui.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zsf.global.GlobalData;
import com.zsf.m_camera.R;
import com.zsf.m_camera.adapter.CollectionFileAdapter;
import com.zsf.m_camera.adapter.bean.CollectionFileBean;
import com.zsf.m_camera.data.CollectionProvider;
import com.zsf.m_camera.manager.FragmentStack;
import com.zsf.m_camera.ui.BaseCollectionActivity;
import com.zsf.m_camera.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zsf
 * @date : 2021/2/1 5:16 PM
 * @desc :
 */
public class MainFragment extends BaseFragment implements View.OnClickListener{

    private TextView openPhoto, openVideo, openFile, openSound, doPhoto, doVideo, doFile, doSound;
    private ImageView doClose, doOpen, doRefresh;
    private RecyclerView recentView;
    private View mask;
    private LoadCollectionTask loadCollectionTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        openPhoto = view.findViewById(R.id.m_camera_TextView_photo);
        openPhoto.setOnClickListener(this);
        openVideo = view.findViewById(R.id.m_camera_TextView_video);
        openVideo.setOnClickListener(this);
        openFile = view.findViewById(R.id.m_camera_TextView_file);
        openFile.setOnClickListener(this);
        openSound = view.findViewById(R.id.m_camera_TextView_sound);
        openSound.setOnClickListener(this);
        doPhoto = view.findViewById(R.id.m_camera_TextView_do_photo);
        doPhoto.setOnClickListener(this);
        doVideo = view.findViewById(R.id.m_camera_TextView_do_video);
        doVideo.setOnClickListener(this);
        doFile = view.findViewById(R.id.m_camera_TextView_do_file);
        doFile.setOnClickListener(this);
        doSound = view.findViewById(R.id.m_camera_TextView_do_sound);
        doSound.setOnClickListener(this);
        doClose = view.findViewById(R.id.m_camera_ImageView_close_mask);
        doClose.setOnClickListener(this);
        doOpen = view.findViewById(R.id.m_camera_ImageView_open_mask);
        doOpen.setOnClickListener(this);
        doRefresh = view.findViewById(R.id.m_camera_ImageView_refresh);
        doRefresh.setOnClickListener(this);
        recentView = view.findViewById(R.id.m_camera_RecyclerView_recent_list);
        mask = view.findViewById(R.id.m_camera_ConstraintLayout_mask);
    }

    private void initData() {
        loadCollectionTask = new LoadCollectionTask();
        loadCollectionTask.execute();
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
        if (id == R.id.m_camera_TextView_photo) {
            mask.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPhoto", true);
            FragmentStack.addFragment(getActivity().getSupportFragmentManager(), MainFragment.this, MediaListFragment.class, 0, ((BaseCollectionActivity)getActivity()).getFragmentContainer(), bundle);
        }
        if (id == R.id.m_camera_TextView_video) {
            mask.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPhoto", false);
            FragmentStack.addFragment(getActivity().getSupportFragmentManager(), MainFragment.this, MediaListFragment.class, 0, ((BaseCollectionActivity)getActivity()).getFragmentContainer(), bundle);

        }
        if (id == R.id.m_camera_TextView_file) {

        }
        if (id == R.id.m_camera_TextView_sound) {

        }

        if (id == R.id.m_camera_TextView_do_photo) {
            mask.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPhoto", true);
            FragmentStack.addFragment(getActivity().getSupportFragmentManager(), MainFragment.this, CameraFragment.class, 0, ((BaseCollectionActivity)getActivity()).getFragmentContainer(), bundle);
        }
        if (id == R.id.m_camera_TextView_do_video) {
            mask.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isPhoto", false);
            FragmentStack.addFragment(getActivity().getSupportFragmentManager(), MainFragment.this, CameraFragment.class, 0, ((BaseCollectionActivity)getActivity()).getFragmentContainer(), bundle);
        }
        if (id == R.id.m_camera_TextView_do_file) {

        }
        if (id == R.id.m_camera_TextView_do_sound) {

        }

        if (id == R.id.m_camera_ImageView_open_mask) {
            mask.setVisibility(View.VISIBLE);
        }
        if (id == R.id.m_camera_ImageView_close_mask) {
            mask.setVisibility(View.GONE);
        }

    }

    public class LoadCollectionTask extends AsyncTask<Void, Void, List<CollectionFileBean>>{

        @Override
        protected List<CollectionFileBean> doInBackground(Void... voids) {
             List<CollectionFileBean> collectionFileBeans = new ArrayList<>();
            Cursor cursor = GlobalData.getContext().getContentResolver().query(CollectionProvider.collectionUri, null, null, null, CollectionProvider.DataContract.FILE_CREATE_TIME + " desc");
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
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobalData.getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recentView.setLayoutManager(linearLayoutManager);
            CollectionFileAdapter mainFileManagerAdapter = new CollectionFileAdapter(MainFragment.this, collectionFileBeans, CollectionFileAdapter.AdapterType.ADAPTER_MAIN);
            recentView.setAdapter(mainFileManagerAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadCollectionTask != null) {
            loadCollectionTask.cancel(true);
            loadCollectionTask = null;
        }
    }

    @Override
    public void back() {
        getActivity().finish();
    }

}
