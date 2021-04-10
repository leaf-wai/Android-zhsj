package com.leaf.zhsjalpha.ui.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.entity.Activity;
import com.leaf.zhsjalpha.entity.ActivityData;
import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.MessageData;
import com.leaf.zhsjalpha.entity.MyCourse;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.JsonUtils;
import com.leaf.zhsjalpha.utils.NumberUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private static final int SHOW_SIZE = 10;

    public MutableLiveData<Boolean> Login;
    public MutableLiveData<List<MyCourse>> myCourses;
    public MutableLiveData<List<Course>> courses;
    public MutableLiveData<List<Activity>> activities;
    public List<Activity> activityList = new ArrayList<>();
    public List<Course> courseList = new ArrayList<>();
    public List<MyCourse> myCourseList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();

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
            public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "加载我的课程失败");
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUserInfo() {
        RetrofitHelper.getInstance().getUserInfoCall().enqueue(new Callback<Result<UserInfo>>() {
            @Override
            public void onResponse(@NotNull Call<Result<UserInfo>> call, @NotNull Response<Result<UserInfo>> response) {
                if (response.body() == null) {
                    ToastUtils.showToast(getApplication().getApplicationContext(), "登录状态已过期，请重新登录");
                    Logout();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<UserInfo>> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "网络错误: " + t.getMessage());
                new Handler().postDelayed(() -> ToastUtils.showToast(getApplication().getApplicationContext(), "登录状态已过期，请重新登录"), 2000);
                Logout();
                Log.d("aaa", t.getMessage());
            }
        });
    }

    public void getCourseDataList() {
        if (getLogin().getValue()) {
            RetrofitHelper.getInstance().initCourseListCall(null, null, null, null, null, null).enqueue(new Callback<Result<DataList<CourseData>>>() {
                @Override
                public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<DataList<CourseData>> result = response.body();
                        if (result.getCode() == 200 && result.getData().getData().size() != 0) {
                            courseList.clear();
                            for (int i = 0; i < SHOW_SIZE; i++) {
                                courseList.add(getCourse(result.getData().getData(), i));
                            }
                            courses.setValue(courseList);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Throwable t) {
                    ToastUtils.showToast(getApplication().getApplicationContext(), "加载课程列表失败");
                    Log.d("aaa", "onFailure: " + t.getMessage());
                }
            });
        } else {
            String JsonData = JsonUtils.getJson(getApplication(), "CourseList.json");
            courseList.clear();
            for (int i = 0; i < SHOW_SIZE; i++) {
                courseList.add(getCourse(JsonUtils.parseCourseData(JsonData), i));
            }
            courses.setValue(courseList);
        }
    }

    private Course getCourse(List<CourseData> list, int index) {
        Course course = new Course();
        course.setClassId(list.get(index).getClassId());
        course.setCourseName(list.get(index).getClassName());
        course.setCourseImgUrl(list.get(index).getCourseImgUrl());
        course.setPrice(list.get(index).getCoursePrice());
        course.setRemain(list.get(index).getRemain());
        course.setPayEndTime(list.get(index).getPayEndTime());
        course.setCourseType(NumberUtils.getCourseTypeName(list.get(index).getCourseType()));
        course.setInterestType(NumberUtils.getInterestTypeName(list.get(index).getInterestType()));
        return course;
    }

    public void getActivityList() {
        if (getLogin().getValue()) {
            RetrofitHelper.getInstance().getActivityListCall(0, SHOW_SIZE, null).enqueue(new Callback<Result<DataList<ActivityData>>>() {
                @Override
                public void onResponse(@NotNull Call<Result<DataList<ActivityData>>> call, @NotNull Response<Result<DataList<ActivityData>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Result<DataList<ActivityData>> result = response.body();
                        if (result.getCode() == 200 && result.getData().getData().size() != 0) {
                            activityList.clear();
                            if (result.getData().getCount() < SHOW_SIZE) {
                                for (int i = 0; i < result.getData().getCount(); i++) {
                                    activityList.add(getActivity(result.getData().getData(), i));
                                }
                            } else {
                                for (int i = 0; i < SHOW_SIZE; i++) {
                                    activityList.add(getActivity(result.getData().getData(), i));
                                }
                            }
                            activities.setValue(activityList);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Result<DataList<ActivityData>>> call, @NotNull Throwable t) {
                    ToastUtils.showToast(getApplication().getApplicationContext(), "加载活动列表失败");
                    Log.d("aaa", "onFailure: " + t.getMessage());
                }
            });
        } else {
            String JsonData = JsonUtils.getJson(getApplication(), "ActivityList.json");
            activityList.clear();
            for (int i = 0; i < SHOW_SIZE; i++) {
                activityList.add(getActivity(JsonUtils.parseActivityData(JsonData), i));
            }
            activities.setValue(activityList);
        }
    }

    private Activity getActivity(List<ActivityData> list, int index) {
        Activity activity = new Activity();
        activity.setActivityName(list.get(index).getActivityName());
        activity.setActivityDescription(list.get(index).getActivityDescription());
        activity.setActivityAddress(list.get(index).getActivityAddress());
        activity.setActivityId(list.get(index).getActivityId());
        activity.setImageUrl(list.get(index).getImageUrl());
        activity.setActivityStartTime(list.get(index).getActivityStartTime());
        activity.setActivityEndTime(list.get(index).getActivityEndTime());
        activity.setMaxCount(list.get(index).getMaxCount());
        return activity;
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