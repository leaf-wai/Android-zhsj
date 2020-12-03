package com.leaf.zhsjalpha.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Notify;

import org.jetbrains.annotations.NotNull;

public class NotifyAdapter extends BaseQuickAdapter<Notify, BaseViewHolder> {

    public NotifyAdapter() {
        super(R.layout.list_notify_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Notify notify) {
        baseViewHolder.setText(R.id.tv_notifyClassName, notify.getClassName());
        baseViewHolder.setText(R.id.tv_notifyContent, notify.getContent());
        baseViewHolder.setText(R.id.tv_notifyTime, notify.getTime());
    }
}
