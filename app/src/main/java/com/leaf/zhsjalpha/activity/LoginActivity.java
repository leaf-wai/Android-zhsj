package com.leaf.zhsjalpha.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityLoginBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.LoginViewModel;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.MD5Utils.md5;
import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<User> {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private ColorStateList list = null;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("登录中…", getResources().getColor(R.color.colorPrimary));
        initView();
        addListener();

        // 子线程解析省市区数据
        new Thread(() -> loginViewModel.getOrganization()).start();

        initCSL();
        addObserver();
        loadUserSave();
    }

    //初始化ColorStateList
    private void initCSL() {
        @SuppressLint("ResourceType") XmlPullParser xpp = getResources().getXml(R.color.outlined_fill);
        try {
            list = ColorStateList.createFromXml(getResources(), xpp);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    //添加观察者
    private void addObserver() {
        loginViewModel.getOrgId().observe(this, integer -> {
            if (integer != 0) {
                binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location_fill));
            } else {
                binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
            }
            binding.btnStulogin.setEnabled(!TextUtils.isEmpty(binding.etUser.getText()) && !TextUtils.isEmpty(binding.etPwd.getText()) && integer != 0);
        });
    }

    //添加监听器
    private void addListener() {
        binding.btnBack.setOnClickListener(this);
        binding.LLLocation.setOnClickListener(this);
        binding.btnStulogin.setOnClickListener(this);
        binding.btnReg.setOnClickListener(this);
        binding.btnForegetPwd.setOnClickListener(this);

        binding.TILUser.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etUser.getText())) {
                    binding.TILUser.setErrorEnabled(true);
                    binding.TILUser.setError("用户名不能为空！");
                } else {
                    binding.TILUser.setErrorEnabled(false);
                    binding.TILUser.setBoxStrokeColorStateList(list);
                }

                binding.btnStulogin.setEnabled(!TextUtils.isEmpty(binding.etUser.getText()) && !TextUtils.isEmpty(binding.etPwd.getText()) && loginViewModel.getOrgId().getValue() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.TILPwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etPwd.getText())) {
                    binding.TILPwd.setErrorEnabled(true);
                    binding.TILPwd.setError("密码不能为空！");
                } else {
                    binding.TILPwd.setErrorEnabled(false);
                    binding.TILPwd.setBoxStrokeColorStateList(list);
                }

                binding.btnStulogin.setEnabled(!TextUtils.isEmpty(binding.etUser.getText()) && !TextUtils.isEmpty(binding.etPwd.getText()) && loginViewModel.getOrgId().getValue() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //初始化View
    private void initView() {
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.btnStulogin.setEnabled(false);
        binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
    }

    //弹出地区选择View
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = loginViewModel.options1Items.size() > 0 ?
                    loginViewModel.options1Items.get(options1).getPickerViewText() : "";

            String opt2tx = loginViewModel.options2Items.size() > 0
                    && loginViewModel.options2Items.get(options1).size() > 0 ?
                    loginViewModel.options2Items.get(options1).get(options2) : "";

            String opt3tx = loginViewModel.options2Items.size() > 0
                    && loginViewModel.options3Items.get(options1).size() > 0
                    && loginViewModel.options3Items.get(options1).get(options2).size() > 0 ?
                    loginViewModel.options3Items.get(options1).get(options2).get(options3) : "";

            String tx = opt1tx + opt2tx + opt3tx;
            binding.tvLocation.setText(tx);
            binding.tvLocation.setTextColor(getResources().getColor(R.color.textBlack));
            new Thread(() -> loginViewModel.requestOrgId(opt1tx, opt2tx, options3)).start();

            loginViewModel.orgName = tx;
        })

                .setTitleText("选择你的学校")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#1ab394"))
                .setCancelColor(Color.GRAY)
                .setContentTextSize(20)
                .build();

        if (loginViewModel.options1Items != null || loginViewModel.options2Items != null || loginViewModel.options3Items != null || loginViewModel.options1Items.size() != 0) {
            pvOptions.setPicker(loginViewModel.options1Items, loginViewModel.options2Items, loginViewModel.options3Items);//三级选择器
            pvOptions.show();
        } else {
            ToastUtils.showToast(getApplicationContext(), "地区列表加载失败", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
        }
    }

    //加载已保存用户名密码
    private void loadUserSave() {
        SharedPreferences rmbRead = getApplicationContext().getSharedPreferences("UserSave", Context.MODE_PRIVATE);
        if (rmbRead.getBoolean("RememberMe", false)) {
            int orgId = rmbRead.getInt("orgId", 0);
            String orgName = rmbRead.getString("orgName", null);
            String studentName = rmbRead.getString("studentName", null);
            String password = rmbRead.getString("password", null);
            if (orgId != 0 && orgName != null && studentName != null && password != null) {
                binding.etUser.setText(studentName);
                binding.etPwd.setText(password);
                binding.ckRemember.setChecked(true);
                loginViewModel.getOrgId().setValue(orgId);
                loginViewModel.orgName = orgName;
                binding.tvLocation.setText(orgName);
                binding.tvLocation.setTextColor(getResources().getColor(R.color.textBlack));
            }
        }
    }

    @Override
    public void onResponse(@NotNull Call<User> call, Response<User> response) {
        if (response.isSuccessful() && response.body() != null) {
            User user = response.body();
            if (user.getCode() == 200) {
                //SharedPreferences保存用户信息
                SharedPreferences.Editor rmbEdit = getApplicationContext().getSharedPreferences("UserSave", Context.MODE_PRIVATE).edit();
                String cookie = response.headers().get("Set-Cookie");
                SharedPreferences.Editor userEdit = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                userEdit.putString("studentName", String.valueOf(binding.etUser.getText()));
                userEdit.putString("userId", user.getDetail());
                userEdit.putString("school", loginViewModel.orgName);
                userEdit.putString("cookie", cookie);
                userEdit.putBoolean("hasLogined", true);
                if (binding.ckRemember.isChecked()) {
                    //SharedPreferences保存学校、用户名和密码
                    rmbEdit.putBoolean("RememberMe", true);
                    rmbEdit.putString("studentName", String.valueOf(binding.etUser.getText()));
                    rmbEdit.putString("password", String.valueOf(binding.etPwd.getText()));
                    rmbEdit.putInt("orgId", loginViewModel.getOrgId().getValue());
                    rmbEdit.putString("orgName", loginViewModel.orgName);
                } else {
                    rmbEdit.putBoolean("RememberMe", false);
                    rmbEdit.remove("studentName");
                    rmbEdit.remove("password");
                    rmbEdit.remove("orgId");
                    rmbEdit.remove("orgName");
                }
                userEdit.apply();
                rmbEdit.apply();
                loadingFragment.dismiss();
                startActivity(new Intent(getApplication(), MainActivity.class));
                ToastUtils.showToast(getApplicationContext(), "登录成功");
                finish();
            } else {
                loadingFragment.dismiss();
                ToastUtils.showToast(getApplicationContext(), "用户名或密码错误！", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
            }
        } else {
            loadingFragment.dismiss();
            ToastUtils.showToast(getApplicationContext(), "网络错误！请稍后重试", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onFailure(@NotNull Call<User> call, Throwable t) {
        Log.d("aaa", "网络错误: " + t.getMessage());
        loadingFragment.dismiss();
        ToastUtils.showToast(getApplicationContext(), "网络错误！请稍后重试", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_stulogin:
                String studentName = String.valueOf(binding.etUser.getText());
                //密码进行MD5加密
                String password = md5(String.valueOf(binding.etPwd.getText()));
                loadingFragment.show(getSupportFragmentManager(), "login");
                new Handler().postDelayed(() -> loginViewModel.login(studentName, password, this), 1000);
                break;
            case R.id.btn_reg:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.LL_location:
                showPickerView();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_foregetPwd:
                startActivity(new Intent(getApplicationContext(), ForgetPwdActivity.class));
        }
    }
}