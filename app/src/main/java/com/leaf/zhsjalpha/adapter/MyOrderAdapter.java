package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.MyOrder;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.viewholder.MyOrderViewHolder;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderViewHolder> {

    private List<MyOrder> myOrders;

    public MyOrderAdapter(List<MyOrder> myOrders) {
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_my_order_item, parent, false);
        return new MyOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {
        MyOrder myOrder = myOrders.get(position);
        holder.tvOrderNumber.setText(String.valueOf(myOrder.getOrderNumber()));
        holder.tvOrderStatus.setText(myOrder.getOrderStatus());
        holder.tvCourseName.setText(myOrder.getCourseName());
        holder.tvOrderDate.setText(myOrder.getOrderDate());
        holder.tvOrderPrice.setText(String.valueOf(myOrder.getOrderPrice()));
        Glide.with(MyApplication.getContext())
                .load(myOrder.courseImgUrl)
                .placeholder(R.drawable.edu)
                .into(holder.roundImageView);
        if (myOrder.getOrderStatus().equals("未支付")) {
            holder.tvOrderStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
            holder.labelPay.setText("需支付 ¥");
            holder.llPay.setVisibility(View.VISIBLE);
            holder.linePay.setVisibility(View.VISIBLE);
        } else if (myOrder.getOrderStatus().equals("已确认")) {
            holder.tvOrderStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
            holder.labelPay.setText("已支付 ¥");
            holder.llPay.setVisibility(View.GONE);
            holder.linePay.setVisibility(View.GONE);
        } else {
            holder.tvOrderStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.gray3));
            holder.labelPay.setText("需支付 ¥");
            holder.llPay.setVisibility(View.GONE);
            holder.linePay.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
