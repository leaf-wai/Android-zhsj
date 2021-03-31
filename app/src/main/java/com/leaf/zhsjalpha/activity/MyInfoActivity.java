package com.leaf.zhsjalpha.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityMyInfoBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.MyInfoViewModel;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class MyInfoActivity extends AppCompatActivity {

    private ActivityMyInfoBinding binding;
    private MyInfoViewModel myInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_info);
        myInfoViewModel = new ViewModelProvider(this).get(MyInfoViewModel.class);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        initToolbar();
        addObserver();
        myInfoViewModel.setUserDetail();
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
    }

    private void addObserver() {
        myInfoViewModel.getStudentName().observe(this, s -> binding.tvName.setText(s));
        myInfoViewModel.getSex().observe(this, s -> binding.tvSex.setText(s));
        myInfoViewModel.getBirthday().observe(this, s -> binding.tvBirthday.setText(s));
        myInfoViewModel.getStudentCard().observe(this, integer -> binding.tvStudentNumber.setText(String.valueOf(integer)));
        myInfoViewModel.getGrade().observe(this, s -> binding.tvGrade.setText(s));
        myInfoViewModel.getPicUrl().observe(this, s -> {
            Glide.with(this)
                    .load(s)
                    .placeholder(R.drawable.ic_avatar)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(binding.rivAvatar);
        });
    }
}