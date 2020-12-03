package com.leaf.zhsjalpha.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.ProcessAdapter;
import com.leaf.zhsjalpha.databinding.ActivityActivityDetailBinding;
import com.leaf.zhsjalpha.entity.ActivityInfoList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.ActivityDetailViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class ActivityDetailActivity extends AppCompatActivity {

    private static String BASE_URL = "https://zhsj.bnuz.edu.cn/ComprehensiveSys";

    private ActivityActivityDetailBinding binding;
    private ActivityDetailViewModel activityDetailViewModel;
    private ProcessAdapter processAdapter;
    private Callback<Result<ActivityInfoList>> callback = new Callback<Result<ActivityInfoList>>() {
        @Override
        public void onResponse(Call<Result<ActivityInfoList>> call, Response<Result<ActivityInfoList>> response) {
            if (response.isSuccessful()) {
                Result<ActivityInfoList> result = response.body();
                Glide.with(MyApplication.getContext())
                        .load(BASE_URL + result.getData().getActivityInfoEntity().getImageLong())
                        .placeholder(R.drawable.no_image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.ivActivityImage);
                binding.tvActivityName.setText(result.getData().getActivityInfoEntity().getActivityName());
                binding.labelActivityTheme.setText(result.getData().getActivityInfoEntity().getActivityTheme());
                binding.tvCoachCount.setText(String.valueOf(result.getData().getActivityInfoEntity().getCoachCount()));
                binding.tvContestantCount.setText(String.valueOf(result.getData().getActivityInfoEntity().getContestantCount()));
                binding.tvActivityStartTime.setText(result.getData().getActivityInfoEntity().getActivityStartTime());
                binding.tvActivityEndTime.setText(result.getData().getActivityInfoEntity().getActivityEndTime());
                binding.tvActivityAddress.setText(result.getData().getActivityInfoEntity().getActivityAddress());
                binding.tvActivityDescription.setText(result.getData().getActivityInfoEntity().getActivityDescription());

                processAdapter.setList(result.getData().getProcessesList());
                processAdapter.setEmptyView(R.layout.view_empty);
            }
        }

        @Override
        public void onFailure(Call<Result<ActivityInfoList>> call, Throwable t) {
            ToastUtils.showToast("加载活动信息失败", Toast.LENGTH_SHORT);
            finish();
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, true);
        binding = ActivityActivityDetailBinding.inflate(getLayoutInflater());
        activityDetailViewModel = new ViewModelProvider(this).get(ActivityDetailViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ToastUtils.getInstance().initToast(this);
        initAdapter();
        addListener();

        binding.statusBarFix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        activityDetailViewModel.getActivityInfo(getIntent().getStringExtra("activityId"), callback);
    }

    private void initAdapter() {
        processAdapter = new ProcessAdapter();
        processAdapter.setAnimationEnable(true);
        processAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        processAdapter.setAnimationFirstOnly(false);
        binding.recyclerViewProcessList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewProcessList.setAdapter(processAdapter);
        processAdapter.setEmptyView(R.layout.view_loading);
    }

    private void addListener() {
        binding.LLBack.setOnClickListener(v -> finish());
        binding.LLBlackBack.setOnClickListener(v -> finish());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nsvDetail.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (Math.abs(scrollY) >= 10) {
                    if (binding.clTitle.getVisibility() == View.GONE) {
                        binding.clTitle.setVisibility(View.VISIBLE);
                        binding.LLBlackBack.setVisibility(View.GONE);
                        binding.statusBarFix.setVisibility(View.VISIBLE);
                        StatusBar.lightStatusBar(ActivityDetailActivity.this, false);

                        Animation animation_come = AnimationUtils.loadAnimation(ActivityDetailActivity.this, R.anim.alpha_detail_come);
                        Animation animation_go = AnimationUtils.loadAnimation(ActivityDetailActivity.this, R.anim.alpha_detail_go);
                        binding.clTitle.setAnimation(animation_come);
                        binding.LLBlackBack.setAnimation(animation_go);
                        binding.statusBarFix.setAnimation(animation_come);
                        animation_come.start();
                        animation_go.start();
                    }
                } else {
                    if (binding.clTitle.getVisibility() == View.VISIBLE) {
                        binding.clTitle.setVisibility(View.GONE);
                        binding.LLBlackBack.setVisibility(View.VISIBLE);
                        binding.statusBarFix.setVisibility(View.INVISIBLE);
                        StatusBar.lightStatusBar(ActivityDetailActivity.this, true);

                        Animation animation_go = AnimationUtils.loadAnimation(ActivityDetailActivity.this, R.anim.alpha_detail_go);
                        Animation animation_come = AnimationUtils.loadAnimation(ActivityDetailActivity.this, R.anim.alpha_detail_come);
                        binding.clTitle.setAnimation(animation_go);
                        binding.LLBlackBack.setAnimation(animation_come);
                        binding.statusBarFix.setAnimation(animation_go);
                        animation_go.start();
                        animation_come.start();
                    }
                }
            });
        }
    }

}