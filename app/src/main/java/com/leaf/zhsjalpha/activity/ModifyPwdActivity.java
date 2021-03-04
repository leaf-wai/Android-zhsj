package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityModifyPwdBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.PasswordViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class ModifyPwdActivity extends AppCompatActivity implements Callback<User> {
    private ActivityModifyPwdBinding binding;
    private PasswordViewModel passwordViewModel;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityModifyPwdBinding.inflate(getLayoutInflater());
        passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.btnSubmit.setOnClickListener(v -> {
            loadingFragment.show(getSupportFragmentManager(), "submit");
            passwordViewModel.modifyPwd(String.valueOf(binding.etPwd.getText()), String.valueOf(binding.etNewPwd.getText()), this);
        });

        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onResponse(@NotNull Call<User> call, Response<User> response) {
        if (response.isSuccessful() && response.body() != null) {
            loadingFragment.dismiss();
            ToastUtils.showToast(getApplicationContext(), response.body().getDetail(), getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
            if (response.body().getCode() == 200) {
                finish();
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<User> call, Throwable t) {
        loadingFragment.dismiss();
        Log.d("aaa", "onFailure: " + t.getMessage());
        ToastUtils.showToast(getApplicationContext(), "网络错误！请稍后重试", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
    }
}