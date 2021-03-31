package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.DeclareAdapter;
import com.leaf.zhsjalpha.databinding.FragmentDeclareListBinding;
import com.leaf.zhsjalpha.viewmodel.DeclareListViewModel;

import org.jetbrains.annotations.NotNull;

public class DeclareListFragment extends Fragment {

    private DeclareAdapter adapter;
    private FragmentDeclareListBinding binding;
    private DeclareListViewModel mViewModel;

    public static DeclareListFragment newInstance() {
        DeclareListFragment fragment = new DeclareListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeclareListBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(getActivity()).get(DeclareListViewModel.class);
        initAdapter();
        addObserver();
        return binding.getRoot();
    }

    private void addObserver() {
        mViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (integer == 404) {
                View emptyView = View.inflate(getContext(), R.layout.view_network_error, null);
                ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                    mViewModel.getMyDeclare(mViewModel.getWeek().getValue());
                    adapter.setEmptyView(R.layout.view_loading);
                });
                adapter.setEmptyView(emptyView);
            }
        });
        mViewModel.getDeclares().observe(getViewLifecycleOwner(), declares -> {
            if (declares.size() == 0 && mViewModel.getLoadingStatus().getValue() == 200) {
                adapter.setList(declares);
                adapter.setEmptyView(R.layout.view_empty);
            } else {
                adapter.removeAllHeaderView();
                View footView = View.inflate(getContext(), R.layout.view_foot, null);
                View headView = View.inflate(getContext(), R.layout.view_head_week, null);
                ((TextView) headView.findViewById(R.id.tv_week)).setText(String.format("第 %d 周", mViewModel.getWeek().getValue()));
                adapter.setList(declares);
                adapter.setHeaderView(headView);
                adapter.setFooterView(footView);
            }
        });
    }

    private void initAdapter() {
        adapter = new DeclareAdapter();
        adapter.setAnimationEnable(true);
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        adapter.setAnimationFirstOnly(false);
        binding.recyclerViewDeclare.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewDeclare.setAdapter(adapter);
        adapter.setEmptyView(R.layout.view_loading);
        adapter.setHeaderWithEmptyEnable(false);
        adapter.setFooterWithEmptyEnable(false);
    }
}