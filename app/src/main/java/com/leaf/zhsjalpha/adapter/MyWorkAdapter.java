package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.MyWork;
import com.leaf.zhsjalpha.viewholder.MyWorkViewHolder;

import java.util.List;

public class MyWorkAdapter extends RecyclerView.Adapter<MyWorkViewHolder> {

    private List<MyWork> myWorks;

    public MyWorkAdapter(List<MyWork> myWorks) {
        this.myWorks = myWorks;
    }

    @NonNull
    @Override
    public MyWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_my_work_item, parent, false);
        return new MyWorkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWorkViewHolder holder, int position) {
        MyWork myWork = myWorks.get(position);
        holder.rivWorkImage.setImageResource(myWork.getWorkImageID());
        holder.rivWorkPic.setImageResource(myWork.getWorkPicID());
        holder.tvWorkCourse.setText(myWork.getWorkCourse());
        holder.tvContent.setText(myWork.getContent());
        holder.tvMyWorkName.setText(myWork.getMyWorkName());
        holder.tvMyWorkContent.setText(myWork.getMyWorkContent());
        holder.tvLike.setText(String.valueOf(myWork.getLike()));
        holder.tvComment.setText(String.valueOf(myWork.getComment()));
    }

    @Override
    public int getItemCount() {
        return myWorks.size();
    }
}
