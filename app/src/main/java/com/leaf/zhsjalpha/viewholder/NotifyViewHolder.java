package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;

public class NotifyViewHolder extends RecyclerView.ViewHolder {
    public TextView notifyHead, notifyTitle, notifyTime, notifyContent;
    public ImageView ivIconNotify;

    public NotifyViewHolder(@NonNull View itemView) {
        super(itemView);
        notifyHead = itemView.findViewById(R.id.tv_notifyHead);
        notifyTitle = itemView.findViewById(R.id.tv_notifyTitle);
        notifyTime = itemView.findViewById(R.id.tv_notifyTime);
        notifyContent = itemView.findViewById(R.id.tv_notifyContent);
        ivIconNotify = itemView.findViewById(R.id.iv_iconNotify);
    }
}
