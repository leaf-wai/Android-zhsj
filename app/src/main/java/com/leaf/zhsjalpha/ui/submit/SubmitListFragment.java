package com.leaf.zhsjalpha.ui.submit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.leaf.zhsjalpha.activity.PostDetailActivity;
import com.leaf.zhsjalpha.adapter.MyPostAdapter;
import com.leaf.zhsjalpha.databinding.FragmentSubmitListBinding;

import org.jetbrains.annotations.NotNull;

public class SubmitListFragment extends Fragment {

    private FragmentSubmitListBinding binding;
    private SubmitListViewModel mViewModel;
    private MyPostAdapter myPostAdapter;

    public static SubmitListFragment newInstance() {
        SubmitListFragment fragment = new SubmitListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubmitListBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(SubmitListViewModel.class);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        addListener();
        addObserver();
    }

    private void initRecyclerView() {
        myPostAdapter = new MyPostAdapter();
        myPostAdapter.setAnimationEnable(true);
        myPostAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        myPostAdapter.setAnimationFirstOnly(false);
        myPostAdapter.setFooterWithEmptyEnable(false);
        binding.recyclerViewWork.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewWork.setAdapter(myPostAdapter);
    }

    private void addListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mViewModel.getMyProduct();
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.setRefreshing(false), 500);
        });
    }

    private void addObserver() {
        mViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (integer == 404) {
                View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty, null, false);
                ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                    binding.swipeRefreshLayout.setRefreshing(true);
                    mViewModel.getMyProduct();
                });
                myPostAdapter.setEmptyView(emptyView);
            }
        });

        mViewModel.getMyProducts().observe(getViewLifecycleOwner(), myProducts -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (myProducts.size() == 0 && mViewModel.getLoadingStatus().getValue() == 200) {
                myPostAdapter.setList(myProducts);
                myPostAdapter.setEmptyView(R.layout.view_empty);
            } else {
                View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_foot, null, false);
                myPostAdapter.setList(myProducts);
                myPostAdapter.setFooterView(footView);
                SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                myPostAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                    intent.putExtra("postId", myProducts.get(position).getPostId());
                    intent.putExtra("classId", mViewModel.productDataList.get(position).getClassId());
                    intent.putExtra("postAuthor", userRead.getString("studentName", null));
                    intent.putExtra("description", userRead.getString("school", null));
                    intent.putExtra("postTitle", myProducts.get(position).getPostTitle());
                    intent.putExtra("postContent", myProducts.get(position).getPostContent());
                    intent.putExtra("postTime", myProducts.get(position).getPostBuildTime());
                    intent.putExtra("commentLevel", mViewModel.productDataList.get(position).getCommentLevel());
                    intent.putExtra("commentContent", mViewModel.productDataList.get(position).getCommentContent());
                    intent.putExtra("thumbUpNumber", String.valueOf(mViewModel.productDataList.get(position).getThumbUpNumbers()));
                    intent.putExtra("commentNumber", String.valueOf(mViewModel.productDataList.get(position).getReplyPostNumbers()));
                    intent.putExtra("postImageUrl", myProducts.get(position).getPostImageUrl());
                    intent.putExtra("thumb", mViewModel.productDataList.get(position).isThumb());
                    intent.putExtra("myProduct", true);
                    startActivity(intent);
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.swipeRefreshLayout.setRefreshing(true);
        mViewModel.getMyProduct();
    }
}