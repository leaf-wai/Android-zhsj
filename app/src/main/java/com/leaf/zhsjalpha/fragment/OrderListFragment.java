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
                View emptyView = View.inflate(getContext(), R.layout.view_empty, null);
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
}