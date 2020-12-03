package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.CourseAdapter;
import com.leaf.zhsjalpha.databinding.ActivityCourseListBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.CourseListViewModel;

import java.util.ArrayList;
import java.util.List;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class CourseListActivity extends AppCompatActivity {

    private Integer courseType = null;
    private Integer interestType = null;
    private Integer grade = null;
    private List<String> typeList = new ArrayList<>();
    private List<String> interestTypeList = new ArrayList<>();
    private List<String> gradeList = new ArrayList<>();

    private CourseAdapter courseAdapter;
    private ActivityCourseListBinding binding;
    private CourseListViewModel courseListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        ToastUtils.getInstance().initToast(this);
        binding = ActivityCourseListBinding.inflate(getLayoutInflater());
        courseListViewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        initFilterData();
        requestList();

        binding.LLDrawer.LLStatusbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
        binding.dlOptions.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
        addListener();
        initAdapter();
        addObserver();
    }

    private void addObserver() {
        courseListViewModel.getLoadingStatus().observe(this, integer -> {
            if (integer == 404) {
                View emptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, null, false);
                ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                    requestList();
                    courseAdapter.setEmptyView(R.layout.view_loading);
                });
                courseAdapter.setEmptyView(emptyView);
            }
        });
        courseListViewModel.getCourses().observe(this, courses -> {
            if (courseAdapter.hasHeaderLayout()) {
                courseAdapter.removeAllHeaderView();
            }
            if (courses.size() == 0 && courseListViewModel.getLoadingStatus().getValue() == 200) {
                courseAdapter.setList(courses);
                courseAdapter.setEmptyView(R.layout.view_empty);
            } else {
                View footView = LayoutInflater.from(this).inflate(R.layout.view_foot, null, false);
                courseAdapter.setList(courses);
                courseAdapter.setFooterView(footView);
                courseAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
                    intent.putExtra("classId", courses.get(position).getClassId());
                    startActivity(intent);
                });
            }
        });
    }

    private void addListener() {
        binding.buttonBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        binding.LLFilter.setOnClickListener(v -> {
            if (binding.dlOptions.isDrawerOpen(GravityCompat.END)) {
                binding.dlOptions.closeDrawer(GravityCompat.END);
            } else {
                binding.dlOptions.openDrawer(GravityCompat.END);
            }

        });
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
        binding.LLDrawer.btnConfirm.setOnClickListener(v -> {
            Double minPrice = null, maxPrice = null;
            if (!binding.LLDrawer.etMinPrice.getText().toString().isEmpty())
                minPrice = Double.valueOf(String.valueOf(binding.LLDrawer.etMinPrice.getText()));
            if (!binding.LLDrawer.etMaxPrice.getText().toString().isEmpty())
                maxPrice = Double.valueOf(String.valueOf(binding.LLDrawer.etMaxPrice.getText()));
            if (getIntent().getStringExtra("keyword") != null)
                courseListViewModel.getCourseDataList(grade, courseType, interestType, minPrice, maxPrice, getIntent().getStringExtra("keyword"));
            else
                courseListViewModel.getCourseDataList(grade, courseType, interestType, minPrice, maxPrice);
            binding.dlOptions.closeDrawer(GravityCompat.END);
            View headView = LayoutInflater.from(this).inflate(R.layout.view_head, null, false);
            courseAdapter.addHeaderView(headView);
        });
        binding.LLDrawer.btnReset.setOnClickListener(v -> {
            binding.LLDrawer.etMinPrice.setText("");
            binding.LLDrawer.etMaxPrice.setText("");
            binding.LLDrawer.lvType.clearAllSelect();
            binding.LLDrawer.lvInterestType.clearAllSelect();
            binding.LLDrawer.lvGrade.clearAllSelect();
            courseType = null;
            interestType = null;
            grade = null;
        });

        binding.LLDrawer.lvType.setOnLabelSelectChangeListener((label, data, isSelect, position) -> {
            if (isSelect) {
                courseType = position;
            }
        });
        binding.LLDrawer.lvInterestType.setOnLabelSelectChangeListener((label, data, isSelect, position) -> {
            if (isSelect) {
                interestType = position;
            }
        });
        binding.LLDrawer.lvGrade.setOnLabelSelectChangeListener((label, data, isSelect, position) -> {
            if (isSelect) {
                grade = position;
            }
        });
    }

    private void initAdapter() {
        courseAdapter = new CourseAdapter(R.layout.list_course_item);
        courseAdapter.setAnimationEnable(true);
        courseAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        courseAdapter.setAnimationFirstOnly(false);
        binding.recyclerViewCourseList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCourseList.setAdapter(courseAdapter);
        courseAdapter.setEmptyView(R.layout.view_loading);
        courseAdapter.setFooterWithEmptyEnable(false);
    }

    private void initFilterData() {
        typeList.add("研学");
        typeList.add("实践");
        typeList.add("服务");
        typeList.add("兴趣");
        interestTypeList.add("非兴趣");
        interestTypeList.add("科学益智类");
        interestTypeList.add("书法绘画类");
        interestTypeList.add("舞蹈体育类");
        interestTypeList.add("音乐艺术类");
        interestTypeList.add("综合语言类");
        gradeList.add("成人");
        gradeList.add("一年级");
        gradeList.add("二年级");
        gradeList.add("三年级");
        gradeList.add("四年级");
        gradeList.add("五年级");
        gradeList.add("六年级");
        gradeList.add("七年级");
        gradeList.add("八年级");
        gradeList.add("九年级");
        gradeList.add("小班");
        gradeList.add("中班");
        gradeList.add("大班");
        binding.LLDrawer.lvType.setLabels(typeList);
        binding.LLDrawer.lvInterestType.setLabels(interestTypeList);
        binding.LLDrawer.lvGrade.setLabels(gradeList);
    }

    private void requestList() {
        if (getIntent().getStringExtra("keyword") != null) {
            binding.tvKeyword.setText(getIntent().getStringExtra("keyword"));
            courseListViewModel.getCourseDataList(getIntent().getStringExtra("keyword"));
        } else {
            courseListViewModel.getCourseDataList();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.dlOptions.isDrawerOpen(GravityCompat.END))
            binding.dlOptions.closeDrawer(GravityCompat.END);
        else
            binding.buttonBack.performClick();
    }
}