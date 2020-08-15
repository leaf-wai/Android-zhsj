package com.leaf.zhsjalpha.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.DataBean;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

public class TopLineAdapter extends BannerAdapter<DataBean, TopLineAdapter.TopLineHolder> {

    public TopLineAdapter(List<DataBean> mDatas) {
        super(mDatas);
    }

    @Override
    public TopLineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TopLineHolder(BannerUtils.getView(parent, R.layout.top_line_item));
    }

    @Override
    public void onBindView(TopLineHolder holder, DataBean data, int position, int size) {
        holder.message.setText(data.title);
        if (data.msgType == 1) {
            holder.type.setText("课程");
        } else if (data.msgType == 2) {
            holder.type.setText("活动");
        } else if (data.msgType == 3) {
            holder.type.setText("订单");
        }
    }

    class TopLineHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView type;

        public TopLineHolder(@NonNull View view) {
            super(view);
            message = view.findViewById(R.id.message);
            type = view.findViewById(R.id.label_type);
        }
    }
}
