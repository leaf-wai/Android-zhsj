package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.widget.RoundImageView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    public RoundImageView rivArticleImage;
    public TextView tvTitle, tvContent;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        rivArticleImage = itemView.findViewById(R.id.riv_articleImage);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvContent = itemView.findViewById(R.id.tv_content);
    }
}
