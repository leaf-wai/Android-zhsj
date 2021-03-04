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
import com.leaf.zhsjalpha.databinding.ActivityFriendsBinding;
import com.leaf.zhsjalpha.entity.OrderTab;
import com.leaf.zhsjalpha.fragment.FriendsFragment;
import com.leaf.zhsjalpha.fragment.NewFriendFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.FriendsViewModel;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class FriendsActivity extends AppCompatActivity {

    private String[] classItem = null;
    private int selectedClass = 0;

    private ActivityFriendsBinding binding;
    private FriendsViewModel friendsViewModel;
    private ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
    private final String[] mTitles = {"已加好友", "好友申请", "新朋友"};
    private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            binding.tlFriends.setCurrentTab(position);
            if (position == 2)
                binding.cvClass.setVisibility(View.GONE);
            else
                binding.cvClass.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        setContentView(binding.getRoot());
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        initTabLayout();
        initViewPager();
        addListener();
        addObserver();
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            tabEntities.add(new OrderTab(mTitles[i], 0, 0));
        }
        binding.tlFriends.setTabData(tabEntities);
        binding.tlFriends.setCurrentTab(0);
        binding.tlFriends.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpFriends.setCurrentItem(position);
                if (position == 2)
                    binding.cvClass.setVisibility(View.GONE);
                else
                    binding.cvClass.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initViewPager() {
        binding.vpFriends.setOffscreenPageLimit(3);
        binding.vpFriends.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0 || position == 1)
                    return FriendsFragment.newInstance(position);
                else
                    return NewFriendFragment.newInstance();
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });

        binding.vpFriends.registerOnPageChangeCallback(changeCallback);
    }

    private void addListener() {
        binding.FLBack.setOnClickListener(v -> onBackPressed());
        binding.llClass.setOnClickListener(v -> {
            showDialogSelectClass();
        });
    }

    private void addObserver() {
        friendsViewModel.getClassName().observe(this, string -> binding.tvClass.setText(string));
        friendsViewModel.getClassItem().observe(this, strings -> {
            if (strings.size() == 0) {
                friendsViewModel.getClassName().postValue("暂无班级");
                binding.ivArrowDown.setVisibility(View.GONE);
                binding.llClass.setClickable(false);
            } else {
                binding.ivArrowDown.setVisibility(View.VISIBLE);
                binding.llClass.setClickable(true);
                classItem = strings.toArray(new String[0]);
            }
        });
    }

    private void showDialogSelectClass() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("请选择班级");
        builder.setSingleChoiceItems(classItem, selectedClass, (dialog, which) -> {
            selectedClass = which;
            friendsViewModel.getClassId().postValue(friendsViewModel.courseDataList.get(which).getClassId());
            friendsViewModel.getClassName().postValue(classItem[which]);

            dialog.dismiss();
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        binding.vpFriends.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }
}