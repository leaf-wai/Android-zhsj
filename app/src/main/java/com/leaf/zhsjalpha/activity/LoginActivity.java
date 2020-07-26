package com.leaf.zhsjalpha.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityLoginBinding;

import static com.leaf.zhsjalpha.utils.MD5Utils.md5;

public class LoginActivity extends AppCompatActivity {

    public static LoginActivity loginActivity;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.btnStulogin.setEnabled(false);

        // 子线程解析省市区数据
        Thread thread = new Thread(() -> loginViewModel.initJsonData());
        thread.start();

        loginViewModel.getOrgId().observe(this, integer -> {
            if (TextUtils.isEmpty(binding.etUser.getText()) || TextUtils.isEmpty(binding.etPwd.getText()) || integer == 0) {
                binding.btnStulogin.setEnabled(false);
            } else {
                binding.btnStulogin.setEnabled(true);
            }
        });

        binding.etUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etUser.getText()) || TextUtils.isEmpty(binding.etPwd.getText()) || loginViewModel.orgId.getValue() == 0) {
                    binding.btnStulogin.setEnabled(false);
                } else {
                    binding.btnStulogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etUser.getText()) || TextUtils.isEmpty(binding.etPwd.getText()) || loginViewModel.orgId.getValue() == 0) {
                    binding.btnStulogin.setEnabled(false);
                } else {
                    binding.btnStulogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginViewModel.getLoginState().observe(this, integer -> {
            switch (integer) {
                case 200:
                    Snackbar.make(binding.clLogin, "登录成功", Snackbar.LENGTH_SHORT).show();
                    if (binding.ckRemember.isChecked()) {
                        SharedPreferences.Editor rmbEdit = getApplicationContext().getSharedPreferences("UserSave", Context.MODE_PRIVATE).edit();
                        rmbEdit.putBoolean("RememberMe", true);
                        rmbEdit.putString("studentName", String.valueOf(binding.etUser.getText()));
                        rmbEdit.putString("password", String.valueOf(binding.etPwd.getText()));
                        rmbEdit.putInt("orgId", loginViewModel.orgId.getValue());
                        rmbEdit.apply();
                    }
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(getApplication(), MainActivity.class));
                        finish();
                    }, 1000);
                    break;
                case 202:
                    Snackbar.make(binding.clLogin, "用户名或密码错误！", Snackbar.LENGTH_SHORT).show();
                    break;
                case 404:
                    Snackbar.make(binding.clLogin, "网络错误！请稍后重试", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });

        View.OnClickListener loginListener = v -> {
            switch (v.getId()) {
                case R.id.btn_stulogin:
                    String studentName = String.valueOf(binding.etUser.getText());
                    //密码进行MD5加密
                    String password = md5(String.valueOf(binding.etPwd.getText()));
                    View progressbar = LayoutInflater.from(this).inflate(R.layout.progressbar_layout, null, false);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setView(progressbar);
                    AlertDialog dialog = builder.show();
                    new Handler().postDelayed(() -> {
                        loginViewModel.login(studentName, password);
                        dialog.dismiss();
                    }, 1000);
                    break;
                case R.id.btn_reg:
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    break;
                case R.id.btn_options:
                    showPickerView();
                    break;
                case R.id.btn_back:
                    finish();
                    break;
            }
        };

        binding.btnBack.setOnClickListener(loginListener);
        binding.btnOptions.setOnClickListener(loginListener);
        binding.btnStulogin.setOnClickListener(loginListener);
        binding.btnReg.setOnClickListener(loginListener);

        loadUserSave();
    }

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
            binding.btnOptions.setText(tx);

            switch (tx) {
                case "广东省珠海市爱实践":
                    loginViewModel.orgId.setValue(246001);
                    break;
                case "广东省珠海市北师大":
                    loginViewModel.orgId.setValue(246002);
                    break;
                case "广东省珠海市香洲一小":
                    loginViewModel.orgId.setValue(246003);
                    break;
                default:
                    loginViewModel.orgId.setValue(0);
                    break;
            }
        })

                .setTitleText("选择你的学校")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#1ab394"))
                .setCancelColor(Color.GRAY)
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(loginViewModel.options1Items, loginViewModel.options2Items, loginViewModel.options3Items);//三级选择器
        pvOptions.show();
    }

    private void loadUserSave() {
        SharedPreferences rmbRead = getApplicationContext().getSharedPreferences("UserSave", Context.MODE_PRIVATE);
        if (rmbRead.getBoolean("RememberMe", false)) {
            binding.etUser.setText(rmbRead.getString("studentName", ""));
            binding.etPwd.setText(rmbRead.getString("password", ""));
            binding.ckRemember.setChecked(true);
            switch (rmbRead.getInt("orgId", 0)) {
                case 246001:
                    loginViewModel.orgId.setValue(246001);
                    binding.btnOptions.setText("广东省珠海市爱实践");
                    break;
                case 246002:
                    loginViewModel.orgId.setValue(246002);
                    binding.btnOptions.setText("广东省珠海市北师大");
                    break;
                case 246003:
                    loginViewModel.orgId.setValue(246003);
                    binding.btnOptions.setText("广东省珠海市香洲一小");
                    break;
                default:
                    loginViewModel.orgId.setValue(0);
                    break;
            }
        }
    }
}