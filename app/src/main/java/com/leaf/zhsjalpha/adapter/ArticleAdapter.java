package com.leaf.zhsjalpha.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Article;
import com.leaf.zhsjalpha.utils.MyApplication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArticleAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public ArticleAdapter(@Nullable List<Article> data) {
        super(R.layout.list_share_item, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Article article) {
        baseViewHolder.setText(R.id.tv_title, article.getTitle());
        baseViewHolder.setText(R.id.tv_workContent, article.getContent());
        Glide.with(MyApplication.getContext())
                .load(article.getArticleImageUrl())
                .placeholder(R.drawable.edu)
                .into((ImageView) baseViewHolder.getView(R.id.riv_articleImg));
    }
}
