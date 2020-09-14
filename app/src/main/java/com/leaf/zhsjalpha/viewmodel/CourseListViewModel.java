package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseListViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<List<Course>> courses;
    public List<Course> courseList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();

    public CourseListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<List<Course>> getCourses() {
        if (courses == null) {
            courses = new MutableLiveData<>();
            courses.setValue(courseList);
        }
        return courses;
    }

    public void getCourseDataListAll() {
        RetrofitHelper.getInstance().getAllClassCall(null).enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<CourseData>>> call, Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<CourseData>> result = response.body();
                    if (result.getCode() == 200) {
                        courseDataList.addAll(result.getData().getData());
                        for (int i = 0; i < courseDataList.size(); i++) {
                            Course course = new Course();
                            course.setCourseName(courseDataList.get(i).getClassName());
                            course.setCourseImgUrl(courseDataList.get(i).getCourseImgUrl());
                            course.setPrice(courseDataList.get(i).getCoursePrice());
                            courseList.add(course);
                        }
                        courses.setValue(courseList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<CourseData>>> call, Throwable t) {
                ToastUtils.showToast("加载课程列表失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void searchCourseData(String keyword) {
        RetrofitHelper.getInstance().getAllClassCall(keyword).enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<CourseData>>> call, Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<CourseData>> result = response.body();
                    if (result.getCode() == 200) {
                        courseDataList.addAll(result.getData().getData());
                        for (int i = 0; i < courseDataList.size(); i++) {
                            Course course = new Course();
                            course.setCourseName(courseDataList.get(i).getClassName());
                            course.setCourseImgUrl(courseDataList.get(i).getCourseImgUrl());
                            course.setPrice(courseDataList.get(i).getCoursePrice());
                            courseList.add(course);
                        }
                        courses.setValue(courseList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<CourseData>>> call, Throwable t) {
                ToastUtils.showToast("加载课程列表失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
