package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.leaf.zhsjalpha.databinding.FragmentCourseDetailBinding;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.CourseDetailViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailFragment extends Fragment {

    private FragmentCourseDetailBinding binding;
    private CourseDetailViewModel courseDetailViewModel;
    private static final String POSITION = "position";
    private static final String CLASSID = "classId";
    private int mPosition;
    private String mClassId;

    private Callback<Result<CourseData>> callback = new Callback<Result<CourseData>>() {
        @Override
        public void onResponse(@NotNull Call<Result<CourseData>> call, Response<Result<CourseData>> response) {
            if (response.isSuccessful()) {
                Result<CourseData> result = response.body();
                if (result.getCode() == 200) {
                    switch (mPosition) {
                        case 1:
                            binding.tvDetail.setText(result.getData().getCoursePrepare() == null ? "暂无数据" : result.getData().getCoursePrepare());
                            break;
                        case 2:
                            binding.tvDetail.setText(result.getData().getCourseTarget() == null ? "暂无数据" : result.getData().getCourseTarget());
                            break;
                        case 3:
                            binding.tvDetail.setText(result.getData().getCourseMission() == null ? "暂无数据" : result.getData().getCourseMission());
                            break;
                    }
                } else {
                    ToastUtils.showToast("加载课程详情失败", Toast.LENGTH_SHORT);
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<CourseData>> call, Throwable t) {
            ToastUtils.showToast("加载课程详情失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    public static CourseDetailFragment newInstance(int position, String classId) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(CLASSID, classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION);
            mClassId = getArguments().getString(CLASSID);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCourseDetailBinding.inflate(getLayoutInflater());
        courseDetailViewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseDetailViewModel.getCourseInfo(mClassId, callback);
    }
}