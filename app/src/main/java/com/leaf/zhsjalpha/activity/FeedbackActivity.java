package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.api.RetrofitHelper;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityFeedbackBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class FeedbackActivity extends AppCompatActivity {

    private ActivityFeedbackBinding binding;
    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        setContentView(binding.getRoot());
        initToolbar();
        binding.btnSubmit.setOnClickListener(v -> {
            loadingFragment.show(getSupportFragmentManager(), "submit");
            RetrofitHelper.getInstance().addFeedbackCall(String.valueOf(binding.etContent.getText())).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loadingFragment.dismiss();
                        ToastUtils.showToast(getApplicationContext(), response.body().getDetail());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                    loadingFragment.dismiss();
                    ToastUtils.showToast(getApplicationContext(), "网络请求失败！请稍后重试");
                    Log.d("aaa", "onFailure: " + t.getMessage());
                }
            });
        });
        binding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.btnSubmit.setEnabled(true);
                } else {
                    binding.btnSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }
}