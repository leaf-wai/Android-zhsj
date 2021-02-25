package com.leaf.zhsjalpha.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.ApplyFriend;

import org.jetbrains.annotations.NotNull;

import retrofit2.Callback;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL2;

public class NewFriendAdapter extends BaseQuickAdapter<ApplyFriend, BaseViewHolder> {
    private Callback<User> callback;

    public NewFriendAdapter(Callback<User> callback) {
        super(R.layout.list_new_friend_item);
        this.callback = callback;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ApplyFriend applyFriend) {
        Glide.with(getContext())
                .load(BASE_URL2 + applyFriend.getPicUrl())
                .placeholder(R.drawable.avatar)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into((ImageView) baseViewHolder.getView(R.id.civ_avatar));
        baseViewHolder.setText(R.id.tv_name, applyFriend.getName());
        baseViewHolder.setText(R.id.tv_apply_content, applyFriend.getApplyContent());
        baseViewHolder.setText(R.id.tv_apply_content, applyFriend.getApplyContent());
        if (applyFriend.getSex() != null) {
            if (applyFriend.getSex().equals("男"))
                ((ImageView) baseViewHolder.getView(R.id.civ_avatar)).setImageDrawable(getContext().getResources().getDrawable(R.drawable.vector_drawable_male));
            else if (applyFriend.getSex().equals("女"))
                ((ImageView) baseViewHolder.getView(R.id.civ_avatar)).setImageDrawable(getContext().getResources().getDrawable(R.drawable.vector_drawable_female));
        }
        baseViewHolder.getView(R.id.btn_apply).setOnClickListener(v -> reviewFriend(applyFriend.getId(), 1, callback));
        baseViewHolder.getView(R.id.btn_refuse).setOnClickListener(v -> reviewFriend(applyFriend.getId(), 2, callback));

    }

    private void reviewFriend(Integer ID, Integer operate, Callback<User> callback) {
        RetrofitHelper.getInstance().reviewFriendCall(ID, operate).enqueue(callback);
    }
}
