package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityForgetPwdBinding;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.ForgetPwdViewModel;

public class ForgetPwdActivity extends AppCompatActivity {

    private ActivityForgetPwdBinding binding;
    private ForgetPwdViewModel forgetPwdViewModel;
    private View progressbar;
    private TextView tvLoading;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPwdBinding.inflate(getLayoutInflater());
        forgetPwdViewModel = new ViewModelProvider(this).get(ForgetPwdViewModel.class);
        setContentView(binding.getRoot());

        binding.btnSubmit.setOnClickListener(v -> {
            String studentName = String.valueOf(binding.etStuName.getText());
            String idcard = String.valueOf(binding.etIdcard.getText());
            String newpassword = String.valueOf(binding.etNewPwd.getText());
            initProgressBar();
            initDialog();
            new Handler().postDelayed(() -> {
                forgetPwdViewModel.resetPwd(studentName, idcard, newpassword);
            }, 1000);
        });
        binding.btnBack.setOnClickListener(v -> finish());

        forgetPwdViewModel.getResetStatus().observe(this, integer -> {
            switch (integer) {
                case 200:
                    dialog.dismiss();
                    startActivity(new Intent(this, LoginActivity.class));
                    ToastUtils.showToast("重置密码成功！请使用新密码登录", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
                case 202:
                    dialog.dismiss();
                    ToastUtils.showToast("重置密码失败！请检查输入的信息", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
                case 404:
                    dialog.dismiss();
                    ToastUtils.showToast("网络请求失败！请重试", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    break;
            }
        });
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
        tvLoading = progressbar.findViewById(R.id.tv_loading);
        tvLoading.setText("正在提交…");
    }
}