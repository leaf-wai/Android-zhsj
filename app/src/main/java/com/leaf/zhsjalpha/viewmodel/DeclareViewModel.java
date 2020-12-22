package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclareViewModel extends AndroidViewModel {

    public MutableLiveData<List<String>> items;
    public MutableLiveData<List<String>> classItem;
    public MutableLiveData<Integer> loadingStatus;
    public List<CurrencyTypeData> currencyTypeList = new ArrayList<>();
    public MutableLiveData<String> classId;
    public MutableLiveData<Integer> score;
    public MutableLiveData<Integer> subcurrencyId;
    public MutableLiveData<Integer> currencyId;

    private List<String> typeItems = new ArrayList<>();
    private List<String> classItemList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();

    public DeclareViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<String>> getClassItem() {
        if (classItem == null) {
            classItem = new MutableLiveData<>();
            classItem.setValue(classItemList);
        }
        return classItem;
    }

    public MutableLiveData<Integer> getScore() {
        if (score == null) {
            score = new MutableLiveData<>();
            score.setValue(0);
        }
        return score;
    }

    public MutableLiveData<Integer> getSubcurrencyId() {
        if (subcurrencyId == null) {
            subcurrencyId = new MutableLiveData<>();
            subcurrencyId.setValue(0);
        }
        return subcurrencyId;
    }

    public MutableLiveData<Integer> getCurrencyId() {
        if (currencyId == null) {
            currencyId = new MutableLiveData<>();
            currencyId.setValue(0);
        }
        return currencyId;
    }

    public MutableLiveData<List<String>> getItems() {
        if (items == null) {
            items = new MutableLiveData<>();
            items.setValue(typeItems);
        }
        return items;
    }

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
        }
        return classId;
    }

    public void getCurrencyType() {
        RetrofitHelper.getInstance().getCurrencyTypeCall().enqueue(new Callback<Result<DataList<CurrencyTypeData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<CurrencyTypeData>>> call, Response<Result<DataList<CurrencyTypeData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<CurrencyTypeData>> result = response.body();
                    if (result.getCode() == 200) {
                        currencyTypeList.addAll(result.getData().getData());
                        for (int i = 0; i < currencyTypeList.size(); i++) {
                            typeItems.add(currencyTypeList.get(i).getSubcurrencyName());
                        }
                        items.setValue(typeItems);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<CurrencyTypeData>>> call, Throwable t) {
                ToastUtils.showToast("加载素养类别失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void postDeclare(String content, Callback<User> callback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("classId", getClassId().getValue());
        hashMap.put("content", content);
        hashMap.put("currencyId", getCurrencyId().getValue());
        hashMap.put("score", getScore().getValue());
        hashMap.put("subcurrencyId", getSubcurrencyId().getValue());
        hashMap.put("yn", 1);
        hashMap.put("contentId", -1);

        RetrofitHelper.getInstance().postDeclareCall(getClassId().getValue(), getRequestBody(hashMap)).enqueue(callback);
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
                        classItem.postValue(classItemList);
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

    private RequestBody getRequestBody(HashMap<String, Object> hashMap) {
        Gson gson = new Gson();
        String str = gson.toJson(hashMap);
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), str);
    }
}
