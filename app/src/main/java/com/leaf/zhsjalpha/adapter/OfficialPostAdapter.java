package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.OfficialPost;
import com.leaf.zhsjalpha.viewholder.OfficialPostViewHolder;

import java.util.List;

public class OfficialPostAdapter extends RecyclerView.Adapter<OfficialPostViewHolder> {

    private List<OfficialPost> officialPosts;

    public OfficialPostAdapter(List<OfficialPost> officialPosts) {
        this.officialPosts = officialPosts;
    }

    @NonNull
    @Override
    public OfficialPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_community_official_post_item, parent, false);
        return new OfficialPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialPostViewHolder holder, int position) {
        OfficialPost officialPost = officialPosts.get(position);
        holder.tvPostContent.setText(officialPost.getPostContent());
        holder.tvTime.setText(officialPost.getPostTime());
        if (officialPost.getComment() != 0)
            holder.tvComment.setText(String.valueOf(officialPost.getComment()));
        if (officialPost.getLike() != 0)
            holder.tvLike.setText(String.valueOf(officialPost.getLike()));
    }

    @Override
    public int getItemCount() {
        return officialPosts.size();
    }
}
