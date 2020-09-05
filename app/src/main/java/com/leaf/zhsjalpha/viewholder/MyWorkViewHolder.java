package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.widget.RoundImageView;

public class MyWorkViewHolder extends RecyclerView.ViewHolder {

    public RoundImageView rivWorkImage, rivWorkPic;
    public TextView tvWorkCourse, tvContent, tvMyWorkName, tvMyWorkContent, tvLike, tvComment;

    public MyWorkViewHolder(@NonNull View itemView) {
        super(itemView);
        rivWorkImage = itemView.findViewById(R.id.riv_workImage);
        rivWorkPic = itemView.findViewById(R.id.riv_workPic);
        tvWorkCourse = itemView.findViewById(R.id.tv_offiicialPost);
        tvContent = itemView.findViewById(R.id.tv_content);
        tvMyWorkName = itemView.findViewById(R.id.tv_myWorkName);
        tvMyWorkContent = itemView.findViewById(R.id.tv_postContent);
        tvLike = itemView.findViewById(R.id.tv_like);
        tvComment = itemView.findViewById(R.id.tv_comment);
    }
}
