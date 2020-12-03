package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.MyOrder;
import com.leaf.zhsjalpha.entity.OrderData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderViewModel extends AndroidViewModel {

    public MutableLiveData<List<MyOrder>> myOrders;
    public MutableLiveData<Integer> loadingStatus;
    public List<MyOrder> myOrderList = new ArrayList<>();
    public List<OrderData> orderDataList = new ArrayList<>();

    public MutableLiveData<List<MyOrder>> getMyOrders() {
        if (myOrders == null) {
            myOrders = new MutableLiveData<>();
        }
        return myOrders;
    }

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MyOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void getMyOrder(int orderStatus) {
        RetrofitHelper.getInstance().getMyOrderCall(null).enqueue(new Callback<Result<DataList<OrderData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<OrderData>>> call, @NotNull Response<Result<DataList<OrderData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<OrderData>> result = response.body();
                    if (result.getCode() == 200) {
                        loadingStatus.setValue(200);
                        myOrderList.clear();
                        orderDataList.clear();
                        orderDataList = result.getData().getData();
                        for (OrderData orderData : orderDataList) {
                            if (orderStatus == -1) {
                                MyOrder myOrder = new MyOrder();
                                myOrder.setOrderId(orderData.getOrderId());
                                myOrder.setClassId(orderData.getClassId());
                                myOrder.setCourseName(orderData.getClassName());
                                myOrder.setOrderPrice(orderData.getTotalfee());
                                myOrder.setOrderDate(orderData.getDate());
                                myOrder.setCourseImgUrl(orderData.getResourceURL());
                                switch (orderData.getPayStatus()) {
                                    case 0:
                                        myOrder.setOrderStatus("未支付");
                                        break;
                                    case 1:
                                        myOrder.setOrderStatus("已确认");
                                        break;
                                    case 2:
                                        myOrder.setOrderStatus("已取消");
                                        break;
                                }
                                myOrderList.add(myOrder);
                            } else {
                                if (orderData.getPayStatus().equals(orderStatus)) {
                                    MyOrder myOrder = new MyOrder();
                                    myOrder.setOrderId(orderData.getOrderId());
                                    myOrder.setClassId(orderData.getClassId());
                                    myOrder.setCourseName(orderData.getClassName());
                                    myOrder.setOrderPrice(orderData.getTotalfee());
                                    myOrder.setOrderDate(orderData.getDate());
                                    myOrder.setCourseImgUrl(orderData.getResourceURL());
                                    switch (orderData.getPayStatus()) {
                                        case 0:
                                            myOrder.setOrderStatus("未支付");
                                            break;
                                        case 1:
                                            myOrder.setOrderStatus("已确认");
                                            break;
                                        case 2:
                                            myOrder.setOrderStatus("已取消");
                                            break;
                                    }
                                    myOrderList.add(myOrder);
                                }
                            }
                        }
                        myOrders.postValue(myOrderList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<OrderData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载订单列表失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

//    private String getCourseImageUrl(String classId) {
//        String courseImgUrl = "";
//        try {
//            Response<Result<CourseData>> response = RetrofitHelper.getInstance().getCourseInfoCall(classId).execute();
//            Result<CourseData> result = response.body();
//            courseImgUrl = result.getData().getCourseImgUrl();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return courseImgUrl;
//    }
}
