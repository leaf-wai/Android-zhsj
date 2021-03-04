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
import com.leaf.zhsjalpha.databinding.ActivityNotifyBinding;
import com.leaf.zhsjalpha.entity.OrderTab;
import com.leaf.zhsjalpha.fragment.NotifyListFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.NotifyViewModel;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class NotifyActivity extends AppCompatActivity {

    private int currentWeek;
    private int selectedWeek = 0;

    private ActivityNotifyBinding binding;
    private NotifyViewModel mViewModel;
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    private final String[] mTitles = {"通知", "课程", "考勤", "评价"};
    private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            binding.tlNotify.setCurrentTab(position);
            if (position == 0)
                binding.cvWeek.setVisibility(View.VISIBLE);
            else
                binding.cvWeek.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityNotifyBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(NotifyViewModel.class);
        setContentView(binding.getRoot());
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        new Thread(() -> currentWeek = mViewModel.getCurrentWeek()).start();

        initTabLayout();
        initViewPager();

        binding.llWeek.setOnClickListener(v -> showDialogWeek());
        binding.FLBack.setOnClickListener(v -> onBackPressed());
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            tabEntities.add(new OrderTab(mTitles[i], 0, 0));
        }
        binding.tlNotify.setTabData(tabEntities);
        binding.tlNotify.setCurrentTab(0);
        binding.tlNotify.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpNotify.setCurrentItem(position);
                if (position == 0)
                    binding.cvWeek.setVisibility(View.VISIBLE);
                else
                    binding.cvWeek.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initViewPager() {
        binding.vpNotify.setOffscreenPageLimit(4);
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

    private void showDialogWeek() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        ArrayList<String> weekItems = new ArrayList<>();
        weekItems.add("所有周次");
        for (int i = 1; i <= currentWeek; i++) {
            weekItems.add("第" + i + "周");
        }
        String[] items = weekItems.toArray(new String[0]);
        builder.setTitle("选择周次")
                .setSingleChoiceItems(items, selectedWeek, (dialog, which) -> {
                    selectedWeek = which;
                    if (which == 0) {
                        mViewModel.getWeek().postValue(which);
                        mViewModel.getTeacherNotice(null);
                    } else {
                        mViewModel.getWeek().postValue(which);
                        mViewModel.getTeacherNotice(which);
                    }
                    dialog.dismiss();
                    if (which == 0)
                        binding.tvWeek.setText("所有周次");
                    else
                        binding.tvWeek.setText(String.format("第 %d 周", which));
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        binding.vpNotify.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }
}