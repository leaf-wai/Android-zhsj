package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.databinding.ActivityDeclareBinding;
import com.leaf.zhsjalpha.entity.OrderTab;
import com.leaf.zhsjalpha.fragment.DeclareFragment;
import com.leaf.zhsjalpha.fragment.DeclareListFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.DeclareListViewModel;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class DeclareActivity extends AppCompatActivity {

    private int currentWeek;

    private ActivityDeclareBinding binding;
    private DeclareListViewModel mViewModel;
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    private final String[] mTitles = {"申报项目", "已申报"};
    private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            binding.tlDeclare.setCurrentTab(position);
            if (position == 0)
                binding.cvWeek.setVisibility(View.GONE);
            else
                binding.cvWeek.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityDeclareBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(DeclareListViewModel.class);
        setContentView(binding.getRoot());
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        new Thread(() -> currentWeek = mViewModel.getCurrentWeek()).start();

        initTabLayout();
        initViewPager();
        addListener();
        addObserver();
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            tabEntities.add(new OrderTab(mTitles[i], 0, 0));
        }
        binding.tlDeclare.setTabData(tabEntities);
        binding.tlDeclare.setCurrentTab(0);
        binding.tlDeclare.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpDeclare.setCurrentItem(position);
                if (position == 0)
                    binding.cvWeek.setVisibility(View.GONE);
                else
                    binding.cvWeek.setVisibility(View.VISIBLE);
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

    private void addListener() {
        binding.FLBack.setOnClickListener(v -> onBackPressed());
        binding.llWeek.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            ArrayList<String> weekItems = new ArrayList<>();
            for (int i = 1; i <= currentWeek; i++) {
                weekItems.add("第" + i + "周");
            }
            String[] items = weekItems.toArray(new String[0]);
            builder.setTitle("选择周次")
                    .setSingleChoiceItems(items, mViewModel.getWeek().getValue() - 1, (dialog, which) -> {
                        mViewModel.getWeek().postValue(which + 1);
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    private void addObserver() {
        mViewModel.getWeek().observe(this, integer -> {
            if (integer != 0) {
                binding.tvWeek.setText(String.format("第 %d 周", integer));
                mViewModel.getMyDeclare(integer);
            }
        });
    }

    @Override
    protected void onDestroy() {
        binding.vpDeclare.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }
}