package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.UserDetail;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.NumberUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInfoViewModel extends AndroidViewModel {

    public MutableLiveData<String> studentName;
    public MutableLiveData<String> sex;
    public MutableLiveData<String> birthday;
    public MutableLiveData<Integer> studentCard;
    public MutableLiveData<String> grade;
    public MutableLiveData<String> picUrl;

    public MyInfoViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getStudentName() {
        if (studentName == null) {
            studentName = new MutableLiveData<>();
            studentName.setValue("");
        }
        return studentName;
    }

    public MutableLiveData<String> getSex() {
        if (sex == null) {
            sex = new MutableLiveData<>();
            sex.setValue("");
        }
        return sex;
    }

    public MutableLiveData<String> getBirthday() {
        if (birthday == null) {
            birthday = new MutableLiveData<>();
            birthday.setValue("");
        }
        return birthday;
    }

    public MutableLiveData<Integer> getStudentCard() {
        if (studentCard == null) {
            studentCard = new MutableLiveData<>();
            studentCard.setValue(0);
        }
        return studentCard;
    }

    public MutableLiveData<String> getGrade() {
        if (grade == null) {
            grade = new MutableLiveData<>();
            grade.setValue("");
        }
        return grade;
    }

    public MutableLiveData<String> getPicUrl() {
        if (picUrl == null) {
            picUrl = new MutableLiveData<>();
            picUrl.setValue("");
        }
        return picUrl;
    }

    public void setUserDetail() {
        RetrofitHelper.getInstance().getUserDetailCall().enqueue(new Callback<Result<UserDetail>>() {
            @Override
            public void onResponse(Call<Result<UserDetail>> call, Response<Result<UserDetail>> response) {
                if (response.isSuccessful()) {
                    Result<UserDetail> result = response.body();
                    if (result.getCode() == 200) {
                        studentName.setValue(result.getData().getName());
                        sex.setValue(result.getData().getSex());
                        birthday.setValue(result.getData().getBirthday());
                        studentCard.setValue(result.getData().getStudentCard());
                        grade.setValue(NumberUtils.getGradeName(result.getData().getGradeId(), getApplication()));
                        picUrl.setValue(result.getData().getPicURL());
                    }
                } else {
                    ToastUtils.showToast("网络请求出错！", Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onFailure(Call<Result<UserDetail>> call, Throwable t) {
                ToastUtils.showToast("网络错误: " + t.getMessage(), Toast.LENGTH_SHORT);
                Log.d("aaa", t.getMessage());
            }
        });
    }

}
