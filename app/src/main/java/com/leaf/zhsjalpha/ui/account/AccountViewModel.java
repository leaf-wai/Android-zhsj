package com.leaf.zhsjalpha.ui.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.MainActivity;
import com.leaf.zhsjalpha.api;
import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountViewModel extends AndroidViewModel {

    public MutableLiveData<String> grade;
    public MutableLiveData<Integer> integral;
    public MutableLiveData<Integer> post;
    public MutableLiveData<Integer> thumbup;
    public MutableLiveData<String> studentName;

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getGrade() {
        if (grade == null) {
            grade = new MutableLiveData<>();
            grade.setValue("未知年级");
        }
        return grade;
    }

    public MutableLiveData<Integer> getIntegral() {
        if (integral == null) {
            integral = new MutableLiveData<>();
            integral.setValue(0);
        }
        return integral;
    }

    public MutableLiveData<Integer> getPost() {
        if (post == null) {
            post = new MutableLiveData<>();
            post.setValue(0);
        }
        return post;
    }

    public MutableLiveData<Integer> getThumbup() {
        if (thumbup == null) {
            thumbup = new MutableLiveData<>();
            thumbup.setValue(0);
        }
        return thumbup;
    }

    public MutableLiveData<String> getStudentName() {
        if (studentName == null) {
            studentName = new MutableLiveData<>();
            studentName.setValue("未登录");
        }
        return studentName;
    }

    public void loadUserInfo() {
        SharedPreferences userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = new ArrayList<>();
                String token = userRead.getString("cookie", null);

                if (token != null) {
                    Cookie cookie = Cookie.parse(url, token);
                    cookies.add(cookie);
                }

                return cookies;
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://zhsj.bnuz.edu.cn/ComprehensiveSys/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api.userInfoApi api = retrofit.create(com.leaf.zhsjalpha.api.userInfoApi.class);
        Call<UserInfo> userInfoCall = api.userInfo();
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                if (userInfo.code == 200) {
                    switch (userInfo.getData().getGradeId()) {
                        case 1:
                            grade.setValue("一年级");
                            break;
                        case 2:
                            grade.setValue("二年级");
                            break;
                        case 3:
                            grade.setValue("三年级");
                            break;
                        case 4:
                            grade.setValue("四年级");
                            break;
                        case 5:
                            grade.setValue("五年级");
                            break;
                        case 6:
                            grade.setValue("六年级");
                            break;
                        case 7:
                            grade.setValue("七年级");
                            break;
                        case 8:
                            grade.setValue("八年级");
                            break;
                        case 9:
                            grade.setValue("九年级");
                            break;
                        default:
                            grade.setValue("未知年级");
                            break;
                    }
                    integral.setValue(userInfo.getData().getIntegral());
                    post.setValue(userInfo.getData().getPostNum());
                    thumbup.setValue(userInfo.getData().getThumbUpNum());
                    studentName.setValue(userInfo.getData().getStudentName());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MyApplication.getContext(), "登录状态已过期，请重新登录", Toast.LENGTH_SHORT).show();
                Logout();
                Navigation.findNavController(MainActivity.mainActivity, R.id.nav_host_fragment).navigate(R.id.navigation_account);
                Log.d("aaa", t.getMessage());
            }
        });
    }

    public void Logout() {
        SharedPreferences.Editor userEdit = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        userEdit.remove("studentName");
        userEdit.remove("cookie");
        userEdit.putBoolean("hasLogined", false);
        userEdit.apply();
        studentName.setValue("未登录");
        grade.setValue("未知年级");
        integral.setValue(0);
        post.setValue(0);
        thumbup.setValue(0);
    }
}