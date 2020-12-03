package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;

import retrofit2.Callback;

public class CourseDetailViewModel extends AndroidViewModel {
    public CourseDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void getCourseInfo(String classId, Callback<Result<CourseData>> callback) {
        RetrofitHelper.getInstance().getCourseInfoCall(classId).enqueue(callback);
    }

}
