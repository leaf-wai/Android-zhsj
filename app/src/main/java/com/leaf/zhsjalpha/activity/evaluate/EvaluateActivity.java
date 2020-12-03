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
        switch (v.getId()) {
            case R.id.LL_evaluate_family:
                startActivity(new Intent(EvaluateActivity.this, FamilyEvaluateActivity.class));
                break;
            case R.id.LL_evaluate_my:
                startActivity(new Intent(EvaluateActivity.this, MyEvaluateActivity.class));
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
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        initToolbar();
        addListener();
    }

    private void initToolbar() {
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}