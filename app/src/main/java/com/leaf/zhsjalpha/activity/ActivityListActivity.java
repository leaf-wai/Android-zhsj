package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.ActivityAdapter;
import com.leaf.zhsjalpha.databinding.ActivityActivityListBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
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
        binding = ActivityActivityListBinding.inflate(getLayoutInflater());
        activityListViewModel = new ViewModelProvider(this).get(ActivityListViewModel.class);
        setContentView(binding.getRoot());
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        initAdapter();
        initLoadMore();
        addListener();
        addObserver();
        requestList();
    }

    private void addListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> requestList());
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
            binding.swipeRefreshLayout.setRefreshing(false);
            switch (integer) {
                //加载第一页失败
                case 404:
                    View emptyView = View.inflate(this, R.layout.view_empty, null);
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
            binding.swipeRefreshLayout.setRefreshing(false);
            if (activities.size() != 0) {
                activityAdapter.addData(activities);
                activityAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Pair<View, String> pair1 = Pair.create(view.findViewById(R.id.riv_activityImg), "activityImage");
                    Pair<View, String> pair2 = Pair.create(view.findViewById(R.id.tv_activityName), "activityName");
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityListActivity.this, pair1, pair2).toBundle();
                    Intent intent = new Intent(ActivityListActivity.this, ActivityDetailActivity.class);
                    intent.putExtra("activityId", activityAdapter.getData().get(position).getActivityId());
                    startActivity(intent, bundle);
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
        binding.swipeRefreshLayout.setRefreshing(true);
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