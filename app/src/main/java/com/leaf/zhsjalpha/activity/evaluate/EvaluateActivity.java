package com.leaf.zhsjalpha.activity.evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityEvaluateBinding;
import com.leaf.zhsjalpha.utils.StatusBar;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class EvaluateActivity extends AppCompatActivity {

    private ActivityEvaluateBinding binding;
    private View.OnClickListener listener = v -> {
        Intent intent = new Intent(EvaluateActivity.this, BaseEvaluateActivity.class);
        switch (v.getId()) {
            case R.id.LL_evaluate_family:
                intent.putExtra("type", "family");
                startActivity(intent);
                break;
            case R.id.LL_evaluate_my:
                intent.putExtra("type", "my");
                startActivity(intent);
                break;
            case R.id.LL_evaluate_friend:
                startActivity(new Intent(EvaluateActivity.this, FriendEvaluateActivity.class));
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityEvaluateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar();
        addListener();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void addListener() {
        binding.LLEvaluateFamily.setOnClickListener(listener);
        binding.LLEvaluateMy.setOnClickListener(listener);
        binding.LLEvaluateFriend.setOnClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}