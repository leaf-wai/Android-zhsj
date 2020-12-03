package com.leaf.zhsjalpha.ui.submit;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.FileUtils;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> Login;
    public MutableLiveData<List<String>> classItem = new MutableLiveData<>();
    public MutableLiveData<String> classId;

    private List<String> classItemList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();

    public String postType;

    public MutableLiveData<String> getClassId() {
        if (classId == null) {
            classId = new MutableLiveData<>();
        }
        return classId;
    }

    public MutableLiveData<List<String>> getClassItem() {
        return classItem;
    }

    public MutableLiveData<Boolean> getLogin() {
        if (Login == null) {
            Login = new MutableLiveData<>();
            Login.setValue(false);
        }
        return Login;
    }

    public SubmitViewModel(@NonNull Application application) {
        super(application);
        getStudentClass();
    }

    public void getStudentClass() {
        RetrofitHelper.getInstance().getStudentClassCall().enqueue(new Callback<Result<DataList<CourseData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<CourseData>> result = response.body();
                    classItemList.clear();
                    if (result.getData().getData().size() != 0) {
                        courseDataList = result.getData().getData();
                        for (CourseData courseData : courseDataList) {
                            classItemList.add(courseData.getClassName());
                        }
                        classItem.postValue(classItemList);
                    } else {
                        ToastUtils.showToast("你还没有班级！", Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<DataList<CourseData>>> call, Throwable t) {
                ToastUtils.showToast("获取班级信息失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void postProduct(Uri uri, String postTitle, String postContent, Callback<User> callback) {
        FileUtils fileUtils = new FileUtils(MyApplication.getContext());
        String imgPath = fileUtils.getFilePathByUri(uri);
        File file = new File(imgPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("classId", getClassId().getValue())
                .addFormDataPart("postTitle", postTitle)
                .addFormDataPart("postContent", postContent)
                .addFormDataPart("postType", postType)
                .addFormDataPart("topicId", "undefined")
                .addFormDataPart("postFile", file.getName(), requestBody);

        RetrofitHelper.getInstance().postProductCall(builder.build()).enqueue(callback);
    }
}