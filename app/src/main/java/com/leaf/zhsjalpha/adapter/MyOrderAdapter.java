package com.leaf.zhsjalpha.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.CourseDetailActivity;
import com.leaf.zhsjalpha.entity.MyOrder;
import com.leaf.zhsjalpha.utils.MyApplication;

import org.jetbrains.annotations.NotNull;

public class MyOrderAdapter extends BaseQuickAdapter<MyOrder, BaseViewHolder> {

    public MyOrderAdapter() {
        super(R.layout.list_my_order_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MyOrder myOrder) {
        baseViewHolder.setText(R.id.tv_orderNumber, String.valueOf(myOrder.getOrderId()));
        baseViewHolder.setText(R.id.tv_orderStatus, myOrder.getOrderStatus());
        baseViewHolder.setText(R.id.tv_courseName, myOrder.getCourseName());
        baseViewHolder.setText(R.id.tv_orderPrice, String.valueOf(myOrder.getOrderPrice()));
        baseViewHolder.setText(R.id.tv_orderDate, myOrder.getOrderDate());
        Glide.with(getContext())
                .load(myOrder.getCourseImgUrl())
                .placeholder(R.drawable.no_image)
                .into((ImageView) baseViewHolder.getView(R.id.riv_courseImg));
        baseViewHolder.getView(R.id.cl_orderCourse).setOnClickListener(v -> {
            Intent intent = new Intent(MyApplication.getContext(), CourseDetailActivity.class);
            intent.putExtra("classId", myOrder.getClassId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getContext().startActivity(intent);
        });
        if (myOrder.getOrderStatus().equals("未支付")) {
            ((TextView) baseViewHolder.getView(R.id.tv_orderStatus)).setTextColor(MyApplication.getContext().getResources().getColor(R.color.red));
            ((TextView) baseViewHolder.getView(R.id.label_pay)).setText("需支付 ¥");
            baseViewHolder.getView(R.id.LL_pay).setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.line_pay).setVisibility(View.VISIBLE);
        } else if (myOrder.getOrderStatus().equals("已确认")) {
            ((TextView) baseViewHolder.getView(R.id.tv_orderStatus)).setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
            ((TextView) baseViewHolder.getView(R.id.label_pay)).setText("已支付 ¥");
            baseViewHolder.getView(R.id.LL_pay).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.line_pay).setVisibility(View.GONE);
        } else {
            ((TextView) baseViewHolder.getView(R.id.tv_orderStatus)).setTextColor(MyApplication.getContext().getResources().getColor(R.color.gray3));
            ((TextView) baseViewHolder.getView(R.id.label_pay)).setText("需支付 ¥");
            baseViewHolder.getView(R.id.LL_pay).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.line_pay).setVisibility(View.GONE);
        }
    }
}
