package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leaf.zhsjalpha.databinding.FragmentOrderListBinding;

public class OrderListFragment extends Fragment {

    private static final String POSITION = "position";
    private FragmentOrderListBinding binding;
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
        binding = FragmentOrderListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (mPosition) {
            case 0:
                binding.tvLabel.setText("全部订单");
                break;
            case 1:
                binding.tvLabel.setText("未支付");
                break;
            case 2:
                binding.tvLabel.setText("已确认");
                break;
            case 3:
                binding.tvLabel.setText("已取消");
                break;
        }
    }
}