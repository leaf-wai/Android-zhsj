package com.leaf.zhsjalpha.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.leaf.zhsjalpha.GetJsonDataUtil;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.JsonBean;
import com.leaf.zhsjalpha.bean.User;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class RegisterActivity extends AppCompatActivity {

    private String sex;
    private int orgId = 0;
    private AutoCompleteTextView textField;
    private MaterialButtonToggleGroup toggleGroup;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private Button tb_sex;
    private Button btn_Options;
    private Button btn_Reg;
    private TextInputEditText et_username;
    private TextInputEditText et_idcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_Options = findViewById(R.id.btn_options);
        btn_Reg = findViewById(R.id.btn_stuReg);
        textField = findViewById(R.id.actv_grade);
        toggleGroup = findViewById(R.id.tb_sex);
        et_username = findViewById(R.id.et_stuName);
        et_idcard = findViewById(R.id.et_idcard);
        btn_Reg.setEnabled(false);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 子线程解析省市区数据
                initJsonData();
            }
        });
        thread.start();

        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_username.getText()) || TextUtils.isEmpty(et_idcard.getText()) || TextUtils.isEmpty(textField.getText())) {
                    btn_Reg.setEnabled(false);
                } else {
                    btn_Reg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_username.getText()) || TextUtils.isEmpty(et_idcard.getText()) || TextUtils.isEmpty(textField.getText())) {
                    btn_Reg.setEnabled(false);
                } else {
                    btn_Reg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_username.getText()) || TextUtils.isEmpty(et_idcard.getText()) || TextUtils.isEmpty(textField.getText())) {
                    btn_Reg.setEnabled(false);
                } else {
                    btn_Reg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        List<String> items = new ArrayList<String>();
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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, items);
        textField.setAdapter(adapter);

        btn_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
            }
        });

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                tb_sex = toggleGroup.findViewById(checkedId);
                if (isChecked && tb_sex.getText().toString().equals("男")) {
                    Log.d("aaa", "男");
                    sex = "男";
                } else if (isChecked && tb_sex.getText().toString().equals("女")) {
                    Log.d("aaa", "女");
                    sex = "女";
                }
            }
        });

    }

    public void register() {
        String studentName = String.valueOf(et_username.getText());
        int idcard = Integer.parseInt(String.valueOf(et_idcard.getText()));
        String grade = String.valueOf(textField.getText());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zhsj.bnuz.edu.cn/ComprehensiveSys/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        registerApi api = retrofit.create(RegisterActivity.registerApi.class);
        Call<User> registerCall = api.register(studentName, grade, sex, idcard, orgId);
        registerCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void back(View view) {
        this.finish();
    }

    //解析数据
    private void initJsonData() {
        String JsonData = new GetJsonDataUtil().getJson(this, "Organizations.json");//获取assets目录下的json文件数据

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
//                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
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

    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                btn_Options.setText(tx);

                switch (tx) {
                    case "广东省珠海市爱实践":
                        orgId = 246001;
                        break;
                    case "广东省珠海市北师大":
                        orgId = 246002;
                        break;
                    case "广东省珠海市香洲一小":
                        orgId = 246003;
                        break;
                    default:
                        orgId = 0;
                }
            }
        })

                .setTitleText("选择你的学校")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#1ab394"))
                .setCancelColor(Color.GRAY)
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    public interface registerApi {
        @FormUrlEncoded
        @POST("student/unfilter/register")
        @Headers("Content-Type:application/x-www-form-urlencoded;charset=UTF-8")
        Call<User> register(@Field("name") String param1, @Field("grade") String param2, @Field("sex") String param3, @Field("idcard") int param4, @Field("school") int param5);
    }
}