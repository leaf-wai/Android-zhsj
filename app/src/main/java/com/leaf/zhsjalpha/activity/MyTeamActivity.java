package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.leaf.zhsjalpha.databinding.ActivityMyTeamBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.TeamViewModel;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class MyTeamActivity extends AppCompatActivity {

    private ActivityMyTeamBinding binding;
    private TeamViewModel teamViewModel;
    private TeamAdapter teamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityMyTeamBinding.inflate(getLayoutInflater());
        teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        setContentView(binding.getRoot());
        initToolbar();
        initRecyclerView();
        addObserver();
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
        teamAdapter = new TeamAdapter(R.layout.list_team_item);
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
                View emptyView = View.inflate(this, R.layout.view_network_error, null);
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
                    Intent intent = new Intent(MyTeamActivity.this, TeamDetailActivity.class);
                    intent.putExtra("teamId", teams.get(position).getTeamId());
                    intent.putExtra("teamName", teams.get(position).getTeamName());
                    intent.putExtra("processId", teamViewModel.teamDataList.get(position).getProcessId());
                    startActivity(intent);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}