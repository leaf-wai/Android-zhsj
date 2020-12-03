package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.ActivityAdapter;
import com.leaf.zhsjalpha.databinding.ActivityActivityListBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.ActivityListViewModel;
import com.leaf.zhsjalpha.widget.LoadMoreView;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class ActivityListActivity extends AppCompatActivity {
    private ActivityActivityListBinding binding;
    private ActivityListViewModel activityListViewModel;
    private ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        ToastUtils.getInstance().initToast(this);
        binding = ActivityActivityListBinding.inflate(getLayoutInflater());
        activityListViewModel = new ViewModelProvider(this).get(ActivityListViewModel.class);
        setContentView(binding.getRoot());

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        initAdapter();
        initLoadMore();
        addListener();
        addObserver();
        requestList();
    }

    private void addListener() {
        binding.buttonBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        binding.LLSearch.setOnClickListener(v -> {
            if (getIntent().getStringExtra("keyword") != null) {
                Intent intent = getIntent();
                intent.putExtra("keyword", binding.tvKeyword.getText());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                startActivity(new Intent(this, SearchActivity.class));
            }
        });
    }

    private void addObserver() {
        activityListViewModel.getLoadingStatus().observe(this, integer -> {
            switch (integer) {
                //加载第一页失败
                case 404:
                    View emptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, null, false);
                    ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                    emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                        requestList();
                        activityAdapter.setEmptyView(R.layout.view_loading);
                    });
                    activityAdapter.setEmptyView(emptyView);
                    break;
                //加载下一页失败
                case 405:
                    activityAdapter.getLoadMoreModule().loadMoreFail();
                    break;
                //显示没有更多数据布局
                case 666:
                    activityAdapter.getLoadMoreModule().loadMoreEnd();
                    break;
                //数据加载完毕
                case 667:
                    activityAdapter.getLoadMoreModule().loadMoreComplete();
                    break;
                //搜索不到数据
                case 668:
                    activityAdapter.setEmptyView(R.layout.view_empty);
                    break;
            }

        });
        activityListViewModel.getActivities().observe(this, activities -> {
            if (activities.size() != 0) {
                activityAdapter.addData(activities);
                activityAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(ActivityListActivity.this, ActivityDetailActivity.class);
                    intent.putExtra("activityId", activityAdapter.getData().get(position).getActivityId());
                    startActivity(intent);
                });
            }
        });
    }

    private void initAdapter() {
        activityAdapter = new ActivityAdapter(R.layout.list_activity_item);
        activityAdapter.setAnimationEnable(true);
        activityAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        activityAdapter.setAnimationFirstOnly(false);
        binding.recyclerViewActivityList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewActivityList.setAdapter(activityAdapter);
        activityAdapter.setEmptyView(R.layout.view_loading);
        activityAdapter.setFooterWithEmptyEnable(false);
    }

    private void requestList() {
        if (getIntent().getStringExtra("keyword") != null) {
            binding.tvKeyword.setText(getIntent().getStringExtra("keyword"));
            activityListViewModel.getActivityList(getIntent().getStringExtra("keyword"));
        } else {
            activityListViewModel.getActivityList(null);
        }
    }

    private void initLoadMore() {
        activityAdapter.getLoadMoreModule().setLoadMoreView(new LoadMoreView());
        activityAdapter.getLoadMoreModule().setOnLoadMoreListener(this::requestList);
        activityAdapter.getLoadMoreModule().setAutoLoadMore(true);
        activityAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }
}