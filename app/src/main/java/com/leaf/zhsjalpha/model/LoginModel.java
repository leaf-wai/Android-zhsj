package com.leaf.zhsjalpha.model;

import com.leaf.zhsjalpha.api.ApiService;
import com.leaf.zhsjalpha.bean.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModel {
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static LoginModel getInstance() {
        return LoginModel.Singleton.INSTANCE;
    }

    public Call<User> getLoginCall(String studentName, String password, int orgId) {
        ApiService service = retrofit.create(ApiService.class);
        Call<User> loginCall = service.login(studentName, password, orgId);
        return loginCall;
    }

    public Call<User> getRegisterCall(String studentName, int grade, String sex, int idcard, int orgId) {
        ApiService service = retrofit.create(ApiService.class);
        Call<User> registerCall = service.register(studentName, grade, sex, idcard, orgId);
        return registerCall;
    }

    public Call<User> getForgetPwdCall(String studentName, String idcard, String newpassword) {
        ApiService service = retrofit.create(ApiService.class);
        Call<User> forgetPwdCall = service.forgetPwd(studentName, idcard, newpassword);
        return forgetPwdCall;
    }

    private static class Singleton {
        private static final LoginModel INSTANCE = new LoginModel();
    }
}
