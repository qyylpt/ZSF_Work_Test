package com.zsf.test.branch.diffutil;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zsf.test.R;

import java.util.List;

/**
 * @author zsf
 * @date 2019/8/6
 */
public class DiffAdapter extends RecyclerView.Adapter<DiffAdapter.DiffVH> {
    private Context context;
    private LayoutInflater mInflater;
    private List<TestBean> datas;


    public DiffAdapter(Context context, List<TestBean> datas) {
        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }
    public void setDatas(List<TestBean> datas){
        this.datas = datas;
    }


    @NonNull
    @Override
    public DiffVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.diff_util_item_layout, parent,false);
        DiffVH viewHolder = new DiffVH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiffVH holder, int position) {
        holder.tv1.setText(datas.get(position).getName());
        holder.tv2.setText(datas.get(position).getDesc());
        holder.iv.setImageResource(datas.get(position).getPic());

    }

    @Override
    public void onBindViewHolder(@NonNull DiffVH holder, int position, @NonNull List<Object> payloads) {
        // 数据没有任何改变
        if (payloads.isEmpty()){
            onBindViewHolder(holder, position);
        } else {
            // 取出在getChangePayload（）方法返回的bundle
            Bundle payload = (Bundle) payloads.get(0);
            TestBean testBean = datas.get(position);
            for (String key : payload.keySet()){
                switch (key){
                    case "KET_DESC":
                        holder.tv2.setText(testBean.getDesc());
                        break;
                    case "KEY_PIC":
                        holder.iv.setImageResource(payload.getInt(key));
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public class DiffVH extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        ImageView iv;
        public DiffVH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
