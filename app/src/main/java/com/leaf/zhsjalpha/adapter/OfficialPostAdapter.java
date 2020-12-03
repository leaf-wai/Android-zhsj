package com.leaf.zhsjalpha.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.OfficialPost;

import org.jetbrains.annotations.NotNull;

public class OfficialPostAdapter extends BaseQuickAdapter<OfficialPost, BaseViewHolder> {

    public OfficialPostAdapter() {
        super(R.layout.list_community_official_post_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, OfficialPost officialPost) {
        baseViewHolder.setText(R.id.tv_postContent, officialPost.getPostContent());
        baseViewHolder.setText(R.id.tv_interest_type, officialPost.getPostTime());
        if (officialPost.getComment() != 0)
            baseViewHolder.setText(R.id.tv_comment, String.valueOf(officialPost.getComment()));
        if (officialPost.getLike() != 0)
            baseViewHolder.setText(R.id.tv_like, String.valueOf(officialPost.getLike()));
    }
}
