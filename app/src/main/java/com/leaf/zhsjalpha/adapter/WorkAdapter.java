package com.leaf.zhsjalpha.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Work;

import org.jetbrains.annotations.NotNull;

public class WorkAdapter extends BaseQuickAdapter<Work, BaseViewHolder> {
    public WorkAdapter() {
        super(R.layout.list_work_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Work work) {
        baseViewHolder.setImageResource(R.id.riv_workImage, work.getWorkImageID());
        baseViewHolder.setText(R.id.tv_workCourse, work.getWorkCourse());
        baseViewHolder.setText(R.id.tv_workContent, work.getContent());
        baseViewHolder.setText(R.id.tv_workDeadline, work.getDeadline());
    }
}
