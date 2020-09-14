package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.OrganizationBean;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.model.LoginModel;
import com.leaf.zhsjalpha.utils.JsonUtils;
import com.leaf.zhsjalpha.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    public List<OrganizationBean> options1Items = new ArrayList<>();
    public String orgName;
    private MutableLiveData<Integer> orgId;
    public ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private MutableLiveData<Integer> loginState;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getLoginState() {
        if (loginState == null) {
            loginState = new MutableLiveData<>();
            loginState.setValue(0);
        }
        return loginState;
    }

    public MutableLiveData<Integer> getOrgId() {
        if (orgId == null) {
            orgId = new MutableLiveData<>();
            orgId.setValue(0);
        }
        return orgId;
    }

    //解析地区数据
    public void initJsonData() {
        String JsonData = JsonUtils.getJson(MyApplication.getContext(), "Organizations.json");//获取assets目录下的json文件数据
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

    public void login(String studentName, String password) {
        LoginModel.getInstance().getLoginCall(studentName, password, orgId.getValue()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user.code == 200) {
                        loginState.setValue(200);
                        String cookie = response.headers().get("Set-Cookie");
                        SharedPreferences.Editor userEdit = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                        userEdit.putString("studentName", studentName);
                        userEdit.putString("school", orgName);
                        userEdit.putString("cookie", cookie);
                        userEdit.putBoolean("hasLogined", true);
                        userEdit.apply();
                    } else {
                        loginState.setValue(202);
                    }
                } else {
                    loginState.setValue(404);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("aaa", "网络错误: " + t.getMessage());
                loginState.setValue(404);
            }
        });
    }

}
