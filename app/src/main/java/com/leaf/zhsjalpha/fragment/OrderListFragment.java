package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leaf.zhsjalpha.adapter.MyOrderAdapter;
import com.leaf.zhsjalpha.databinding.FragmentOrderListBinding;
import com.leaf.zhsjalpha.entity.MyOrder;

import java.util.ArrayList;

public class OrderListFragment extends Fragment {

    private static final String POSITION = "position";
    private FragmentOrderListBinding binding;
    private MyOrderAdapter myOrderAdapter;
    private MyOrderAdapter myOrderAdapter1;
    private MyOrderAdapter myOrderAdapter2;
    private MyOrderAdapter myOrderAdapter3;
    private ArrayList<MyOrder> myOrders = new ArrayList<>();
    private ArrayList<MyOrder> myOrders1 = new ArrayList<>();
    private ArrayList<MyOrder> myOrders2 = new ArrayList<>();
    private ArrayList<MyOrder> myOrders3 = new ArrayList<>();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initOrderData();
        initOrderData1();
        initOrderData2();
        initOrderData3();
        binding = FragmentOrderListBinding.inflate(getLayoutInflater());
        myOrderAdapter = new MyOrderAdapter(myOrders);
        myOrderAdapter1 = new MyOrderAdapter(myOrders1);
        myOrderAdapter2 = new MyOrderAdapter(myOrders2);
        myOrderAdapter3 = new MyOrderAdapter(myOrders3);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        switch (mPosition) {
            case 0:
                binding.recyclerViewOrder.setAdapter(myOrderAdapter);
                break;
            case 1:
                binding.recyclerViewOrder.setAdapter(myOrderAdapter1);
                break;
            case 2:
                binding.recyclerViewOrder.setAdapter(myOrderAdapter2);
                break;
            case 3:
                binding.recyclerViewOrder.setAdapter(myOrderAdapter3);
                break;
        }
    }

    private void initOrderData() {
        MyOrder myOrder = new MyOrder();
        myOrder.setOrderNumber(10004468);
        myOrder.setOrderStatus("未支付");
        myOrder.setCourseName("写好汉字");
        myOrder.setOrderDate("2020-01-01 19:00");
        myOrder.setOrderPrice(1999);

        MyOrder myOrder2 = new MyOrder();
        myOrder2.setOrderNumber(10004245);
        myOrder2.setOrderStatus("已确认");
        myOrder2.setCourseName("算术");
        myOrder2.setOrderDate("2020-01-01 09:00");
        myOrder2.setOrderPrice(2999);

        MyOrder myOrder3 = new MyOrder();
        myOrder3.setOrderNumber(10004468);
        myOrder3.setOrderStatus("已取消");
        myOrder3.setCourseName("绘画");
        myOrder3.setOrderDate("2020-01-01 10:00");
        myOrder3.setOrderPrice(999);

        myOrders.add(myOrder);
        myOrders.add(myOrder2);
        myOrders.add(myOrder3);
    }

    private void initOrderData1() {
        MyOrder myOrder = new MyOrder();
        myOrder.setOrderNumber(10004468);
        myOrder.setOrderStatus("未支付");
        myOrder.setCourseName("写好汉字");
        myOrder.setOrderDate("2020-01-01 19:00");
        myOrder.setOrderPrice(1999);
        myOrders1.add(myOrder);
    }

    private void initOrderData2() {
        MyOrder myOrder = new MyOrder();
        myOrder.setOrderNumber(10004245);
        myOrder.setOrderStatus("已确认");
        myOrder.setCourseName("算术");
        myOrder.setOrderDate("2020-01-01 09:00");
        myOrder.setOrderPrice(2999);
        myOrders2.add(myOrder);
    }

    private void initOrderData3() {
        MyOrder myOrder = new MyOrder();
        myOrder.setOrderNumber(10004468);
        myOrder.setOrderStatus("已取消");
        myOrder.setCourseName("绘画");
        myOrder.setOrderDate("2020-01-01 10:00");
        myOrder.setOrderPrice(999);
        myOrders3.add(myOrder);
    }
}