package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.Activity;
import com.leaf.zhsjalpha.entity.ActivityData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListViewModel extends AndroidViewModel {

    private static final int pageSize = 10;
    private int pindex = 0;
    private int total = 0;

    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<List<Activity>> activities;
    public List<Activity> activityList = new ArrayList<>();
    public List<ActivityData> activityDataList = new ArrayList<>();

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<List<Activity>> getActivities() {
        if (activities == null) {
            activities = new MutableLiveData<>();
            activities.setValue(activityList);
        }
        return activities;
    }

    public ActivityListViewModel(@NonNull Application application) {
        super(application);
    }

    public void getActivityList(String keyword) {
        RetrofitHelper.getInstance().getActivityListCall(pindex, pageSize, keyword).enqueue(new Callback<Result<DataList<ActivityData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<ActivityData>>> call, @NotNull Response<Result<DataList<ActivityData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<ActivityData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.setValue(200);
                        total = result.getData().getTotalnum();
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
                            loadingStatus.setValue(667);
                            if (activityDataList.size() == pageSize)
                                pindex++;
                            else if (activityDataList.size() < pageSize)
                                loadingStatus.setValue(666);
                        } else if (pindex != 0 && result.getData().getData().size() == 0) {
                            loadingStatus.setValue(666);
                        } else if (pindex == 0 && result.getData().getData().size() == 0) {
                            loadingStatus.setValue(668);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<ActivityData>>> call, @NotNull Throwable t) {
                if (pindex == 0) {
                    ToastUtils.showToast("加载活动列表失败", Toast.LENGTH_SHORT);
                    loadingStatus.setValue(404);
                } else {
                    loadingStatus.setValue(405);
                }
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
