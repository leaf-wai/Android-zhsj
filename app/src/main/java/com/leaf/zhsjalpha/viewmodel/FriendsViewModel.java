package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Friend;
import com.leaf.zhsjalpha.entity.FriendData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.StudentData;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<String> classId;
    public MutableLiveData<String> className;
    public MutableLiveData<List<String>> classItem;
    public MutableLiveData<List<Friend>> friend;
    public MutableLiveData<List<Friend>> allStudents;

    private List<String> classItemList = new ArrayList<>();
    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> studentList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();
    public List<FriendData> friendDataList = new ArrayList<>();
    public List<StudentData> studentDataList = new ArrayList<>();

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<String> getClassId() {
        if (classId == null) {
            classId = new MutableLiveData<>();
            classId.setValue("");
        }
        return classId;
    }

    public MutableLiveData<String> getClassName() {
        if (className == null) {
            className = new MutableLiveData<>();
            className.setValue("暂无班级");
        }
        return className;
    }

    public MutableLiveData<List<String>> getClassItem() {
        if (classItem == null) {
            classItem = new MutableLiveData<>();
            classItem.setValue(classItemList);
        }
        return classItem;
    }

    public MutableLiveData<List<Friend>> getFriend() {
        if (friend == null) {
            friend = new MutableLiveData<>();
        }
        return friend;
    }

    public MutableLiveData<List<Friend>> getAllStudents() {
        if (allStudents == null) {
            allStudents = new MutableLiveData<>();
        }
        return allStudents;
    }

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        getStudentClass();
    }

    public void getStudentClass() {
        RetrofitHelper.getInstance().getStudentClassCall().enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<CourseData>> result = response.body();
                    if (result.getData().getData().size() != 0) {
                        courseDataList = result.getData().getData();
                        for (CourseData courseData : courseDataList) {
                            classItemList.add(courseData.getClassName());
                        }
                        getClassItem().postValue(classItemList);
                        getClassId().postValue(courseDataList.get(0).getClassId());
                        getClassName().postValue(classItemList.get(0));
                    } else {
                        ToastUtils.showToast(getApplication().getApplicationContext(), "你还没有班级！");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "获取班级信息失败");
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getFriendList() {
        RetrofitHelper.getInstance().getFriendListCall(getClassId().getValue()).enqueue(new Callback<Result<DataList<FriendData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<FriendData>>> call, @NotNull Response<Result<DataList<FriendData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<FriendData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.setValue(200);
                        friendList.clear();
                        friendDataList = result.getData().getData();
                        for (FriendData friendData : friendDataList) {
                            Friend friend = new Friend();
                            friend.setName(friendData.getStudentName());
                            friend.setSex(friendData.getSex());
                            friend.setPicUrl(friendData.getPicURL());
                            friendList.add(friend);
                        }
                        friend.postValue(friendList);
                    }
                } else
                    loadingStatus.setValue(404);
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<FriendData>>> call, @NotNull Throwable t) {
                loadingStatus.setValue(404);
                ToastUtils.showToast(getApplication().getApplicationContext(), "获取好友列表失败");
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void addFriend(String studentId, String applyContent) {
        RetrofitHelper.getInstance().addFriendCall(studentId, applyContent).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        ToastUtils.showToast(getApplication().getApplicationContext(), response.body().getDetail());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "申请添加好友失败，请稍后重试！");
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void deleteFriend(String studentId, Callback<User> callback) {
        RetrofitHelper.getInstance().deleteFriendCall(studentId).enqueue(callback);
    }

    public void getAllStudent() {
        RetrofitHelper.getInstance().getAllStudentListCall(getClassId().getValue(), null).enqueue(new Callback<Result<DataList<StudentData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<StudentData>>> call, @NotNull Response<Result<DataList<StudentData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<StudentData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.setValue(200);
                        studentList.clear();
                        studentDataList = result.getData().getData();
                        for (StudentData studentData : studentDataList) {
                            Friend friend = new Friend();
                            friend.setName(studentData.getName());
                            friend.setSex(studentData.getSex());
                            friend.setPicUrl(studentData.getPicURL());
                            studentList.add(friend);
                        }
                        allStudents.postValue(studentList);
                    }
                } else
                    loadingStatus.setValue(404);
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<StudentData>>> call, @NotNull Throwable t) {
                loadingStatus.setValue(404);
                ToastUtils.showToast(getApplication().getApplicationContext(), "获取学生列表失败");
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
