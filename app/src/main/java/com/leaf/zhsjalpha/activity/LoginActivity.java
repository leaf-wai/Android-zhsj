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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityLoginBinding;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.LoginViewModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static com.leaf.zhsjalpha.utils.MD5Utils.md5;

public class LoginActivity extends AppCompatActivity {

    public static LoginActivity loginActivity;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private ColorStateList list = null;
    private AlertDialog dialog;
    private View progressbar;
    private View.OnClickListener loginListener = v -> {
        switch (v.getId()) {
            case R.id.btn_stulogin:
                String studentName = String.valueOf(binding.etUser.getText());
                //密码进行MD5加密
                String password = md5(String.valueOf(binding.etPwd.getText()));
                initProgressBar();
                initDialog();
                new Handler().postDelayed(() -> loginViewModel.login(studentName, password), 1000);
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
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        initView();
        addListener();

        // 子线程解析省市区数据
        Thread thread = new Thread(() -> loginViewModel.initJsonData());
        thread.start();

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

    private void initDialog() {
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(progressbar);
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 350;
        params.height = 400;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    private void initProgressBar() {
        progressbar = LayoutInflater.from(this).inflate(R.layout.progressbar_layout, null, false);
        Sprite doubleBounce = new DoubleBounce();
        doubleBounce.setColor(getResources().getColor(R.color.colorPrimary));
        ProgressBar progressBar = progressbar.findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(doubleBounce);
        TextView tvLoading = progressbar.findViewById(R.id.tv_loading);
        tvLoading.setText("登录中…");
    }

    //添加观察者
    private void addObserver() {
        loginViewModel.getOrgId().observe(this, integer -> {
            if (integer != 0) {
                binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location_fill));
            } else {
                binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
            }

            if (TextUtils.isEmpty(binding.etUser.getText()) || TextUtils.isEmpty(binding.etPwd.getText()) || integer == 0) {
                binding.btnStulogin.setEnabled(false);
            } else {
                binding.btnStulogin.setEnabled(true);
            }
        });
        loginViewModel.getLoginState().observe(this, integer -> {
            switch (integer) {
                case 200:
                    SharedPreferences.Editor rmbEdit = getApplicationContext().getSharedPreferences("UserSave", Context.MODE_PRIVATE).edit();
                    if (binding.ckRemember.isChecked()) {
                        rmbEdit.putBoolean("RememberMe", true);
                        rmbEdit.putString("studentName", String.valueOf(binding.etUser.getText()));
                        rmbEdit.putString("password", String.valueOf(binding.etPwd.getText()));
                        rmbEdit.putInt("orgId", loginViewModel.getOrgId().getValue());
                    } else {
                        rmbEdit.putBoolean("RememberMe", false);
                        rmbEdit.remove("studentName");
                        rmbEdit.remove("password");
                        rmbEdit.remove("orgId");
                    }
                    rmbEdit.apply();
                    dialog.dismiss();
                    startActivity(new Intent(getApplication(), MainActivity.class));
                    ToastUtils.showToast("登录成功", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case 202:
                    dialog.dismiss();
                    ToastUtils.showToast("用户名或密码错误！", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
                case 404:
                    dialog.dismiss();
                    ToastUtils.showToast("网络错误！请稍后重试", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
            }
        });
    }

    //添加监听器
    private void addListener() {
        binding.btnBack.setOnClickListener(loginListener);
        binding.LLLocation.setOnClickListener(loginListener);
        binding.btnStulogin.setOnClickListener(loginListener);
        binding.btnReg.setOnClickListener(loginListener);
        binding.btnForegetPwd.setOnClickListener(loginListener);

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

                if (TextUtils.isEmpty(binding.etUser.getText()) || TextUtils.isEmpty(binding.etPwd.getText()) || loginViewModel.getOrgId().getValue() == 0) {
                    binding.btnStulogin.setEnabled(false);
                } else {
                    binding.btnStulogin.setEnabled(true);
                }
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

                if (TextUtils.isEmpty(binding.etUser.getText()) || TextUtils.isEmpty(binding.etPwd.getText()) || loginViewModel.getOrgId().getValue() == 0) {
                    binding.btnStulogin.setEnabled(false);
                } else {
                    binding.btnStulogin.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //初始化View
    private void initView() {
        binding.btnStulogin.setEnabled(false);
        binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
        ToastUtils.getInstance().initToast(this);
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

            switch (tx) {
                case "广东省珠海市爱实践":
                    loginViewModel.getOrgId().setValue(246001);
                    break;
                case "广东省珠海市北师大":
                    loginViewModel.getOrgId().setValue(246002);
                    break;
                case "广东省珠海市香洲一小":
                    loginViewModel.getOrgId().setValue(246003);
                    break;
                default:
                    loginViewModel.getOrgId().setValue(0);
                    break;
            }
            loginViewModel.orgName = tx;
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

    //加载已保存用户名密码
    private void loadUserSave() {
        SharedPreferences rmbRead = getApplicationContext().getSharedPreferences("UserSave", Context.MODE_PRIVATE);
        if (rmbRead.getBoolean("RememberMe", false)) {
            binding.etUser.setText(rmbRead.getString("studentName", ""));
            binding.etPwd.setText(rmbRead.getString("password", ""));
            binding.ckRemember.setChecked(true);
            switch (rmbRead.getInt("orgId", 0)) {
                case 246001:
                    loginViewModel.getOrgId().setValue(246001);
                    binding.tvLocation.setText("广东省珠海市爱实践");
                    loginViewModel.orgName = "广东省珠海市爱实践";
                    binding.tvLocation.setTextColor(getResources().getColor(R.color.textBlack));
                    break;
                case 246002:
                    loginViewModel.getOrgId().setValue(246002);
                    binding.tvLocation.setText("广东省珠海市北师大");
                    loginViewModel.orgName = "广东省珠海市北师大";
                    binding.tvLocation.setTextColor(getResources().getColor(R.color.textBlack));
                    break;
                case 246003:
                    loginViewModel.getOrgId().setValue(246003);
                    binding.tvLocation.setText("广东省珠海市香洲一小");
                    loginViewModel.orgName = "广东省珠海市香洲一小";
                    binding.tvLocation.setTextColor(getResources().getColor(R.color.textBlack));
                    break;
                default:
                    loginViewModel.getOrgId().setValue(0);
                    break;
            }
        }
    }
}