package com.leaf.zhsjalpha.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.leaf.zhsjalpha.bean.UserDetail;
import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final int DEFAULT_TIMEOUT = 10;
    private SharedPreferences userRead = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {

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
    }).connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
    private String BASE_URL = "https://zhsj.bnuz.edu.cn/ComprehensiveSys/";
    private Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build();

    public static RetrofitHelper getInstance() {
        return Singleton.INSTANCE;
    }

    public Call<Result<UserInfo>> getUserInfoCall() {
        return retrofit.create(ZhsjService.class).getUserInfo();
    }

    public Call<Result<UserDetail>> getUserDetailCall() {
        return retrofit.create(ZhsjService.class).getUserDetail();
    }

    public Call<Result<DataList<CurrencyTypeData>>> getCurrencyTypeCall() {
        return retrofit.create(ZhsjService.class).getCurrencyType();
    }

    public Call<ResponseBody> postDeclareCall(String classId, String content, String contentId, int currencyId, int score, int subcurrencyId, int yn) {
        return retrofit.create(ZhsjService.class).postDeclare(classId, content, contentId, currencyId, score, subcurrencyId, yn);
    }

    public Call<Result<DataList<CourseData>>> getAllClassCall(String keyword) {
        return retrofit.create(ZhsjService.class).getAllClass(keyword);
    }

    /**
     * 获取RetrofitHelper对象的单例
     */
    private static class Singleton {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }
}
