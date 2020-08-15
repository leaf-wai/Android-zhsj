package com.leaf.zhsjalpha.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.SearchActivity;
import com.leaf.zhsjalpha.adapter.CourseAdapter;
import com.leaf.zhsjalpha.adapter.ImageNetAdapter;
import com.leaf.zhsjalpha.adapter.MyCourseAdapter;
import com.leaf.zhsjalpha.adapter.TopLineAdapter;
import com.leaf.zhsjalpha.bean.DataBean;
import com.leaf.zhsjalpha.databinding.FragmentHomeBinding;
import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.entity.MyCourse;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;
import com.youth.banner.util.BannerUtils;
import com.youth.banner.util.LogUtils;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class HomeFragment extends Fragment implements OnPageChangeListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private CourseAdapter courseAdapter;
    private MyCourseAdapter myCourseAdapter;
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<MyCourse> myCourses = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initCourseData();
        initMyCourseData();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        courseAdapter = new CourseAdapter(courses);
        myCourseAdapter = new MyCourseAdapter(myCourses);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        BannerUtils.setBannerRound(binding.banner2, 20);

        addObserver();
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.d("onPageSelected:" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        binding.recyclerViewCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMyCourse.setLayoutManager(linearLayoutManager);
        binding.recyclerViewCourse.setAdapter(courseAdapter);
        binding.recyclerViewMyCourse.setAdapter(myCourseAdapter);

        binding.banner.setAdapter(new ImageNetAdapter(DataBean.getTestData3()))//设置适配器
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getContext()))//设置指示器
                .addOnPageChangeListener(this)//添加切换监听
                .setOnBannerListener((data, position) -> {
                    Log.d("aaa", "position：" + position);
                });//设置点击事件,传this也行

        binding.banner2.setAdapter(new TopLineAdapter(DataBean.getTestData2()))
                .setOrientation(Banner.VERTICAL)
                .setOnBannerListener((data, position) -> {
                    Snackbar.make(binding.banner, ((DataBean) data).title, Snackbar.LENGTH_SHORT).show();
                    Log.d("aaa", "position：" + position);
                });
    }

    private void addObserver() {
        binding.btnLoadMore.setOnClickListener(v -> {
            binding.ivArrowDownMore.setVisibility(View.GONE);
            binding.pbLoading.setVisibility(View.VISIBLE);
            binding.tvLoadmore.setText("加载中");
            new Handler().postDelayed(() -> {
                initCourseData();
                courseAdapter.notifyDataSetChanged();
                binding.btnLoadMore.setVisibility(View.GONE);
            }, 1000);
        });

        binding.LLSearch.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SearchActivity.class));
        });
    }

    private void initCourseData() {
        Course course = new Course();
        course.setId(1);
        course.setCourseName("生活中的科学");
        course.setDate("2020-04-03报名");
        course.setAge("8-14岁");
        course.setCourseImageID(R.drawable.vector_drawable_logo_zhsj);
        course.setPrice("¥ 5999");
        course.setOriginalPrice("¥̶6̶9̶9̶9̶");
        course.setType("最新课程");
        courses.add(course);

        course = new Course();
        course.setId(2);
        course.setCourseName("生活中的科学");
        course.setDate("2020-04-03报名");
        course.setAge("8-14岁");
        course.setCourseImageID(R.drawable.vector_drawable_logo_zhsj);
        course.setPrice("¥ 5999");
        course.setOriginalPrice("¥̶6̶9̶9̶9̶");
        course.setType("最新课程");
        courses.add(course);

        course = new Course();
        course.setId(3);
        course.setCourseName("生活中的科学");
        course.setDate("2020-04-03报名");
        course.setAge("8-14岁");
        course.setCourseImageID(R.drawable.vector_drawable_logo_zhsj);
        course.setPrice("¥ 5999");
        course.setOriginalPrice("¥̶6̶9̶9̶9̶");
        course.setType("最新课程");
        courses.add(course);
    }

    private void initMyCourseData() {
        MyCourse myCourse = new MyCourse();
        myCourse.setId(1);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);

        myCourse = new MyCourse();
        myCourse.setId(2);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);

        myCourse = new MyCourse();
        myCourse.setId(3);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);

        myCourse = new MyCourse();
        myCourse.setId(4);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);

        myCourse = new MyCourse();
        myCourse.setId(5);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);

        myCourse = new MyCourse();
        myCourse.setId(6);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);

        myCourse = new MyCourse();
        myCourse.setId(7);
        myCourse.setCourseName("乐高课堂");
        myCourse.setCourseImageID(R.drawable.fm_lego);
        myCourses.add(myCourse);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBar.lightStatusBar(getActivity(), false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBar.lightStatusBar(getActivity(), false);
        }
    }
}