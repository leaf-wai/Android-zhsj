package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.FriendData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.FileUtils;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.JsonUtils.getRequestBody;

public class EvaluateViewModel extends AndroidViewModel {

    public MutableLiveData<List<String>> items;
    public MutableLiveData<List<String>> friendItems;
    public MutableLiveData<Integer> subcurrencyId;
    public MutableLiveData<Integer> currencyId;
    public MutableLiveData<Integer> score;
    public List<CurrencyTypeData> currencyTypeList = new ArrayList<>();
    private List<String> friendItemList = new ArrayList<>();
    private List<String> typeItems = new ArrayList<>();
    public List<FriendData> friendDataList = new ArrayList<>();

    public MutableLiveData<List<String>> getItems() {
        if (items == null) {
            items = new MutableLiveData<>();
            items.setValue(typeItems);
        }
        return items;
    }

    public MutableLiveData<List<String>> getFriendItems() {
        if (friendItems == null) {
            friendItems = new MutableLiveData<>();
            friendItems.setValue(friendItemList);
        }
        return friendItems;
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

    public MutableLiveData<Integer> getScore() {
        if (score == null) {
            score = new MutableLiveData<>();
            score.setValue(0);
        }
        return score;
    }

    public EvaluateViewModel(@NonNull Application application) {
        super(application);
        getFriendList();
    }

    public void getFriendList() {
        RetrofitHelper.getInstance().getFriendListCall(null).enqueue(new Callback<Result<DataList<FriendData>>>() {
            @Override
            public void onResponse(Call<Result<DataList<FriendData>>> call, Response<Result<DataList<FriendData>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<FriendData>> result = response.body();
                    if (result.getCode() == 200) {
                        if (result.getData().getData().size() != 0) {
                            friendDataList = result.getData().getData();
                            for (FriendData friendData : friendDataList) {
                                friendItemList.add(friendData.getStudentName());
                            }
                            getFriendItems().postValue(friendItemList);
                        } else {
                            ToastUtils.showToast("加载同伴列表失败", Toast.LENGTH_SHORT);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<FriendData>>> call, Throwable t) {
                ToastUtils.showToast("加载同伴列表失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
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
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void quickEvaluate(String updateList, Integer target, Callback<User> callback) {
        RetrofitHelper.getInstance().quickEvaluateCall(updateList, target).enqueue(callback);
    }

    public void customEvaluate(Uri uri, String content, Integer target, Callback<User> callback) {
        FileUtils fileUtils = new FileUtils(MyApplication.getContext());
        String imgPath = fileUtils.getFilePathByUri(uri);
        File file = new File(imgPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", content)
                .addFormDataPart("currencyId", String.valueOf(getCurrencyId().getValue()))
                .addFormDataPart("subcurrencyId", String.valueOf(getSubcurrencyId().getValue()))
                .addFormDataPart("evaluationScore", String.valueOf(getScore().getValue()))
                .addFormDataPart("target", String.valueOf(target))
                .addFormDataPart("postFile", file.getName(), requestBody);

        RetrofitHelper.getInstance().customEvaluateCall(builder.build()).enqueue(callback);
    }

    public void customFriendEvaluate(String friendList, String content, Callback<User> callback) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("content", content);
        hashMap.put("currencyId", getCurrencyId().getValue());
        hashMap.put("evaluationScore", getScore().getValue());
        hashMap.put("subcurrencyId", getSubcurrencyId().getValue());

        RetrofitHelper.getInstance().customFriendEvaluateCall(friendList, getRequestBody(hashMap)).enqueue(callback);
    }

    public void quickFriendEvaluate(String updateList, String friendList, Callback<User> callback) {
        RetrofitHelper.getInstance().quickFriendEvaluateCall(updateList, friendList).enqueue(callback);
    }

    public void getTemplate(Integer target, Callback<Result<DataList<CurrencyTypeData>>> callback) {
        RetrofitHelper.getInstance().getTemplate(target).enqueue(callback);
    }
}
