package com.leaf.zhsjalpha.model.network;

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
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.ScheduleData;
import com.leaf.zhsjalpha.entity.StudentData;
import com.leaf.zhsjalpha.entity.TeacherNoticeData;
import com.leaf.zhsjalpha.entity.TeamData;
import com.leaf.zhsjalpha.entity.WeekInfo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ZhsjService {
    @GET("teacher/provinces")
    Call<Result<DataList<String>>> getProvince();

    @GET("teacher/provinces/{province}/cities")
    Call<Result<DataList<String>>> getCity(@Path("province") String province);

    @GET("teacher/provinces/{province}/cities/{city}/organizations")
    Call<Result<DataList<Organization>>> getOrganization(@Path("province") String province, @Path("city") String city);

    @GET("student/getStudentInfo")
    Call<Result<UserInfo>> getUserInfo();

    @GET("student/getStudentDetail")
    Call<Result<UserDetail>> getUserDetail();

    @GET("student/initCurrencyType")
    Call<Result<DataList<CurrencyTypeData>>> getCurrencyType();

    @GET("student/getAllClass")
    Call<Result<DataList<CourseData>>> getAllClass(@Query("keyword") String keyword);

    @GET("student/initCourseList")
    Call<Result<DataList<CourseData>>> initCourseList(@Query("gradeId") Integer gradeId, @Query("courseType") Integer courseType, @Query("interestType") Integer interestType, @Query("minprice") Double minprice, @Query("maxprice") Double maxprice, @Query("keyword") String keyword);

    @GET("student/getCourseInfo")
    Call<Result<CourseData>> getCourseInfo(@Query("classId") String classId);

    @GET("student/initStudentClass")
    Call<Result<DataList<CourseData>>> getStudentClass();

    @GET("student/getCurrentWeek")
    Call<Result<Integer>> getCurrentWeek();

    @GET("student/getMyDeclare")
    Call<Result<DataList<DeclareData>>> getMyDeclare(@Query("week") Integer week);

    @GET("student/getMyCourse")
    Call<Result<DataList<CourseData>>> getMyCourse(@Query("keyword") String keyword);

    @GET("student/initTemplate")
    Call<Result<DataList<CurrencyTypeData>>> initTemplate(@Query("target") Integer target);

    @GET("student/getFriendList")
    Call<Result<DataList<FriendData>>> getFriendList(@Query("classId") String classId);

    @GET("student/getMessages")
    Call<Result<DataList<MessageData>>> getMessages(@Query("type") Integer type);

    @GET("student/notice/{userId}")
    Call<Result<DataList<TeacherNoticeData>>> getTeacherNotice(@Path("userId") String userId, @Query("week") Integer week);

    @GET("student/getMyOrder")
    Call<Result<DataList<OrderData>>> getMyOrder(@Query("keyword") String keyword);

    @GET("student/getMyProduct")
    Call<Result<DataList<ProductData>>> getMyProduct(@Query("classId") String classId);

    @GET("student/getAllProduct")
    Call<Result<DataList<ProductData>>> getAllProduct(@Query("classId") String classId);

    @GET("student/initRadar")
    Call<Result<Radar>> getRadarData(@Query("classId") String classId, @Query("week") Integer week);

    @GET("student/getAllStudent")
    Call<Result<DataList<StudentData>>> getAllStudentList(@Query("classId") String classId, @Query("keyword") String keyword);

    @GET("student/getFriendApplication")
    Call<Result<DataList<ApplyFriendData>>> getFriendApplication();

    @GET("student/Team/{userId}")
    Call<Result<DataList<TeamData>>> getTeam(@Path("userId") String userId, @Query("processId") String processId);

    @GET("student/getTeachScheduleByWeek")
    Call<Result<ScheduleData>> getSchedule(@Query("week") Integer week);

    @GET("teacher/getWeekInfo")
    Call<Result<DataList<WeekInfo>>> getWeekInfo();

    @FormUrlEncoded
    @POST("api/activity/getAllActivities")
    Call<Result<DataList<ActivityData>>> getActivityList(@Field("pindex") Integer pindex, @Field("number") Integer number, @Field("keyword") String keyword);

    @POST("api/activity/activityDetail/activity/{activityId}")
    Call<Result<ActivityInfoList>> getActivityInfo(@Path("activityId") String activityId);

    @FormUrlEncoded
    @POST("api/login/student")
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<User> login(@Field("studentName") String param1, @Field("password") String param2, @Field("orgId") int param3);

    @FormUrlEncoded
    @POST("student/unfilter/register")
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<User> register(@Field("name") String param1, @Field("grade") int param2, @Field("sex") String param3, @Field("idcard") int param4, @Field("school") int param5);

    @POST("student/unfilter/forgetPwd")
    Call<User> forgetPwd(@Query("studentName") String studentName, @Query("idcard") String idcard, @Query("newpassword") String newpassword);

    @POST("teacher/modifyTeacherPassword")
    Call<User> modifyPwd(@Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    @POST("student/customDeclare")
    Call<User> postDeclare(@Query("classId") String classId, @Body RequestBody requestBody);

    @POST("student/customEvalue")
    Call<User> customEvaluate(@Body RequestBody Body);

    @POST("student/updateContent")
    Call<User> quickEvaluate(@Query("updateList") String updateList, @Query("target") Integer target);

    @POST("student/customFriendEvalue")
    Call<User> customFriendEvaluate(@Query("friendList") String friendList, @Body RequestBody requestBody);

    @POST("student/updateFriendContent")
    Call<User> quickFriendEvaluate(@Query("updateList") String updateList, @Query("friendList") String friendList);

    @POST("student/read")
    Call<User> read(@Query("messageId") Integer messageId);

    @POST("student/postProduct")
    Call<User> postProduct(@Body RequestBody requestBody);

    @POST("student/thumbUp")
    Call<User> thumbUp(@Query("classId") String classId, @Query("postId") String postId);

    @POST("student/addFriend")
    Call<User> addFriend(@Query("studentId") String studentId, @Query("applyContent") String applyContent);

    @POST("student/deleteFriend")
    Call<User> deleteFriend(@Query("deleteId") String deleteId);

    @POST("student/addFeedback")
    Call<User> addFeedback(@Query("content") String content);

    @POST("student/reviewFriend")
    Call<User> reviewFriend(@Query("ID") Integer ID, @Query("operate") Integer operate);

    @POST("student/Team/{userId}")
    Call<User> createTeam(@Path("userId") String userId, @Query("processId") String processId, @Query("teamName") String teamName, @Query("teamType") String teamType, @Query("parentMen") String parentMen, @Query("parentWomen") String parentWomen, @Body RequestBody requestBody);

    @POST("student/Team/{teamId}/teammate")
    Call<User> addTeammate(@Path("teamId") String teamId, @Body RequestBody requestBody);

    @DELETE("student/Team/{teamId}/{processId}")
    Call<User> deleteTeam(@Path("teamId") String teamId, @Path("processId") String processId);

    @DELETE("student/Team/{teamId}/{processId}/teammate/{userId}")
    Call<User> deleteTeammate(@Path("teamId") String teamId, @Path("processId") String processId, @Path("userId") String userId);

    @DELETE("student/Team/{teamId}/{processId}/{userId}")
    Call<User> quitTeam(@Path("teamId") String teamId, @Path("processId") String processId, @Path("userId") String userId);

    @POST("student/Team/{teamId}/{processId}/{userId}")
    Call<User> attendActivity(@Path("teamId") String teamId, @Path("processId") String processId, @Path("userId") String userId, @Query("teamProcessId") String teamProcessId);
}