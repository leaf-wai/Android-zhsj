package com.leaf.zhsjalpha.network;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.bean.UserDetail;
import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ZhsjService {
    @GET("student/getStudentInfo")
    Call<Result<UserInfo>> getUserInfo();

    @GET("student/getStudentDetail")
    Call<Result<UserDetail>> getUserDetail();

    @GET("student/initCurrencyType")
    Call<Result<DataList<CurrencyTypeData>>> getCurrencyType();

    @GET("student/getAllClass")
    Call<Result<DataList<CourseData>>> getAllClass(@Query("keyword") String keyword);

    @FormUrlEncoded
    @POST("api/login/student")
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<User> login(@Field("studentName") String param1, @Field("password") String param2, @Field("orgId") int param3);

    @FormUrlEncoded
    @POST("student/unfilter/register")
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<User> register(@Field("name") String param1, @Field("grade") int param2, @Field("sex") String param3, @Field("idcard") int param4, @Field("school") int param5);

    @FormUrlEncoded
    @POST("student/unfilter/forgetPwd")
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<User> forgetPwd(@Field("studentName") String param1, @Field("idcard") String param2, @Field("newpassword") String param3);

    @FormUrlEncoded
    @POST("student/customDeclare")
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<ResponseBody> postDeclare(@Field("classId") String param1, @Field("content") String param2, @Field("contentId") String param3, @Field("currencyId") int param4, @Field("score") int param5, @Field("subcurrencyId") int param6, @Field("yn") int param7);
}