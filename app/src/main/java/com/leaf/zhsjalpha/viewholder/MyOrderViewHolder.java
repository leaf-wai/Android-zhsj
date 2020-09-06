package com.leaf.zhsjalpha.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.widget.RoundImageView;

public class MyOrderViewHolder extends RecyclerView.ViewHolder {

    public TextView tvOrderNumber, tvOrderStatus, tvCourseName, labelPay, tvOrderPrice, tvOrderDate;
    public RoundImageView roundImageView;
    public LinearLayout llPay;
    public View linePay;

    public MyOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        tvOrderNumber = itemView.findViewById(R.id.tv_orderNumber);
        tvOrderStatus = itemView.findViewById(R.id.tv_orderStatus);
        tvCourseName = itemView.findViewById(R.id.tv_courseName);
        labelPay = itemView.findViewById(R.id.label_pay);
        tvOrderPrice = itemView.findViewById(R.id.tv_orderPrice);
        tvOrderDate = itemView.findViewById(R.id.tv_orderDate);
        roundImageView = itemView.findViewById(R.id.riv_courseImg);
        llPay = itemView.findViewById(R.id.LL_pay);
        linePay = itemView.findViewById(R.id.line_pay);
    }
}
