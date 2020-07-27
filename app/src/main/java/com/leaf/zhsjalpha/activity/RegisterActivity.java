package com.leaf.zhsjalpha.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityRegisterBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    private Button tb_sex;
    private ColorStateList list;
    private View.OnClickListener reglistener = v -> {
        switch (v.getId()) {
            case R.id.btn_stuReg:
                String studentName = String.valueOf(binding.etStuName.getText());
                int idcard = Integer.parseInt(String.valueOf(binding.etIdcard.getText()));
                String grade = String.valueOf(binding.actvGrade.getText());
                registerViewModel.register(studentName, idcard, grade);
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
        initView();

        // 子线程解析省市区数据
        Thread thread = new Thread(() -> registerViewModel.initJsonData());
        thread.start();

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

    private void initView() {
        registerViewModel.initArrayList();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, registerViewModel.items);
        binding.actvGrade.setAdapter(adapter);
        binding.btnStuReg.setEnabled(false);
        binding.cvLocation.setBackground(getDrawable(R.drawable.bg_location));
        binding.btnBack.setOnClickListener(reglistener);
        binding.LLLocation.setOnClickListener(reglistener);
        binding.btnStuReg.setOnClickListener(reglistener);
    }

    private void addObserver() {
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

                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || registerViewModel.orgId.getValue() == 0) {
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

                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || registerViewModel.orgId.getValue() == 0) {
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

                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || registerViewModel.orgId.getValue() == 0) {
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
                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || registerViewModel.sex == null || registerViewModel.orgId.getValue() == 0) {
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
                if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.orgId.getValue() == 0) {
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

        registerViewModel.getOrgId().observe(this, integer -> {
            if (integer != 0) {
                binding.cvLocation.setBackground(getDrawable(R.drawable.bg_location_fill));
            } else {
                binding.cvLocation.setBackground(getDrawable(R.drawable.bg_location));
            }

            if (TextUtils.isEmpty(binding.etStuName.getText()) || TextUtils.isEmpty(binding.etIdcard.getText()) || TextUtils.isEmpty(binding.actvGrade.getText()) || !binding.ckAgree.isChecked() || registerViewModel.sex == null || integer == 0) {
                binding.btnStuReg.setEnabled(false);
            } else {
                binding.btnStuReg.setEnabled(true);
            }
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
                    registerViewModel.orgId.setValue(246001);
                    break;
                case "广东省珠海市北师大":
                    registerViewModel.orgId.setValue(246002);
                    break;
                case "广东省珠海市香洲一小":
                    registerViewModel.orgId.setValue(246003);
                    break;
                default:
                    registerViewModel.orgId.setValue(0);
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