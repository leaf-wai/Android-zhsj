package com.leaf.zhsjalpha.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Course;

import org.jetbrains.annotations.NotNull;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL2;

public class CourseAdapter extends BaseQuickAdapter<Course, BaseViewHolder> {
    public CourseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Course course) {
        Glide.with(getContext())
                .load(BASE_URL2 + course.getCourseImgUrl())
                .placeholder(R.drawable.no_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into((ImageView) baseViewHolder.getView(R.id.riv_courseImg));
        baseViewHolder.setText(R.id.tv_title, course.getCourseName());
        baseViewHolder.setText(R.id.tv_endTime, course.getPayEndTime());
        baseViewHolder.setText(R.id.tv_remain, String.valueOf(course.getRemain()));
        baseViewHolder.setText(R.id.label_courseType, course.getCourseType());
        baseViewHolder.setText(R.id.label_interestType, course.getInterestType());
        if (course.getPrice() == 0) {
            baseViewHolder.setText(R.id.tv_price, "免费");
        } else {
            baseViewHolder.setText(R.id.tv_price, String.valueOf(course.getPrice()));
        }
    }
}
