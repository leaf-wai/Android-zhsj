package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.GradeBean;
import com.leaf.zhsjalpha.bean.OrganizationBean;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.model.LoginModel;
import com.leaf.zhsjalpha.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends AndroidViewModel {

    public String sex;
    public List<OrganizationBean> options1Items = new ArrayList<>();
    public List<GradeBean> grade = new ArrayList<>();
    private List<String> gradeItems = new ArrayList<>();
    private MutableLiveData<Integer> orgId;
    private MutableLiveData<Integer> gradeId;
    private MutableLiveData<Integer> registerStatus;
    public ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private MutableLiveData<List<String>> items;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getOrgId() {
        if (orgId == null) {
            orgId = new MutableLiveData<>();
            orgId.setValue(0);
        }
        return orgId;
    }

    public MutableLiveData<Integer> getGradeId() {
        if (gradeId == null) {
            gradeId = new MutableLiveData<>();
            gradeId.setValue(0);
        }
        return gradeId;
    }

    public MutableLiveData<Integer> getRegisterStatus() {
        if (registerStatus == null) {
            registerStatus = new MutableLiveData<>();
            registerStatus.setValue(0);
        }
        return registerStatus;
    }

    public MutableLiveData<List<String>> getItems() {
        if (items == null) {
            items = new MutableLiveData<>();
            items.setValue(gradeItems);
        }
        return items;
    }

    public void initGradeList() {
        String JsonData = JsonUtils.getJson(getApplication(), "Grade.json");
        Log.d("aaa", "initGradeList: " + JsonData);
        grade = JsonUtils.parseGradeData(JsonData);
        for (int i = 0; i < grade.size(); i++) {
            gradeItems.add(grade.get(i).getGradeName());
        }
        Log.d("aaa", "initGradeList: " + gradeItems.toString());
        getItems().setValue(gradeItems);
    }

    //解析数据
    public void initOrgData() {
        String JsonData = JsonUtils.getJson(getApplication(), "Organizations.json");//获取assets目录下的json文件数据
        ArrayList<OrganizationBean> organizationBean = JsonUtils.parseOrgData(JsonData);//用Gson 转成实体
        options1Items = organizationBean;

        for (int i = 0; i < organizationBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < organizationBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = organizationBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (organizationBean.get(i).getCityList().get(c).getArea() == null
                        || organizationBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(organizationBean.get(i).getCityList().get(c).getArea());
                }
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            options2Items.add(cityList);
            options3Items.add(province_AreaList);
        }
    }

    public void register(String studentName, int idcard) {
        LoginModel.getInstance().getRegisterCall(studentName, gradeId.getValue(), sex, idcard, orgId.getValue()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user.code == 200) {
                        registerStatus.setValue(200);
                    } else if (user.code == 11) {
                        registerStatus.setValue(11);
                    } else {
                        registerStatus.setValue(404);
                    }
                } else {
                    registerStatus.setValue(404);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("aaa", "网络错误: " + t.getMessage());
                registerStatus.setValue(404);
            }
        });
    }

}
