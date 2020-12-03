package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.TeammateAdapter;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityTeamDetailBinding;
import com.leaf.zhsjalpha.entity.ActivityUser;
import com.leaf.zhsjalpha.fragment.TeammateDetailFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.TeamViewModel;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class TeamDetailActivity extends AppCompatActivity {

    private int deletePosition;
    private List<ActivityUser> activityUserList = new ArrayList<>();

    private ActivityTeamDetailBinding binding;
    private TeamViewModel teamViewModel;
    private TeammateAdapter teammateAdapter;
    private TeammateDetailFragment dialogFragment;

    private Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT);
                if (response.body().getCode() == 200) {
                    finish();
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            ToastUtils.showToast("解散小队失败，请稍后重试！", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    private Callback<User> deleteCallback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT);
                    activityUserList.remove(deletePosition);
                    teammateAdapter.notifyItemRemoved(deletePosition);
                    teammateAdapter.notifyItemRangeChanged(deletePosition, teamViewModel.getActivityUsers().getValue().size() - deletePosition);
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            ToastUtils.showToast("删除队员失败，请稍后重试！", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityTeamDetailBinding.inflate(getLayoutInflater());
        teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ToastUtils.getInstance().initToast(this);
        initToolbar();
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.srlTeammates.setColorSchemeResources(R.color.colorPrimary);
        binding.srlTeammates.setOnRefreshListener(() -> teamViewModel.getActivityUser(getIntent().getStringExtra("teamId")));
        binding.toolbar.setTitle(getIntent().getStringExtra("teamName"));

        initRecyclerView();
        addObserver();
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void initRecyclerView() {
        teammateAdapter = new TeammateAdapter();
        binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.swipeRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        binding.swipeRecyclerView.setOnItemMenuClickListener(menuItemClickListener);
        binding.swipeRecyclerView.setOnItemClickListener((view, adapterPosition) -> binding.swipeRecyclerView.smoothOpenRightMenu(adapterPosition));
        binding.swipeRecyclerView.setAdapter(teammateAdapter);
    }

    private void addObserver() {
        teamViewModel.getLoadingStatus().observe(this, integer -> {
            if (binding.srlTeammates.isRefreshing())
                binding.srlTeammates.setRefreshing(false);
        });

        teamViewModel.getActivityUsers().observe(this, activityUsers -> {
            if (activityUsers.size() != 0) {
                activityUserList = activityUsers;
                teammateAdapter.notifyDataSetChanged(activityUserList);
            }
        });
    }

    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_80);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        SwipeMenuItem infoItem = new SwipeMenuItem(this).setBackground(R.drawable.selector_info)
                .setText("详细信息")
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);

        SwipeMenuItem deleteItem = new SwipeMenuItem(this).setBackground(R.drawable.selector_delete)
                .setText("踢出小队")
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);


        swipeRightMenu.addMenuItem(infoItem);
        swipeRightMenu.addMenuItem(deleteItem);
    };

    private OnItemMenuClickListener menuItemClickListener = (menuBridge, position) -> {
        menuBridge.closeMenu();

        int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            ActivityUser activityUser = activityUserList.get(position);
            if (menuPosition == 0) {
                dialogFragment = new TeammateDetailFragment().newInstance(activityUser.getUserName(), activityUser.getUserIdCard(), activityUser.getUserRace(), activityUser.getGradeId(), activityUser.getTel(), activityUser.getWx(), activityUser.getUserSex());
                dialogFragment.show(getSupportFragmentManager(), "teammateDetail");
                new Handler().postDelayed(() -> dialogFragment.getDialog().setCanceledOnTouchOutside(false), 200);
            } else if (menuPosition == 1) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setTitle("确定踢出小队？");
                builder.setNegativeButton("取消", (dialog, which) -> {

                });
                builder.setPositiveButton("确定", (dialog, which) -> {
                    deletePosition = position;
                    teamViewModel.deleteTeammate(deleteCallback, getIntent().getStringExtra("teamId"), getIntent().getStringExtra("processId"), activityUser.getUserId());
                });
                builder.show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.team_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_teammate_menu:
                Intent intent = new Intent(TeamDetailActivity.this, AddTeammateActivity.class);
                intent.putExtra("teamId", getIntent().getStringExtra("teamId"));
                startActivity(intent);
                break;
            case R.id.delete_team_menu:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setTitle("确定解散小队？");
                builder.setNegativeButton("取消", (dialog, which) -> {

                });
                builder.setPositiveButton("确定", (dialog, which) -> {
                    teamViewModel.deleteTeam(callback, getIntent().getStringExtra("teamId"), getIntent().getStringExtra("processId"));
                });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        teamViewModel.getActivityUser(getIntent().getStringExtra("teamId"));
        binding.srlTeammates.setRefreshing(true);
    }
}