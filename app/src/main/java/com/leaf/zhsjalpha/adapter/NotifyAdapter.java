package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Notify;
import com.leaf.zhsjalpha.viewholder.NotifyViewHolder;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyViewHolder> {
    private List<Notify> notifies;

    public NotifyAdapter(List<Notify> notifies) {
        this.notifies = notifies;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_notify_item, parent, false);
        return new NotifyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        Notify notify = notifies.get(position);
        holder.notifyTitle.setText(notify.getTitle());
        holder.notifyContent.setText(notify.getContent());
        holder.notifyTime.setText(notify.getTime());
        if (notify.getType().equals("course")) {
            holder.notifyHead.setText("课程提醒");
            holder.ivIconNotify.setImageResource(R.drawable.vector_drawable_course_circle);
        } else if (notify.getType().equals("evaluate")) {
            holder.notifyHead.setText("评价提醒");
            holder.ivIconNotify.setImageResource(R.drawable.vector_drawable_evaluate_circle);
        } else if (notify.getType().equals("work")) {
            holder.notifyHead.setText("作业提醒");
            holder.ivIconNotify.setImageResource(R.drawable.vector_drawable_work_circle);
        } else if (notify.getType().equals("attendance")) {
            holder.notifyHead.setText("考勤提醒");
            holder.ivIconNotify.setImageResource(R.drawable.vector_drawable_attendance_circle);
        }
    }

    @Override
    public int getItemCount() {
        return notifies.size();
    }
}
