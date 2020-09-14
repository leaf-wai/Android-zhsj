package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclareViewModel extends AndroidViewModel {

    public MutableLiveData<List<String>> items;
    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<Integer> declareStatus;
    public List<CurrencyTypeData> currencyTypeList = new ArrayList<>();
    public MutableLiveData<Integer> score;
    public MutableLiveData<Integer> subcurrencyId;
    public MutableLiveData<Integer> currencyId;
    private List<String> typeItems = new ArrayList<>();

    public DeclareViewModel(@NonNull Application application) {
        super(application);
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

    public MutableLiveData<Integer> getDeclareStatus() {
        if (declareStatus == null) {
            declareStatus = new MutableLiveData<>();
            declareStatus.setValue(0);
        }
        return declareStatus;
    }

    public void getCurrencyType() {
        RetrofitHelper.getInstance().getCurrencyTypeCall().enqueue(new Callback<Result<DataList<CurrencyTypeData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<CurrencyTypeData>>> call, Response<Result<DataList<CurrencyTypeData>>> response) {
                if (response.isSuccessful()) {
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

    public void postDeclare(String classId, String content, String contentId, int currencyId, int score, int subcurrencyId, int yn) {
        RetrofitHelper.getInstance().postDeclareCall(classId, content, contentId, currencyId, score, subcurrencyId, yn).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("aaa", "onResponse: " + response.body().string());
                        declareStatus.setValue(200);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("aaa", "onFailure: " + t.getMessage());
                declareStatus.setValue(404);
            }
        });
    }
}
