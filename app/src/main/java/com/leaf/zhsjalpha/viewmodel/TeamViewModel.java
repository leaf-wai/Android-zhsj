package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.bean.UserDetail;
import com.leaf.zhsjalpha.entity.ActivityUser;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.Team;
import com.leaf.zhsjalpha.entity.TeamData;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.JsonUtils.getRequestBody;

public class TeamViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> loadingStatus;
    private MutableLiveData<List<Team>> teams;
    private MutableLiveData<List<ActivityUser>> activityUsers;
    public String userId;
    public String processId;
    public String userName;
    private String sex;
    private Integer gradeId;

    private List<ActivityUser> activityUserList = new ArrayList<>();
    private List<Team> teamList = new ArrayList<>();
    public List<TeamData> teamDataList = new ArrayList<>();

    public MutableLiveData<Integer> getLoadingStatus() {
        if (loadingStatus == null) {
            loadingStatus = new MutableLiveData<>();
            loadingStatus.setValue(0);
        }
        return loadingStatus;
    }

    public MutableLiveData<List<Team>> getTeams() {
        if (teams == null) {
            teams = new MutableLiveData<>();
        }
        return teams;
    }

    public MutableLiveData<List<ActivityUser>> getActivityUsers() {
        if (activityUsers == null) {
            activityUsers = new MutableLiveData<>();
        }
        return activityUsers;
    }

    public TeamViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = userRead.getString("userId", null);
        getUserDetail();
    }

    public void getTeam() {
        RetrofitHelper.getInstance().getTeamCall(userId, null).enqueue(new Callback<Result<DataList<TeamData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<TeamData>>> call, @NotNull Response<Result<DataList<TeamData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<TeamData>> result = response.body();
                    if (result.getCode() == 200) {
                        getLoadingStatus().setValue(200);
                        teamList.clear();
                        teamDataList = result.getData().getData();
                        for (TeamData teamData : teamDataList) {
                            Team team = new Team();
                            team.setTeamId(teamData.getTeamId());
                            team.setProcessId(teamData.getProcessId());
                            team.setTeamName(teamData.getTeamName());
                            team.setTeamLeader(teamData.getContactName());
                            team.setTeamType(teamData.getTeamType());
                            teamList.add(team);
                        }
                        getTeams().postValue(teamList);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<TeamData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载小队失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getActivityUser(String teamId) {
        RetrofitHelper.getInstance().getTeamCall(userId, null).enqueue(new Callback<Result<DataList<TeamData>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<TeamData>>> call, @NotNull Response<Result<DataList<TeamData>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<TeamData>> result = response.body();
                    if (result.getCode() == 200) {
                        getLoadingStatus().setValue(200);
                        teamDataList = result.getData().getData();
                        for (TeamData teamData : teamDataList) {
                            if (teamData.getTeamId().equals(teamId)) {
                                activityUserList = teamData.getActivityUsers();
                                activityUsers.postValue(activityUserList);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<TeamData>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载队员失败", Toast.LENGTH_SHORT);
                loadingStatus.setValue(404);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUserDetail() {
        RetrofitHelper.getInstance().getUserDetailCall().enqueue(new Callback<Result<UserDetail>>() {
            @Override
            public void onResponse(@NotNull Call<Result<UserDetail>> call, @NotNull Response<Result<UserDetail>> response) {
                if (response.isSuccessful()) {
                    Result<UserDetail> result = response.body();
                    if (result.getCode() == 200) {
                        userName = result.getData().getName();
                        sex = result.getData().getSex();
                        gradeId = result.getData().getGradeId();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<UserDetail>> call, @NotNull Throwable t) {
                ToastUtils.showToast("网络错误: " + t.getMessage(), Toast.LENGTH_SHORT);
                Log.d("aaa", t.getMessage());
            }
        });
    }

    public void createTeam(Callback<User> callback, String teamName, String teamType, String parentMen, String parentWomen, String isCaptain, String isCoach, String tel, String userIdCard, String userRace, String wx) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gradeId", gradeId);
        hashMap.put("isCoach", isCoach);
        hashMap.put("isCaptain", isCaptain);
        hashMap.put("tel", tel);
        hashMap.put("userIdCard", userIdCard);
        hashMap.put("userName", userName);
        hashMap.put("userRace", userRace);
        hashMap.put("userSex", sex);
        hashMap.put("wx", wx);

        RetrofitHelper.getInstance().createTeamCall(userId, processId, teamName, teamType, parentMen, parentWomen, getRequestBody(hashMap)).enqueue(callback);
    }

    public void deleteTeammate(Callback<User> callback, String teamId, String processId, String userId) {
        RetrofitHelper.getInstance().deleteTeammateCall(teamId, processId, userId).enqueue(callback);
    }

    public void deleteTeam(Callback<User> callback, String teamId, String processId) {
        RetrofitHelper.getInstance().deleteTeamCall(teamId, processId).enqueue(callback);
    }

    public void attendActivity(Callback<User> callback, String teamId, String processId) {
        RetrofitHelper.getInstance().attendActivityCall(teamId, processId, userId, this.processId).enqueue(callback);
    }

}
