package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.MessageData;
import com.leaf.zhsjalpha.entity.Notify;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.TeacherNoticeData;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.TimeUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<Integer> week = new MutableLiveData<>();
    public MutableLiveData<List<Notify>> notifies;
    public MutableLiveData<List<Notify>> teacherNotices;
    public List<Notify> notifyList = new ArrayList<>();
    public List<Notify> teacherNoticeList = new ArrayList<>();
    public List<MessageData> messageDataList = new ArrayList<>();
    public List<TeacherNoticeData> teacherNoticeDataList = new ArrayList<>();

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<Integer> getWeek() {
        return week;
    }

    public MutableLiveData<List<Notify>> getNotifies() {
        if (notifies == null) {
            notifies = new MutableLiveData<>();
        }
        return notifies;
    }

    public MutableLiveData<List<Notify>> getTeacherNotices() {
        if (teacherNotices == null) {
            teacherNotices = new MutableLiveData<>();
        }
        return teacherNotices;
    }

    public NotifyViewModel(@NonNull Application application) {
        super(application);
        new Thread(() -> week.postValue(getCurrentWeek())).start();
    }

    public Integer getCurrentWeek() {
        Integer week;
        try {
            Response<Result<Integer>> response = RetrofitHelper.getInstance().getCurrentWeekCall().execute();
            Result<Integer> result = response.body();
            week = result.getData();
        } catch (IOException e) {
            e.printStackTrace();
            week = 1;
        }
        return week;
    }

    public void getMessages(Integer type) {
        RetrofitHelper.getInstance().getMessagesCall(1).enqueue(new Callback<Result<DataList<MessageData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<MessageData>>> call, @NotNull Response<Result<DataList<MessageData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<MessageData>> result = response.body();
                    if (result.getCode() == 200) {
                        getLoadingStatus().setValue(200);
                        notifyList.clear();
                        messageDataList.clear();
                        for (MessageData messageData : result.getData().getData()) {
                            if (messageData.getType() == type)
                                messageDataList.add(messageData);
                        }
                        for (MessageData messageData : messageDataList) {
                            Notify notify = new Notify();
                            notify.setClassName(messageData.getClassName());
                            notify.setContent(messageData.getContent());
                            notify.setTime(TimeUtils.calTime(messageData.getCreateTime()));
                            notifyList.add(notify);
                        }
                        getNotifies().postValue(notifyList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<MessageData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载消息失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getTeacherNotice(Integer week) {
        SharedPreferences userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = userRead.getString("userId", null);

        RetrofitHelper.getInstance().getTeacherNoticeCall(userId, week).enqueue(new Callback<Result<DataList<TeacherNoticeData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<TeacherNoticeData>>> call, Response<Result<DataList<TeacherNoticeData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<TeacherNoticeData>> result = response.body();
                    if (result.getCode() == 200) {
                        getLoadingStatus().setValue(200);
                        teacherNoticeList.clear();
                        teacherNoticeDataList = result.getData().getData();
                        for (TeacherNoticeData teacherNoticeData : teacherNoticeDataList) {
                            Notify notify = new Notify();
                            notify.setClassName(teacherNoticeData.getClassName());
                            notify.setTeacherName(teacherNoticeData.getTeacherName());
                            notify.setContent(teacherNoticeData.getNoticeContent());
                            notify.setTime(TimeUtils.calTime(teacherNoticeData.getNoticeTime()));
                            notify.setResourceURL(teacherNoticeData.getResourceURL());
                            teacherNoticeList.add(notify);
                        }
                        getTeacherNotices().postValue(teacherNoticeList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<TeacherNoticeData>>> call, Throwable t) {
                ToastUtils.showToast("加载消息失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void read(Integer messageId) {
        RetrofitHelper.getInstance().readCall(messageId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful())
                    Log.d("aaa", "onResponse: " + response.body().getDetail());
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
