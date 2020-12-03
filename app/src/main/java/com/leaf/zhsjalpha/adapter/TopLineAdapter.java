package com.leaf.zhsjalpha.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.TopMessage;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

public class TopLineAdapter extends BannerAdapter<TopMessage, TopLineAdapter.TopLineHolder> {

    public TopLineAdapter(List<TopMessage> mDatas) {
        super(mDatas);
    }

    @Override
    public TopLineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TopLineHolder(BannerUtils.getView(parent, R.layout.top_line_item));
    }

    @Override
    public void onBindView(TopLineHolder holder, TopMessage data, int position, int size) {
        holder.message.setText(data.getTitle());
        holder.time.setText(data.getTime());
        if (data.getType() == 1) {
            holder.type.setText("课程");
        } else if (data.getType() == 2) {
            holder.type.setText("考勤");
        } else if (data.getType() == 3) {
            holder.type.setText("评价");
        }
    }

    class TopLineHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView type;
        public TextView time;

        public TopLineHolder(@NonNull View view) {
            super(view);
            message = view.findViewById(R.id.message);
            type = view.findViewById(R.id.label_type);
            time = view.findViewById(R.id.time);
        }
    }
}
