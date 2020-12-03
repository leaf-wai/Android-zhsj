package com.leaf.zhsjalpha.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.MyProduct;

import org.jetbrains.annotations.NotNull;

import cc.shinichi.library.ImagePreview;

public class MyPostAdapter extends BaseQuickAdapter<MyProduct, BaseViewHolder> {
    private static String BASE_URL = "https://zhsj.bnuz.edu.cn/ComprehensiveSys/";

    public MyPostAdapter() {
        super(R.layout.list_my_post_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MyProduct myProduct) {
        if (myProduct.getPostImageUrl() != null) {
            baseViewHolder.getView(R.id.riv_postImage).setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(BASE_URL + myProduct.getPostImageUrl())
                    .placeholder(R.drawable.no_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into((ImageView) baseViewHolder.getView(R.id.riv_postImage));
        } else {
            baseViewHolder.getView(R.id.riv_postImage).setVisibility(View.GONE);
        }

        baseViewHolder.setText(R.id.tv_postTitle, myProduct.getPostTitle());
        baseViewHolder.setText(R.id.tv_postContent, myProduct.getPostContent());
        baseViewHolder.setText(R.id.tv_interest_type, myProduct.getPostBuildTime());
        baseViewHolder.setText(R.id.tv_like, String.valueOf(myProduct.getThumbUpNumber()));
        baseViewHolder.setText(R.id.tv_comment, String.valueOf(myProduct.getCommentNumber()));

        baseViewHolder.getView(R.id.riv_postImage).setOnClickListener(v -> {
            ImagePreview
                    .getInstance()
                    .setContext(getContext())
                    .setIndex(0)
                    .setImage(BASE_URL + myProduct.getPostImageUrl())
                    .setShowCloseButton(true)
                    .setEnableDragClose(true)
                    .setEnableUpDragClose(true)
                    .setErrorPlaceHolder(R.drawable.image_broken)
                    .start();
        });
    }
}
