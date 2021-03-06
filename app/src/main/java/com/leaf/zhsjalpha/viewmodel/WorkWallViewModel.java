package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.ProductData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkWallViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> Login;
    public MutableLiveData<Integer> loadingStatus;
    public MutableLiveData<List<ProductData>> productData;

    public MutableLiveData<Boolean> getLogin() {
        if (Login == null) {
            Login = new MutableLiveData<>();
            Login.setValue(false);
        }
        return Login;
    }

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<List<ProductData>> getProductData() {
        if (productData == null) {
            productData = new MutableLiveData<>();
        }
        return productData;
    }

    public WorkWallViewModel(@NonNull Application application) {
        super(application);
    }

    public void getStudentClass(Callback<Result<DataList<CourseData>>> callback) {
        RetrofitHelper.getInstance().getStudentClassCall().enqueue(callback);
    }

    public void getAllProduct(String classID) {
        RetrofitHelper.getInstance().getAllProductCall(classID).enqueue(new Callback<Result<DataList<ProductData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<ProductData>>> call, @NotNull Response<Result<DataList<ProductData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<ProductData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.postValue(200);
                        productData.postValue(result.getData().getData());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<ProductData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast(getApplication().getApplicationContext(), "加载作品列表失败");
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
