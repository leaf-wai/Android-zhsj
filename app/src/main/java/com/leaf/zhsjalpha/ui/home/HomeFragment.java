package com.leaf.zhsjalpha.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.ActivityDetailActivity;
import com.leaf.zhsjalpha.activity.ActivityListActivity;
import com.leaf.zhsjalpha.activity.CourseDetailActivity;
import com.leaf.zhsjalpha.activity.CourseListActivity;
import com.leaf.zhsjalpha.activity.DeclareActivity;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.activity.NotifyActivity;
import com.leaf.zhsjalpha.activity.RadarActivity;
import com.leaf.zhsjalpha.activity.RegisterActivity;
import com.leaf.zhsjalpha.activity.ScheduleActivity;
import com.leaf.zhsjalpha.activity.SearchActivity;
import com.leaf.zhsjalpha.activity.evaluate.EvaluateActivity;
import com.leaf.zhsjalpha.adapter.ActivityAdapter;
import com.leaf.zhsjalpha.adapter.CourseAdapter;
import com.leaf.zhsjalpha.adapter.ImageNetAdapter;
import com.leaf.zhsjalpha.adapter.MyCourseAdapter;
import com.leaf.zhsjalpha.adapter.TopLineAdapter;
import com.leaf.zhsjalpha.bean.DataBean;
import com.leaf.zhsjalpha.bean.TopMessage;
import com.leaf.zhsjalpha.databinding.FragmentHomeBinding;
import com.leaf.zhsjalpha.entity.Article;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.MessageData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.TimeUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;
import com.youth.banner.util.BannerUtils;
import com.youth.banner.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class HomeFragment extends Fragment implements OnPageChangeListener {

    private HomeViewModel mViewModel;
    private FragmentHomeBinding binding;
    private CourseAdapter courseAdapter;
    private MyCourseAdapter myCourseAdapter;
    private ActivityAdapter activityAdapter;
//    private ArticleAdapter articleAdapter;
    private ArrayList<Article> articles = new ArrayList<>();
    private List<MessageData> messageDataList = new ArrayList<>();
    private List<TopMessage> topMessageList = new ArrayList<>();
    private View.OnClickListener listener = v -> {
        SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.FL_notify:
                if (userRead.getBoolean("hasLogined", false)) {
                    startActivity(new Intent(getActivity(), NotifyActivity.class));
                } else {
                    ToastUtils.showToast("请先登录综合实践平台", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_declare:
                if (userRead.getBoolean("hasLogined", false)) {
                    startActivity(new Intent(getActivity(), DeclareActivity.class));
                } else {
                    ToastUtils.showToast("请先登录综合实践平台", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_evaluate:
                if (userRead.getBoolean("hasLogined", false)) {
                    startActivity(new Intent(getActivity(), EvaluateActivity.class));
                } else {
                    ToastUtils.showToast("请先登录综合实践平台", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_moreCourse:
                startActivity(new Intent(getActivity(), CourseListActivity.class));
                break;
            case R.id.LL_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.LL_moreShare:
                break;
            case R.id.LL_moreActivity:
                startActivity(new Intent(getActivity(), ActivityListActivity.class));
                break;
            case R.id.LL_schedule:
                if (userRead.getBoolean("hasLogined", false)) {
                    startActivity(new Intent(getActivity(), ScheduleActivity.class));
                } else {
                    ToastUtils.showToast("请先登录综合实践平台", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_radar:
                if (userRead.getBoolean("hasLogined", false)) {
                    startActivity(new Intent(getActivity(), RadarActivity.class));
                } else {
                    ToastUtils.showToast("请先登录综合实践平台", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
        }
    };

    private Callback<Result<DataList<MessageData>>> callback = new Callback<Result<DataList<MessageData>>>() {
        @Override
        public void onResponse(@NotNull Call<Result<DataList<MessageData>>> call, Response<Result<DataList<MessageData>>> response) {
            if (response.isSuccessful()) {
                if (binding.swipeRefreshLayout.isRefreshing()) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
                Result<DataList<MessageData>> result = response.body();
                if (result.getCode() == 200) {
                    topMessageList.clear();
                    messageDataList = result.getData().getData();
                    for (MessageData messageData : messageDataList) {
                        TopMessage topMessage = new TopMessage();
                        topMessage.setTitle(messageData.getContent());
                        topMessage.setTime(TimeUtils.calTime(messageData.getCreateTime()));
                        topMessage.setType(messageData.getType());
                        topMessageList.add(topMessage);
                    }
                    binding.banner2.setAdapter(new TopLineAdapter(topMessageList))
                            .setOrientation(Banner.VERTICAL)
                            .setOnBannerListener((data, position) -> startActivity(new Intent(getActivity(), NotifyActivity.class)));
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<DataList<MessageData>>> call, Throwable t) {
            ToastUtils.showToast("加载消息失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(this);

//        initShareArticle();
        initRecycleView();
        initView();
        addListener();
        addObserver();

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
        mViewModel.getMessages(callback);

        binding.banner.setAdapter(new ImageNetAdapter(DataBean.getTestData3()))//设置适配器
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getContext()))//设置指示器
                .setDelayTime(5000)
                .addOnPageChangeListener(this)//添加切换监听
                .setOnBannerListener((data, position) -> {
                    Log.d("aaa", "position：" + position);
                });

        binding.banner3.setAdapter(new ImageNetAdapter(DataBean.getTestData3())).addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(getContext()))
                .setDelayTime(5000)
                .addOnPageChangeListener(this)
                .setOnBannerListener((data, position) -> {
                    Log.d("aaa", "position：" + position);
                });
    }

    private void initView() {
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        BannerUtils.setBannerRound(binding.banner2, 20);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void addListener() {
        binding.btnLogin.setOnClickListener(listener);
        binding.btnRegister.setOnClickListener(listener);
        binding.FLNotify.setOnClickListener(listener);
        binding.LLDeclare.setOnClickListener(listener);
        binding.LLEvaluate.setOnClickListener(listener);
        binding.LLMoreCourse.setOnClickListener(listener);
        binding.LLSearch.setOnClickListener(listener);
        binding.LLMoreShare.setOnClickListener(listener);
        binding.LLMoreActivity.setOnClickListener(listener);
        binding.LLSchedule.setOnClickListener(listener);
        binding.LLRadar.setOnClickListener(listener);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadLoginState();
            if (mViewModel.getLogin().getValue()) {
                mViewModel.getMyCourse();
                mViewModel.getCourseDataList();
                mViewModel.getActivityList();
                mViewModel.getMessages(callback);
            }
        });
    }

    private void addObserver() {
        mViewModel.getLogin().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.swipeRefreshLayout.setRefreshing(true);
                mViewModel.getMyCourse();
                mViewModel.getCourseDataList();
                mViewModel.getActivityList();
                mViewModel.getMessages(callback);
            }
        });

        mViewModel.getMyCourses().observe(getViewLifecycleOwner(), myCourses -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (myCourses.size() == 0) {
                binding.cvMycourse.setVisibility(View.GONE);
                myCourseAdapter.setList(myCourses);
                myCourseAdapter.setEmptyView(R.layout.view_empty);
            } else {
                binding.cvMycourse.setVisibility(View.VISIBLE);
                myCourseAdapter.setList(myCourses);
                myCourseAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                    intent.putExtra("classId", myCourses.get(position).getClassId());
                    startActivity(intent);
                });
            }
        });

        mViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (courses.size() == 0) {
                courseAdapter.setList(courses);
                courseAdapter.setEmptyView(R.layout.view_empty);
            } else {
                courseAdapter.setList(courses);
                courseAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                    intent.putExtra("classId", courses.get(position).getClassId());
                    startActivity(intent);
                });
            }
        });

        mViewModel.getActivities().observe(getViewLifecycleOwner(), activities -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (activities.size() == 0) {
                activityAdapter.setList(activities);
                activityAdapter.setEmptyView(R.layout.view_empty);
            } else {
                activityAdapter.setList(activities);
                activityAdapter.setOnItemClickListener((adapter, view, position) -> {
                    Intent intent = new Intent(getActivity(), ActivityDetailActivity.class);
                    intent.putExtra("activityId", activities.get(position).getActivityId());
                    startActivity(intent);
                });
            }
        });
    }

    private void loadLoginState() {
        SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (userRead.getBoolean("hasLogined", false)) {
            mViewModel.getLogin().setValue(true);
        } else {
            mViewModel.getLogin().setValue(false);
        }
    }

    private void initRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //MyCourse
        myCourseAdapter = new MyCourseAdapter();
        binding.recyclerViewMyCourse.setLayoutManager(linearLayoutManager);
        binding.recyclerViewMyCourse.setAdapter(myCourseAdapter);

        //Course
        courseAdapter = new CourseAdapter(R.layout.list_home_course_item);
        binding.recyclerViewCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCourse.setAdapter(courseAdapter);
        courseAdapter.setEmptyView(R.layout.view_empty);

        //Activity
        activityAdapter = new ActivityAdapter(R.layout.list_home_activity_item);
        binding.recyclerViewActivity.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewActivity.setAdapter(activityAdapter);
        activityAdapter.setEmptyView(R.layout.view_empty);

        //Share
//        articleAdapter = new ArticleAdapter(articles);
//        binding.recylerViewShare.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recylerViewShare.setAdapter(articleAdapter);

    }

//    private void initShareArticle() {
//        Article article = new Article();
//        article.setTitle("11岁，从最初兴趣到各项荣誉");
//        article.setContent("满怀热情的小少年，满怀热情地探索与设计。11岁，从最初兴趣到各项荣誉，到底是一种怎么样的存在。所谓11岁，从最初兴趣到各项荣誉，关键是11岁，从最初兴趣到各项荣誉需要如何写。");
//        article.setArticleImageUrl("https://pic.baixiongz.com/uploads/2020/09/17/b2d671c6ff596.jpg");
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//    }

    @Override
    public void onResume() {
        super.onResume();
        binding.swipeRefreshLayout.setRefreshing(true);
        loadLoginState();
        mViewModel.getUserInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("aaa", "onHiddenChanged: " + hidden);
        if (!hidden) {
            StatusBar.lightStatusBar(getActivity(), false);
            loadLoginState();
        }
    }
}