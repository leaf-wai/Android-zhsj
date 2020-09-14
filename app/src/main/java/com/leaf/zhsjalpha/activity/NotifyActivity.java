package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityNotifyBinding;
import com.leaf.zhsjalpha.entity.OrderTab;
import com.leaf.zhsjalpha.fragment.NotifyListFragment;
import com.leaf.zhsjalpha.utils.StatusBar;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class NotifyActivity extends AppCompatActivity {

    private ActivityNotifyBinding binding;
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    private String[] mTitles = {"课程", "评价", "作业", "考勤"};
    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            binding.tlNotify.setCurrentTab(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityNotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        initToolbar();
        initTabLayout();
        initViewPager();
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            tabEntities.add(new OrderTab(mTitles[i]));
        }
        binding.tlNotify.setTabData(tabEntities);
        binding.tlNotify.setCurrentTab(0);
        binding.tlNotify.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpNotify.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initViewPager() {
        binding.vpNotify.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        binding.vpNotify.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return NotifyListFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });

        binding.vpNotify.registerOnPageChangeCallback(changeCallback);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        binding.vpNotify.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }
}