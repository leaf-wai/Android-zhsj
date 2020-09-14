package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.model.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPwdViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> resetStatus;

    public ForgetPwdViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getResetStatus() {
        if (resetStatus == null) {
            resetStatus = new MutableLiveData<>();
            resetStatus.setValue(0);
        }
        return resetStatus;
    }

    public void resetPwd(String studentName, String idcard, String newpassword) {
        LoginModel.getInstance().getForgetPwdCall(studentName, idcard, newpassword).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user.code == 200) {
                        resetStatus.setValue(200);
                    } else if (user.code == 202) {
                        resetStatus.setValue(202);
                    } else {
                        resetStatus.setValue(404);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("aaa", "网络错误: " + t.getMessage());
                resetStatus.setValue(404);
            }
        });
    }
}
