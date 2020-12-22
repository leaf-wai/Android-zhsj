package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.entity.ApplyFriend;
import com.leaf.zhsjalpha.entity.ApplyFriendData;
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

public class NewFriendViewModel extends AndroidViewModel {

    private MutableLiveData<List<ApplyFriend>> applyFriends;

    public List<ApplyFriend> applyFriendList = new ArrayList<>();
    public List<ApplyFriendData> applyFriendDataList = new ArrayList<>();

    public MutableLiveData<List<ApplyFriend>> getApplyFriends() {
        if (applyFriends == null) {
            applyFriends = new MutableLiveData<>();
        }
        return applyFriends;
    }

    public NewFriendViewModel(@NonNull Application application) {
        super(application);
        getApplyFriendList();
    }

    public void getApplyFriendList() {
        RetrofitHelper.getInstance().getFriendApplicationCall().enqueue(new Callback<Result<DataList<ApplyFriendData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<ApplyFriendData>>> call, @NotNull Response<Result<DataList<ApplyFriendData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<ApplyFriendData>> result = response.body();
                    if (result.getCode() == 200) {
                        applyFriendList.clear();
                        applyFriendDataList = result.getData().getData();
                        for (ApplyFriendData applyFriendData : applyFriendDataList) {
                            if (applyFriendData.getIsCheck() == 0) {
                                ApplyFriend applyFriend = new ApplyFriend();
                                applyFriend.setName(applyFriendData.getApplyName());
                                applyFriend.setSex(applyFriendData.getSex());
                                applyFriend.setApplyContent(applyFriendData.getApplyContent());
                                applyFriend.setPicUrl(applyFriendData.getPicURL());
                                applyFriend.setId(applyFriendData.getId());
                                applyFriendList.add(applyFriend);
                            } else {
                                applyFriendList.remove(applyFriendData);
                            }
                        }
                        applyFriends.postValue(applyFriendList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<ApplyFriendData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("获取好友申请列表失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }
}
