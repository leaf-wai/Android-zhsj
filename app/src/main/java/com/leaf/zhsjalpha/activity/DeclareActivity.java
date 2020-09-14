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
import com.leaf.zhsjalpha.databinding.ActivityDeclareBinding;
import com.leaf.zhsjalpha.entity.OrderTab;
import com.leaf.zhsjalpha.fragment.DeclareFragment;
import com.leaf.zhsjalpha.fragment.DeclareListFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class DeclareActivity extends AppCompatActivity {

    private ActivityDeclareBinding binding;
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    private String[] mTitles = {"申报项目", "已申报"};
    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            binding.tlDeclare.setCurrentTab(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityDeclareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ToastUtils.getInstance().initToast(getApplicationContext());

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
        binding.tlDeclare.setTabData(tabEntities);
        binding.tlDeclare.setCurrentTab(0);
        binding.tlDeclare.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpDeclare.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initViewPager() {
        binding.vpDeclare.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        binding.vpDeclare.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0)
                    return DeclareFragment.newInstance();
                else
                    return DeclareListFragment.newInstance();
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });

        binding.vpDeclare.registerOnPageChangeCallback(changeCallback);
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
        binding.vpDeclare.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }
}