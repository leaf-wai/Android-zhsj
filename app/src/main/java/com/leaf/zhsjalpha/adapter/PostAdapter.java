package com.leaf.zhsjalpha.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.ProductData;
import com.leaf.zhsjalpha.utils.TimeUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import cc.shinichi.library.ImagePreview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL;

public class PostAdapter extends BaseQuickAdapter<ProductData, BaseViewHolder> {
    public PostAdapter() {
        super(R.layout.list_post_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ProductData productData) {
        if (productData.getFileUrl() != null) {
            baseViewHolder.getView(R.id.riv_postImage).setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(BASE_URL + productData.getFileUrl())
                    .placeholder(R.drawable.no_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into((ImageView) baseViewHolder.getView(R.id.riv_postImage));
        } else {
            baseViewHolder.getView(R.id.riv_postImage).setVisibility(View.GONE);
        }

        SharedPreferences userRead = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        baseViewHolder.setText(R.id.tv_studentName, productData.getStudentName() == null ? "匿名" : productData.getStudentName());
        baseViewHolder.setText(R.id.tv_description, userRead.getString("school", null));
        baseViewHolder.setText(R.id.tv_postTitle, productData.getPostTitle());
        baseViewHolder.setText(R.id.tv_postContent, productData.getPostContent());
        baseViewHolder.setText(R.id.tv_interest_type, TimeUtils.calTime(productData.getBuildTime()));
        if (productData.getThumbUpNumbers() == 0)
            baseViewHolder.setText(R.id.tv_like, "点赞");
        else
            baseViewHolder.setText(R.id.tv_like, String.valueOf(productData.getThumbUpNumbers()));
        if (productData.isThumb()) {
            ((ImageView) baseViewHolder.getView(R.id.iv_like)).setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            ((TextView) baseViewHolder.getView(R.id.tv_like)).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        }else {
            ((ImageView) baseViewHolder.getView(R.id.iv_like)).setColorFilter(getContext().getResources().getColor(R.color.gray3));
            ((TextView) baseViewHolder.getView(R.id.tv_like)).setTextColor(getContext().getResources().getColor(R.color.gray3));
        }

        baseViewHolder.getView(R.id.ll_like).setOnClickListener(v -> {
            thumbUp(productData.getClassId(), productData.getPostId(), new Callback<User>() {
                @Override
                public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (productData.isThumb()) {
                            if (response.body().getDetail().equals("取消点赞成功")) {
                                if ((productData.getThumbUpNumbers() - 1) != 0)
                                    baseViewHolder.setText(R.id.tv_like, String.valueOf(productData.getThumbUpNumbers() - 1));
                                else
                                    baseViewHolder.setText(R.id.tv_like, "点赞");
                                ((ImageView) baseViewHolder.getView(R.id.iv_like)).setColorFilter(getContext().getResources().getColor(R.color.gray3));
                                ((TextView) baseViewHolder.getView(R.id.tv_like)).setTextColor(getContext().getResources().getColor(R.color.gray3));
                            } else if (response.body().getDetail().equals("点赞成功")) {
                                baseViewHolder.setText(R.id.tv_like, String.valueOf(productData.getThumbUpNumbers()));
                                ((ImageView) baseViewHolder.getView(R.id.iv_like)).setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
                                ((TextView) baseViewHolder.getView(R.id.tv_like)).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                            }
                        } else {
                            if (response.body().getDetail().equals("点赞成功")) {
                                baseViewHolder.setText(R.id.tv_like, String.valueOf(productData.getThumbUpNumbers() + 1));
                                ((ImageView) baseViewHolder.getView(R.id.iv_like)).setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
                                ((TextView) baseViewHolder.getView(R.id.tv_like)).setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                            } else if (response.body().getDetail().equals("取消点赞成功")) {
                                if (productData.getThumbUpNumbers() != 0)
                                    baseViewHolder.setText(R.id.tv_like, String.valueOf(productData.getThumbUpNumbers()));
                                else
                                    baseViewHolder.setText(R.id.tv_like, "点赞");
                                ((ImageView) baseViewHolder.getView(R.id.iv_like)).setColorFilter(getContext().getResources().getColor(R.color.gray3));
                                ((TextView) baseViewHolder.getView(R.id.tv_like)).setTextColor(getContext().getResources().getColor(R.color.gray3));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                    ToastUtils.showToast(getContext(), "网络请求失败！请稍后重试");
                    Log.d("aaa", "onFailure: " + t.getMessage());
                }
            });
        });

        baseViewHolder.getView(R.id.riv_postImage).setOnClickListener(v -> ImagePreview
                .getInstance()
                .setContext(getContext())
                .setIndex(0)
                .setImage(BASE_URL + productData.getFileUrl())
                .setShowCloseButton(true)
                .setEnableDragClose(true)
                .setEnableUpDragClose(true)
                .setErrorPlaceHolder(R.drawable.image_broken)
                .start());
    }

    private void thumbUp(String classId, String postId, Callback<User> callback) {
        RetrofitHelper.getInstance().thumbUpCall(classId, postId).enqueue(callback);
    }
}
