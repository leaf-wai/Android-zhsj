package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.StudentData;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

import static com.leaf.zhsjalpha.utils.JsonUtils.getRequestBody;

public class AddTeammateViewModel extends AndroidViewModel {

    public String userId;
    public String userName;
    public String sex;
    public Integer gradeId;

    public List<String> classList = new ArrayList<>();
    public List<List<String>> classStudentList = new ArrayList<>();
    public List<List<StudentData>> classStudentDataList = new ArrayList<>();
    public List<CourseData> courseDataList = new ArrayList<>();

    public AddTeammateViewModel(@NonNull Application application) {
        super(application);
        new Thread(() -> getAllStudent()).start();
    }

    public void getAllStudent() {
        Result<DataList<CourseData>> result;
        try {
            result = RetrofitHelper.getInstance().getStudentClassCall().execute().body();
            if (result.getData().getData().size() != 0) {
                courseDataList = result.getData().getData();
                for (CourseData courseData : courseDataList) {
                    classList.add(courseData.getClassName());
                    List<String> studentList = new ArrayList<>();
                    Result<DataList<StudentData>> result1;
                    try {
                        result1 = RetrofitHelper.getInstance().getAllStudentListCall(courseData.getClassId(), null).execute().body();
                        List<StudentData> studentDataList = result1.getData().getData();
                        for (StudentData studentData : studentDataList) {
                            studentList.add(studentData.getName());
                        }
                        classStudentList.add(studentList);
                        classStudentDataList.add(studentDataList);
                    } catch (IOException e) {
                        e.printStackTrace();
                        ToastUtils.showToast("加载学生列表失败", Toast.LENGTH_SHORT);
                    }
                }
            } else {
                ToastUtils.showToast("你还没有班级！", Toast.LENGTH_SHORT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showToast("获取班级信息失败", Toast.LENGTH_SHORT);
        }
    }

    public void addTeammate(Callback<User> callback, String teamId, String isCoach, String isCaptain, String tel, String userIdCard, String userRace, String wx) {
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

        RetrofitHelper.getInstance().addTeammateCall(teamId, getRequestBody(hashMap)).enqueue(callback);
    }

}
