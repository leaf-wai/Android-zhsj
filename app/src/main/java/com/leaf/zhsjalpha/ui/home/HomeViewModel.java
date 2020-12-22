package com.leaf.zhsjalpha.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.entity.Activity;
import com.leaf.zhsjalpha.entity.ActivityData;
import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.MessageData;
import com.leaf.zhsjalpha.entity.MyCourse;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> Login;
    public MutableLiveData<List<MyCourse>> myCourses;
    public MutableLiveData<List<Course>> courses;
    public MutableLiveData<List<Activity>> activities;
    public List<Activity> activityList = new ArrayList<>();
    public List<ActivityData> activityDataList = new ArrayList<>();
    public List<Course> courseList = new ArrayList<>();
    public List<MyCourse> myCourseList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();
    public List<CourseData> courseDataList1 = new ArrayList<>();

    public MutableLiveData<List<Course>> getCourses() {
        if (courses == null) {
            courses = new MutableLiveData<>();
        }
        return courses;
    }

    public MutableLiveData<List<MyCourse>> getMyCourses() {
        if (myCourses == null) {
            myCourses = new MutableLiveData<>();
        }
        return myCourses;
    }

    public MutableLiveData<Boolean> getLogin() {
        if (Login == null) {
            Login = new MutableLiveData<>();
            Login.setValue(false);
        }
        return Login;
    }

    public MutableLiveData<List<Activity>> getActivities() {
        if (activities == null) {
            activities = new MutableLiveData<>();
        }
        return activities;
    }

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getMyCourse() {
        RetrofitHelper.getInstance().getMyCourseCall(null).enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<CourseData>> result = response.body();
                    if (result.getCode() == 200) {
                        myCourseList.clear();
                        courseDataList.clear();
                        courseDataList = result.getData().getData();
                        for (CourseData courseData : courseDataList) {
                            MyCourse myCourse = new MyCourse();
                            myCourse.setCourseName(courseData.getClassName());
                            myCourse.setCourseImageUrl(courseData.getCourseImgUrl());
                            myCourse.setClassId(courseData.getClassId());
                            myCourseList.add(myCourse);
                        }
                        myCourses.postValue(myCourseList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<CourseData>>> call, Throwable t) {
                ToastUtils.showToast("加载我的课程失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUserInfo() {
        RetrofitHelper.getInstance().getUserInfoCall().enqueue(new Callback<Result<UserInfo>>() {
            @Override
            public void onResponse(@NotNull Call<Result<UserInfo>> call, @NotNull Response<Result<UserInfo>> response) {
                if (response.body() == null) {
                    ToastUtils.showToast("登录状态已过期，请重新登录", Toast.LENGTH_SHORT);
                    Logout();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<UserInfo>> call, @NotNull Throwable t) {
                ToastUtils.showToast("网络错误: " + t.getMessage(), Toast.LENGTH_SHORT);
                new Handler().postDelayed(() -> ToastUtils.showToast("登录状态已过期，请重新登录", Toast.LENGTH_SHORT), 2000);
                Logout();
                Log.d("aaa", t.getMessage());
            }
        });
    }

    public void getCourseDataList() {
        RetrofitHelper.getInstance().initCourseListCall(null, null, null, null, null, null).enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<CourseData>> result = response.body();
                    if (result.getCode() == 200) {
                        courseList.clear();
                        courseDataList1.clear();
                        courseDataList1 = result.getData().getData();
                        for (int i = 0; i < 5; i++) {
                            Course course = new Course();
                            course.setClassId(courseDataList1.get(i).getClassId());
                            course.setCourseName(courseDataList1.get(i).getClassName());
                            course.setCourseImgUrl(courseDataList1.get(i).getCourseImgUrl());
                            course.setPrice(courseDataList1.get(i).getCoursePrice());
                            course.setRemain(courseDataList1.get(i).getRemain());
                            course.setPayEndTime(courseDataList1.get(i).getPayEndTime());
                            switch (courseDataList1.get(i).getCourseType()) {
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
                            switch (courseDataList1.get(i).getInterestType()) {
                                case 0:
                                    course.setInterestType("音乐艺术类");
                                    break;
                                case 1:
                                    course.setInterestType("书法绘画类");
                                    break;
                                case 2:
                                    course.setInterestType("科学益智类");
                                    break;
                                case 3:
                                    course.setInterestType("舞蹈体育类");
                                    break;
                                case 4:
                                    course.setInterestType("综合语言类");
                                    break;
                                case 5:
                                    course.setInterestType("非艺术类");
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
                ToastUtils.showToast("加载课程列表失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getActivityList() {
        RetrofitHelper.getInstance().getActivityListCall(0, 5, null).enqueue(new Callback<Result<DataList<ActivityData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<ActivityData>>> call, @NotNull Response<Result<DataList<ActivityData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<ActivityData>> result = response.body();
                    if (result.getCode() == 200) {
                        activityList.clear();
                        activityDataList.clear();
                        if (result.getData().getData().size() != 0) {
                            activityDataList = result.getData().getData();
                            for (ActivityData activityData : activityDataList) {
                                Activity activity = new Activity();
                                activity.setActivityName(activityData.getActivityName());
                                activity.setActivityDescription(activityData.getActivityDescription());
                                activity.setActivityAddress(activityData.getActivityAddress());
                                activity.setActivityId(activityData.getActivityId());
                                activity.setImageUrl(activityData.getImageUrl());
                                activity.setActivityStartTime(activityData.getActivityStartTime());
                                activity.setActivityEndTime(activityData.getActivityEndTime());
                                activity.setMaxCount(activityData.getMaxCount());
                                activityList.add(activity);
                            }
                            activities.setValue(activityList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<ActivityData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载活动列表失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getMessages(Callback<Result<DataList<MessageData>>> callback) {
        RetrofitHelper.getInstance().getMessagesCall(1).enqueue(callback);
    }

    public void Logout() {
        Login.setValue(false);
        SharedPreferences.Editor userEdit = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        userEdit.remove("studentName");
        userEdit.remove("userId");
        userEdit.remove("cookie");
        userEdit.remove("school");
        userEdit.putBoolean("hasLogined", false);
        userEdit.apply();
    }
}