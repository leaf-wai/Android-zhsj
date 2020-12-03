package com.leaf.zhsjalpha.fragment;

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
import com.leaf.zhsjalpha.adapter.PostAdapter;
import com.leaf.zhsjalpha.databinding.FragmentPostListBinding;
import com.leaf.zhsjalpha.utils.TimeUtils;
import com.leaf.zhsjalpha.viewmodel.WorkWallViewModel;

import org.jetbrains.annotations.NotNull;

public class PostListFragment extends Fragment {
    private String mClassID;
    private static final String CLASSID = "classId";

    private FragmentPostListBinding binding;
    private WorkWallViewModel mViewModel;
    private PostAdapter postAdapter;

    public static PostListFragment newInstance(String classId) {
        PostListFragment fragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putString(CLASSID, classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentPostListBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(WorkWallViewModel.class);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        if (getArguments() != null) {
            mClassID = getArguments().getString(CLASSID);
            mViewModel.getAllProduct(mClassID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        postAdapter = new PostAdapter();
        postAdapter.setAnimationEnable(true);
        postAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        postAdapter.setAnimationFirstOnly(false);
        postAdapter.setFooterWithEmptyEnable(false);
        binding.recyclerViewWork.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewWork.setAdapter(postAdapter);
    }

    private void addListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mViewModel.getAllProduct(mClassID);
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
                    mViewModel.getAllProduct(mClassID);
                });
                postAdapter.setEmptyView(emptyView);
            }
        });

        mViewModel.getProductData().observe(getViewLifecycleOwner(), productData -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (productData.size() == 0 && mViewModel.getLoadingStatus().getValue() == 200) {
                postAdapter.setList(productData);
                postAdapter.setEmptyView(R.layout.view_empty);
            } else {
                View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_foot, null, false);
                postAdapter.setList(productData);
                postAdapter.setFooterView(footView);
                SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                postAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                    intent.putExtra("postId", productData.get(position).getPostId());
                    intent.putExtra("classId", productData.get(position).getClassId());
                    intent.putExtra("postAuthor", productData.get(position).getStudentName() == null ? "匿名" : productData.get(position).getStudentName());
                    intent.putExtra("description", userRead.getString("school", null));
                    intent.putExtra("postTitle", productData.get(position).getPostTitle());
                    intent.putExtra("postContent", productData.get(position).getPostContent());
                    intent.putExtra("postTime", TimeUtils.calTime(productData.get(position).getBuildTime()));
                    intent.putExtra("commentLevel", productData.get(position).getCommentLevel());
                    intent.putExtra("commentContent", productData.get(position).getCommentContent());
                    intent.putExtra("thumbUpNumber", String.valueOf(productData.get(position).getThumbUpNumbers()));
                    intent.putExtra("commentNumber", String.valueOf(productData.get(position).getReplyPostNumbers()));
                    intent.putExtra("postImageUrl", productData.get(position).getFileUrl());
                    intent.putExtra("thumb", productData.get(position).isThumb());
                    intent.putExtra("myProduct", false);
                    startActivity(intent);
                });
            }
        });
    }
}
