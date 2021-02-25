package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Declare;
import com.leaf.zhsjalpha.entity.DeclareData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.JsonUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclareListViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<List<Declare>> declares;
    public MutableLiveData<Integer> week;
    public List<Declare> declareList = new ArrayList<>();
    public List<DeclareData> declareDataList = new ArrayList<>();
    public List<CurrencyTypeData> currencyTypeDataList = new ArrayList<>();

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<List<Declare>> getDeclares() {
        if (declares == null) {
            declares = new MutableLiveData<>();
        }
        return declares;
    }

    public MutableLiveData<Integer> getWeek() {
        return week;
    }

    public DeclareListViewModel(@NonNull Application application) {
        super(application);
        week = new MutableLiveData<>();
        new Thread(() -> week.postValue(getCurrentWeek())).start();

        new Handler().postDelayed(() -> getMyDeclare(getWeek().getValue()), 1000);
    }

    public void getMyDeclare(Integer week) {
        RetrofitHelper.getInstance().getMyDeclareCall(week).enqueue(new Callback<Result<DataList<DeclareData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<DeclareData>>> call, @NotNull Response<Result<DataList<DeclareData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<DeclareData>> result = response.body();
                    if (result.getCode() == 200) {
                        getLoadingStatus().setValue(200);
                        declareDataList.clear();
                        declareList.clear();
                        declareDataList = result.getData().getData();
                        for (DeclareData declareData : declareDataList) {
                            Declare declare = new Declare();
                            declare.setContent(declareData.getContent());
                            declare.setSubcurrencyName(getSubcurrencyName(declareData.getSubcurrencyId()));
                            declare.setSubmitTime(declareData.getSubmitTime());
                            declare.setScore(declareData.getScore());
                            declareList.add(declare);
                        }
                        getDeclares().postValue(declareList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<DeclareData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "加载申报历史失败");
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
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

    private String getSubcurrencyName(Integer subcurrencyId) {
        String JsonData = JsonUtils.getJson(getApplication(), "CurrencyType.json");
        String subcurrencyName = null;
        currencyTypeDataList = JsonUtils.parseCurrencyTypeData(JsonData);
        for (CurrencyTypeData currencyTypeData : currencyTypeDataList) {
            if (currencyTypeData.getSubcurrencyId().equals(subcurrencyId))
                subcurrencyName = currencyTypeData.getSubcurrencyName();
        }
        return subcurrencyName == null ? "未知分类" : subcurrencyName;
    }
}
