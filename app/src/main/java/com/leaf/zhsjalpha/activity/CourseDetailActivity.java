package com.leaf.zhsjalpha.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityCourseDetailBinding;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.fragment.CourseDetailFragment;
import com.leaf.zhsjalpha.utils.NumberUtils;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.CourseDetailViewModel;
import com.yuruiyin.appbarlayoutbehavior.AppBarLayoutBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL;
import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class CourseDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, Callback<Result<CourseData>> {
    private ActivityCourseDetailBinding binding;
    private CourseDetailViewModel courseDetailViewModel;

    private boolean isScrolling = false;

    private CourseDetailFragment fragment_one;
    private CourseDetailFragment fragment_two;
    private CourseDetailFragment fragment_three;

    private static final int HOME_ONE = 0;
    private static final int HOME_TWO = 1;
    private static final int HOME_THREE = 2;
    private int index;
    private int currentTabIndex = 0;

    private ArrayList<TextView> textViews = new ArrayList<>();
    private TextView[] mTabs;
    private TextView[] mTabs_second;
    private FragmentManager manager;
    private ArrayList<Fragment> list_fragment = new ArrayList<>();

    private final View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.tv_bottom_prepare:
            case R.id.tv_top_prepare:
                switchFragment(R.id.tv_top_prepare);
                break;

            case R.id.tv_bottom_target:
            case R.id.tv_top_target:
                switchFragment(R.id.tv_top_target);
                break;

            case R.id.tv_bottom_mission:
            case R.id.tv_top_mission:
                switchFragment(R.id.tv_top_mission);
                break;

            case R.id.LL_back:
            case R.id.LL_black_back:
                onBackPressed();
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, true);
        courseDetailViewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.statusBarFix.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        courseDetailViewModel.getCourseInfo(getIntent().getStringExtra("classId"), this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addListener() {
        binding.appBar.addOnOffsetChangedListener(this);
        binding.tvDetail.setOnTouchListener((View v, MotionEvent ev) -> {
                    if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (binding.clTitle.getVisibility() == View.VISIBLE && !isScrolling && !binding.tvDetail.isSelected()) {
                            binding.nestedScrollView.scrollTo(0, 0);
                            binding.nestedScrollView.smoothScrollTo(0, 0);
                            CoordinatorLayout.Behavior behavior =
                                    ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                            AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                            appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);
                            setAppBarLayoutOffset(binding.appBar, -(int) (binding.appBar.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - getStatusBarHeight(this)));
                            selectTitle(binding.tvDetail);
                        }
                    }
                    return false;
                }
        );

        binding.tvBrief.setOnTouchListener((View v, MotionEvent ev) -> {
                    if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (!binding.tvBrief.isSelected()) {
                            isScrolling = true;
                            binding.nestedScrollView.scrollTo(0, 0);
                            binding.nestedScrollView.smoothScrollTo(0, 0);
                            CoordinatorLayout.Behavior behavior =
                                    ((CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams()).getBehavior();
                            AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                            appBarLayoutBehavior.onInterceptTouchEvent(binding.coordinator, binding.appBar, ev);
                            binding.appBar.setExpanded(true, true);
                            selectTitle(binding.tvBrief);
                        }
                    }
                    return false;
                }
        );

        binding.nestedScrollView.setFadingEdgeLength(0);
        binding.tvTopPrepare.setOnClickListener(listener);
        binding.tvBottomPrepare.setOnClickListener(listener);
        binding.tvTopTarget.setOnClickListener(listener);
        binding.tvBottomTarget.setOnClickListener(listener);
        binding.tvTopMission.setOnClickListener(listener);
        binding.tvBottomMission.setOnClickListener(listener);
        binding.LLBack.setOnClickListener(listener);
        binding.LLBlackBack.setOnClickListener(listener);
    }

    public void switchFragment(int id) {
        FragmentTransaction ft = manager.beginTransaction();
        TextView relativeLayout = findViewById(id);
        String tag = (String) relativeLayout.getTag();
        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            int num = Integer.parseInt(tag);
            ft.add(R.id.frameLayout, list_fragment.get(num), tag);
        }

        for (int i = 0; i < list_fragment.size(); i++) {
            Fragment fragment = list_fragment.get(i);
            if (fragment.getTag() != null) {
                if (fragment.getTag().equals(tag)) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }
        ft.commitAllowingStateLoss();
        switch (id) {
            case R.id.tv_top_prepare://首页
                index = HOME_ONE;
                break;
            case R.id.tv_top_target:
                index = HOME_TWO;
                break;
            case R.id.tv_top_mission:
                index = HOME_THREE;
                break;
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs_second[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        mTabs_second[index].setSelected(true);
        currentTabIndex = index;
    }

    public void selectTitle(TextView textView) {
        for (int i = 0; i < textViews.size(); i++) {
            if (textView == textViews.get(i)) {
                if (!textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(true);
                    ScaleAnimation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    textViews.get(i).startAnimation(animation);
                }
            } else {
                if (textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(false);
                    ScaleAnimation animation = new ScaleAnimation(1.2f, 1, 1.2f, 1,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    textViews.get(i).startAnimation(animation);
                }
            }

        }
    }

    private void initArray() {
        textViews.add(binding.tvBrief);
        textViews.add(binding.tvDetail);
    }

    private void initFragment() {
        manager = getSupportFragmentManager();
        mTabs = new TextView[3];
        mTabs[0] = binding.tvTopPrepare;
        mTabs[1] = binding.tvTopTarget;
        mTabs[2] = binding.tvTopMission;


        mTabs_second = new TextView[3];
        mTabs_second[0] = binding.tvBottomPrepare;
        mTabs_second[1] = binding.tvBottomTarget;
        mTabs_second[2] = binding.tvBottomMission;

        fragment_one = CourseDetailFragment.newInstance(1, getIntent().getStringExtra("classId"));
        fragment_two = CourseDetailFragment.newInstance(2, getIntent().getStringExtra("classId"));
        fragment_three = CourseDetailFragment.newInstance(3, getIntent().getStringExtra("classId"));

        list_fragment.add(fragment_one);
        list_fragment.add(fragment_two);
        list_fragment.add(fragment_three);
        switchFragment(R.id.tv_top_prepare);
    }

    public void setAppBarLayoutOffset(AppBarLayout appBar, int offset) {
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {

            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();

            if (topAndBottomOffset != offset) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(appBarLayoutBehavior.getTopAndBottomOffset(), offset);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int offsetOther = (int) animation.getAnimatedValue();
                        appBarLayoutBehavior.setTopAndBottomOffset(offsetOther);
                        if (binding.clTitle.getVisibility() == View.GONE) {
                            binding.clTitle.setVisibility(View.VISIBLE);
                            binding.LLBlackBack.setVisibility(View.GONE);
                            binding.statusBarFix.setVisibility(View.VISIBLE);
                            StatusBar.lightStatusBar(getParent(), false);

                            Animation animation_come = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.alpha_detail_come);
                            Animation animation_go = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.alpha_detail_go);
                            binding.clTitle.setAnimation(animation_come);
                            binding.statusBarFix.setAnimation(animation_come);
                            binding.LLBlackBack.setAnimation(animation_go);
                            animation_come.start();
                            animation_go.start();
                        }
                    }
                });
                valueAnimator.start();
            }
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (Math.abs(i) >= 10) {
            if (binding.clTitle.getVisibility() == View.GONE) {
                binding.clTitle.setVisibility(View.VISIBLE);
                binding.LLBlackBack.setVisibility(View.GONE);
                binding.statusBarFix.setVisibility(View.VISIBLE);
                StatusBar.lightStatusBar(CourseDetailActivity.this, false);

                Animation animation_come = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.alpha_detail_come);
                Animation animation_go = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.alpha_detail_go);
                binding.clTitle.setAnimation(animation_come);
                binding.LLBlackBack.setAnimation(animation_go);
                binding.statusBarFix.setAnimation(animation_come);
                animation_come.start();
                animation_go.start();
            }
        } else {
            if (binding.clTitle.getVisibility() == View.VISIBLE) {
                isScrolling = false;
                binding.clTitle.setVisibility(View.GONE);
                binding.LLBlackBack.setVisibility(View.VISIBLE);
                binding.statusBarFix.setVisibility(View.INVISIBLE);
                StatusBar.lightStatusBar(CourseDetailActivity.this, true);

                Animation animation_go = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.alpha_detail_go);
                Animation animation_come = AnimationUtils.loadAnimation(CourseDetailActivity.this, R.anim.alpha_detail_come);
                binding.clTitle.setAnimation(animation_go);
                binding.LLBlackBack.setAnimation(animation_come);
                binding.statusBarFix.setAnimation(animation_go);
                animation_go.start();
                animation_come.start();
            }
        }

        if (Math.abs(i) >= appBarLayout.getTotalScrollRange() - getResources().getDimension(R.dimen.dp_50) - StatusBar.getStatusBarHeight(this)) {
            //选中详情
            if (!binding.tvDetail.isSelected()) {
                selectTitle(binding.tvDetail);
            }
            binding.llTopSecondTitle.setVisibility(View.VISIBLE);
        } else {
            if (!binding.tvBrief.isSelected()) {
                selectTitle(binding.tvBrief);
            }
            binding.llTopSecondTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponse(@NotNull Call<Result<CourseData>> call, Response<Result<CourseData>> response) {
        if (response.isSuccessful() && response.body() != null) {
            Result<CourseData> result = response.body();
            if (result.getCode() == 200) {
                Glide.with(getApplicationContext())
                        .load(BASE_URL + result.getData().getCourseImgUrl())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .placeholder(R.drawable.no_image)
                        .into(binding.ivCourseImage);
                binding.tvCourseName.setText(result.getData().getClassName());
                binding.tvTeacherName.setText(result.getData().getTeacherName());
                binding.tvStudentNumber.setText(String.valueOf(result.getData().getStudentNumber()));
                binding.tvPayStartTime.setText(result.getData().getPayStartTime());
                binding.tvPayEndTime.setText(result.getData().getPayEndTime());
                binding.tvRemain.setText(result.getData().getRemain() == null ? "未知" : String.valueOf(result.getData().getRemain()));
                if (result.getData().getRegularTimeDto() != null) {
                    binding.tvStartDate.setText(result.getData().getRegularTimeDto().getCourseStartDate() == null ? "未知" : result.getData().getRegularTimeDto().getCourseStartDate());
                    binding.tvEndDate.setText(result.getData().getRegularTimeDto().getCourseEndDate() == null ? "未知" : result.getData().getRegularTimeDto().getCourseEndDate());
                    String courseTime = NumberUtils.getWeekName(Integer.parseInt(result.getData().getRegularTimeDto().getWeek().get(0))) + " " + result.getData().getRegularTimeDto().getCourseStartTime() + "-" + result.getData().getRegularTimeDto().getCourseEndTime();
                    binding.tvCourseTime.setText(courseTime);
                    binding.tvClassroom.setText(result.getData().getRegularTimeDto().getRegularClassRoom());
                }

                if (result.getData().getFitGradeId() != null) {
                    String[] gradeArray = result.getData().getFitGradeId().split(",");
                    String fitGrade = "";
                    for (String s : gradeArray) {
                        if (NumberUtils.isInteger(s))
                            fitGrade = fitGrade + " " + NumberUtils.getGradeName(Integer.parseInt(s), getApplication());
                    }
                    binding.tvFitGrade.setText(fitGrade);
                }

                if (result.getData().getCoursePrice() == 0) {
                    binding.tvPrice.setText("免费");
                } else {
                    binding.tvPrice.setText(String.valueOf(result.getData().getCoursePrice()));
                }
                binding.labelCourseType.setText(NumberUtils.getCourseTypeName(result.getData().getCourseType()));
                binding.labelInterestType.setText(NumberUtils.getInterestTypeName(result.getData().getInterestType()));
                addListener();
                initFragment();
                initArray();
            } else {
                ToastUtils.showToast(getApplicationContext(), "加载课程信息失败");
                onBackPressed();
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<Result<CourseData>> call, Throwable t) {
        ToastUtils.showToast(getApplicationContext(), "加载课程信息失败");
        onBackPressed();
        Log.d("aaa", "onFailure: " + t.getMessage());
    }
}