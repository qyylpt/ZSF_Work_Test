package com.zsf.m_camera.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsf.global.GlobalData;
import com.zsf.m_camera.R;
import com.zsf.m_camera.adapter.bean.CollectionFileBean;
import com.zsf.m_camera.adapter.holder.RecentHolder;
import com.zsf.m_camera.ui.BaseFragment;

import java.util.List;
import java.util.zip.Inflater;

/**
 * @author : zsf
 * @date : 2021/2/4 11:52 AM
 * @desc :
 */
public class CollectionFileAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<CollectionFileBean> collectionFileBeans;

    private BaseFragment fragment;

    private LayoutInflater layoutInflater;

    private int adapterType;


    public CollectionFileAdapter(BaseFragment fragment, List<CollectionFileBean> collectionFileBeans, int adapterType) {
        this.fragment = fragment;
        this.collectionFileBeans = collectionFileBeans;
        this.adapterType = adapterType;
        layoutInflater = LayoutInflater.from(GlobalData.getContext());
    }

    public void setAdapter(List<CollectionFileBean> collectionFileBeans) {
        this.collectionFileBeans = collectionFileBeans;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case AdapterType.ADAPTER_MAIN :
                view = layoutInflater.inflate(R.layout.item_fragment_main, parent, false);
                return new RecentHolder(fragment, view);
            case AdapterType.ADAPTER_PHOTO :
                return null;
            case AdapterType.ADAPTER_VIDEO :
                return null;
            case AdapterType.ADAPTER_FILE :
                return null;
            case AdapterType.ADAPTER_SOUND :
                return null;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.bindView(collectionFileBeans.get(position), position);
    }

    @Override
    public int getItemCount() {
        return collectionFileBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (adapterType) {
            case AdapterType.ADAPTER_MAIN :
                return AdapterType.ADAPTER_MAIN;
            case AdapterType.ADAPTER_PHOTO :
                return AdapterType.ADAPTER_PHOTO;
            case AdapterType.ADAPTER_VIDEO :
                return AdapterType.ADAPTER_VIDEO;
            case AdapterType.ADAPTER_FILE :
                return AdapterType.ADAPTER_FILE;
            case AdapterType.ADAPTER_SOUND :
                return AdapterType.ADAPTER_SOUND;
            default:
                break;
        }
        return -1;
    }

    public static class AdapterType{

        /**
         * 首页
         */
        public static final int ADAPTER_MAIN = 0;

        /**
         * 图片
         */
        public static final int ADAPTER_PHOTO = 1;

        /**
         * 视频
         */
        public static final int ADAPTER_VIDEO = 2;

        /**
         * 文档
         */
        public static final int ADAPTER_FILE = 3;

        /**
         * 音频
         */
        public static final int ADAPTER_SOUND = 4;

    }
}
