package com.leaf.zhsjalpha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.CourseAdapter;
import com.leaf.zhsjalpha.databinding.ActivityCourseListBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.CourseListViewModel;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class CourseListActivity extends AppCompatActivity {

    private CourseAdapter courseAdapter;
    private ActivityCourseListBinding binding;
    private CourseListViewModel courseListViewModel;
    private View progressbar;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        ToastUtils.getInstance().initToast(this);
        binding = ActivityCourseListBinding.inflate(getLayoutInflater());
        courseListViewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (getIntent().getStringExtra("keyword") != null) {
            binding.tvKeyword.setText(getIntent().getStringExtra("keyword"));
            courseListViewModel.searchCourseData(getIntent().getStringExtra("keyword"));
        } else {
            courseListViewModel.getCourseDataListAll();
        }

        initDialog();
        addListener();
        initAdapter();
        addObserver();
    }

    private void addObserver() {
        courseListViewModel.getLoadingStatus().observe(this, integer -> {
            if (integer == 404) {
                if (dialog.isShowing())
                    new Handler().postDelayed(() -> dialog.dismiss(), 300);
            }
        });
        courseListViewModel.getCourses().observe(this, courses -> {
            courseAdapter.setCourses(courses);
            courseAdapter.notifyDataSetChanged();
            if (dialog.isShowing())
                new Handler().postDelayed(() -> dialog.dismiss(), 300);
        });
    }

    private void addListener() {
        binding.buttonBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        binding.LLSearch.setOnClickListener(v -> {
            if (getIntent().getStringExtra("keyword") != null) {
                Intent intent = getIntent();
                intent.putExtra("keyword", binding.tvKeyword.getText());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                startActivity(new Intent(this, SearchActivity.class));
            }
        });
    }

    private void initAdapter() {
        courseAdapter = new CourseAdapter();
        binding.recyclerViewCourseList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCourseList.setAdapter(courseAdapter);
    }

    private void initDialog() {
        initProgressBar();
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
        tvLoading.setText("加载中…");
    }

    @Override
    public void onBackPressed() {
        binding.buttonBack.performClick();
    }
}