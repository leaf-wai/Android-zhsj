package com.leaf.zhsjalpha.ui.community;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.flyco.tablayout.listener.OnTabSelectListener;
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

public class CommunityFragment extends Fragment implements OnTabSelectListener {

    private List<CourseData> courseDataList = new ArrayList<>();
    private List<String> classItemList = new ArrayList<>();

    private static String TAG = "community";
    private WorkWallViewModel workWallViewModel;
    private FragmentCommunityBinding binding;

    private Callback<Result<DataList<CourseData>>> callback = new Callback<Result<DataList<CourseData>>>() {
        @Override
        public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
            if (response.isSuccessful()) {
                Result<DataList<CourseData>> result = response.body();
                if (result.getData().getData().size() != 0) {
                    courseDataList = result.getData().getData();
                    for (CourseData courseData : courseDataList) {
                        classItemList.add(courseData.getClassName());
                    }
                    initTabLayout(classItemList);
                } else {
                    ToastUtils.showToast("你还没有班级！", Toast.LENGTH_SHORT);
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Throwable t) {
            ToastUtils.showToast("获取班级信息失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workWallViewModel = new ViewModelProvider(this).get(WorkWallViewModel.class);
        binding = FragmentCommunityBinding.inflate(getLayoutInflater());
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addObserver();
    }

    private void initTabLayout(List<String> classItemList) {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] mTitles = classItemList.toArray(new String[classItemList.size()]);
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
                workWallViewModel.getStudentClass(callback);
            }
        });
    }

    public void loadLoginState() {
        SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (userRead.getBoolean("hasLogined", false)) {
            workWallViewModel.getLogin().setValue(true);
        } else {
            workWallViewModel.getLogin().setValue(false);
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
}