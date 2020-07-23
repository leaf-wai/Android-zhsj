package com.leaf.zhsjalpha.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.leaf.zhsjalpha.adapter.ImageNetAdapter;
import com.leaf.zhsjalpha.adapter.TopLineAdapter;
import com.leaf.zhsjalpha.bean.DataBean;
import com.leaf.zhsjalpha.databinding.FragmentHomeBinding;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;
import com.youth.banner.util.BannerUtils;
import com.youth.banner.util.LogUtils;

public class HomeFragment extends Fragment implements OnPageChangeListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        BannerUtils.setBannerRound(binding.topBanner, 20);
        BannerUtils.setBannerRound(binding.topLine, 20);
        BannerUtils.setBannerRound(binding.banner2, 20);
        BannerUtils.setBannerRound(binding.clMycourse, 20);
        BannerUtils.setBannerRound(binding.clSignincourse, 20);

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.d("onPageSelected:" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.banner.setAdapter(new ImageNetAdapter(DataBean.getTestData3()))//设置适配器
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getContext()))//设置指示器
                .addOnPageChangeListener(this)//添加切换监听
                .setOnBannerListener((data, position) -> {
                    Log.d("aaa", "position：" + position);
                });//设置点击事件,传this也行

        binding.banner2.setAdapter(new TopLineAdapter(DataBean.getTestData2()))
                .setOrientation(Banner.VERTICAL)
                .setOnBannerListener((data, position) -> {
                    Snackbar.make(binding.banner, ((DataBean) data).title, Snackbar.LENGTH_SHORT).show();
                    Log.d("aaa", "position：" + position);
                });
    }
}