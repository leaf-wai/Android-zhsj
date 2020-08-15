package com.leaf.zhsjalpha.ui.account;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountViewModel extends AndroidViewModel {

    private static String TAG = "aaa";
    public MutableLiveData<Boolean> Login;
    public MutableLiveData<String> grade;
    public MutableLiveData<String> school;
    public MutableLiveData<Integer> integral;
    public MutableLiveData<Integer> post;
    public MutableLiveData<Integer> thumbup;
    public MutableLiveData<Integer> studentNo;
    public MutableLiveData<String> studentName;

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getLogin() {
        if (Login == null) {
            Login = new MutableLiveData<>();
            Login.setValue(false);
        }
        return Login;
    }

    public MutableLiveData<String> getGrade() {
        if (grade == null) {
            grade = new MutableLiveData<>();
            grade.setValue("未知年级");
        }
        return grade;
    }

    public MutableLiveData<String> getSchool() {
        if (school == null) {
            school = new MutableLiveData<>();
            school.setValue("未知学校");
        }
        return school;
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

    public MutableLiveData<Integer> getStudentNo() {
        if (studentNo == null) {
            studentNo = new MutableLiveData<>();
            studentNo.setValue(1701050097);
        }
        return studentNo;
    }

    public void loadLoginState() {
        Log.d(TAG, "loadLoginState: " + Login.getValue());
        SharedPreferences userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        school.setValue(userRead.getString("school", null));
        if (!Login.getValue()) {
            if (userRead.getBoolean("hasLogined", false)) {
                Log.d(TAG, "loadLoginState: setUserInfo");
                setUserInfo();
                Login.setValue(true);
            } else {
                Login.setValue(false);
            }
        }
    }

    public void setUserInfo() {
        RetrofitHelper.getInstance().getUserInfoCall().enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
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
                } else {
                    ToastUtils.showToast("网络请求出错，请重新登录", Toast.LENGTH_SHORT);
                    Logout();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                ToastUtils.showToast("网络错误: " + t.getMessage(), Toast.LENGTH_SHORT);
                new Handler().postDelayed(() -> ToastUtils.showToast("登录状态已过期，请重新登录", Toast.LENGTH_SHORT), 2000);
                Logout();
                Log.d("aaa", t.getMessage());
            }
        });

//        SharedPreferences userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
//
//            @Override
//            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//                List<Cookie> cookies = new ArrayList<>();
//                String token = userRead.getString("cookie", null);
//
//                if (token != null) {
//                    Cookie cookie = Cookie.parse(url, token);
//                    cookies.add(cookie);
//                }
//
//                return cookies;
//            }
//        }).build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl("https://zhsj.bnuz.edu.cn/ComprehensiveSys/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        api.userInfoApi api = retrofit.create(com.leaf.zhsjalpha.network.api.userInfoApi.class);
//        Call<UserInfo> userInfoCall = api.userInfo();
//        userInfoCall.enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                UserInfo userInfo = response.body();
//                if (userInfo.code == 200) {
//                    switch (userInfo.getData().getGradeId()) {
//                        case 1:
//                            grade.setValue("一年级");
//                            break;
//                        case 2:
//                            grade.setValue("二年级");
//                            break;
//                        case 3:
//                            grade.setValue("三年级");
//                            break;
//                        case 4:
//                            grade.setValue("四年级");
//                            break;
//                        case 5:
//                            grade.setValue("五年级");
//                            break;
//                        case 6:
//                            grade.setValue("六年级");
//                            break;
//                        case 7:
//                            grade.setValue("七年级");
//                            break;
//                        case 8:
//                            grade.setValue("八年级");
//                            break;
//                        case 9:
//                            grade.setValue("九年级");
//                            break;
//                        default:
//                            grade.setValue("未知年级");
//                            break;
//                    }
//                    integral.setValue(userInfo.getData().getIntegral());
//                    post.setValue(userInfo.getData().getPostNum());
//                    thumbup.setValue(userInfo.getData().getThumbUpNum());
//                    studentName.setValue(userInfo.getData().getStudentName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserInfo> call, Throwable t) {
//                ToastUtils.showToast("网络错误: " + t.getMessage(), Toast.LENGTH_SHORT);
//                ToastUtils.showToast("登录状态已过期，请重新登录", Toast.LENGTH_SHORT);
//                Logout();
//                Navigation.findNavController(MainActivity.mainActivity, R.id.nav_host_fragment).navigate(R.id.navigation_account);
//                Log.d("aaa", t.getMessage());
//            }
//        });
    }

    public void Logout() {
        Login.setValue(false);
        SharedPreferences.Editor userEdit = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        userEdit.remove("studentName");
        userEdit.remove("cookie");
        userEdit.remove("school");
        userEdit.putBoolean("hasLogined", false);
        userEdit.apply();
        studentName.setValue("未登录");
        grade.setValue("未知年级");
        integral.setValue(0);
        post.setValue(0);
        thumbup.setValue(0);
    }
}