package com.leaf.zhsjalpha.ui.community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.databinding.FragmentCommunityBinding;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.fragment.PostListFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.WorkWallViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class CommunityFragment extends Fragment implements OnTabSelectListener, Callback<Result<DataList<CourseData>>> {

    private List<CourseData> courseDataList = new ArrayList<>();
    private List<String> classItemList = new ArrayList<>();
    private WorkWallViewModel workWallViewModel;
    private FragmentCommunityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workWallViewModel = new ViewModelProvider(this).get(WorkWallViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false);
        binding.setData(workWallViewModel);
        binding.setLifecycleOwner(this);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addObserver();
        binding.btnLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
    }

    private void initTabLayout(List<String> classItemList) {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] mTitles = classItemList.toArray(new String[0]);
        for (CourseData courseData : courseDataList) {
            mFragments.add(PostListFragment.newInstance(courseData.getClassId()));
        }
        binding.tlWorkWall.setViewPager(binding.vpWorkWall, mTitles, getActivity(), mFragments);
        binding.tlWorkWall.setCurrentTab(0);
    }

    private void addObserver() {
        workWallViewModel.getLogin().observe(getViewLifecycleOwner(), aBoolean -> {
            Log.d("aaa", "addObserver: " + aBoolean);
            if (aBoolean) {
                workWallViewModel.getStudentClass(this);
            }
        });
    }

    public void loadLoginState() {
        SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (userRead.getBoolean("hasLogined", false)) {
            if (!workWallViewModel.getLogin().getValue()) {
                workWallViewModel.getLogin().setValue(true);
            }
        } else {
            if (workWallViewModel.getLogin().getValue()) {
                workWallViewModel.getLogin().setValue(false);
            }
        }
    }

    @Override
    public void onTabSelect(int position) {
        binding.vpWorkWall.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onResume() {
        super.onResume();
        loadLoginState();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBar.lightStatusBar(getActivity(), false);
            loadLoginState();
        }
    }

    @Override
    public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, Response<Result<DataList<CourseData>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            Result<DataList<CourseData>> result = response.body();
            if (result.getData().getData().size() != 0) {
                courseDataList = result.getData().getData();
                for (CourseData courseData : courseDataList) {
                    classItemList.add(courseData.getClassName());
                }
                initTabLayout(classItemList);
            } else {
                ToastUtils.showToast(getContext(), "你还没有班级！");
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, Throwable t) {
        ToastUtils.showToast(getContext(), "获取班级信息失败");
        Log.d("aaa", "onFailure: " + t.getMessage());
    }
}