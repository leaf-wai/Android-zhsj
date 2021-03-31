package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityAboutBinding;
import com.leaf.zhsjalpha.utils.StatusBar;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;
    private static final String GITHUB_URL = "https://github.com/leaf-wai/Android-zhsj";
    private static final String ZHSJ_URL = "https://zhsj.bnuz.edu.cn/slogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar();
        binding.LLGithub.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL))));
        binding.LLWeb.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ZHSJ_URL))));
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