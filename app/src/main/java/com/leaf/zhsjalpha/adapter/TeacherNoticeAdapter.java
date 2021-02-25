package com.leaf.zhsjalpha.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Notify;

import org.jetbrains.annotations.NotNull;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL2;

public class TeacherNoticeAdapter extends BaseQuickAdapter<Notify, BaseViewHolder> {
    public TeacherNoticeAdapter() {
        super(R.layout.list_teacher_notice_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Notify notify) {
        baseViewHolder.setText(R.id.tv_notifyClassName, notify.getClassName());
        baseViewHolder.setText(R.id.tv_notifyTeacherName, notify.getTeacherName());
        baseViewHolder.setText(R.id.tv_notifyContent, notify.getContent());
        baseViewHolder.setText(R.id.tv_notifyTime, notify.getTime());
        Glide.with(getContext())
                .load(BASE_URL2 + notify.getResourceURL())
                .placeholder(R.drawable.no_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into((ImageView) baseViewHolder.getView(R.id.riv_resource));
    }
}
