package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.entity.ActivityInfoList;
import com.leaf.zhsjalpha.entity.Result;

import retrofit2.Callback;

public class ActivityDetailViewModel extends AndroidViewModel {

    public ActivityDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void getActivityInfo(String activityId, Callback<Result<ActivityInfoList>> callback) {
        RetrofitHelper.getInstance().getActivityInfoCall(activityId).enqueue(callback);
    }
}
