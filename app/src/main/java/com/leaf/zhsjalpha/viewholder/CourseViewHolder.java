package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.widget.RoundImageView;

public class CourseViewHolder extends RecyclerView.ViewHolder {
    public RoundImageView rivCourseImage;
    public TextView tvCourseName, labelAge, tvPrice, tvOriginalPrice;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        rivCourseImage = itemView.findViewById(R.id.riv_courseImg);
        tvCourseName = itemView.findViewById(R.id.tv_title);
        tvOriginalPrice = itemView.findViewById(R.id.tv_originalPrice);
        tvPrice = itemView.findViewById(R.id.tv_price);
        labelAge = itemView.findViewById(R.id.label_age);
    }
}
