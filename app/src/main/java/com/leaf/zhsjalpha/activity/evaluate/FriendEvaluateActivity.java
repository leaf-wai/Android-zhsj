package com.leaf.zhsjalpha.activity.evaluate;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityFriendEvaluateBinding;
import com.leaf.zhsjalpha.fragment.evaluate.FriendBackFragment;
import com.leaf.zhsjalpha.fragment.evaluate.FriendFrontFragment;
import com.leaf.zhsjalpha.utils.MyPagerHelper;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.EvaluateViewModel;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class FriendEvaluateActivity extends AppCompatActivity {

    private ActivityFriendEvaluateBinding binding;
    private EvaluateViewModel mViewModel;
    private View.OnClickListener listener_front = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyPagerHelper.setCurrentItem(binding.vpEvaluate, 1, 1200);
        }
    };
    private View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyPagerHelper.setCurrentItem(binding.vpEvaluate, 0, 1200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityFriendEvaluateBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(EvaluateViewModel.class);
        setContentView(binding.getRoot());
        mViewModel.getCurrencyType();
        initToolbar();
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

    private void initViewPager() {
        binding.vpEvaluate.setOffscreenPageLimit(2);
        binding.vpEvaluate.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0)
                    return FriendFrontFragment.newInstance(listener_front);
                else
                    return FriendBackFragment.newInstance(listener_back);
            }

            @Override
            public int getItemCount() {
                return 2;
            }

        });
        CardFlipPageTransformer2 cardFlipPageTransformer = new CardFlipPageTransformer2();
        cardFlipPageTransformer.setScalable(true);
        binding.vpEvaluate.setPageTransformer(cardFlipPageTransformer);
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
}