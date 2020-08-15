package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.widget.RoundImageView;

public class MyCourseViewHolder extends RecyclerView.ViewHolder {
    public RoundImageView rivCourseImage;
    public TextView tvCourseName;

    public MyCourseViewHolder(@NonNull View itemView) {
        super(itemView);
        rivCourseImage = itemView.findViewById(R.id.riv_courseImage);
        tvCourseName = itemView.findViewById(R.id.tv_courseName);
    }
}
