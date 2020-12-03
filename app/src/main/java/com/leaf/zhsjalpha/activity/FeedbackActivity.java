package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityFeedbackBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
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
        setSupportActionBar(binding.toolbar);
        ToastUtils.getInstance().initToast(this);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        initToolbar();
        binding.btnSubmit.setOnClickListener(v -> {
            loadingFragment.show(getSupportFragmentManager(), "submit");
            RetrofitHelper.getInstance().addFeedbackCall(String.valueOf(binding.etFeedback.getText())).enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                    if (response.isSuccessful()) {
                        loadingFragment.dismiss();
                        ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                    loadingFragment.dismiss();
                    ToastUtils.showToast("网络请求失败！请稍后重试", Toast.LENGTH_SHORT);
                    Log.d("aaa", "onFailure: " + t.getMessage());
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }
}