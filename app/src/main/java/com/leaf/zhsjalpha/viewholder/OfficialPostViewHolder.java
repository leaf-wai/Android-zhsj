package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;

public class OfficialPostViewHolder extends RecyclerView.ViewHolder {

    public TextView tvPostContent, tvTime, tvComment, tvLike;

    public OfficialPostViewHolder(@NonNull View itemView) {
        super(itemView);
        tvPostContent = itemView.findViewById(R.id.tv_postContent);
        tvTime = itemView.findViewById(R.id.tv_time);
        tvComment = itemView.findViewById(R.id.tv_comment);
        tvLike = itemView.findViewById(R.id.tv_like);
    }
}
