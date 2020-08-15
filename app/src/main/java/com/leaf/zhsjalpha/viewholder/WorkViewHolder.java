package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.widget.RoundImageView;

public class WorkViewHolder extends RecyclerView.ViewHolder {
    public RoundImageView rivWorkImage;
    public TextView tvWorkCourse, tvContent, tvDeadline;

    public WorkViewHolder(@NonNull View itemView) {
        super(itemView);
        rivWorkImage = itemView.findViewById(R.id.riv_workImage);
        tvWorkCourse = itemView.findViewById(R.id.tv_workCourse);
        tvContent = itemView.findViewById(R.id.tv_content);
        tvDeadline = itemView.findViewById(R.id.tv_deadline);
    }
}
