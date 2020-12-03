package com.leaf.zhsjalpha.ui.submit;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.MyProduct;
import com.leaf.zhsjalpha.entity.ProductData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.TimeUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitListViewModel extends AndroidViewModel {
    public MutableLiveData<List<MyProduct>> myProducts;
    public MutableLiveData<Integer> loadingStatus;

    public List<MyProduct> myProductList = new ArrayList<>();
    public List<ProductData> productDataList = new ArrayList<>();

    public MutableLiveData<List<MyProduct>> getMyProducts() {
        if (myProducts == null) {
            myProducts = new MutableLiveData<>();
        }
        return myProducts;
    }

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public SubmitListViewModel(@NonNull Application application) {
        super(application);
    }

    public void getMyProduct() {
        RetrofitHelper.getInstance().getMyProductCall(null).enqueue(new Callback<Result<DataList<ProductData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<ProductData>>> call, Response<Result<DataList<ProductData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<ProductData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.postValue(200);
                        myProductList.clear();
                        productDataList = result.getData().getData();
                        for (ProductData productData : productDataList) {
                            MyProduct myProduct = new MyProduct();
                            myProduct.setPostTitle(productData.getPostTitle());
                            myProduct.setPostContent(productData.getPostContent());
                            myProduct.setPostImageUrl(productData.getFileUrl());
                            myProduct.setPostId(productData.getPostId());
                            myProduct.setPostBuildTime(TimeUtils.calTime(productData.getBuildTime()));
                            myProduct.setCommentNumber(productData.getReplyPostNumbers());
                            myProduct.setThumbUpNumber(productData.getThumbUpNumbers());
                            myProductList.add(myProduct);
                        }
                        myProducts.postValue(myProductList);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<ProductData>>> call, Throwable t) {
                ToastUtils.showToast("加载作品列表失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
