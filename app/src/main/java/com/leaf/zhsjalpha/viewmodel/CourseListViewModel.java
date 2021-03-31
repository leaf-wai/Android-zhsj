package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.net.UnknownHostException;
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

    public void getCourseDataList() {
        getCourseDataList(null, null, null, null, null, null);
    }

    public void getCourseDataList(String keyword) {
        getCourseDataList(null, null, null, null, null, keyword);
    }

    public void getCourseDataList(Integer gradeId, Integer courseType, Integer interestType, Double minprice, Double maxprice) {
        getCourseDataList(gradeId, courseType, interestType, minprice, maxprice, null);
    }

    public void getCourseDataList(Integer gradeId, Integer courseType, Integer interestType, Double minprice, Double maxprice, String keyword) {
        RetrofitHelper.getInstance().initCourseListCall(gradeId, courseType, interestType, minprice, maxprice, keyword).enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<CourseData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.setValue(200);
                        courseList.clear();
                        courseDataList.clear();
                        courseDataList = result.getData().getData();
                        for (CourseData courseData : courseDataList) {
                            Course course = new Course();
                            course.setClassId(courseData.getClassId());
                            course.setCourseName(courseData.getClassName());
                            course.setCourseImgUrl(courseData.getCourseImgUrl());
                            course.setPrice(courseData.getCoursePrice());
                            course.setRemain(courseData.getRemain());
                            course.setPayEndTime(courseData.getPayEndTime());
                            switch (courseData.getCourseType()) {
                                case 0:
                                    course.setCourseType("研学");
                                    break;
                                case 1:
                                    course.setCourseType("实践");
                                    break;
                                case 2:
                                    course.setCourseType("服务");
                                    break;
                                case 3:
                                    course.setCourseType("兴趣");
                                    break;
                                default:
                                    course.setCourseType("未知");
                                    break;
                            }
                            switch (courseData.getInterestType()) {
                                case 0:
                                    course.setInterestType("非兴趣");
                                    break;
                                case 1:
                                    course.setInterestType("科学益智类");
                                    break;
                                case 2:
                                    course.setInterestType("书法绘画类");
                                    break;
                                case 3:
                                    course.setInterestType("舞蹈体育类");
                                    break;
                                case 4:
                                    course.setInterestType("音乐艺术类");
                                    break;
                                case 5:
                                    course.setInterestType("综合语言类");
                                    break;
                                default:
                                    course.setInterestType("未知");
                                    break;
                            }
                            courseList.add(course);
                        }
                        courses.setValue(courseList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "加载课程列表失败");
                if (t instanceof UnknownHostException)
                    loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
