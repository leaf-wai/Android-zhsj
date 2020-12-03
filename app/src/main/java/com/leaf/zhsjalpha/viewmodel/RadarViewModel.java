package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Radar;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.Statistic;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RadarViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> week;
    public MutableLiveData<String> classId;
    public MutableLiveData<String> className;
    public MutableLiveData<RadarData> radarData = new MutableLiveData<>();
    public MutableLiveData<List<String>> radarLabel = new MutableLiveData<>();
    public MutableLiveData<List<String>> classItem;

    public List<Statistic> statisticList = new ArrayList<>();
    public List<RadarEntry> entries = new ArrayList<>();
    public List<String> label = new ArrayList<>();
    private List<String> classItemList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();

    public MutableLiveData<Integer> getWeek() {
        return week;
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

    public MutableLiveData<RadarData> getRadarData() {
        return radarData;
    }

    public MutableLiveData<List<String>> getRadarLabel() {
        return radarLabel;
    }

    public MutableLiveData<List<String>> getClassItem() {
        if (classItem == null) {
            classItem = new MutableLiveData<>();
            classItem.setValue(classItemList);
        }
        return classItem;
    }

    public RadarViewModel(@NonNull Application application) {
        super(application);
        week = new MutableLiveData<>();
        getStudentClass();
        new Thread(() -> {
            week.postValue(getCurrentWeek());
        }).start();
    }

    public Integer getCurrentWeek() {
        try {
            Response<Result<Integer>> response = RetrofitHelper.getInstance().getCurrentWeekCall().execute();
            Result<Integer> result = response.body();
            return result.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public void getRadarData(String classId, Integer week) {
        Log.d("aaa", "getRadarData: "+classId+week);
        RetrofitHelper.getInstance().getRadarDataCall(classId, week).enqueue(new Callback<Result<Radar>>() {
            @Override
            public void onResponse(@NotNull Call<Result<Radar>> call, @NotNull Response<Result<Radar>> response) {
                if (response.isSuccessful()) {
                    Result<Radar> result = response.body();
                    statisticList.clear();
                    entries.clear();
                    if (result.getCode() == 200) {
                        statisticList = result.getData().getStatisticsEntityList();
                        if (statisticList.size() != 0) {
                            for (Statistic statistic : statisticList) {
                                Log.d("aaa", "onResponse: " + statistic.getName() + statistic.getNum());
                                entries.add(new RadarEntry(statistic.getNum()));
                                label.add(statistic.getName());
                            }
                            RadarDataSet radarDataSet = new RadarDataSet(entries, getClassItem().getValue().get(0));
                            radarDataSet.setColor(getApplication().getResources().getColor(R.color.colorPrimary));
                            radarDataSet.setFillColor(getApplication().getResources().getColor(R.color.colorPrimary));
                            radarDataSet.setDrawFilled(true);
                            radarDataSet.setFillAlpha(180);
                            radarDataSet.setLineWidth(2f);
                            radarDataSet.setDrawHighlightCircleEnabled(true);
                            radarDataSet.setDrawHighlightIndicators(false);

                            RadarData data = new RadarData(radarDataSet);
                            data.setDrawValues(false);
                            radarLabel.postValue(label);
                            radarData.postValue(data);
                        } else {
                            radarData.postValue(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<Radar>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载雷达图数据失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getStudentClass() {
        RetrofitHelper.getInstance().getStudentClassCall().enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful()) {
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
                        ToastUtils.showToast("你还没有班级！", Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<CourseData>>> call, Throwable t) {
                ToastUtils.showToast("获取班级信息失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
