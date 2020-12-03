package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.MyOrderAdapter;
import com.leaf.zhsjalpha.databinding.FragmentOrderListBinding;
import com.leaf.zhsjalpha.viewmodel.MyOrderViewModel;

import org.jetbrains.annotations.NotNull;

public class OrderListFragment extends Fragment {

    private static final String POSITION = "position";
    private FragmentOrderListBinding binding;
    private MyOrderViewModel mViewModel;
    private MyOrderAdapter myOrderAdapter;

    private int mPosition;

    public static OrderListFragment newInstance(int position) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MyOrderViewModel.class);
        binding = FragmentOrderListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycleView();
        addObserver();

        switch (mPosition) {
            case 0:
                mViewModel.getMyOrder(-1);
                break;
            case 1:
                mViewModel.getMyOrder(0);
                break;
            case 2:
                mViewModel.getMyOrder(1);
                break;
            case 3:
                mViewModel.getMyOrder(2);
                break;
        }
    }

    private void initRecycleView() {
        myOrderAdapter = new MyOrderAdapter();
        myOrderAdapter.setAnimationEnable(true);
        myOrderAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        myOrderAdapter.setAnimationFirstOnly(false);
        binding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewOrder.setAdapter(myOrderAdapter);
        myOrderAdapter.setEmptyView(R.layout.view_loading);
    }

    private void addObserver() {
        mViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (integer == 404) {
                View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty, null, false);
                ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                    mViewModel.getMyOrder(mPosition - 1);
                    myOrderAdapter.setEmptyView(R.layout.view_loading);
                });
                myOrderAdapter.setEmptyView(emptyView);
            }
        });

        mViewModel.getMyOrders().observe(getViewLifecycleOwner(), myOrders -> {
            if (myOrders.size() == 0 && mViewModel.getLoadingStatus().getValue() == 200) {
                myOrderAdapter.setList(myOrders);
                myOrderAdapter.setEmptyView(R.layout.view_empty);
            } else {
                myOrderAdapter.setList(myOrders);
            }
        });

    }

//    private void initOrderData() {
//        MyOrder myOrder = new MyOrder();
//        myOrder.setOrderId("10004468");
//        myOrder.setOrderStatus("未支付");
//        myOrder.setCourseName("写好汉字");
//        myOrder.setOrderDate("2020-01-01 19:00");
//        myOrder.setOrderPrice(1999);
//
//        MyOrder myOrder2 = new MyOrder();
//        myOrder2.setOrderId(10004245);
//        myOrder2.setOrderStatus("已确认");
//        myOrder2.setCourseName("算术");
//        myOrder2.setOrderDate("2020-01-01 09:00");
//        myOrder2.setOrderPrice(2999);
//
//        MyOrder myOrder3 = new MyOrder();
//        myOrder3.setOrderId(10004468);
//        myOrder3.setOrderStatus("已取消");
//        myOrder3.setCourseName("绘画");
//        myOrder3.setOrderDate("2020-01-01 10:00");
//        myOrder3.setOrderPrice(999);
//
//        myOrders.add(myOrder);
//        myOrders.add(myOrder2);
//        myOrders.add(myOrder3);
//    }

//    private void initOrderData1() {
//        MyOrder myOrder = new MyOrder();
//        myOrder.setOrderId(10004468);
//        myOrder.setOrderStatus("未支付");
//        myOrder.setCourseName("写好汉字");
//        myOrder.setOrderDate("2020-01-01 19:00");
//        myOrder.setOrderPrice(1999);
//        myOrders1.add(myOrder);
//    }
//
//    private void initOrderData2() {
//        MyOrder myOrder = new MyOrder();
//        myOrder.setOrderId(10004245);
//        myOrder.setOrderStatus("已确认");
//        myOrder.setCourseName("算术");
//        myOrder.setOrderDate("2020-01-01 09:00");
//        myOrder.setOrderPrice(2999);
//        myOrders2.add(myOrder);
//    }
//
//    private void initOrderData3() {
//        MyOrder myOrder = new MyOrder();
//        myOrder.setOrderId(10004468);
//        myOrder.setOrderStatus("已取消");
//        myOrder.setCourseName("绘画");
//        myOrder.setOrderDate("2020-01-01 10:00");
//        myOrder.setOrderPrice(999);
//        myOrders3.add(myOrder);
//    }
}