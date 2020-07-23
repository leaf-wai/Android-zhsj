package com.leaf.zhsjalpha.activity;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.leaf.zhsjalpha.api;
import com.leaf.zhsjalpha.bean.JsonBean;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.util.GetJsonDataUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        String JsonData = new GetJsonDataUtil().getJson(getApplication(), "Organizations.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

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

    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public void register(String studentName, int idcard, String grade) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zhsj.bnuz.edu.cn/ComprehensiveSys/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api.registerApi api = retrofit.create(com.leaf.zhsjalpha.api.registerApi.class);
        Call<User> registerCall = api.register(studentName, grade, sex, idcard, orgId.getValue());
        registerCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Toast.makeText(getApplication(), user.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
