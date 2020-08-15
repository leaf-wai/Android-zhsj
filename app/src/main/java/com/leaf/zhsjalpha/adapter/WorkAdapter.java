package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Work;
import com.leaf.zhsjalpha.viewholder.WorkViewHolder;

import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkViewHolder> {

    private List<Work> works;

    public WorkAdapter(List<Work> works) {
        this.works = works;
    }

    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_work_item, parent, false);
        return new WorkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        Work work = works.get(position);
        holder.rivWorkImage.setImageResource(work.getWorkImageID());
        holder.tvWorkCourse.setText(work.getWorkCourse());
        holder.tvContent.setText(work.getContent());
        holder.tvDeadline.setText(work.getDeadline());
    }

    @Override
    public int getItemCount() {
        return works.size();
    }
}
