package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.ActivityUser;
import com.leaf.zhsjalpha.utils.MyApplication;

import java.util.List;

public class TeammateAdapter extends RecyclerView.Adapter<TeammateAdapter.ViewHolder> {

    private List<ActivityUser> activityUserList;

    public void notifyDataSetChanged(List<ActivityUser> activityUserList) {
        this.activityUserList = activityUserList;
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeammateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeammateAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teammate_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeammateAdapter.ViewHolder holder, int position) {
        if (activityUserList.size() != 0) {
            ActivityUser activityUser = activityUserList.get(position);
            holder.tvName.setText(activityUser.getUserName());
            if (activityUser.getIsCaptain() == 1)
                holder.tvIdentity.setText("队长");
            else
                holder.tvIdentity.setText("队员");

            if (activityUser.getUserSex().equals("男"))
                holder.ivSex.setImageDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.vector_drawable_male));
            else if (activityUser.getUserSex().equals("女"))
                holder.ivSex.setImageDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.vector_drawable_female));
        }
    }

    @Override
    public int getItemCount() {
        return activityUserList == null ? 0 : activityUserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSex;
        TextView tvName, tvIdentity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvIdentity = itemView.findViewById(R.id.tv_identity);
            ivSex = itemView.findViewById(R.id.iv_sex);
        }
    }
}
