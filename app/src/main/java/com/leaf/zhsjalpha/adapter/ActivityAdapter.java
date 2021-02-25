package com.leaf.zhsjalpha.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Activity;

import org.jetbrains.annotations.NotNull;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL2;

public class ActivityAdapter extends BaseQuickAdapter<Activity, BaseViewHolder> implements LoadMoreModule {
    public ActivityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Activity activity) {
        Glide.with(getContext())
                .load(BASE_URL2 + activity.getImageUrl())
                .placeholder(R.drawable.no_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into((ImageView) baseViewHolder.getView(R.id.riv_activityImg));
        baseViewHolder.setText(R.id.tv_activityName, activity.getActivityName());
        baseViewHolder.setText(R.id.tv_activityDescription, activity.getActivityDescription());
        baseViewHolder.setText(R.id.tv_address, activity.getActivityAddress());
        baseViewHolder.setText(R.id.tv_startDate, activity.getActivityStartTime());
        baseViewHolder.setText(R.id.tv_endDate, activity.getActivityEndTime());
        baseViewHolder.setText(R.id.tv_maxCount, String.valueOf(activity.getMaxCount()));
    }
}
