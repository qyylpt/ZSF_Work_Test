package com.zsf.m_sms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author zsf
 * @date 2019/12/9
 * @Usage
 */
public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> {
    private List<SMS> smsList;
    private boolean isClick = true;

    public SMSAdapter(List<SMS> smsList) {
        this.smsList = smsList;
    }

    public void setData(List<SMS> smsList, boolean isClick){
        this.smsList = smsList;
        this.isClick = isClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMS sms = null;
        if (!isClick){
            sms = smsList.get((smsList.size()-1) - position);
        } else {
            sms = smsList.get(position);
        }
        holder.smsAddress.setText(sms.getSmsAddress());
        holder.smsSendTime.setText(sms.getSmsSendTime());
        holder.smsContent.setText(sms.getSmsContent());
        holder.smsStatus.setText(sms.getSmsStatus());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView smsAddress;
        TextView smsSendTime;
        TextView smsContent;
        TextView smsStatus;
        public ViewHolder(View view){
            super(view);
            smsAddress = view.findViewById(R.id.m_sms_textView_sms_send_address);
            smsContent = view.findViewById(R.id.m_sms_textView_sms_content);
            smsSendTime = view.findViewById(R.id.m_sms_textView_sms_send_time);
            smsStatus = view.findViewById(R.id.m_sms_textView_sms_status);
        }

    }

}
