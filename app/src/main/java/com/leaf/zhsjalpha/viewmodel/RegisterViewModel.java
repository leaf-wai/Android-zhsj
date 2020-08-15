package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.bean.JsonBean;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.model.LoginModel;
import com.leaf.zhsjalpha.utils.JsonUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends AndroidViewModel {

    public String sex;
    public MutableLiveData<Integer> orgId;
    public List<String> items;
    public List<JsonBean> options1Items = new ArrayList<>();
    public ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    public ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

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

    public void initArrayList() {
        items = new ArrayList<>();
        items.add("一年级");
        items.add("二年级");
        items.add("三年级");
        items.add("四年级");
        items.add("五年级");
        items.add("六年级");
        items.add("七年级");
        items.add("八年级");
        items.add("九年级");
        items.add("小班");
        items.add("中班");
        items.add("大班");
        items.add("成人");
    }

    //解析数据
    public void initJsonData() {
        String JsonData = JsonUtils.getJson(getApplication(), "Organizations.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = JsonUtils.parseData(JsonData);//用Gson 转成实体
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            options2Items.add(cityList);
            options3Items.add(province_AreaList);
        }
    }

    public void register(String studentName, int idcard, String grade) {
        LoginModel.getInstance().getRegisterCall(studentName, idcard, grade, sex, orgId.getValue()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                ToastUtils.showToast(user.toString(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ToastUtils.showToast(t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

}
