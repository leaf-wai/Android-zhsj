package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityForgetPwdBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.PasswordViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class ForgetPwdActivity extends AppCompatActivity implements Callback<User> {

    private ActivityForgetPwdBinding binding;
    private PasswordViewModel forgetPwdViewModel;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityForgetPwdBinding.inflate(getLayoutInflater());
        forgetPwdViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        setContentView(binding.getRoot());

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        binding.btnSubmit.setOnClickListener(v -> {
            String studentName = String.valueOf(binding.etStuName.getText());
            String idcard = String.valueOf(binding.etIdcard.getText());
            String newpassword = String.valueOf(binding.etNewPwd.getText());
            loadingFragment.show(getSupportFragmentManager(), "forget");
            new Handler().postDelayed(() -> {
                forgetPwdViewModel.resetPwd(studentName, idcard, newpassword, this);
            }, 1000);
        });
        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onResponse(@NotNull Call<User> call, Response<User> response) {
        if (response.isSuccessful() && response.body() != null) {
            User user = response.body();
            if (user.code == 200) {
                loadingFragment.dismiss();
                startActivity(new Intent(getApplication(), LoginActivity.class));
                ToastUtils.showToast(getApplicationContext(), "重置密码成功！请使用新密码登录", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
            } else if (user.code == 202) {
                loadingFragment.dismiss();
                ToastUtils.showToast(getApplicationContext(), "重置密码失败！请检查输入的信息", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
            } else {
                loadingFragment.dismiss();
                ToastUtils.showToast(getApplicationContext(), "网络请求失败！请重试", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<User> call, Throwable t) {
        Log.d("aaa", "网络错误: " + t.getMessage());
        loadingFragment.dismiss();
        ToastUtils.showToast(getApplicationContext(), "网络请求失败！请重试", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
    }
}