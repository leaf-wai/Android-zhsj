package com.leaf.zhsjalpha.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.bean.UserDetail;
import com.leaf.zhsjalpha.bean.UserInfo;
import com.leaf.zhsjalpha.entity.ActivityData;
import com.leaf.zhsjalpha.entity.ActivityInfoList;
import com.leaf.zhsjalpha.entity.ApplyFriendData;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.DeclareData;
import com.leaf.zhsjalpha.entity.FriendData;
import com.leaf.zhsjalpha.entity.MessageData;
import com.leaf.zhsjalpha.entity.OrderData;
import com.leaf.zhsjalpha.entity.Organization;
import com.leaf.zhsjalpha.entity.ProductData;
import com.leaf.zhsjalpha.entity.Radar;
import com.leaf.zhsjalpha.entity.Resource;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.ScheduleData;
import com.leaf.zhsjalpha.entity.StudentData;
import com.leaf.zhsjalpha.entity.TeacherNoticeData;
import com.leaf.zhsjalpha.entity.TeamData;
import com.leaf.zhsjalpha.entity.WeekInfo;
import com.leaf.zhsjalpha.utils.MyApplication;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final int DEFAULT_TIMEOUT = 30;
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
        //打印retrofit日志
        Log.d("aaa", "retrofitBack = " + message);
    });
    private SharedPreferences userRead = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {

                @Override
                public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {

                }

                @Override
                public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
                    List<Cookie> cookies = new ArrayList<>();
                    String token = userRead.getString("cookie", null);

                    if (token != null) {
                        Cookie cookie = Cookie.parse(url, token);
                        cookies.add(cookie);
                    }

                    return cookies;
                }
            })
            .retryOnConnectionFailure(true)
            .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static RetrofitHelper getInstance() {
        return Singleton.INSTANCE;
    }

    public Call<Result<DataList<String>>> getProvinceCall() {
        return retrofit.create(ApiService.class).getProvince();
    }

    public Call<Result<DataList<String>>> getCityCall(String province) {
        return retrofit.create(ApiService.class).getCity(province);
    }

    public Call<Result<DataList<Organization>>> getOrganizationCall(String province, String city) {
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return retrofit.create(ApiService.class).getOrganization(province, city);
    }

    public Call<Result<UserInfo>> getUserInfoCall() {
        return retrofit.create(ApiService.class).getUserInfo();
    }

    public Call<Result<UserDetail>> getUserDetailCall() {
        return retrofit.create(ApiService.class).getUserDetail();
    }

    public Call<Result<DataList<CurrencyTypeData>>> getCurrencyTypeCall() {
        return retrofit.create(ApiService.class).getCurrencyType();
    }

    public Call<User> postDeclareCall(String classId, RequestBody requestBody) {
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return retrofit.create(ApiService.class).postDeclare(classId, requestBody);
    }

    public Call<Result<DataList<CourseData>>> getAllClassCall(String keyword) {
        return retrofit.create(ApiService.class).getAllClass(keyword);
    }

    public Call<Result<DataList<CourseData>>> initCourseListCall(Integer gradeId, Integer courseType, Integer interestType, Double minprice, Double maxprice, String keyword) {
        return retrofit.create(ApiService.class).initCourseList(gradeId, courseType, interestType, minprice, maxprice, keyword);
    }

    public Call<Result<CourseData>> getCourseInfoCall(String classId) {
        return retrofit.create(ApiService.class).getCourseInfo(classId);
    }

    public Call<Result<DataList<ActivityData>>> getActivityListCall(Integer pindex, Integer number, String keyword) {
        return retrofit.create(ApiService.class).getActivityList(pindex, number, keyword);
    }

    public Call<Result<ActivityInfoList>> getActivityInfoCall(String activityId) {
        return retrofit.create(ApiService.class).getActivityInfo(activityId);
    }

    public Call<Result<DataList<CourseData>>> getStudentClassCall() {
        return retrofit.create(ApiService.class).getStudentClass();
    }

    public Call<Result<DataList<DeclareData>>> getMyDeclareCall(Integer week) {
        return retrofit.create(ApiService.class).getMyDeclare(week);
    }

    public Call<Result<DataList<CourseData>>> getMyCourseCall(String keyword) {
        return retrofit.create(ApiService.class).getMyCourse(keyword);
    }

    public Call<Result<Integer>> getCurrentWeekCall() {
        return retrofit.create(ApiService.class).getCurrentWeek();
    }

    public Call<Result<DataList<CurrencyTypeData>>> getTemplate(Integer target) {
        return retrofit.create(ApiService.class).initTemplate(target);
    }

    public Call<User> customEvaluateCall(RequestBody body) {
        return retrofit.create(ApiService.class).customEvaluate(body);
    }

    public Call<User> quickEvaluateCall(String updateList, Integer target) {
        return retrofit.create(ApiService.class).quickEvaluate(updateList, target);
    }

    public Call<Result<DataList<FriendData>>> getFriendListCall(String classId) {
        return retrofit.create(ApiService.class).getFriendList(classId);
    }

    public Call<User> customFriendEvaluateCall(String friendList, RequestBody requestBody) {
        return retrofit.create(ApiService.class).customFriendEvaluate(friendList, requestBody);
    }

    public Call<User> quickFriendEvaluateCall(String updateList, String friendList) {
        return retrofit.create(ApiService.class).quickFriendEvaluate(updateList, friendList);
    }

    public Call<Result<DataList<MessageData>>> getMessagesCall(Integer type) {
        return retrofit.create(ApiService.class).getMessages(type);
    }

    public Call<Result<DataList<TeacherNoticeData>>> getTeacherNoticeCall(String userId, Integer week) {
        return retrofit.create(ApiService.class).getTeacherNotice(userId, week);
    }

    public Call<User> readCall(Integer messageId) {
        return retrofit.create(ApiService.class).read(messageId);
    }

    public Call<Result<DataList<OrderData>>> getMyOrderCall(String keyword) {
        return retrofit.create(ApiService.class).getMyOrder(keyword);
    }

    public Call<User> modifyPwdCall(String oldPassword, String newPassword) {
        return retrofit.create(ApiService.class).modifyPwd(oldPassword, newPassword);
    }

    public Call<User> postProductCall(RequestBody body) {
        return retrofit.create(ApiService.class).postProduct(body);
    }

    public Call<User> thumbUpCall(String classId, String postId) {
        return retrofit.create(ApiService.class).thumbUp(classId, postId);
    }

    public Call<User> addFriendCall(String studentId, String applyContent) {
        return retrofit.create(ApiService.class).addFriend(studentId, applyContent);
    }

    public Call<User> deleteFriendCall(String deleteId) {
        return retrofit.create(ApiService.class).deleteFriend(deleteId);
    }

    public Call<User> addFeedbackCall(String content) {
        return retrofit.create(ApiService.class).addFeedback(content);
    }

    public Call<User> reviewFriendCall(Integer ID, Integer operate) {
        return retrofit.create(ApiService.class).reviewFriend(ID, operate);
    }

    public Call<Result<DataList<ProductData>>> getMyProductCall(String classId) {
        return retrofit.create(ApiService.class).getMyProduct(classId);
    }

    public Call<Result<DataList<ProductData>>> getAllProductCall(String classId) {
        return retrofit.create(ApiService.class).getAllProduct(classId);
    }

    public Call<Result<Radar>> getRadarDataCall(String classId, Integer week) {
        return retrofit.create(ApiService.class).getRadarData(classId, week);
    }

    public Call<Result<DataList<StudentData>>> getAllStudentListCall(String classId, String keyword) {
        return retrofit.create(ApiService.class).getAllStudentList(classId, keyword);
    }

    public Call<Result<DataList<ApplyFriendData>>> getFriendApplicationCall() {
        return retrofit.create(ApiService.class).getFriendApplication();
    }

    public Call<Result<DataList<TeamData>>> getTeamCall(String userId, String processId) {
        return retrofit.create(ApiService.class).getTeam(userId, processId);
    }

    public Call<User> createTeamCall(String userId, String processId, String teamName, String teamType, String parentMen, String parentWomen, RequestBody requestBody) {
        return retrofit.create(ApiService.class).createTeam(userId, processId, teamName, teamType, parentMen, parentWomen, requestBody);
    }

    public Call<User> addTeammateCall(String teamId, RequestBody requestBody) {
        return retrofit.create(ApiService.class).addTeammate(teamId, requestBody);
    }

    public Call<User> deleteTeamCall(String teamId, String processId) {
        return retrofit.create(ApiService.class).deleteTeam(teamId, processId);
    }

    public Call<User> deleteTeammateCall(String teamId, String processId, String userId) {
        return retrofit.create(ApiService.class).deleteTeammate(teamId, processId, userId);
    }

    public Call<User> quitTeamCall(String teamId, String processId, String userId) {
        return retrofit.create(ApiService.class).quitTeam(teamId, processId, userId);
    }

    public Call<User> attendActivityCall(String teamId, String processId, String userId, String teamProcessId) {
        return retrofit.create(ApiService.class).attendActivity(teamId, processId, userId, teamProcessId);
    }

    public Call<Result<ScheduleData>> getScheduleCall(Integer week) {
        return retrofit.create(ApiService.class).getSchedule(week);
    }

    public Call<Result<DataList<WeekInfo>>> getWeekInfoCall() {
        return retrofit.create(ApiService.class).getWeekInfo();
    }

    public Call<Result<DataList<Resource>>> getTeachResourceCall() {
        return retrofit.create(ApiService.class).getTeachResource();
    }

    /**
     * 获取RetrofitHelper对象的单例
     */
    private static class Singleton {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }
}
