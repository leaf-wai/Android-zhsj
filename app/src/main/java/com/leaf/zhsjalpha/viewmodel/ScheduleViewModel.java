package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.ScheduleData;
import com.leaf.zhsjalpha.entity.WeekInfo;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;

import java.io.IOException;

import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> week = new MutableLiveData<>();

    public MutableLiveData<Integer> getWeek() {
        return week;
    }

    public ScheduleViewModel(@NonNull Application application) {
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

    public void getSchedule(Integer week, Callback<Result<ScheduleData>> callback) {
        RetrofitHelper.getInstance().getScheduleCall(week).enqueue(callback);
    }

    public void getWeekInfo(Callback<Result<DataList<WeekInfo>>> callback) {
        RetrofitHelper.getInstance().getWeekInfoCall().enqueue(callback);
    }
}
