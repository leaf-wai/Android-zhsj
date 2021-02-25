package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.model.LoginModel;
import com.leaf.zhsjalpha.utils.MD5Utils;

import retrofit2.Callback;

public class PasswordViewModel extends AndroidViewModel {

    public PasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void resetPwd(String studentName, String idcard, String newpassword, Callback<User> callback) {
        LoginModel.getInstance().getForgetPwdCall(studentName, idcard, newpassword).enqueue(callback);
    }

    public void modifyPwd(String oldPwd, String newPwd, Callback<User> callback) {
        String oldPassword = MD5Utils.md5(oldPwd);
        String newPassword = MD5Utils.md5(newPwd);
        RetrofitHelper.getInstance().modifyPwdCall(oldPassword, newPassword).enqueue(callback);
    }
}
