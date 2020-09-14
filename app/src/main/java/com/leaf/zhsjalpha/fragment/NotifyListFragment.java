package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.NotifyAdapter;
import com.leaf.zhsjalpha.databinding.FragmentNotifyListBinding;
import com.leaf.zhsjalpha.entity.Notify;

import java.util.ArrayList;

public class NotifyListFragment extends Fragment {

    private static final String POSITION = "position";
    private ArrayList<Notify> notifies = new ArrayList<>();
    private FragmentNotifyListBinding binding;
    private NotifyAdapter notifyAdapter;
    private int mPosition;

    public static NotifyListFragment newInstance(int position) {
        NotifyListFragment fragment = new NotifyListFragment();
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
        initNotifyData();
        binding = FragmentNotifyListBinding.inflate(getLayoutInflater());
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        notifyAdapter = new NotifyAdapter(notifies);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerViewNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewNotify.setAdapter(notifyAdapter);
//        switch (mPosition) {
//            case 0:
//                binding.recyclerViewNotify.setAdapter(myOrderAdapter);
//                break;
//            case 1:
//                binding.recyclerViewNotify.setAdapter(myOrderAdapter1);
//                break;
//            case 2:
//                binding.recyclerViewNotify.setAdapter(myOrderAdapter2);
//                break;
//            case 3:
//                binding.recyclerViewNotify.setAdapter(myOrderAdapter3);
//                break;
//        }

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.setRefreshing(false), 500);
        });
    }

    private void initNotifyData() {
        Notify notify = new Notify();
        notify.setType("course");
        notify.setTitle("选课成功");
        notify.setContent("吉他进阶课程选课成功");
        notify.setTime("20分钟前");
        notifies.add(notify);
        notifies.add(notify);
        notifies.add(notify);
        notifies.add(notify);
        notifies.add(notify);
    }
}