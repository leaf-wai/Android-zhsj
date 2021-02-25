package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.TeamAdapter;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivitySelectTeamBinding;
import com.leaf.zhsjalpha.fragment.CreateTeamFragment;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.TeamViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class SelectTeamActivity extends AppCompatActivity {

    private ActivitySelectTeamBinding binding;
    private TeamViewModel teamViewModel;
    private TeamAdapter teamAdapter;
    private LoadingFragment loadingFragment;

    private Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            if (response.isSuccessful() && response.body() != null) {
                loadingFragment.dismiss();
                ToastUtils.showToast(getApplicationContext(), response.body().getDetail());
                if (response.body().getCode() == 200) {
                    finish();
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, Throwable t) {
            loadingFragment.dismiss();
            ToastUtils.showToast(getApplicationContext(), "报名失败，请稍后重试！");
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivitySelectTeamBinding.inflate(getLayoutInflater());
        teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        setContentView(binding.getRoot());
        initToolbar();
        initRecyclerView();
        addObserver();
        teamViewModel.processId = getIntent().getStringExtra("processId");
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> teamViewModel.getTeam());
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void initRecyclerView() {
        teamAdapter = new TeamAdapter(R.layout.list_select_team_item);
        teamAdapter.setAnimationEnable(true);
        teamAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        teamAdapter.setAnimationFirstOnly(false);
        binding.recyclerViewTeam.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewTeam.setAdapter(teamAdapter);
        teamAdapter.setEmptyView(R.layout.view_loading);
    }

    private void addObserver() {
        teamViewModel.getLoadingStatus().observe(this, integer -> {
            if (binding.swipeRefreshLayout.isRefreshing())
                binding.swipeRefreshLayout.setRefreshing(false);
            if (integer == 404) {
                View emptyView = View.inflate(this, R.layout.view_empty, null);
                ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                    teamViewModel.getTeam();
                    teamAdapter.setEmptyView(R.layout.view_loading);
                });
                teamAdapter.setEmptyView(emptyView);
            }
        });

        teamViewModel.getTeams().observe(this, teams -> {
            if (teams.size() == 0 && teamViewModel.getLoadingStatus().getValue() == 200) {
                teamAdapter.setList(teams);
                teamAdapter.setEmptyView(R.layout.view_empty);
            } else {
                teamAdapter.setList(teams);
                teamAdapter.setOnItemClickListener((adapter, view, position) -> {
                    loadingFragment.show(getSupportFragmentManager(), "submit");
                    teamViewModel.attendActivity(callback, teams.get(position).getTeamId(), teams.get(position).getProcessId());
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        teamViewModel.getTeam();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_team_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.create_team:
                CreateTeamFragment dialogFragment = new CreateTeamFragment();
                dialogFragment.show(getSupportFragmentManager(), "create_team");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}