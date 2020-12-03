package com.leaf.zhsjalpha.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.MyCourse;

import org.jetbrains.annotations.NotNull;

public class MyCourseAdapter extends BaseQuickAdapter<MyCourse, BaseViewHolder> {

    private static String BASE_URL = "https://zhsj.bnuz.edu.cn/ComprehensiveSys";

    public MyCourseAdapter() {
        super(R.layout.list_my_course_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MyCourse myCourse) {
        baseViewHolder.setText(R.id.tv_courseName, myCourse.getCourseName());
        Glide.with(getContext())
                .load(BASE_URL + myCourse.getCourseImageUrl())
                .placeholder(R.drawable.no_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into((ImageView) baseViewHolder.getView(R.id.riv_courseImg));
    }
}
