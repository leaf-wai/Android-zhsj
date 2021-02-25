package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.User;

import retrofit2.Callback;

public class PostDetailViewModel extends AndroidViewModel {

    public PostDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void thumbUp(String classId, String postId, Callback<User> callback) {
        RetrofitHelper.getInstance().thumbUpCall(classId, postId).enqueue(callback);
    }
}
