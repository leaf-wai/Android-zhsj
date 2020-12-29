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
import com.leaf.zhsjalpha.databinding.ActivityBaseEvaluateBinding;
import com.leaf.zhsjalpha.fragment.evaluate.BaseBackFragment;
import com.leaf.zhsjalpha.fragment.evaluate.BaseFrontFragment;
import com.leaf.zhsjalpha.utils.MyPagerHelper;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.EvaluateViewModel;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class BaseEvaluateActivity extends AppCompatActivity {
    private String TYPE;
    private ActivityBaseEvaluateBinding binding;
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
        TYPE = getIntent().getStringExtra("type");
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityBaseEvaluateBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(EvaluateViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ToastUtils.getInstance().initToast(this);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.transparent));
        mViewModel.getCurrencyType();
        initToolbar();
        initView();
        initViewPager();
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void initView() {
        switch (TYPE) {
            case "family":
                binding.llEvaluate.setBackground(getResources().getDrawable(R.drawable.evaluate_home_gradient));
                getSupportActionBar().setTitle(getResources().getString(R.string.evaluate_family_name));
                break;
            case "my":
                binding.llEvaluate.setBackground(getResources().getDrawable(R.drawable.evaluate_my_gradient));
                getSupportActionBar().setTitle(getResources().getString(R.string.evaluate_my_name));
                break;
        }
    }

    private void initViewPager() {
        binding.vpEvaluate.setOffscreenPageLimit(2);
        binding.vpEvaluate.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (TYPE) {
                    case "family":
                        if (position == 0)
                            return BaseFrontFragment.newInstance("family", listener_front);
                        else
                            return BaseBackFragment.newInstance("family", listener_back);
                    case "my":
                        if (position == 0)
                            return BaseFrontFragment.newInstance("my", listener_front);
                        else
                            return BaseBackFragment.newInstance("my", listener_back);
                }
                return null;
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}