package com.leaf.zhsjalpha.ui.submit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.databinding.FragmentSubmitBinding;
import com.leaf.zhsjalpha.utils.StatusBar;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class SubmitFragment extends Fragment {

    private FragmentSubmitBinding binding;
    private SubmitViewModel mViewModel;
    private TabLayoutMediator mediator;

    private final ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            //可以来设置选中时tab的大小
            int tabCount = binding.tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
                TextView tabView = (TextView) tab.getCustomView();
                tabView.setTextSize(15);
                if (tab.getPosition() == position) {
                    ScaleAnimation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    tabView.setAnimation(animation);
                } else {
                    ScaleAnimation animation = new ScaleAnimation(1.2f, 1, 1.2f, 1,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(200);
                    animation.setFillAfter(true);
                    tabView.setAnimation(animation);
                }
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SubmitViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_submit, container, false);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(this);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewPager();
        binding.btnLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
    }

    private void initViewPager() {
        final String[] tabs = new String[]{"提交作品", "我的作品"};
        binding.viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        binding.viewPager2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 1:
                        return SubmitListFragment.newInstance();
                    case 0:
                    default:
                        return SubmitProductFragment.newInstance();
                }
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });

        binding.viewPager2.registerOnPageChangeCallback(changeCallback);

        mediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager2, true, (tab, position) -> {
            TextView tabView = new TextView(getContext());
            tabView.setText(tabs[position]);
            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{};
            int[] colors = new int[]{getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.gray3)};
            ColorStateList stateList = new ColorStateList(states, colors);
            tabView.setTextColor(stateList);
            AssetManager assets = getContext().getAssets();
            Typeface font = Typeface.createFromAsset(assets, "fonts/miprobold.ttf");
            tabView.setTypeface(font);
            tab.setCustomView(tabView);
        });
        mediator.attach();
    }

    public void loadLoginState() {
        SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (userRead.getBoolean("hasLogined", false)) {
            mViewModel.getLogin().setValue(true);
        } else {
            mViewModel.getLogin().setValue(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLoginState();
    }

    @Override
    public void onDestroy() {
        mediator.detach();
        binding.viewPager2.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBar.lightStatusBar(getActivity(), true);
            loadLoginState();
        }
    }

}

