package com.leaf.zhsjalpha;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.bean.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class api {
    public interface userInfoApi {
        @GET("student/getStudentInfo")
        Call<UserInfo> userInfo();
    }

    public interface loginApi {
        @FormUrlEncoded
        @POST("api/login/student")
        @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
        Call<User> login(@Field("studentName") String param1, @Field("password") String param2, @Field("orgId") int param3);
    }

    public interface registerApi {
        @FormUrlEncoded
        @POST("student/unfilter/register")
        @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
        Call<User> register(@Field("name") String param1, @Field("grade") String param2, @Field("sex") String param3, @Field("idcard") int param4, @Field("school") int param5);
    }
}
