package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Friend;
import com.leaf.zhsjalpha.utils.MyApplication;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private static String BASE_URL = "https://zhsj.bnuz.edu.cn/ComprehensiveSys/";
    private List<Friend> friendList;

    public void notifyDataSetChanged(List<Friend> friendList) {
        this.friendList = friendList;
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friends_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (friendList.size() != 0) {
            Friend friend = friendList.get(position);
            holder.tvName.setText(friend.getName());
            if (friend.getSex().equals("男")) {
                holder.ivSex.setImageDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.vector_drawable_male));
            } else if (friend.getSex().equals("女")) {
                holder.ivSex.setImageDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.vector_drawable_female));
            }
            Glide.with(MyApplication.getContext())
                    .load(BASE_URL + friend.getPicUrl())
                    .placeholder(R.drawable.avatar)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.civAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return friendList == null ? 0 : friendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        ImageView ivSex;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            civAvatar = itemView.findViewById(R.id.civ_avatar);
            ivSex = itemView.findViewById(R.id.iv_sex);
        }
    }
}
