package com.leaf.zhsjalpha.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Declare;

import org.jetbrains.annotations.NotNull;

public class DeclareAdapter extends BaseQuickAdapter<Declare, BaseViewHolder> {
    public DeclareAdapter() {
        super(R.layout.list_declare_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Declare declare) {
        baseViewHolder.setText(R.id.tv_workContent, declare.getContent());
        baseViewHolder.setText(R.id.tv_suyang, declare.getSubcurrencyName());
        baseViewHolder.setText(R.id.tv_interest_type, declare.getSubmitTime());
        baseViewHolder.setText(R.id.tv_score, String.valueOf(declare.getScore()));
    }
}
