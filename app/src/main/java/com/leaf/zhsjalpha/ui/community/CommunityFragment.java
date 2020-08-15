package com.leaf.zhsjalpha.ui.community;

import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.FragmentCommunityBinding;
import com.leaf.zhsjalpha.utils.StatusBar;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class CommunityFragment extends Fragment {

    private static String TAG = "community";
    private CommunityViewModel communityViewModel;
    private FragmentCommunityBinding binding;
    private TabLayoutMediator mediator;

    private int activeSize = 18;
    private int normalSize = 16;
    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            //可以来设置选中时tab的大小
            int tabCount = binding.tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
                TextView tabView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {
                    tabView.setTextSize(activeSize);
                } else {
                    tabView.setTextSize(normalSize);
                }
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        binding = FragmentCommunityBinding.inflate(getLayoutInflater());

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.white));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] tabs = new String[]{"官方动态", "交流社区"};

        binding.viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        binding.viewPager2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return CommunityListFragment.newInstance(position);
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
            Typeface font = Typeface.createFromAsset(assets, "fonts/mipromedium.ttf");
            tabView.setTypeface(font);
            tab.setCustomView(tabView);
        });
        mediator.attach();
    }

    @Override
    public void onStart() {
        super.onStart();
        StatusBar.lightStatusBar(getActivity(), true);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        StatusBar.lightStatusBar(getActivity(), true);
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
        }
    }
}