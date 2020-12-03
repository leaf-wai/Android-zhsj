package com.leaf.zhsjalpha.viewmodel;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.OrgBean;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Organization;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.LoginModel;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    public List<OrgBean> options1Items = new ArrayList<>();
    public String orgName;
    private MutableLiveData<Integer> orgId;
    public List<List<String>> options2Items = new ArrayList<>();
    public List<List<List<String>>> options3Items = new ArrayList<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getOrgId() {
        if (orgId == null) {
            orgId = new MutableLiveData<>();
            orgId.setValue(0);
        }
        return orgId;
    }

    public void login(String studentName, String password, Callback<User> callback) {
        LoginModel.getInstance().getLoginCall(studentName, password, orgId.getValue()).enqueue(callback);
    }

    public void getOrganization() {
        List<OrgBean> orgBeans = new ArrayList<>();
        Result<DataList<String>> result;
        Result<DataList<String>> result1;
        Result<DataList<Organization>> result2;
        try {
            result = RetrofitHelper.getInstance().getProvinceCall().execute().body();
            List<String> province = result.getData().getData();
            for (String i:province) {
                result1 = RetrofitHelper.getInstance().getCityCall(i).execute().body();
                List<OrgBean.CityBean> cityBeans = new ArrayList<>();
                List<String> city = result1.getData().getData();
                List<List<String>> province_OrgList = new ArrayList<>();
                if (city.size() == 0 || city == null) {
                    OrgBean.CityBean cityBean = new OrgBean.CityBean();
                    cityBean.setName("暂无数据");
                    cityBean.setArea(Collections.singletonList("暂无数据"));
                    cityBeans.add(cityBean);
                    city.add("暂无数据");
                    province_OrgList.add(Collections.singletonList("暂无数据"));
                } else {
                    for (String j:city) {
//                        Log.d("aaa", "getOrganization: " + i + " " + j);
                        result2 = RetrofitHelper.getInstance().getOrganizationCall(i, j).execute().body();
                        List<Organization> organizations = result2.getData().getData();
                        List<String> org = new ArrayList<>();
                        if (organizations.size() == 0) {
                            org.add("暂无数据");
                        } else {
                            for (Organization k:organizations) {
                                org.add(k.getOrgName());
                            }
                        }
                        OrgBean.CityBean cityBean = new OrgBean.CityBean();
                        cityBean.setName(j);
                        cityBean.setArea(org);
                        cityBeans.add(cityBean);
                        province_OrgList.add(org);
                    }
                }
                OrgBean orgBean = new OrgBean();
                orgBean.setName(i.trim());
                orgBean.setCity(cityBeans);
                orgBeans.add(orgBean);
                options1Items = orgBeans;
                options2Items.add(city);
                options3Items.add(province_OrgList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Looper.prepare();
            ToastUtils.showToast("网络错误！加载地区列表失败", Toast.LENGTH_SHORT, MyApplication.getContext().getResources().getColor(R.color.textBlack), MyApplication.getContext().getResources().getColor(R.color.white));
            Looper.loop();
        }
    }

    //获取OrgId
    public void requestOrgId(String province, String city, int position) {
        RetrofitHelper.getInstance().getOrganizationCall(province, city).enqueue(new Callback<Result<DataList<Organization>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<Organization>>> call, @NotNull Response<Result<DataList<Organization>>> response) {
                if (response.isSuccessful()) {
                    Result<DataList<Organization>> result = response.body();
                    getOrgId().postValue(result.getData().getData().get(position).getOrgId());
                    Log.d("aaa", "requestOrgId: " + getOrgId().getValue());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<Organization>>> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + t.getMessage());
                ToastUtils.showToast("获取ID失败", Toast.LENGTH_SHORT, MyApplication.getContext().getResources().getColor(R.color.textBlack), MyApplication.getContext().getResources().getColor(R.color.white));
            }
        });

    }
}
