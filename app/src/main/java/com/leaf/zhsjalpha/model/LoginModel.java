package com.leaf.zhsjalpha.model;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.model.network.ZhsjService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModel {

    public String BASE_URL = "https://zhsj.bnuz.edu.cn/ComprehensiveSys/";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ;

    public static LoginModel getInstance() {
        return LoginModel.Singleton.INSTANCE;
    }

    public Call<User> getLoginCall(String studentName, String password, int orgId) {
        ZhsjService service = retrofit.create(ZhsjService.class);
        Call<User> loginCall = service.login(studentName, password, orgId);
        return loginCall;
    }

    public Call<User> getRegisterCall(String studentName, int grade, String sex, int idcard, int orgId) {
        ZhsjService service = retrofit.create(ZhsjService.class);
        Call<User> registerCall = service.register(studentName, grade, sex, idcard, orgId);
        return registerCall;
    }

    public Call<User> getForgetPwdCall(String studentName, String idcard, String newpassword) {
        ZhsjService service = retrofit.create(ZhsjService.class);
        Call<User> forgetPwdCall = service.forgetPwd(studentName, idcard, newpassword);
        return forgetPwdCall;
    }

    private static class Singleton {
        private static final LoginModel INSTANCE = new LoginModel();
    }
}
