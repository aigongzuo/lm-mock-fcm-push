package com.kang.mockfcmpush;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Activity mActivity;
    private List<String> data;
    private int selectedPosition = -1;

    public MyAdapter(Activity activity, List<String> data) {
        this.mActivity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        holder.textView.setText(data.get(adapterPosition));
        holder.checkBox.setChecked(adapterPosition == selectedPosition);
        holder.itemView.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            startLMService(data.get(adapterPosition));
        });
    }

    public void startLMService(String json) {
        try {
            String packageName = "com.plusme.live";
            String className = "com.kxsimon.push.fcmpush.service.MockingFcmPushService";
            String action = "com.google.firebase.MESSAGING_EVENT_MOCK";
            Intent intent = new Intent(action);
//        intent.setClassName(packageName, className);
            intent.setPackage(packageName);
//        intent.setAction(action);
//        intent.setClassName(packageName, className);
            intent.putExtra("remoteMessage", json);
            mActivity.startService(intent);
            Toast.makeText(mActivity, "发送成功", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            Toast.makeText(mActivity, "发送失败，请查看使用说明", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            checkBox = itemView.findViewById(R.id.check_box);
        }
    }
}
