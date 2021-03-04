package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityNewEvaluateBinding;
import com.leaf.zhsjalpha.entity.OrderTab;
import com.leaf.zhsjalpha.fragment.evaluate.BaseEvaluateFragment;
import com.leaf.zhsjalpha.fragment.evaluate.FriendEvaluateFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.EvaluateViewModel;

import java.util.ArrayList;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class NewEvaluateActivity extends AppCompatActivity {

    private final String[] mTitles = {"家庭", "自我", "伙伴"};
    private final int[] mSelectedIcons = {R.drawable.vector_drawable_home_fill, R.drawable.vector_drawable_my_fill, R.drawable.ic_group_fill};
    private final int[] mUnSelectedIcons = {R.drawable.vector_drawable_home, R.drawable.vector_drawable_my, R.drawable.ic_group};
    private ActivityNewEvaluateBinding binding;
    private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            binding.tlEvaluate.setCurrentTab(position);
        }
    };
    private EvaluateViewModel mViewModel;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEvaluateBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(EvaluateViewModel.class);
        setContentView(binding.getRoot());
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        initToolbar();
        mViewModel.getCurrencyType();
        initTabLayout();
        initViewPager();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void initTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new OrderTab(mTitles[i], mSelectedIcons[i], mUnSelectedIcons[i]));
        }
        binding.tlEvaluate.setTabData(mTabEntities);
        binding.tlEvaluate.setCurrentTab(0);
        binding.tlEvaluate.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                binding.vpEvaluate.setCurrentItem(position);
                switch (position) {
                    case 0:
                        binding.llEvaluate.setBackground(getResources().getDrawable(R.drawable.evaluate_home_gradient));
                        break;
                    case 1:
                        binding.llEvaluate.setBackground(getResources().getDrawable(R.drawable.evaluate_my_gradient));
                        break;
                    case 2:
                        binding.llEvaluate.setBackground(getResources().getDrawable(R.drawable.evaluate_friend_gradient));
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initViewPager() {
        binding.vpEvaluate.setOffscreenPageLimit(3);
        binding.vpEvaluate.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0)
                    return BaseEvaluateFragment.newInstance("family");
                else if (position == 1)
                    return BaseEvaluateFragment.newInstance("my");
                else
                    return FriendEvaluateFragment.newInstance();
            }

            @Override
            public int getItemCount() {
                return mTitles.length;
            }
        });
        binding.vpEvaluate.registerOnPageChangeCallback(changeCallback);
        binding.vpEvaluate.setUserInputEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.vpEvaluate.unregisterOnPageChangeCallback(changeCallback);
    }
}