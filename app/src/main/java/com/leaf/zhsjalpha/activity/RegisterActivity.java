package com.leaf.zhsjalpha.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.leaf.zhsjalpha.databinding.ActivityRegisterBinding;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.RegisterViewModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    private Button tb_sex;
    private ColorStateList list;
    private View progressbar;
    private AlertDialog dialog;
    private View.OnClickListener reglistener = v -> {
        switch (v.getId()) {
            case R.id.btn_stuReg:
                String studentName = String.valueOf(binding.etStuName.getText());
                int idcard = Integer.parseInt(String.valueOf(binding.etIdcard.getText()));
                initProgressBar();
                initDialog();
                new Handler().postDelayed(() -> {
                    registerViewModel.register(studentName, idcard);
                }, 1000);
                break;
            case R.id.LL_location:
                showPickerView();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // 子线程解析省市区数据
        Thread thread = new Thread(() -> {
            registerViewModel.initOrgData();
        });
        thread.start();
        initView();
        addListener();
        initCSL();
        addObserver();
    }

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
        tvLoading.setText("注册中…");
    }

    private void initView() {
        registerViewModel.initGradeList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_actv_item, registerViewModel.getItems().getValue());
        binding.actvGrade.setAdapter(adapter);
        binding.btnStuReg.setEnabled(false);
        binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
        ToastUtils.getInstance().initToast(this);
    }

    private void addObserver() {
        registerViewModel.getOrgId().observe(this, integer -> {
            if (integer != 0) {
                binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location_fill));
            } else {
                binding.cvLocation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
            }

            if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || integer == 0) {
                binding.btnStuReg.setEnabled(false);
            } else {
                binding.btnStuReg.setEnabled(true);
            }
        });

        registerViewModel.getRegisterStatus().observe(this, integer -> {
            switch (integer) {
                case 200:
                    dialog.dismiss();
                    startActivity(new Intent(this, LoginActivity.class));
                    ToastUtils.showToast("注册成功！", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
                case 11:
                    dialog.dismiss();
                    ToastUtils.showToast("重复注册！请重试", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
                case 404:
                    dialog.dismiss();
                    ToastUtils.showToast("网络请求失败！请重试", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
            }

        });
    }

    private void addListener() {
        binding.btnBack.setOnClickListener(reglistener);
        binding.LLLocation.setOnClickListener(reglistener);
        binding.btnStuReg.setOnClickListener(reglistener);

        binding.TILUser.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etStuName.getText())) {
                    binding.TILUser.setErrorEnabled(true);
                    binding.TILUser.setError("姓名不能为空！");
                } else {
                    binding.TILUser.setErrorEnabled(false);
                    binding.TILUser.setBoxStrokeColorStateList(list);
                }

                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || registerViewModel.getOrgId().getValue() == 0) {
                    binding.btnStuReg.setEnabled(false);
                } else {
                    binding.btnStuReg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.TILIdcard.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etIdcard.getText())) {
                    binding.TILIdcard.setErrorEnabled(true);
                    binding.TILIdcard.setError("身份证后六位不能为空！");
                } else {
                    binding.TILIdcard.setErrorEnabled(false);
                    binding.TILIdcard.setBoxStrokeColorStateList(list);
                }

                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || registerViewModel.getOrgId().getValue() == 0) {
                    binding.btnStuReg.setEnabled(false);
                } else {
                    binding.btnStuReg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.actvGrade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.actvGrade.getText())) {
                    binding.TILGrade.setErrorEnabled(true);
                    binding.TILGrade.setError("年级不能为空！");
                } else {
                    binding.TILGrade.setErrorEnabled(false);
                    binding.TILGrade.setBoxStrokeColorStateList(list);
                }

                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || registerViewModel.getOrgId().getValue() == 0) {
                    binding.btnStuReg.setEnabled(false);
                } else {
                    binding.btnStuReg.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.ckAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || registerViewModel.sex == null || registerViewModel.getOrgId().getValue() == 0) {
                    binding.btnStuReg.setEnabled(false);
                } else {
                    binding.btnStuReg.setEnabled(true);
                }
            } else {
                binding.btnStuReg.setEnabled(false);
            }
        });

        binding.tbSex.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            tb_sex = binding.tbSex.findViewById(checkedId);
            if (isChecked && tb_sex.getText().toString().equals("男")) {
                registerViewModel.sex = "男";
                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.getOrgId().getValue() == 0) {
                    binding.btnStuReg.setEnabled(false);
                } else {
                    binding.btnStuReg.setEnabled(true);
                }
            } else if (isChecked && tb_sex.getText().toString().equals("女")) {
                registerViewModel.sex = "女";
                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || String.valueOf(registerViewModel.getOrgId()).equals("0")) {
                    binding.btnStuReg.setEnabled(false);
                } else {
                    binding.btnStuReg.setEnabled(true);
                }
            } else {
                registerViewModel.sex = null;
                binding.btnStuReg.setEnabled(false);
            }
        });

        binding.actvGrade.setOnItemClickListener((parent, view, position, id) -> {
            registerViewModel.getGradeId().setValue(registerViewModel.grade.get(position).getGradeId());
        });
    }

    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = registerViewModel.options1Items.size() > 0 ?
                    registerViewModel.options1Items.get(options1).getPickerViewText() : "";

            String opt2tx = registerViewModel.options2Items.size() > 0
                    && registerViewModel.options2Items.get(options1).size() > 0 ?
                    registerViewModel.options2Items.get(options1).get(options2) : "";

            String opt3tx = registerViewModel.options2Items.size() > 0
                    && registerViewModel.options3Items.get(options1).size() > 0
                    && registerViewModel.options3Items.get(options1).get(options2).size() > 0 ?
                    registerViewModel.options3Items.get(options1).get(options2).get(options3) : "";

            String tx = opt1tx + opt2tx + opt3tx;
            binding.tvLocation.setText(tx);
            binding.tvLocation.setTextColor(getResources().getColor(R.color.textBlack));

            switch (tx) {
                case "广东省珠海市爱实践":
                    registerViewModel.getOrgId().setValue(246001);
                    break;
                case "广东省珠海市北师大":
                    registerViewModel.getOrgId().setValue(246002);
                    break;
                case "广东省珠海市香洲一小":
                    registerViewModel.getOrgId().setValue(246003);
                    break;
                default:
                    registerViewModel.getOrgId().setValue(0);
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

        pvOptions.setPicker(registerViewModel.options1Items, registerViewModel.options2Items, registerViewModel.options3Items);//三级选择器
        pvOptions.show();
    }
}